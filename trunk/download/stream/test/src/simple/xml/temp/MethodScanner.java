package simple.xml.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementArray;
import simple.xml.ElementList;
import simple.xml.Text;

/**
 * Rule 1) 
 *    matches are performed on annotation only, and the most specialized class
 *    wins the reference. 
 * 
 * Rule 2)
 *    the get and set methods MUST have the exactl same type. This is validated
 *    when the build is performed right at the end of scanning.
 *    
 * Rule 3)
 *    there MUST always be a setter and a getter method, if either one is missing
 *    then an exception is thrown as the schema is invalid.
 *    
 * Rule 4)
 *    
 * 
 * 
 * @author Niall Gallagher
 *
 */
public class MethodScanner {
   
   private ContactList list;
   
   private PartMap write;
   
   private PartMap read;
   
   private Class type;
   
   public MethodScanner(Class type) {
      this.list = new ContactList();
      this.write = new PartMap();
      this.read = new PartMap();
      this.type = type;
   }
   
   public ContactList getContacts() {
      return list;
   }
   
   public void run() throws Exception {
      scan(type);
      build();
      validate();
   }
   
   private void scan(Class type) throws Exception {
      Class real = type;
      
      do {           
         scan(real, type);
         type = type.getSuperclass();
      }
      while(type != null);    
   }
   
   private void scan(Class real, Class type) throws Exception {
      Method[] method = type.getDeclaredMethods();

      for(int i = 0; i < method.length; i++) {
         scan(method[i]);              
      }     
   }
   
   public void scan(Method method) throws Exception {
      Annotation[] list = method.getDeclaredAnnotations();
      
      for(int i = 0; i < list.length; i++) {
         scan(method, list[i]);                       
      }  
   }
   
   public void scan(Method method, Annotation label) throws Exception {
      if(label instanceof Attribute) {
         process(method, label);
      }
      if(label instanceof ElementList) {
         process(method, label);
      }
      if(label instanceof ElementArray) {
         process(method, label);
      }
      if(label instanceof Element) {
         process(method, label);
      }             
      if(label instanceof Text) {
         process(method, label);
      }
   }
   
   public void process(Method method, Annotation label) throws Exception {
      if(isGet(method)) {
         process(new ReadPart(method, label), label);
      } 
      if(isSet(method)) {
         process(new WritePart(method, label), label);
      } 
   }
   
   public void process(ReadPart method, Annotation label) {
      if(!read.containsKey(label)) {
         read.put(label, method);
      }
   }
   
   public void process(WritePart method, Annotation label) {
      if(!write.containsKey(label)) {
         write.put(label, method);
      }
   }
   
   public void build() throws Exception {
      for(Annotation label : read) {
         MethodPart part = read.get(label);
         
         if(part != null) {
            build(part, label);
         }
      }
   }
   
   public void build(MethodPart read, Annotation label) throws Exception {
      MethodPart match = write.take(label);
      
      if(match == null) {
         throw new Exception("No setter");
      }
      Class type = match.getType();
      
      if(type != read.getType()) {
         throw new Exception("Match failure for "+type);
      }      
      list.add(new MethodContact(read, match));      
   }   
   
   public void validate() throws Exception {
      for(Annotation label : write) {
         MethodPart part = write.get(label);
         
         if(part != null) {
            throw new Exception("No get for " + label);
         }
      }
   }
   
   public boolean isGet(Method method) throws Exception {
      String name = method.getName();
      
      if(!name.startsWith("get")) {
         return false;
      }
      Class type = method.getReturnType();
         
      if(type == Void.class) {
         throw new Exception("Void");
      }
      return true;
   }
   
   public boolean isSet(Method method) throws Exception {
      String name = method.getName();
      
      if(!name.startsWith("set")) {
         return false;
      }
      Class[] list = method.getParameterTypes();
         
      if(list.length != 1) {
         throw new Exception("Incorrect signature");
      }
      return true;
   }
   
   public class PartMap extends HashMap<Annotation, MethodPart> implements Iterable<Annotation>{
      
      public Iterator<Annotation> iterator() {
         return keySet().iterator();
      }
      
      public MethodPart take(Annotation label) {
         return remove(label);
      }
   }

}
