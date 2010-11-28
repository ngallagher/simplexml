/*
 * TreeModel.java November 2010
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The <code>TreeModel</code> object is used to build a tree like
 * structure to represent the XML schema for an annotated class. The
 * model is responsible  for building, ordering, and validating all
 * criteria used to represent the class schema. This is immutable
 * to ensure it can be reused many time, in a concurrent environment.
 * Each time attribute and element definitions are requested they
 * are build as new <code>LabelMap</code> objects using a provided
 * context. This ensures the mappings can be styled as required.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Context
 */
class TreeModel implements Model {
  
   /**
    * This holds the mappings for elements within the model.
    */
   private final LabelMap attributes;
   
   /**
    * This holds the mappings for elements within the model.
    */
   private final LabelMap elements;
   
   /**
    * This holds the mappings for the models within this instance.
    */
   private final ModelMap models;
   
   /**
    * This is the serialization policy enforced on this model.
    */
   private final Policy policy;
   
   /**
    * This must be a valid XML element representing the name.
    */
   private final String name;
   
   /**
    * Constructor for the <code>TreeModel</code> object. This can be
    * used to register the attributes and elements associated with
    * an annotated class. Also, if there are any path references, 
    * this can contain a tree of models mirroring the XML structure.
    * 
    * @param policy this is the serialization policy enforced
    */
   public TreeModel(Policy policy) {
      this(policy, null);
   }
   
   /**
    * Constructor for the <code>TreeModel</code> object. This can be
    * used to register the attributes and elements associated with
    * an annotated class. Also, if there are any path references, 
    * this can contain a tree of models mirroring the XML structure.
    * 
    * @param policy this is the serialization policy enforced
    * @param name this is the XML element name for this model
    */
   public TreeModel(Policy policy, String name) {
      this.attributes = new LabelMap(policy);
      this.elements = new LabelMap(policy);
      this.models = new ModelMap();
      this.policy = policy;
      this.name = name;
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
   public Model lookup(Expression path) {
      Expression target = path.getPath(1, 0);
      String name = path.getFirst();
      Model model = models.get(name);
      
      if(!path.isPath()) {
         return model;
      }
      if(model == null) {
         return null;
      }         
      return model.lookup(target); 
   }
   
   /**
    * This is used to register an XML entity within the model. The
    * registration process has the affect of telling the model that
    * it will contain a specific, named, XML entity. It also has 
    * the affect of ordering them within the model, such that the
    * first registered entity is the first iterated over.
    * 
    * @param name this is the name of the element to register
    */   
   public void registerElement(String name) throws Exception {
      elements.put(name, null);
   }   
   
   /**
    * This is used to register an XML entity within the model. The
    * registration process has the affect of telling the model that
    * it will contain a specific, named, XML entity. It also has 
    * the affect of ordering them within the model, such that the
    * first registered entity is the first iterated over.
    * 
    * @param name this is the name of the element to register
    */
   public void registerAttribute(String name) throws Exception {
      attributes.put(name, null);
   }
   
   /**
    * This is used to register an XML entity within the model. The
    * registration process has the affect of telling the model that
    * it will contain a specific, named, XML entity. It also has 
    * the affect of ordering them within the model, such that the
    * first registered entity is the first iterated over.
    * 
    * @param label this is the label to register with the model
    */   
   public void registerAttribute(Label label) throws Exception {
      String name = label.getName();
      
      if(attributes.get(name) != null) {
         throw new PersistenceException("Annotation of name '%s' declared twice", name);
      }
      attributes.put(name, label);
   }   
   
   /**
    * This is used to register an XML entity within the model. The
    * registration process has the affect of telling the model that
    * it will contain a specific, named, XML entity. It also has 
    * the affect of ordering them within the model, such that the
    * first registered entity is the first iterated over.
    * 
    * @param label this is the label to register with the model
    */   
   public void registerElement(Label label) throws Exception {
      String name = label.getName();
      
      if(elements.get(name) != null) {
         throw new PersistenceException("Annotation of name '%s' declared twice", name);
      }
      elements.put(name, label);
   }
   
   /**
    * This is used to build a map from a <code>Context</code> object.
    * Building a map in this way ensures that any style specified by
    * the context can be used to create the XML element and attribute
    * names in the styled format. It also ensures that the model
    * remains immutable as it only provides copies of its data.
    * 
    * @param context the context associated with the serialization
    * 
    * @return this returns a map built from the specified context
    */   
   public ModelMap buildModels(Context context) throws Exception {
      return models.build(context);
   }

   /**
    * This is used to build a map from a <code>Context</code> object.
    * Building a map in this way ensures that any style specified by
    * the context can be used to create the XML element and attribute
    * names in the styled format. It also ensures that the model
    * remains immutable as it only provides copies of its data.
    * 
    * @param context the context associated with the serialization
    * 
    * @return this returns a map built from the specified context
    */   
   public LabelMap buildAttributes(Context context) throws Exception {
      return attributes.build(context);
   }

   /**
    * This is used to build a map from a <code>Context</code> object.
    * Building a map in this way ensures that any style specified by
    * the context can be used to create the XML element and attribute
    * names in the styled format. It also ensures that the model
    * remains immutable as it only provides copies of its data.
    * 
    * @param context the context associated with the serialization
    * 
    * @return this returns a map built from the specified context
    */
   public LabelMap buildElements(Context context) throws Exception{
      return elements.build(context);
   }

   /**
    * This method is used to look for a <code>Model</code> that
    * matches the specified element name. If no such model exists
    * then this will return null. This is used as an alternative
    * to providing an XPath expression to navigate the tree.
    * 
    * @param name this is the name of the model to be acquired
    * 
    * @return this returns the model located by the expression
    */
   public Model lookup(String name) {
      return models.get(name);
   }
   
   /**
    * This is used to determine if the provided name represents
    * a model. This is useful when validating the model as it
    * allows determination of a named model, which is an element. 
    * 
    * @param name this is the name of the section to determine
    * 
    * @return this returns true if the model is registered
    */
   public boolean isModel(String name) {
      return models.containsKey(name);
   }

   /**
    * This is used to determine if the provided name represents
    * an element. This is useful when validating the model as 
    * it allows determination of a named XML element. 
    * 
    * @param name this is the name of the section to determine
    * 
    * @return this returns true if the element is registered
    */
   public boolean isElement(String name) {
      return elements.containsKey(name);
   }
   
   /**
    * This is used to determine if the provided name represents
    * an attribute. This is useful when validating the model as 
    * it allows determination of a named XML attribute
    * 
    * @param name this is the name of the attribute to determine
    * 
    * @return this returns true if the attribute is registered
    */
   public boolean isAttribute(String name) {
      return attributes.containsKey(name);
   }
   
   /**
    * This will return the names of all elements contained within
    * the model. This includes the names of all XML elements that
    * have been registered as well as any other models that have
    * been added. Iteration is done in an ordered manner, according
    * to the registration of elements and models.
    * 
    * @return an order list of the elements and models registered
    */
   public Iterator<String> iterator() {
      List<String> list = new ArrayList<String>();
      
      for(String name : elements.keySet()) {
         list.add(name);        
      }
      return list.iterator();      
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
   public void validate(Class type) throws Exception {
      validateAttributes(type);
      validateElements(type);
      validateModels(type);
   }
   
   /**
    * This is used to validate the models within the instance. This
    * will basically result in validation of the entire tree. Once
    * finished all models contained within the tree will be valid.
    * If any model is invalid an exception will be thrown.
    * 
    * @param type this is the type this model is created for
    */
   private void validateModels(Class type) throws Exception {
      for(Model model : models) {
         model.validate(type);
      }
   }
   
   /**
    * This is used to validate the individual attributes within the
    * model. Validation is done be acquiring all the attributes and
    * determining if they are null. If they are null this means that
    * an ordering has been imposed on a non-existing attribute.
    * 
    * @param type this is the type this model is created for   
    */
   private void validateAttributes(Class type) throws Exception {
      Set<String> keys = attributes.keySet();
      
      for(String name : keys) {
         Label label = attributes.get(name);
         
         if(label == null) {
            throw new AttributeException("Ordered attribute '%s' does not exist in %s", name, type);
         }
      }
   }
   
   /**
    * This is used to validate the individual elements within the
    * model. Validation is done be acquiring all the elements and
    * determining if they are null. If they are null this means that
    * an ordering has been imposed on a non-existing element.
    * 
    * @param type this is the type this model is created for   
    */
   private void validateElements(Class type) throws Exception {
      Set<String> keys = elements.keySet();
      
      for(String name : keys) {
         Model model = models.get(name);
         Label label = elements.get(name);
         
         if(model == null && label == null) {
            throw new ElementException("Ordered element '%s' does not exist in %s", name, type);
         }
         if(model != null && label != null) {
            if(!model.isEmpty()) {
               throw new ElementException("Element '%s' is also a path name in %s", name, type);
            }
         }
      }
   }
   
   /**
    * This is used to register an XML entity within the model. The
    * registration process has the affect of telling the model that
    * it will contain a specific, named, XML entity. It also has 
    * the affect of ordering them within the model, such that the
    * first registered entity is the first iterated over.
    * 
    * @param label this is the label to register with the model
    */
   public void register(Label label) throws Exception {
      if(label.isAttribute()) {
         registerAttribute(label);
      } else {
         registerElement(label);
      }
   }

   /**
    * This is used to register a <code>Model</code> within this
    * model. Registration of a model creates a tree of models that
    * can be used to represent an XML structure. Each model can
    * contain elements and attributes associated with a type.
    * 
    * @param name this is the name of the model to be registered
    * 
    * @return this returns the model that was registered
    */
   public Model register(String name) throws Exception {
      Model model = models.get(name);
      
      if (model == null) {
         return create(name);
      }
      return model;
   }
   
   /**
    * This is used to register a <code>Model</code> within this
    * model. Registration of a model creates a tree of models that
    * can be used to represent an XML structure. Each model can
    * contain elements and attributes associated with a type.
    * 
    * @param name this is the name of the model to be registered
    * 
    * @return this returns the model that was registered
    */
   private Model create(String name) throws Exception {
      Model model = new TreeModel(policy, name);
      
      if(name != null) {
         elements.put(name, null);
         models.put(name, model);         
      }
      return model;
   }
   
   /**
    * This is used to perform a recursive search of the models that
    * have been registered, if a model has elements or attributes
    * then this returns true. If however no other model contains 
    * any attributes or elements then this will return false.
    * 
    * @return true if any model has elements or attributes
    */
   private boolean isComposite() {
      for(Model group : models) {
         if(group.isEmpty()) {
            return false;
         }
      }
      return !models.isEmpty();
   }
   
   /**
    * Used to determine if a model is empty. A model is considered
    * empty if that model does not contain any registered elements
    * or attributes. However, if the model contains other models
    * that have registered elements or attributes it is not empty.
    * 
    * @return true if the model does not contain registrations
    */
   public boolean isEmpty() {
      if(!elements.isEmpty()) {
         return false;
      }
      if(!attributes.isEmpty()) {
         return false;
      }
      return !isComposite();
   }
   
   /**
    * This is used to return the name of the model. The name is 
    * must be a valid XML element name. It is used when a style
    * is applied to a section as the model name must be styled.
    * 
    * @return this returns the name of this model instance
    */
   public String getName() {
      return name;
   }
}