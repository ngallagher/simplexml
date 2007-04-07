package simple.xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementArray;
import simple.xml.ElementList;
import simple.xml.Root;
import simple.xml.Text;

public class XScanner  {
   
   private FieldScanner fields;
   
   private MethodScanner methods;

   private LabelMap attributes;

   private LabelMap elements;

   private Method commit;
   
   private Method validate;

   private Method persist;

   private Method complete;   

   private Label text;

   private Root root;
   

   public XScanner(Class type) throws Exception {  
      this.attributes = new LabelMap(null);
      this.elements = new LabelMap(null);      
      this.scan(type);
   }       
   

   private void scan(Class type) throws Exception {
      Class real = type;
      
      while(type != null) {
         if(root == null) {              
            root(type);
         }            
         scan(type, real);
         type = type.getSuperclass();
      }
      field(type);
      method(type);
      validate(type);
   }
   
   private void scan(Class real, Class type) throws Exception {
      Method[] method = type.getDeclaredMethods();

      for(int i = 0; i < method.length; i++) {
         scan(method[i]);              
      }      
   }
   
   
   private void validate(Class type) throws Exception {
      if(text != null) {
         if(!elements.isEmpty()) {
            throw new TextException("Elements used with %s in %s", text, type);
         }
      }
   }
   
   private void root(Class type) {
      if(type.isAnnotationPresent(Root.class)) {
          root = (Root)type.getAnnotation(Root.class);
      }
   }
   
   public void field(Class type) throws Exception {
      ContactList list = new FieldScanner(type);
      
      for(Contact contact : list) {
         scan(contact, contact.getAnnotation());
      }
   }
   
   
   public void method(Class type) throws Exception {
      ContactList list = new MethodScanner(type);
      
      for(Contact contact : list) {           
         scan(contact, contact.getAnnotation());
      }
   }
   
   private void scan(Contact field, Annotation label) throws Exception {
      if(label instanceof Attribute) {
         process(field, label, attributes);
      }
      if(label instanceof ElementList) {
         process(field, label, elements);
      }
      if(label instanceof ElementArray) {
         process(field, label, elements);
      }
      if(label instanceof Element) {
         process(field, label, elements);
      }             
      if(label instanceof Text) {
         process(field, label);
      }
   }
   
   private void process(Contact field, Annotation type) throws Exception {
      //Label label = LabelFactory.getInstance(field, type);
      
      if(text != null) {
         throw new TextException("Multiple text annotations in %s", type);
      }
      //text = label;
   }
   
   private void process(Contact field, Annotation type, Map map) throws Exception {
     //Label label = LabelFactory.getInstance(field, type);
     //String name = label.getName().toLowerCase();
      
      //if(map.containsKey(name)) {
      //   throws new Exception("Annotation of name %s declared twice", name);
      //}
      //map.put(name, label);      
   }
   
   private void scan(Method method) {
      if(commit == null) {           
         commit(method);
      }
      if(validate == null) {      
         validate(method);
      }
      if(persist == null) {      
         persist(method);
      }
      if(complete == null) {      
         complete(method);
      }        
   }

   private void commit(Method method) {
      Annotation mark = method.getAnnotation(Commit.class);

      if(mark != null) {
         commit = method;                    
      }      
   }

   private void validate(Method method) {
      Annotation mark = method.getAnnotation(Validate.class);

      if(mark != null) {
         validate = method;                    
      }      
   }
   
   private void persist(Method method) {
      Annotation mark = method.getAnnotation(Persist.class);

      if(mark != null) {
         persist = method;                    
      }      
   }
 
   private void complete(Method method) {
      Annotation mark = method.getAnnotation(Complete.class);

      if(mark != null) {
         complete = method;                    
      }      
   }      
}