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
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementUnion;
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
    * This is used to resolve the labels using parameters.
    */
   private LabelResolver resolver;
   
   /**
    * For validation all attributes must be stored in the builder.
    */
   private LabelMap attributes;
   
   /**
    * For validation all elements must be stored in the builder.
    */
   private LabelMap elements;
 
   /**
    * This is used to maintain the text labels for the class.
    */
   private LabelMap texts;
   
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
      this.builder = new ExpressionBuilder(type);
      this.assembler = new ModelAssembler(builder, type);
      this.root = new TreeModel(scanner, type);
      this.attributes = new LabelMap(scanner);
      this.elements = new LabelMap(scanner);
      this.texts = new LabelMap(scanner);
      this.resolver = new LabelResolver();
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
      if(label instanceof ElementUnion) {
         union(field, label, elements);
      }
      if(label instanceof ElementListUnion) {
         union(field, label, elements);
      }
      if(label instanceof ElementMapUnion) {
         union(field, label, elements);
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
   private void union(Contact field, Annotation type, LabelMap map) throws Exception {
      Annotation[] list = extract(type);
      
      for(Annotation value : list) {
         Label label = LabelFactory.getInstance(field, type, value);
         String path = label.getPath();
         String name = label.getName();
         
         if(map.get(path) != null) {
            throw new PersistenceException("Duplicate annotation of name '%s' on %s", name, label);
         }
         process(field, label, map);
         validate(label, path);
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
      String path = label.getPath();
      String name = label.getName();
      
      if(map.get(path) != null) {
         throw new PersistenceException("Duplicate annotation of name '%s' on %s", name, field);
      }
      process(field, label, map);
      validate(label, path);
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
      Expression expression = label.getExpression();
      String path = label.getPath();
      Model model = root;
      
      if(!expression.isEmpty()) {
         model = register(expression);
      }
      resolver.register(label);
      model.register(label);      
      map.put(path, label);
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
      Expression expression = label.getExpression();
      String path = label.getPath();
      Model model = root;
      
      if(!expression.isEmpty()) {
         model = register(expression);
      }
      if(texts.get(path) != null) {
         throw new TextException("Multiple text annotations in %s", type);
      }
      resolver.register(label);
      model.register(label);
      texts.put(path, label);
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
    * This is used to extract the individual annotations associated
    * with the union annotation provided. If the annotation does
    * not represent a union then this will return null.
    * 
    * @param label this is the annotation to extract from
    * 
    * @return this returns an array of annotations from the union
    */
   private Annotation[] extract(Annotation label) throws Exception {
      Class union = label.annotationType();
      Method[] list = union.getDeclaredMethods();
      
      if(list.length != 1) {
         throw new UnionException("Annotation '%s' is not a valid union for %s", label, type);
      }
      Method method = list[0];
      Object value = method.invoke(label);
      
      return (Annotation[])value;
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
    * <p>
    * If the path references a <code>Model</code> then that is an
    * element only if it is not empty. If the model is empty this
    * means that it was used in the <code>Order</code> annotation
    * only and this does not refer to a value XML element.
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
         int index = target.getIndex();
         
         if(model.isElement(name)) {
            return true;
         }
         if(model.isModel(name)) {
            Model element = model.lookup(name, index);
            
            if(element.isEmpty()) {
               return false;
            }
            return true;
         }
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
   private Model register(Expression path) throws Exception {   
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
   private Model create(Expression path) throws Exception {
      Model model = root;
   
      while(model != null) {
         String prefix = path.getPrefix();
         String name = path.getFirst();
         int index = path.getIndex();

         if(name != null) {
            model = model.register(name, prefix, index);
         }
         if(!path.isPath()) {
            break;
         }
         path = path.getPath(1);
      }
      return model;
   }
   
   /**
    * This is used to validate the configuration of the scanned class.
    * If a <code>Text</code> annotation has been used with elements
    * then validation will fail and an exception will be thrown. 
    * 
    * @param type this is the object type that is being scanned
    */
   public void validate(Class type) throws Exception {
      Creator creator = scanner.getCreator();
      Order order = scanner.getOrder();
      
      validateUnions(type);
      validateElements(type, order);
      validateAttributes(type, order);
      validateParameters(creator);
      validateConstructors(type);
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
    */
   private void validateText(Class type) throws Exception {
      if(root.getText() != null) {
         if(!elements.isEmpty()) {
            throw new TextException("Elements used with %s in %s", text, type);
         }
         if(root.isComposite()) {
            throw new TextException("Paths used with %s in %s", text, type);
         }
      }  else {
         if(scanner.isEmpty()) {
            primitive = isEmpty();
         }
      }
   }
   
   /**
    * This is used to validate the unions that have been defined
    * within the type. Union validation is done by determining if 
    * the union has consistent inline values. If one annotation in
    * the union declaration is inline, then all must be inline.
    * 
    * @param type this is the type to validate the unions for
    */
   private void validateUnions(Class type) throws Exception {
      for(Label label : elements) {
         Collection<String> options = label.getPaths();
         Contact contact = label.getContact();
         
         for(String option : options) {
            Annotation union = contact.getAnnotation();
            Label other = elements.get(option);
            
            if(label.isInline() != other.isInline()) {
               throw new UnionException("Inline must be consistent in %s for %s", union, contact);
            }
            if(label.isRequired() != other.isRequired()) {
               throw new UnionException("Required must be consistent in %s for %s", union, contact);
            }
         }    
      }
   }
   
   /**
    * This is used to validate the configuration of the scanned class.
    * If an ordered element is specified but does not refer to an
    * existing element then this will throw an exception.
    * 
    * @param type this is the object type that is being scanned
    * @param order this is the order that is to be validated
    */
   private void validateElements(Class type, Order order) throws Exception {
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
    * @param order this is the order that is to be validated
    */
   private void validateAttributes(Class type, Order order) throws Exception {
      if(order != null) {
         for(String name : order.attributes()) {
            if(!isAttribute(name)) {
               throw new AttributeException("Ordered attribute '%s' missing in %s", name, type);
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
    * @param type this is the type to validate constructors for
    */   
   private void validateConstructors(Class type) throws Exception {
      Creator creator = scanner.getCreator();
      List<Initializer> list = creator.getInitializers();      

      if(creator.isDefault()) {
         validateConstructors(elements);
         validateConstructors(attributes);
      }
      if(!list.isEmpty()) {
         validateConstructors(elements, list);
         validateConstructors(attributes, list);
      }
   }
   
   /**
    * This is used when there are only default constructors. It will
    * check to see if any of the annotated fields or methods is read
    * only. If a read only method or field is found then this will
    * throw an exception to indicate that it is not valid. 
    * 
    * @param map this is the map of values that is to be validated
    */
   private void validateConstructors(LabelMap map) throws Exception {
      for(Label label : map) {
         if(label != null) {
            Contact contact = label.getContact();
            
            if(contact.isReadOnly()) {
               throw new ConstructorException("Default constructor can not accept read only %s in %s", label, type);
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
    * @param map this is the map that contains the labels to validate
    * @param list this is the list of builders to validate
    */
   private void validateConstructors(LabelMap map, List<Initializer> list) throws Exception {      
      for(Label label : map) {         
         if(label != null) {
            validateConstructor(label, list);
         }
      }   
      if(list.isEmpty()) {
         throw new ConstructorException("No constructor accepts all read only values in %s", type);
      }
   }
   
   /**
    * This is used to ensure that final methods and fields have a 
    * constructor parameter that allows the value to be injected in
    * to. Validating the constructor in this manner ensures that the
    * class schema remains fully serializable and deserializable.
    * 
    * @param label this is the variable to check in constructors
    * @param list this is the list of builders to validate
    */
   private void validateConstructor(Label label, List<Initializer> list) throws Exception {
      Iterator<Initializer> iterator = list.iterator();
      
      while(iterator.hasNext()) {
         Initializer initializer = iterator.next();
         Contact contact = label.getContact();
         String path = label.getPath();
         
         if(contact.isReadOnly()) {
            Parameter value = initializer.getParameter(path);
            Collection<String> options = label.getNames();
            
               if(value == null) {
               for(String option : options) {
                  value = initializer.getParameter(option);
               
                  if(value != null) {
                     break;
                  }
               }
            }
            if(value == null) {
               iterator.remove();
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
    */
   private void validateParameters(Creator creator) throws Exception {
      List<Parameter> list = creator.getParameters();
      
      for(Parameter parameter : list) {
         Label label = resolver.resolve(parameter);
         String path = parameter.getPath();
         
         if(label == null) {
            throw new ConstructorException("Parameter '%s' does not have a match in %s", path, type);
         }
      }
   }
   
   /**
    * This is used to validate the <code>Parameter</code> object that
    * exist in the constructors. Validation is performed against the
    * annotated methods and fields to ensure that they match up.
    * 
    * @param label this is the annotated method or field to validate
    * @param name this is the name of the parameter to validate with
    */
   private void validate(Label label, String name) throws Exception {
      Creator factory = scanner.getCreator();
      Parameter parameter = factory.getParameter(name);
      
      if(parameter != null) {
         validate(label, parameter);
      }
   }
   
   /**
    * This is used to validate the <code>Parameter</code> object that
    * exist in the constructors. Validation is performed against the
    * annotated methods and fields to ensure that they match up.
    * 
    * @param label this is the annotated method or field to validate
    * @param parameter this is the parameter to validate with
    */
   private void validate(Label label, Parameter parameter) throws Exception {
      Collection<String> options = label.getNames();
      Contact contact = label.getContact();
      String name = parameter.getName();
      Class expect = contact.getType();
      
      if(expect != parameter.getType()) {
         throw new ConstructorException("Type does not match %s for '%s' in %s", label, name, parameter);
      }
      if(!options.contains(name)) {
         String require = label.getName();
         
         if(name != require) {
            if(name == null || require == null) {
               throw new ConstructorException("Annotation does not match %s for '%s' in %s", label, name, parameter);
            }
            if(!name.equals(require)) {
               throw new ConstructorException("Annotation does not match %s for '%s' in %s", label, name, parameter);              
            }
         }
      }   
      validateAnnotations(label, parameter);
   }
   
   /**
    * This is used to validate the annotations associated with a field
    * and a matching constructor parameter. Each constructor parameter
    * paired with an annotated field or method must be the same 
    * annotation type and must also contain the same name.
    * 
    * @param label this is the label associated with the parameter
    * @param parameter this is the constructor parameter to use
    */
   private void validateAnnotations(Label label, Parameter parameter) throws Exception {
      Annotation field = label.getAnnotation();
      Annotation argument = parameter.getAnnotation();
      String name = parameter.getName();     
      
      if(!comparer.equals(field, argument)) {
         Class expect = field.annotationType();
         Class actual = argument.annotationType();
         
         if(!expect.equals(actual)) {
            throw new ConstructorException("Annotation %s does not match %s for '%s' in %s", actual, expect, name, parameter);  
         } 
      }
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