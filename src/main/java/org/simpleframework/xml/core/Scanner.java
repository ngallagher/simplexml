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

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
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
class Scanner implements Policy {
   
   /**
    * This is used to store all XML attributes and XML elements.
    */
   private StructureBuilder builder;
   
   /**
    * This method acts as a pointer to the types commit process.
    */
   private ClassScanner scanner;
   
   /**
    * This defines the structure build from the class annotations.
    */
   private Structure structure;
   
   /**
    * This is the default access type to be used for this scanner.
    */
   private DefaultType access;
   
   /**
    * This is the name of the class as taken from the root class.
    */
   private String name;
   
   /**
    * This is the type that is being scanned by this scanner.
    */
   private Class type;
   
   /**
    * This is used to determine if the defaults are required.
    */
   private boolean required;
   
   /**
    * Constructor for the <code>Scanner</code> object. This is used 
    * to scan the provided class for annotations that are used to
    * build a schema for an XML file to follow. 
    * 
    * @param type this is the type that is scanned for a schema
    */
   public Scanner(Class type) throws Exception {  
      this.scanner = new ClassScanner(type);
      this.builder = new StructureBuilder(this, type); 
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
    * This is used to create a <code>Section</code> given the context
    * used for serialization. A section is an XML structure that 
    * contains all the elements and attributes defined for the class.
    * Each section is a tree like structure defining exactly where
    * each attribute an element is located within the source XML.
    * 
    * @param context this is the context used to style the values
    * 
    * @return this will return a section for serialization
    */
   public Section getSection(Context context) {
      return structure.getSection(context);
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
      return structure.getRevision();
   }
   
   /**
    * This is used to acquire the <code>Order</code> annotation for
    * the class schema. The order annotation defines the order that
    * the elements and attributes should appear within the document.
    * Providing order in this manner makes the resulting XML more
    * predictable. If no order is provided, appearance is random.
    * 
    * @return this returns the order, if any, defined for the class
    */
   public Order getOrder() {
      return scanner.getOrder();
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
      return structure.getVersion();
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
      return structure.getText();
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
      return structure.isPrimitive();
   }
   
   /**
    * This is used to determine whether the scanned class represents
    * a primitive type. A primitive type is a type that contains no
    * XML annotations and so cannot be serialized with an XML form.
    * Instead primitives a serialized using transformations.
    * 
    * @return this returns true if no XML annotations were found
    */
   public boolean isEmpty() {
      return scanner.getRoot() == null;
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
      access(type);
      field(type);
      method(type);
      validate(type);
      commit(type);
   }
   
   /**
    * Once the scanner has completed extracting the annotations and
    * validating the resulting structure this is called to complete 
    * the process. This will build a <code>Structure</code> object and
    * clean up any data structures no longer required by the scanner.
    * 
    * @param type this is the type that this scanner has scanned
    */
   private void commit(Class type) throws Exception {
      if(structure == null) {
         structure = builder.build(type);
      }
      builder = null;
   }
   
   /**
    * This is used to acquire the optional order annotation to provide
    * order to the elements and attributes for the generated XML. This
    * acts as an override to the order provided by the declaration of
    * the types within the object.  
    * 
    * @param type this is the type to be scanned for the order
    */
   private void order(Class<?> type) throws Exception {
      builder.assemble(type);
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
      builder.validate(type);
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
    * This is used to determine the access type for the class. The
    * access type is specified by the <code>DefaultType</code>
    * enumeration. Setting a default access tells this scanner to
    * synthesize an XML annotation for all fields or methods that
    * do not have associated annotations. 
    * 
    * @param type this is the type to acquire the default type for
    */
   private void access(Class<?> type) {
      Default holder = scanner.getDefault();
      
      if(holder != null) {
         required = holder.required();
         access = holder.value();
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
   private void field(Class type) throws Exception {
      ContactList list = new FieldScanner(type, access, required);
      
      for(Contact contact : list) {
         Annotation label = contact.getAnnotation();
         
         if(label != null) {
            builder.process(contact, label);
         }
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
      ContactList list = new MethodScanner(type, access, required);
      
      for(Contact contact : list) {
         Annotation label = contact.getAnnotation();
         
         if(label != null) {
            builder.process(contact, label);
         }
      }
   }
}
