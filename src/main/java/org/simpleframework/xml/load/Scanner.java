/*
 * Scanner.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.load;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.beans.Introspector;

/**
 * The <code>Scanner</code> object performs the reflective inspection
 * of a class and builds a map of attributes and elements for each
 * annotated field. This acts as a cachable container for reflection
 * actions performed on a specific type. When scanning the provided
 * class this inserts the scanned field as a <code>Label</code> in to
 * a map so that it can be retrieved by name. Annotations classified
 * as attributes have the <code>Attribute</code> annotation, all other
 * annotated fields are stored as elements.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.Schema
 */ 
class Scanner  {
  
   /**
    * This is used to store all labels that are XML attributes.
    */
   private LabelMap attributes;
   
   /**
    * This is used to store all labels that are XML elements.
    */
   private LabelMap elements;

   /**
    * This method acts as a pointer to the types commit process.
    */
   private Method commit;
   
   /**
    * This method acts as a pointer to the types validate process.
    */
   private Method validate;

   /**
    * This method acts as a pointer to the types persist process.
    */
   private Method persist;

   /**
    * This method acts as a pointer to the types complete process.
    */
   private Method complete;   
   
   /**
    * This method is used as a pointer to the replacement method.
    */
   private Method replace;
   
   /**
    * This method is used as a pointer to the resolution method.
    */
   private Method resolve;

   /**
    * This is used to store all labels that are XML text values.
    */
   private Label text;
   
   /**
    * This is the optional order annotation for the scanned class.
    */
   private Order order;

   /**
    * This is the optional root annotation for the scanned class.
    */
   private Root root;
   
   /**
    * This is the name of the class as taken from the root class.
    */
   private String name;
   
   /**
    * This is used to specify whether the type is a primitive class.
    */
   private boolean primitive;
   
   /**
    * Constructor for the <code>Schema</code> object. This is used 
    * to scan the provided class for annotations that are used to
    * build a schema for an XML file to follow. 
    * 
    * @param type this is the type that is scanned for a schema
    */
   public Scanner(Class type) throws Exception {           
      this.attributes = new LabelMap(this);
      this.elements = new LabelMap(this);      
      this.scan(type);
   }       

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML attributes. This returns a new map
    * each time the method is called, the goal is to ensure that any
    * object using the label map can manipulate it without changing
    * the core details of the schema, allowing it to be cached.
    * 
    * @param source this is the source used to style the names 
    *
    * @return map with the details extracted from the schema class
    */ 
   public LabelMap getAttributes(Source source) throws Exception {
      return attributes.clone(source);
   }        

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML elements. The annotations that are
    * considered elements are the <code>ElementList</code> and the
    * <code>Element</code> annotations. This returns a copy of the
    * details extracted from the schema class so this can be cached.
    * 
    * @param source this is the source used to style the names
    *
    * @return a map containing the details for XML elements
    */
   public LabelMap getElements(Source source) throws Exception {
      return elements.clone(source);
   }
   
   /**
    * This returns the <code>Label</code> that represents the text
    * annotation for the scanned class. Only a single text annotation
    * can be used per class, so this returns only a single label
    * rather than a <code>LabelMap</code> object. Also if this is
    * not null then the elements label map must be empty.
    * 
    * @return this returns the text label for the scanned class
    */
   public Label getText() {
      return text;
   }
   
   /**
    * This returns the name of the class processed by this scanner.
    * The name is either the name as specified in the last found
    * <code>Root</code> annotation, or if a name was not specified
    * within the discovered root then the Java Bean class name of
    * the last class annotated with a root annotation.
    * 
    * @return this returns the name of the object being scanned
    */
   public String getName() {
      return name;
   }

   /**
    * This method is used to retrieve the schema class commit method
    * during the deserialization process. The commit method must be
    * marked with the <code>Commit</code> annotation so that when the
    * object is deserialized the persister has a chance to invoke the
    * method so that the object can build further data structures.
    * 
    * @return this returns the commit method for the schema class
    */
   public Method getCommit() {
      return commit;           
   }
   
   /**
    * This method is used to return the <code>Conduit</code> for this
    * class. The conduit is a means to deliver invocations to the
    * object for the persister callback methods. It aggregates all of
    * the persister callback methods in to a single object.
    * 
    * @return this returns a conduit used for delivering callbacks
    */
   public Conduit getConduit() {
      return new Conduit(this);
   }

   /**
    * This method is used to retrieve the schema class validation
    * method during the deserialization process. The validation method
    * must be marked with the <code>Validate</code> annotation so that
    * when the object is deserialized the persister has a chance to 
    * invoke that method so that object can validate its field values.
    * 
    * @return this returns the validate method for the schema class
    */   
   public Method getValidate() {
      return validate;       
   }
   
   /**
    * This method is used to retrieve the schema class persistence
    * method. This is invoked during the serialization process to
    * get the object a chance to perform an nessecary preparation
    * before the serialization of the object proceeds. The persist
    * method must be marked with the <code>Persist</code> annotation.
    * 
    * @return this returns the persist method for the schema class
    */
   public Method getPersist() {
      return persist;           
   }

   /**
    * This method is used to retrieve the schema class completion
    * method. This is invoked after the serialization process has
    * completed and gives the object a chance to restore its state
    * if the persist method required some alteration or locking.
    * This is marked with the <code>Complete</code> annotation.
    * 
    * @return returns the complete method for the schema class
    */   
   public Method getComplete() {
      return complete;           
   }
   
   /**
    * This method is used to retrieve the schema class replacement
    * method. The replacement method is used to substitute an object
    * that has been deserialized with another object. This allows
    * a seamless delegation mechanism to be implemented. This is
    * marked with the <code>Replace</code> annotation. 
    * 
    * @return returns the replace method for the schema class
    */
   public Method getReplace() {
      return replace;
   }
   
   /**
    * This method is used to retrieve the schema class replacement
    * method. The replacement method is used to substitute an object
    * that has been deserialized with another object. This allows
    * a seamless delegation mechanism to be implemented. This is
    * marked with the <code>Replace</code> annotation. 
    * 
    * @return returns the replace method for the schema class
    */
   public Method getResolve() {
      return resolve;
   }

   /**
    * This is used to determine whether the scanned class represents
    * a primitive type. A primitive type is a type that contains no
    * XML annotations and so cannot be serialized with an XML form.
    * Instead primitives a serialized using transformations.
    * 
    * @return this returns true if no XML annotations were found
    */
   public boolean isPrimitive() {
      return primitive;
   }
   
   /**
    * This is used to determine whether the scanned class represents
    * a primitive type. A primitive type is a type that contains no
    * XML annotations and so cannot be serialized with an XML form.
    * Instead primitives a serialized using transformations.
    * 
    * @return this returns true if no XML annotations were found
    */
   private boolean isEmpty() {
      if(!elements.isEmpty()) {
         return false;
      }
      if(!attributes.isEmpty()) {
         return false;
      }
      if(text != null) {
         return false;
      }
      return root == null;
   }
   
   /**
    * This method is used to determine whether strict mappings are
    * required. Strict mapping means that all labels in the class
    * schema must match the XML elements and attributes in the
    * source XML document. When strict mapping is disabled, then
    * XML elements and attributes that do not exist in the schema
    * class will be ignored without breaking the parser.
    *
    * @return true if strict parsing is enabled, false otherwise
    */ 
   public boolean isStrict() {
      if(root != null) {
         return root.strict();              
      }              
      return true;
   }
  
   /**
    * Scan the fields and methods such that the given class is scanned 
    * first then all super classes up to the root <code>Object</code>. 
    * All fields and methods from the most specialized classes override 
    * fields and methods from higher up the inheritance heirarchy. This
    * means that annotated details can be overridden and so may not 
    * have a value assigned to them during deserialization.
    * 
    * @param type the class to extract fields and methods from
    */   
   private void scan(Class type) throws Exception {
      Class real = type;
      
      while(type != null) {
         if(root == null) {              
            root(type);
         }     
         if(order == null) {
            order(type);
         }
         scan(real, type);
         type = type.getSuperclass();
      }      
      process(real); 
   }

   /**
    * This is used to scan the specified class for methods so that
    * the persister callback annotations can be collected. These
    * annotations help object implementations to validate the data
    * that is injected into the instance during deserialization.
    * 
    * @param real this is the actual type of the scanned class 
    * @param type this is a type from within the class heirarchy
    * 
    * @throws Exception thrown if the class schema is invalid
    */
   private void scan(Class real, Class type) throws Exception {
      Method[] method = type.getDeclaredMethods();

      for(int i = 0; i < method.length; i++) {
         Method next = method[i];
         
         if(!next.isAccessible()) {
            next.setAccessible(true);
         }
         scan(next);              
      }     
   }
   
   /**
    * This is used to validate the configuration of the scanned class.
    * If a <code>Text</code> annotation has been used with elements
    * then validation will fail and an exception will be thrown. 
    * 
    * @param type this is the object type that is being scanned
    * 
    * @throws Exception if text and element annotations are present
    */
   private void validate(Class type) throws Exception {
      if(text != null) {
         if(!elements.isEmpty()) {
            throw new TextException("Elements used with %s in %s", text, type);
         }
      }  else {
         primitive = isEmpty();
      }
      if(order != null) {
         validateElements(type);
         validateAttributes(type);
      }
   }
   
   /**
    * This is used to validate the configuration of the scanned class.
    * If an ordered element is specified but does not refer to an
    * existing element then this will throw an exception.
    * 
    * @param type this is the object type that is being scanned
    * 
    * @throws Exception if an ordered element does not exist
    */
   private void validateElements(Class type) throws Exception {
      for(String name : order.elements()) {
         Label label = elements.get(name);
         
         if(label == null) {
            throw new ElementException("Ordered element '%s' missing for %s", name, type);
         }
      }
   }
   
   /**
    * This is used to validate the configuration of the scanned class.
    * If an ordered attribute is specified but does not refer to an
    * existing attribute then this will throw an exception.
    * 
    * @param type this is the object type that is being scanned
    * 
    * @throws Exception if an ordered attribute does not exist
    */
   private void validateAttributes(Class type) throws Exception {
      for(String name : order.attributes()) {
         Label label = attributes.get(name);
         
         if(label == null) {
            throw new AttributeException("Ordered attribute '%s' missing for %s", name, type);
         }
      }
   } 

   /**
    * This is used to acquire the optional <code>Root</code> from the
    * specified class. The root annotation provides information as
    * to how the object is to be parsed as well as other information
    * such as the name of the object if it is to be serialized.
    *
    * @param type this is the type of the class to be inspected
    */    
   private void root(Class<?> type) {
      String real = type.getSimpleName();
      String text = real;

      if(type.isAnnotationPresent(Root.class)) {
         root = type.getAnnotation(Root.class);
         text = root.name();

         if(isEmpty(text)) {
            text = Introspector.decapitalize(real);
         }      
         name = text.intern();      
      }
   }
   
   /**
    * This is used to acquire the optional order annotation to provide
    * order to the elements and attributes for the generated XML. This
    * acts as an override to the order provided by the declaration of
    * the types within the object.  
    * 
    * @param type this is the type to be scanned for the order
    */
   private void order(Class<?> type) {
      if(type.isAnnotationPresent(Order.class)) {
         order = type.getAnnotation(Order.class);
         
         for(String name : order.elements()) {
            elements.put(name, null);            
         }
         for(String name : order.attributes()) {
            attributes.put(name, null);
         }
      }
   }
   
   /**
    * This method is used to determine if a root annotation value is
    * an empty value. Rather than determining if a string is empty
    * be comparing it to an empty string this method allows for the
    * value an empty string represents to be changed in future.
    * 
    * @param value this is the value to determine if it is empty
    * 
    * @return true if the string value specified is an empty value
    */
   private boolean isEmpty(String value) {
      return value.length() == 0;
   }
   
   /**
    * This is used to scan the specified object to extract the fields
    * and methods that are to be used in the serialization process.
    * This will acquire all fields and getter setter pairs that have
    * been annotated with the XML annotations.
    *
    * @param type this is the object type that is to be scanned
    */  
   private void process(Class type) throws Exception {
      field(type);
      method(type);
      validate(type);
   }
  
   /**
    * This is used to acquire the contacts for the annotated fields 
    * within the specified class. The field contacts are added to
    * either the attributes or elements map depending on annotation.
    * 
    * @param type this is the object type that is to be scanned
    */    
   public void field(Class type) throws Exception {
      ContactList list = new FieldScanner(type);
      
      for(Contact contact : list) {
         scan(contact, contact.getAnnotation());
      }
   }
   
   /**
    * This is used to acquire the contacts for the annotated fields 
    * within the specified class. The field contacts are added to
    * either the attributes or elements map depending on annotation.
    * 
    * @param type this is the object type that is to be scanned
    */ 
   public void method(Class type) throws Exception {
      ContactList list = new MethodScanner(type);
      
      for(Contact contact : list) {
         scan(contact, contact.getAnnotation());
      }
   }
   
   /**
    * This reflectively checks the annotation to determine the type 
    * of annotation it represents. If it represents an XML schema
    * annotation it is used to create a <code>Label</code> which can
    * be used to represent the field within the source object.
    * 
    * @param field the field that the annotation comes from
    * @param label the annotation used to model the XML schema
    * 
    * @throws Exception if there is more than one text annotation
    */   
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
      if(label instanceof ElementMap) {
         process(field, label, elements);
      }
      if(label instanceof Element) {
         process(field, label, elements);
      }             
      if(label instanceof Text) {
         process(field, label);
      }
   }
   
   /**
    * This is used to process the <code>Text</code> annotations that
    * are present in the scanned class. This will set the text label
    * for the class and an ensure that if there is more than one
    * text label within the class an exception is thrown.
    * 
    * @param field the field the annotation was extracted from
    * @param type the annotation extracted from the field
    * 
    * @throws Exception if there is more than one text annotation
    */   
   private void process(Contact field, Annotation type) throws Exception {
      Label label = LabelFactory.getInstance(field, type);
      
      if(text != null) {
         throw new TextException("Multiple text annotations in %s", type);
      }
      text = label;
   }
   
   /**
    * This is used when all details from a field have been gathered 
    * and a <code>Label</code> implementation needs to be created. 
    * This will build a label instance based on the field annotation.
    * If a label with the same name was already inserted then it is
    * ignored and the value for that field will not be serialized. 
    * 
    * @param field the field the annotation was extracted from
    * @param type the annotation extracted from the field
    * @param map this is used to collect the label instance created
    * 
    * @throws Exception thrown if the label can not be created
    */   
   private void process(Contact field, Annotation type, LabelMap map) throws Exception {
      Label label = LabelFactory.getInstance(field, type);
      String name = label.getName();
      
      if(map.get(name) != null) {
         throw new PersistenceException("Annotation of name '%s' declared twice", name);
      }
      map.put(name, label);      
   }
   
   /**
    * Scans the provided method for a persister callback method. If 
    * the method contains an method annotated as a callback that 
    * method is stored so that it can be invoked by the persister
    * during the serialization and deserialization process.
    * 
    * @param method this is the method to scan for callback methods
    */
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
      if(replace == null) {
         replace(method);              
      }   
      if(resolve == null) {
         resolve(method);              
      }  
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Replace</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void replace(Method method) {
      Annotation mark = method.getAnnotation(Replace.class);

      if(mark != null) {
         replace = method;                    
      }      
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Resolve</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void resolve(Method method) {
      Annotation mark = method.getAnnotation(Resolve.class);

      if(mark != null) {
         resolve = method;                    
      }      
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Commit</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void commit(Method method) {
      Annotation mark = method.getAnnotation(Commit.class);

      if(mark != null) {
         commit = method;                    
      }    
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Validate</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void validate(Method method) {
      Annotation mark = method.getAnnotation(Validate.class);

      if(mark != null) {
         validate = method;                    
      }         
   }
   
   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Persist</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */    
   private void persist(Method method) {
      Annotation mark = method.getAnnotation(Persist.class);

      if(mark != null) {
         persist = method;                    
      }      
   }

   /**
    * This method is used to check the provided method to determine
    * if it contains the <code>Complete</code> annotation. If the
    * method contains the required annotation it is stored so that
    * it can be invoked during the deserialization process.
    *
    * @param method this is the method checked for the annotation
    */ 
   private void complete(Method method) {
      Annotation mark = method.getAnnotation(Complete.class);

      if(mark != null) {
         complete = method;                    
      }      
   }      
}
