/*
 * Scanner.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

import java.lang.annotation.Annotation;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;

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
 * @see org.simpleframework.xml.core.Schema
 */ 
class Scanner {
   
   /**
    * This method acts as a pointer to the types commit process.
    */
   private ClassScanner scanner;
   
   /**
    * This is used to store all labels that are XML attributes.
    */
   private LabelMap attributes;
   
   /**
    * This is used to store all labels that are XML elements.
    */
   private LabelMap elements;   
   
   /**
    * This is used to compare the annotations being scanned.
    */
   private Comparer comparer;
   
   /**
    * This is the version label used to read the version attribute.
    */
   private Label version;

   /**
    * This is used to store all labels that are XML text values.
    */
   private Label text;
   
   /**
    * This is the name of the class as taken from the root class.
    */
   private String name;
   
   /**
    * This is the type that is being scanned by this scanner.
    */
   private Class type;
   
   /**
    * This is used to specify whether the type is a primitive class.
    */
   private boolean primitive;
   
   /**
    * Constructor for the <code>Scanner</code> object. This is used 
    * to scan the provided class for annotations that are used to
    * build a schema for an XML file to follow. 
    * 
    * @param type this is the type that is scanned for a schema
    */
   public Scanner(Class type) throws Exception {  
      this.scanner = new ClassScanner(type);
      this.attributes = new LabelMap(this);
      this.elements = new LabelMap(this); 
      this.comparer = new Comparer();
      this.type = type;
      this.scan(type);
   }      
   
   /**
    * This is used to acquire the type that this scanner scans for
    * annotations to be used in a schema. Exposing the class that
    * this represents allows the schema it creates to be known.
    * 
    * @return this is the type that this creator will represent
    */
   public Class getType() {
      return type;
   }
   
   /**
    * This is used to create the object instance. It does this by
    * either delegating to the default no argument constructor or by
    * using one of the annotated constructors for the object. This
    * allows deserialized values to be injected in to the created
    * object if that is required by the class schema.
    * 
    * @return this returns the creator for the class object
    */
   public Creator getCreator() {
      return scanner.getCreator();
   }
   
   /**
    * This is used to acquire the <code>Decorator</code> for this.
    * A decorator is an object that adds various details to the
    * node without changing the overall structure of the node. For
    * example comments and namespaces can be added to the node with
    * a decorator as they do not affect the deserialization.
    * 
    * @return this returns the decorator associated with this
    */
   public Decorator getDecorator() {
      return scanner.getDecorator();
   }
   
   /**
    * This method is used to return the <code>Caller</code> for this
    * class. The caller is a means to deliver invocations to the
    * object for the persister callback methods. It aggregates all of
    * the persister callback methods in to a single object.
    * 
    * @return this returns a caller used for delivering callbacks
    */
   public Caller getCaller(Context context) {
      return new Caller(this, context);
   }

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML attributes. This returns a new map
    * each time the method is called, the goal is to ensure that any
    * object using the label map can manipulate it without changing
    * the core details of the schema, allowing it to be cached.
    * 
    * @param context this is the context used to style the names 
    *
    * @return map with the details extracted from the schema class
    */ 
   public LabelMap getAttributes(Context context) throws Exception {
      return attributes.clone(context);
   }        

   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML elements. The annotations that are
    * considered elements are the <code>ElementList</code> and the
    * <code>Element</code> annotations. This returns a copy of the
    * details extracted from the schema class so this can be cached.
    * 
    * @param context this is the context used to style the names
    *
    * @return a map containing the details for XML elements
    */
   public LabelMap getElements(Context context) throws Exception {
      return elements.clone(context);
   }
  
   /**
    * This is the <code>Version</code> for the scanned class. It 
    * allows the deserialization process to be configured such that
    * if the version is different from the schema class none of
    * the fields and methods are required and unmatched elements
    * and attributes will be ignored.
    * 
    * @return this returns the version of the class that is scanned
    */
   public Version getRevision() {
      if(version != null) {
         return version.getContact().getAnnotation(Version.class);
      }
      return null;
   }
   
   /**
    * This returns the <code>Label</code> that represents the version
    * annotation for the scanned class. Only a single version can
    * exist within the class if more than one exists an exception is
    * thrown. This will read only floating point types such as double.
    * 
    * @return this returns the label used for reading the version
    */
   public Label getVersion() {
      return version;
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
   public Function getCommit() {
      return scanner.getCommit();           
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
   public Function getValidate() {
      return scanner.getValidate();       
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
   public Function getPersist() {
      return scanner.getPersist();           
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
   public Function getComplete() {
      return scanner.getComplete();           
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
   public Function getReplace() {
      return scanner.getReplace();
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
   public Function getResolve() {
      return scanner.getResolve();
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
      Root root = scanner.getRoot();
      
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
      return scanner.isStrict();
   }
   
   /**
    * This is used to scan the specified object to extract the fields
    * and methods that are to be used in the serialization process.
    * This will acquire all fields and getter setter pairs that have
    * been annotated with the XML annotations.
    *
    * @param type this is the object type that is to be scanned
    */  
   private void scan(Class type) throws Exception {
      root(type);
      order(type);
      field(type);
      method(type);
      validate(type);
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
      Creator creator = scanner.getCreator();
      Order order = scanner.getOrder();
      
      validateElements(type, order);
      validateAttributes(type, order);
      validateParameters(creator);
      validateText(type);
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
   private void validateText(Class type) throws Exception {
      if(text != null) {
         if(!elements.isEmpty()) {
            throw new TextException("Elements used with %s in %s", text, type);
         }
      }  else {
         primitive = isEmpty();
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
   private void validateElements(Class type, Order order) throws Exception {
      Creator factory = scanner.getCreator();
      List<Builder> builders = factory.getBuilders();
      
      for(Builder builder : builders) {
         validateConstructor(builder, elements);
      }
      if(order != null) {
         for(String name : order.elements()) {
            Label label = elements.get(name);
            
            if(label == null) {
               throw new ElementException("Ordered element '%s' missing for %s", name, type);
            }
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
   private void validateAttributes(Class type, Order order) throws Exception {
      Creator factory = scanner.getCreator();
      List<Builder> builders = factory.getBuilders();
      
      for(Builder builder : builders) {
         validateConstructor(builder, elements);
      }
      if(order != null) {
         for(String name : order.attributes()) {
            Label label = attributes.get(name);
            
            if(label == null) {
               throw new AttributeException("Ordered attribute '%s' missing for %s", name, type);
            }
         }
      }
   } 

   /**
    * This is used to ensure that final methods and fields have a 
    * constructor parameter that allows the value to be injected in
    * to. Validating the constructor in this manner ensures that the
    * class schema remains fully serializable and deserializable.
    * 
    * @param builder this is the builder to validate the labels with
    * @param map this is the map that contains the labels to validate
    * 
    * @throws Exception this is thrown if the validation fails
    */
   private void validateConstructor(Builder builder, LabelMap map) throws Exception {
      for(Label label : map) {         
         if(label != null) {
            Contact contact = label.getContact();
            String name = label.getName();
            
            if(contact.isReadOnly()) {
               Parameter value = builder.getParameter(name);
               
               if(value == null) {
                  throw new ConstructorException("No match found for %s in %s", contact, type);
               }
            }        
         }
      } 
   }
   
   /**
    * This is used to ensure that for each parameter in the builder
    * there is a matching method or field. This ensures that the
    * class schema is fully readable and writable. If not method or
    * field annotation exists for the parameter validation fails.
    * 
    * @param creator this is the creator to validate the labels with
    * 
    * @throws Exception this is thrown if the validation fails
    */
   private void validateParameters(Creator creator) throws Exception {
      List<Parameter> list = creator.getParameters();
      
      for(Parameter parameter : list) {
         String name = parameter.getName();
         Label label = elements.get(name);
         
         if(label == null) {
            label = attributes.get(name);
         }
         if(label == null) {
            throw new ConstructorException("Parameter '%s' does not have a match in %s", name, type);
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
      Root root = scanner.getRoot();
      String text = real;

      if(root != null) {
         text = root.name();

         if(isEmpty(text)) {
            text = Reflector.getName(real);
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
      Order order = scanner.getOrder();
      
      if(order != null) {
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
    * be used to represent the field within the context object.
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
      if(label instanceof Version) {
         version(field, label);
      }
      if(label instanceof Text) {
         text(field, label);
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
   private void text(Contact field, Annotation type) throws Exception {
      Label label = LabelFactory.getInstance(field, type);
      
      if(text != null) {
         throw new TextException("Multiple text annotations in %s", type);
      }
      text = label;
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
   private void version(Contact field, Annotation type) throws Exception {
      Label label = LabelFactory.getInstance(field, type);
      
      if(version != null) {
         throw new AttributeException("Multiple version annotations in %s", type);
      }
      version = label;
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
      validate(label, name);
   }
   
   /**
    * This is used to validate the <code>Parameter</code> object that
    * exist in the constructors. Validation is performed against the
    * annotated methods and fields to ensure that they match up.
    * 
    * @param field this is the annotated method or field to validate
    * @param name this is the name of the parameter to validate with
    * 
    * @throws Exception thrown if the validation fails
    */
   private void validate(Label field, String name) throws Exception {
      Creator factory = scanner.getCreator();
      Parameter parameter = factory.getParameter(name);
      
      if(parameter != null) {
         validate(field, parameter);
      }
   }
   
   /**
    * This is used to validate the <code>Parameter</code> object that
    * exist in the constructors. Validation is performed against the
    * annotated methods and fields to ensure that they match up.
    * 
    * @param field this is the annotated method or field to validate
    * @param parameter this is the parameter to validate with
    * 
    * @throws Exception thrown if the validation fails
    */
   private void validate(Label field, Parameter parameter) throws Exception {
      Contact contact = field.getContact();
      Annotation label = contact.getAnnotation();
      Annotation match = parameter.getAnnotation();
      String name = field.getName();
      
      if(!comparer.equals(label, match)) {
         throw new ConstructorException("Annotation does not match for '%s' in %s", name, type);
      }
      Class expect = contact.getType();
      
      if(expect != parameter.getType()) {
         throw new ConstructorException("Parameter does not match field for '%s' in %s", name, type);
      }     
   }
}
