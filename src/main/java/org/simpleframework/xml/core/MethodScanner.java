/*
 * MethodScanner.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import static org.simpleframework.xml.DefaultType.PROPERTY;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.Version;

/**
 * The <code>MethodScanner</code> object is used to scan an object 
 * for matching get and set methods for an XML annotation. This will
 * scan for annotated methods starting with the most specialized
 * class up the class hierarchy. Thus, annotated methods can be 
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
    * This is a factory used for creating property method parts.
    */
   private final MethodPartFactory factory;
   
   /**
    * This is used to acquire the hierarchy for the class scanned.
    */
   private final Hierarchy hierarchy;
   
   /**
    * This is the default access type to be used for this scanner.
    */
   private final DefaultType access;
   
   /**
    * This is used to collect all the set methods from the object.
    */
   private final PartMap write;
   
   /**
    * This is used to collect all the get methods from the object.
    */
   private final PartMap read;
   
   /**
    * This is the type of the object that is being scanned.
    */
   private final Class type;
   
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
      this(type, null);
   }
   
   /**
    * Constructor for the <code>MethodScanner</code> object. This is
    * used to create an object that will scan the specified class
    * such that all bean property methods can be paired under the
    * XML annotation specified within the class.
    * 
    * @param type this is the type that is to be scanned for methods
    * @param access this is the access type for default values
    * 
    * @throws Exception thrown if there was a problem scanning
    */
   public MethodScanner(Class type, DefaultType access) throws Exception {
      this(type, access, true);
   }
   
   /**
    * Constructor for the <code>MethodScanner</code> object. This is
    * used to create an object that will scan the specified class
    * such that all bean property methods can be paired under the
    * XML annotation specified within the class.
    * 
    * @param type this is the type that is to be scanned for methods
    * @param access this is the access type for default values
    * @param required used to determine if defaults are required
    * 
    * @throws Exception thrown if there was a problem scanning
    */
   public MethodScanner(Class type, DefaultType access, boolean required) throws Exception {
      this.factory = new MethodPartFactory(required);
      this.hierarchy = new Hierarchy(type);
      this.write = new PartMap();
      this.read = new PartMap();
      this.access = access;
      this.type = type;
      this.scan(type);
   }
   
   /**
    * This method is used to scan the class hierarchy for each class
    * in order to extract methods that contain XML annotations. If
    * a method is annotated it is converted to a contact so that
    * it can be used during serialization and deserialization.
    * 
    * @param type this is the type to be scanned for methods
    * 
    * @throws Exception thrown if the object schema is invalid
    */
   private void scan(Class type) throws Exception {
      for(Class next : hierarchy) {
         scan(next, access);
      }
      for(Class next : hierarchy) {
         scan(next, type);
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
   private void scan(Class type, Class real) throws Exception {
      Method[] list = type.getDeclaredMethods();

      for(Method method : list) {
         scan(method);              
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
      
      for(Annotation label : list) {
         scan(method, label);                       
      }  
   }
   
   /**
    * This is used to scan all the methods of the class in order to
    * determine if it should have a default annotation. If the method
    * should have a default XML annotation then it is added to the
    * list of contacts to be used to form the class schema.
    * 
    * @param type this is the type to have its methods scanned
    * @param access this is the default access type for the class
    */
   private void scan(Class type, DefaultType access) throws Exception {
      Method[] list = type.getDeclaredMethods();

      if(access == PROPERTY) {
         for(Method method : list) {
            Class value = factory.getType(method);
            
            if(value != null) {
               process(method);
            }
         }  
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
      if(label instanceof ElementUnion) {
         process(method, label);
      }
      if(label instanceof ElementListUnion) {
         process(method, label);
      }
      if(label instanceof ElementMapUnion) {
         process(method, label);
      }
      if(label instanceof ElementList) {
         process(method, label);
      }
      if(label instanceof ElementArray) {
         process(method, label);
      }
      if(label instanceof ElementMap) {
         process(method, label);
      }
      if(label instanceof Element) {
         process(method, label);
      }    
      if(label instanceof Transient) {
         remove(method, label);
      }
      if(label instanceof Version) {
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
      MethodPart part = factory.getInstance(method, label);
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
    * This is used to classify the specified method into either a get
    * or set method. If the method is neither then an exception is
    * thrown to indicate that the XML annotations can only be used
    * with methods following the Java Bean naming conventions. Once
    * the method is classified is is added to either the read or 
    * write map so that it can be paired after scanning is complete.
    * 
    * @param method this is the method that is to be classified
    */  
   private void process(Method method) throws Exception {
      MethodPart part = factory.getInstance(method);
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
      
      if(name != null) {
         map.put(name, method);
      }
   }
   
   /**
    * This method is used to remove a particular method from the list
    * of contacts. If the <code>Transient</code> annotation is used
    * by any method then this method must be removed from the schema.
    * In particular it is important to remove methods if there are
    * defaults applied to the class.
    * 
    * @param method this is the method that is to be removed
    * @param label this is the label associated with the method
    */
   private void remove(Method method, Annotation label) throws Exception {
      MethodPart part = factory.getInstance(method, label);
      MethodType type = part.getMethodType();     
      
      if(type == MethodType.GET) {
         remove(part, read);
      }
      if(type == MethodType.IS) {
         remove(part, read);
      }
      if(type == MethodType.SET) {
         remove(part, write);
      }
   } 
   
   /**
    * This is used to remove the method part from the specified map.
    * Removal is performed using the name of the method part. If it
    * has been scanned and added to the map then it will be removed
    * and will not form part of the class schema.
    * 
    * @param part this is the part to be removed from the map 
    * @param map this is the map to removed the method part from
    */
   private void remove(MethodPart part, PartMap map) throws Exception {
      String name = part.getName();
      
      if(name != null) {
         map.remove(name);
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
    * @param name this is the Java Bean methods name to be matched   
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build(MethodPart read, String name) throws Exception {      
      MethodPart match = write.take(name);

      if(match != null) {
         build(read, match);
      } else {
         build(read); // read only
      }
   }   
   
   /**
    * This method is used to create a read only contact. A read only
    * contact object is used when there is constructor injection used
    * by the class schema. So, read only methods can be used in a 
    * fully serializable and deserializable object.
    * 
    * @param read this is the part to add as a read only contact      
    *  
    * @throws Exception thrown if there is a problem matching methods
    */
   private void build(MethodPart read) throws Exception {
      add(new MethodContact(read));
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
    * @param write this is a get method that has been extracted
    * @param name this is the Java Bean methods name to be matched 
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
    * @see org.simpleframework.xml.core.MethodPart
    */
   private class PartMap extends LinkedHashMap<String, MethodPart> implements Iterable<String>{
      
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