/*
 * MethodScanner.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package simple.xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import simple.xml.ElementArray;
import simple.xml.ElementList;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Text;
import java.util.Iterator;
import java.util.HashMap;

/**
 * The <code>MethodScanner</code> object is used to scan an object 
 * for matching get and set methods for an XML annotation. This will
 * scan for annotated methods starting with the most specialized
 * class up the class heirarchy. Thys, Annotated methods can be 
 * overridden in a type specialization.
 * <p>
 * The annotated methods must be either a getter or setter method
 * following the Java Beans naming conventions. This convention is
 * such that a method must begin with "get", "set", or "is". A pair
 * of set and get methods for an annotation must make use of the
 * same type. For instance if the return type for the get method
 * was <code>String</code> then the set method must have a single
 * argument parameter that takes a <code>String.class</code> type.
 * <p>
 * For a method to be considered there must be both the get and set
 * methods. If either method is missing then the scanner fails with
 * an exception. Also, if an annotation marks a method which does
 * not follow Java Bean naming conventions an exception is thrown.
 *    
 * @author Niall Gallagher
 */
final class MethodScanner extends ContactList {
  
   /**
    * This is used to collect all the set methods from the object.
    */
   private PartMap write;
   
   /**
    * This is used to collect all the get methods from the object.
    */
   private PartMap read;
   
   /**
    * This is the type of the object that is being scanned.
    */
   private Class type;
   
   /**
    * Constructor for the <code>MethodScanner</code> object. This is
    * used to create an object that will scan the specified class
    * such that all bean property methods can be paired under the
    * XML annotation specified within the class.
    * 
    * @param type this is the type that is to be scanned for methods
    * 
    * @throws Exception thrown if there was a problem scanning
    */
   public MethodScanner(Class type) throws Exception {
      this.write = new PartMap();
      this.read = new PartMap();
      this.type = type;
      this.scan(type);
   }
   
   /**
    * This method is used to scan the class heirarchy for each class
    * in order to extract methods that contain XML annotations. If
    * a method is annotated it is converted to a contact so that
    * it can be used during serialization and deserialization.
    * 
    * @param type this is the type to be scanned for methods
    * 
    * @throws Exception thrown if the object schema is invalid
    */
   private void scan(Class type) throws Exception {
      Class real = type;
      
      while(type != null) {         
         scan(real, type);
         type = type.getSuperclass();
      }
      build();
      validate();
   }
   
   /**
    * This is used to scan the declared methods within the specified
    * class. Each method will be check to determine if it contains
    * an XML element and can be used as a <code>Contact</code> for
    * an entity within the object.
    * 
    * @param real this is the actual type of the object scanned
    * @param type this is one of the super classes for the object
    * 
    * @throws Exception thrown if the class schema is invalid
    */
   private void scan(Class real, Class type) throws Exception {
      Method[] method = type.getDeclaredMethods();

      for(int i = 0; i < method.length; i++) {
         scan(method[i]);              
      }     
   }
   
   /**
    * This is used to scan all annotations within the given method.
    * Each annotation is checked against the set of supported XML
    * annotations. If the annotation is one of the XML annotations
    * then the method is considered for acceptance as either a
    * get method or a set method for the annotated property.
    * 
    * @param method the method to be scanned for XML annotations
    * 
    * @throws Exception if the method is not a Java Bean method
    */
   private void scan(Method method) throws Exception {
      Annotation[] list = method.getDeclaredAnnotations();
      
      for(int i = 0; i < list.length; i++) {
         scan(method, list[i]);                       
      }  
   }
   
   /**
    * This reflectively checks the annotation to determine the type 
    * of annotation it represents. If it represents an XML schema
    * annotation it is used to create a <code>Contact</code> which 
    * can be used to represent the method within the source object.
    * 
    * @param method the method that the annotation comes from
    * @param label the annotation used to model the XML schema
    * 
    * @throws Exception if there is more than one text annotation
    */ 
   private void scan(Method method, Annotation label) throws Exception {
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
  
   /**
    * This is used to classify the specified method into either a get
    * or set method. If the method is neither then an exception is
    * thrown to indicate that the XML annotations can only be used
    * with methods following the Java Bean naming conventions. Once
    * the method is classified is is added to either the read or 
    * write map so that it can be paired after scanning is complete.
    * 
    * @param method this is the method that is to be classified
    * @param label this is the annotation applied to the method
    */  
   private void process(Method method, Annotation label) throws Exception {
      MethodType type = getType(method);
      
      if(type == MethodType.GET) {
         process(new ReadPart(method, label), label, read);
      }
      if(type == MethodType.SET) {
         process(new WritePart(method, label), label, write);
      }
      if(type == MethodType.NORMAL) {
         throw new MethodException("Annotation %s must mark a set or get method", label);
      }
   }  
   
   /**
    * This is used to determine whether the specified method can be
    * inserted into the given <code>PartMap</code>. This ensures 
    * that there can not be two of the same annotations within the
    * same class in the heirarchy.
    * 
    * @param method this is the method part that is to be inserted
    * @param label this is the annotation that labels the method
    * @param map this is the part map used to contain the method
    */
   private void process(MethodPart method, Annotation label, PartMap map) {
      if(!map.containsKey(label)) {
         map.put(label, method);
      }
   }
   
   /**
    * This method is used to pair the get methods with a matching set
    * method. This pairs methods using the annotation, the annotation
    * must match exactly, meaning all attributes for the annotation
    * must be the same constants. If there is not an exact match then
    * the get and set methods will not match.
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build() throws Exception {
      for(Annotation label : read) {
         MethodPart part = read.get(label);
         
         if(part != null) {
            build(part, label);
         }
      }
   }
   
   /**
    * This method is used to pair the get methods with a matching set
    * method. This pairs methods using the annotation, the annotation
    * must match exactly, meaning all attributes for the annotation
    * must be the same constants. If there is not an exact match then
    * the get and set methods will not match.
    * 
    * @param read this is a get method that has been extracted
    * @param label this is the annotation matching the get method
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build(MethodPart read, Annotation label) throws Exception {
      MethodPart match = write.take(label);
      
      if(match == null) {
         throw new MethodException("No matching set method for %s in %s", label, type);
      }
      Class type = match.getType();
      
      if(type != read.getType()) {
         throw new MethodException("Method types do not match for %s in %s", label, type);
      }      
      add(new MethodContact(read, match));      
   }   
   
   /**
    * This is used to validate the object once all the get methods
    * have been matched with a set method. This ensures that there
    * is not a set method within the object that does not have a
    * match, therefore violating the contract of a property.
    * 
    * @throws Exception thrown if there is a unmatched set method
    */
   private void validate() throws Exception {
      for(Annotation label : write) {
         MethodPart part = write.get(label);
         
         if(part != null) {
            throw new MethodException("No matching get method for %s, in %s", label, type);
         }
      }
   }
   
   /**
    * This is used to classify a method. Methods are classified with
    * a combination of the method name and arguments. If a method
    * follows the Java Bean naming conventions then a method 
    * starting with "get" or "is" that does not have parameters is a
    * getter and a method that begins with "set" is a setter method.
    * 
    * @param method this is the method that is to be classified
    *
    * @return this returns the type that the specified method is
    */ 
   private MethodType getType(Method method) throws Exception {
      if(isGet(method)) {
         return MethodType.GET;
      }
      if(isSet(method)) {
         return MethodType.SET;              
      }      
      return MethodType.NORMAL;
   }
  
   /**
    * This is used to determine if the specified method is a get
    * method that follows the Java Bean naming convention. In order
    * for a method to qualify as a get method is must accept no
    * parameters and return a type other than void, also the name
    * of the method must begin with "get" or "is". 
    *
    * @param method this is the method that is to be checked
    * 
    * @return this returns true if the method specified is a get
    * 
    * @throws Exception if the naming conventions are followed but
    *                   the method contains parameters
    */ 
   private boolean isGet(Method method) throws Exception {
      String name = method.getName();
      
      if(!name.startsWith("get") && !name.startsWith("is")){
         return false;
      }
      Class[] list = method.getParameterTypes();
         
      if(list.length != 0) {
         throw new MethodException("Get method %s in %s contains parameters", name, type);
      }
      return true;
   }
   
   /**
    * This is used to determine if the specified method is a set
    * method that follows the Java Bean naming convention. In order
    * for a method to qualify as a set method it must accept a 
    * single argument parameter and begin with the string "set".
    *  
    * @param method this is the method that is to be checked
    * 
    * @return this returns true if the method specified is a set
    * 
    * @throws Exception if the naming conventions are followed but
    *                   it accepts the wrong number of parameters
    */
   private boolean isSet(Method method) throws Exception {
      String name = method.getName();
      
      if(!name.startsWith("set")) {
         return false;
      }
      Class[] list = method.getParameterTypes();
         
      if(list.length != 1) {
         throw new MethodException("Set method %s has invalid signature in %s", name, type);         
      }
      return true;
   }
   
   /**
    * The <code>PartMap</code> is used to contain method parts using
    * the annotation for that method part. This allows annotations
    * to for the method parts to be iterated in a convinient manner
    * such that the keyed method parts can be extracted easily. 
    */
   private class PartMap extends HashMap<Annotation, MethodPart> implements Iterable<Annotation>{
      
      /**
       * This returns an iterator for <code>Annotation</code> objects
       * which act as keys to method parts of a Java Bean property.
       * 
       * @return this returns an iterator for the annotation keys
       */
      public Iterator<Annotation> iterator() {
         return keySet().iterator();
      }
      
      /**
       * This is used to acquire the method part for the specified
       * annotation. This will remove the method part from this map
       * so that it can be checked later to ensure what remains.
       * 
       * @param label this is the annotation to get the method with
       * 
       * @return this returns the method part for the given key
       */
      public MethodPart take(Annotation label) {
         return remove(label);
      }
   }

   /**
    * This is used to classify a method. Methods are classified with
    * a combination of the method name and arguments. If a method
    * follows the Java Bean naming conventions then a method 
    * starting with "get" or "is" that does not have parameters is a
    * getter and a method that begins with "set" is a setter method.
    */ 
   private enum MethodType {
      NORMAL,
      GET,
      SET      
   }

}
