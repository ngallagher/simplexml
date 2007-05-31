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

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
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
 * class up the class heirarchy. Thus, annotated methods can be 
 * overridden in a type specialization.
 * <p>
 * The annotated methods must be either a getter or setter method
 * following the Java Beans naming conventions. This convention is
 * such that a method must begin with "get", "set", or "is". A pair
 * of set and get methods for an annotation must make use of the
 * same type. For instance if the return type for the get method
 * was <code>String</code> then the set method must have a single
 * argument parameter that takes a <code>String</code> type.
 * <p>
 * For a method to be considered there must be both the get and set
 * methods. If either method is missing then the scanner fails with
 * an exception. Also, if an annotation marks a method which does
 * not follow Java Bean naming conventions an exception is thrown.
 *    
 * @author Niall Gallagher
 */
class MethodScanner extends ContactList {
                  
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
    * class. Each method will be checked to determine if it contains
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
      MethodPart part = MethodPartFactory.getInstance(method, label);
      MethodType type = part.getMethodType();     
      
      if(type == MethodType.GET) {
         process(part, read);
      }
      if(type == MethodType.IS) {
         process(part, read);
      }
      if(type == MethodType.SET) {
         process(part, write);
      }
   } 
   
   /**
    * This is used to determine whether the specified method can be
    * inserted into the given <code>PartMap</code>. This ensures 
    * that only the most specialized method is considered, which 
    * enables annotated methods to be overridden in subclasses.
    * 
    * @param method this is the method part that is to be inserted
    * @param map this is the part map used to contain the method
    */
   private void process(MethodPart method, PartMap map) {
      String name = method.getName();
      
      if(!map.containsKey(name)) {
         map.put(name, method);
      }
   }
   
   /**
    * This method is used to pair the get methods with a matching set
    * method. This pairs methods using the Java Bean method name, the
    * names must match exactly, meaning that the case and value of
    * the strings must be identical. Also in order for this to succeed
    * the types for the methods and the annotation must also match.
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build() throws Exception {
      for(String name : read) {
         MethodPart part = read.get(name);
         
         if(part != null) {
            build(part, name);
         }
      }
   }
   
   /**
    * This method is used to pair the get methods with a matching set
    * method. This pairs methods using the Java Bean method name, the
    * names must match exactly, meaning that the case and value of
    * the strings must be identical. Also in order for this to succeed
    * the types for the methods and the annotation must also match.
    * 
    * @param read this is a get method that has been extracted
    * @param name this is the Java Bean methos name to be matched   
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build(MethodPart read, String name) throws Exception {      
      MethodPart match = write.take(name);
      Method method = read.getMethod();
      
      if(match == null) {
         throw new MethodException("No matching set method for %s in %s", method, type);
      } 
      build(read, match);     
   }   
   
   /**
    * This method is used to pair the get methods with a matching set
    * method. This pairs methods using the Java Bean method name, the
    * names must match exactly, meaning that the case and value of
    * the strings must be identical. Also in order for this to succeed
    * the types for the methods and the annotation must also match.
    * 
    * @param read this is a get method that has been extracted
    * @param write this is the write method to compare details with      
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build(MethodPart read, MethodPart write) throws Exception {
      Annotation label = read.getAnnotation();
      String name = read.getName();
      
      if(!write.getAnnotation().equals(label)) {
         throw new MethodException("Annotations do not match for '%s' in %s", name, type);
      }
      Class type = read.getType();
      
      if(type != write.getType()) {
         throw new MethodException("Method types do not match for %s in %s", name, type);
      }
      add(new MethodContact(read, write));
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
      for(String name : write) {
         MethodPart part = write.get(name);
         
         if(part != null) {
            validate(part, name);
         }
      }
   }
   
   /**
    * This is used to validate the object once all the get methods
    * have been matched with a set method. This ensures that there
    * is not a set method within the object that does not have a
    * match, therefore violating the contract of a property.
    * 
    * @param read this is a get method that has been extracted
    * @param name this is the Java Bean methos name to be matched 
    * 
    * @throws Exception thrown if there is a unmatched set method
    */
   private void validate(MethodPart write, String name) throws Exception {      
      MethodPart match = read.take(name);     
      Method method = write.getMethod();      
         
      if(match == null) {
         throw new MethodException("No matching get method for %s in %s", method, type);
      }      
   }
   
   /**
    * The <code>PartMap</code> is used to contain method parts using
    * the Java Bean method name for the part. This ensures that the
    * scanned and extracted methods can be acquired using a common 
    * name, which should be the parsed Java Bean method name.
    * 
    * @see simple.xml.load.MethodPart
    */
   private class PartMap extends HashMap<String, MethodPart> implements Iterable<String>{
      
      /**
       * This returns an iterator for the Java Bean method names for
       * the <code>MethodPart</code> objects that are stored in the
       * map. This allows names to be iterated easily in a for loop.
       * 
       * @return this returns an iterator for the method name keys
       */
      public Iterator<String> iterator() {
         return keySet().iterator();
      }
      
      /**
       * This is used to acquire the method part for the specified
       * method name. This will remove the method part from this map
       * so that it can be checked later to ensure what remains.
       * 
       * @param name this is the method name to get the method with       
       * 
       * @return this returns the method part for the given key
       */
      public MethodPart take(String name) {
         return remove(name);
      }
   }
}