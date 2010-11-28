/*
 * StructureBuilder.java November 2010
 *
 * Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;

/**
 * The <code>StructureBuilder</code> object is used to build the XML
 * structure of an annotated class. Once all the information scanned
 * from the class has been collected a <code>Structure</code> object
 * can be built using this object. The structure instance will 
 * contain relevant information regarding the class schema.
 * <p>
 * This builder exposes several methods, which are invoked in a
 * sequence by the <code>Scanner</code> object. In particular there
 * is a <code>process</code> method which is used to consume the
 * annotated fields and methods. With the annotations it then builds
 * the underlying structure representing the class schema.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Scanner
 */
class StructureBuilder {

   /**
    * This is used to build XPath expressions from annotations.
    */
   private ExpressionBuilder builder;
   
   /**
    * This is used to perform the initial ordered registrations. 
    */
   private ModelAssembler assembler;
   
   /**
    * For validation all attributes must be stored in the builder.
    */
   private LabelMap attributes;
   
   /**
    * For validation all elements must be stored in the builder.
    */
   private LabelMap elements;
 
   /**
    * This is used for validation to compare annotations used.
    */
   private Comparer comparer;
   
   /**
    * This is the source scanner that is used to scan the class.
    */
   private Scanner scanner;
   
   /**
    * This is the version annotation extracted from the class.
    */
   private Label version;
   
   /**
    * This represents a text annotation extracted from the class.
    */
   private Label text;
   
   /**
    * This the core model used to represent the XML structure.
    */
   private Model root;
   
   /**
    * This is the type that the XML structure is being built for.
    */
   private Class type;
   
   /**
    * This is used to determine if the scanned class is primitive.
    */
   private boolean primitive;

   /**
    * Constructor for the <code>StructureBuilder</code> object. This
    * is used to process all the annotations for a class schema and
    * build a hierarchical model representing the required structure.
    * Once the structure has been built then it is validated to
    * ensure that all elements and attributes exist.
    * 
    * @param scanner this is the scanner used to scan annotations
    * @param type this is the type that is being scanned
    */
   public StructureBuilder(Scanner scanner, Class type) throws Exception {
      this.builder = new ExpressionBuilder();
      this.assembler = new ModelAssembler(builder);
      this.attributes = new LabelMap(scanner);
      this.elements = new LabelMap(scanner);
      this.root = new TreeModel(scanner);
      this.comparer = new Comparer();
      this.scanner = scanner;
      this.type = type;
   }   
   
   /**
    * This is used to acquire the optional order annotation to provide
    * order to the elements and attributes for the generated XML. This
    * acts as an override to the order provided by the declaration of
    * the types within the object.  
    * 
    * @param type this is the type to be scanned for the order
    */
   public void assemble(Class type) throws Exception {
      Order order = scanner.getOrder();
      
      if(order != null) {
         assembler.assemble(root, order);
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
   public void process(Contact field, Annotation label) throws Exception {
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
      process(field, label, map);
      validate(label, name);
   }
   
   /**
    * This is used when all details from a field have been gathered 
    * and a <code>Label</code> implementation needs to be created. 
    * This will build a label instance based on the field annotation.
    * If a label with the same name was already inserted then it is
    * ignored and the value for that field will not be serialized. 
    * 
    * @param field the field the annotation was extracted from
    * @param label this is the label representing a field or method
    * @param map this is used to collect the label instance created
    * 
    * @throws Exception thrown if the label can not be created
    */
   private void process(Contact field, Label label, LabelMap map) throws Exception {
      String name = label.getName();
      String path = label.getPath();
      Model model = root;
      
      if(path != null) {
         model = register(path);
      }
      model.register(label);      
      map.put(name, label);
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
    * This is used to build the <code>Structure</code> that has been
    * built. The structure will contain all the details required to
    * serialize and deserialize the type. Once created the structure
    * is immutable, and can be used to create <code>Section</code>
    * objects, which contains the element and attribute details.
    * 
    * @param type this is the type that represents the schema class
    * 
    * @return this returns the structure that has been built
    */
   public Structure build(Class type) {
      return new Structure(root, version, text, primitive);
   }
   
   /**
    * This is used to determine if the specified XPath expression
    * represents an element within the root model. This will return
    * true if the specified path exists as either an element or
    * as a valid path to an existing model.
    * 
    * @param path this is the path to search for the element
    * 
    * @return this returns true if an element or model exists
    */
   private boolean isElement(String path)throws Exception {
      Expression target = builder.build(path);
      Model model = lookup(target);
      
      if(model != null) {
         String name = target.getLast();
      
         if(model.isElement(name)) {
            return true;
         }
         return model.isModel(name);
      }
      return false;
   }
   
   /**
    * This is used to determine if the specified XPath expression
    * represents an attribute within the root model. This returns
    * true if the specified path exists as either an attribute.
    * 
    * @param path this is the path to search for the attribute
    * 
    * @return this returns true if the attribute exists
    */
   private boolean isAttribute(String path) throws Exception {
      Expression target = builder.build(path);
      Model model = lookup(target);
      
      if(model != null) { 
         String name = target.getLast();
         return model.isAttribute(name);
      }
      return false;
   } 
   
   /**
    * This method is used to look for a <code>Model</code> that
    * matches the specified expression. If no such model exists
    * then this will return null. Using an XPath expression allows
    * a tree like structure to be navigated with ease.
    * 
    * @param path an XPath expression used to locate a model
    * 
    * @return this returns the model located by the expression
    */
   private Model lookup(Expression path) throws Exception {
      Expression target = path.getPath(0, 1);
      
      if(path.isPath()) {
         return root.lookup(target);
      }
      return root;
   }   

   /**
    * This is used to register a <code>Model</code> for this builder.
    * Registration of a model creates a tree of models that is used 
    * to represent an XML structure. Each model can contain elements 
    * and attributes associated with a type.
    * 
    * @param path this is the path of the model to be resolved
    * 
    * @return this returns the model that was registered
    */
   private Model register(String path) throws Exception {      
      Model model = root.lookup(path);
      
      if (model != null) {
         return model;
      }      
      return create(path);
   }
   
   /**
    * This is used to register a <code>Model</code> for this builder.
    * Registration of a model creates a tree of models that is used 
    * to represent an XML structure. Each model can contain elements 
    * and attributes associated with a type.
    * 
    * @param path this is the path of the model to be resolved
    * 
    * @return this returns the model that was registered
    */
   private Model create(String path) throws Exception {
      Expression expression = builder.build(path);
      Model model = root;
   
      for(String segment : expression) {
         model = model.register(segment);
      }
      return model;
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
   public void validate(Class type) throws Exception {
      Creator creator = scanner.getCreator();
      Order order = scanner.getOrder();
      
      validateElements(type, order);
      validateAttributes(type, order);
      validateParameters(creator);
      validateModel(type);
      validateText(type);      
   }
   
   /**
    * This is used to validate the model to ensure all elements and
    * attributes are valid. Validation also ensures that any order
    * specified by an annotated class did not contain invalid XPath
    * values, or redundant elements and attributes.
    * 
    * @param type this is the object type representing the schema
    * 
    * @throws Exception if text and element annotations are present
    */
   private void validateModel(Class type) throws Exception {
      if(!root.isEmpty()) {
         root.validate(type);
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
   private void validateText(Class type) throws Exception {
      if(text != null) {
         if(!elements.isEmpty()) {
            throw new TextException("Elements used with %s in %s", text, type);
         }
      }  else {
         if(scanner.isEmpty()) {
            primitive = isEmpty();
         }
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
            if(!isElement(name)) {
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
            if(!isAttribute(name)) {
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
         
         if(isEmpty(name)) {
            label = text;
         }
         if(label == null) {
            label = attributes.get(name);
         }
         if(label == null) {
            throw new ConstructorException("Parameter '%s' does not have a match in %s", name, type);
         }
      }
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
    * This is used to determine if the structure is empty. To check
    * to see if the structure is empty all models within the tree
    * must be examined. Also, if there is a text annotation then it
    * is not considered to be empty.
    * 
    * @return true if the structure represents an empty schema
    */
   private boolean isEmpty() {
      if(text != null) {
         return false;
      }
      return root.isEmpty();
   } 
}