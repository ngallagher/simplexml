/*
 * GroupExtractor.java March 2011
 *
 * Copyright (C) 2011, Niall Gallagher <niallg@users.sf.net>
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * The <code>GroupExtractor</code> represents an extractor for labels
 * associated with a particular union annotation. This extractor 
 * registers <code>Label</code> by name and by type. Acquiring
 * the label by type allows the serialization process to dynamically
 * select a label, and thus converter, based on the instance type.
 * On deserialization a label is dynamically selected based on name.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Group
 */
class GroupExtractor implements Group {

   /**
    * This represents a factory for creating union extractors.
    */
   private final ExtractorFactory factory;
   
   /**
    * This represents the union label to be used for this group.
    */
   private final Annotation label;
   
   /**
    * This contains each label registered by name and by type.
    */
   private final Registry registry;
   
   /**
    * This contains each label registered by label name.
    */
   private final LabelMap elements;
   
   /**
    * Constructor for the <code>GroupExtractor</code> object. This
    * will create an extractor for the provided union annotation.
    * Each individual declaration within the union is extracted
    * and made available within the group.
    * 
    * @param contact this is the annotated field or method
    * @param label this is the label associated with the contact
    */
   public GroupExtractor(Contact contact, Annotation label) throws Exception{
      this.factory = new ExtractorFactory(contact, label);
      this.elements = new LabelMap();
      this.registry = new Registry(elements);
      this.label = label;
      this.extract();
   } 
   
   /**
    * This is used to acquire the names for each label associated
    * with this <code>Group</code> instance. The names provided
    * here are not styled according to a serialization context.
    * 
    * @return this returns the names of each union extracted
    */
   public Set<String> getNames() throws Exception {
      return elements.keySet();
   }
   
   /**
    * This is used to acquire the names for each label associated
    * with this <code>Group</code> instance. The names provided
    * here are styled according to a serialization context.
    * 
    * @param context this is the context used to style names
    * 
    * @return this returns the names of each union extracted
    */
   public Set<String> getNames(Context context) throws Exception {
      return getElements(context).keySet();
   }

   /**
    * This is used to acquire a <code>LabelMap</code> containing the
    * labels available to the group. Providing a context object 
    * ensures that each of the labels is mapped to a name that is
    * styled according to the internal style of the context.
    *
    * @param context this is the context used for serialization
    * 
    * @return this returns a label map containing the labels 
    */
   public LabelMap getElements(Context context) throws Exception {
      return elements.build(context);
   }

   /**
    * This is used to acquire a <code>Label</code> based on the type
    * of an object. Selecting a label based on the type ensures that
    * the serialization process can dynamically convert an object
    * to XML. If the type is not supported, this returns null.
    * 
    * @param type this is the type to select the label from
    * 
    * @return this returns the label based on the type
    */
   public Label getLabel(Class type) {
      return registry.get(type);
   }
   
   /**
    * This is used to determine if the associated type represents a
    * label defined within the union group. If the label exists
    * this returns true, if not then this returns false.
    * 
    * @param type this is the type to check for
    * 
    * @return this returns true if a label for the type exists
    */
   public boolean isValid(Class type) {
      return registry.containsKey(type);
   }
   
   /**
    * This is used to extract the labels associated with the group.
    * Extraction will instantiate a <code>Label</code> object for
    * an individual annotation declared within the union. Each of
    * the label instances is then registered by both name and type.
    */
   private void extract() throws Exception {
      Extractor extractor = factory.getInstance();
    
      if(extractor != null) {
         extract(extractor);
      }
   }     
   
   /**
    * This is used to extract the labels associated with the group.
    * Extraction will instantiate a <code>Label</code> object for
    * an individual annotation declared within the union. Each of
    * the label instances is then registered by both name and type.
    * 
    * @param extractor this is the extractor to get labels for
    */
   private void extract(Extractor extractor) throws Exception {
      List<Annotation> list = extractor.getAnnotations();
      
      for(Annotation label : list) {
         extract(extractor, label);
      }
   }
   
   /**
    * This is used to extract the labels associated with the group.
    * Extraction will instantiate a <code>Label</code> object for
    * an individual annotation declared within the union. Each of
    * the label instances is then registered by both name and type.
    * 
    * @param extractor this is the extractor to get labels for
    * @param value this is an individual annotation declared
    */
   private void extract(Extractor extractor, Annotation value) throws Exception {
      Label label = extractor.getLabel(value);
      Class type = extractor.getType(value);
      String name = label.getName();
      
      if(registry != null) {
         registry.register(name, label);
         registry.register(type, label);
      }
   }
   
   /**
    * This returns a string representation of the union group.
    * Providing a string representation in this way ensures that the
    * group can be used in exception messages and for any debugging.
    * 
    * @return this returns a string representation of the group
    */
   public String toString() {
      return label.toString();
   }
   
   /**
    * The <code>Registry</code> object is used to maintain mappings
    * from types to labels. Each of the mapppings can be used to 
    * dynamically select a label based on the instance type that is
    * to be serialized. This also registers based on the label name.
    * 
    * @author Niall Gallagher
    */
   private static class Registry extends LinkedHashMap<Class, Label> {
   
      /**
       * This maintains a mapping between label names and labels.
       */
      private final LabelMap elements;
      
      /**
       * Constructor for the <code>Registry</code> object. This is
       * used to register label instances using both the name and
       * type of the label. Registration in this way ensures that
       * each label can be dynamically selected.
       * 
       * @param elements this contains name to label mappings
       */
      public Registry(LabelMap elements){
         this.elements = elements;
      }
      
      /**
       * This is used to register a label based on the name. This is
       * done to ensure the label instance can be dynamically found
       * during the deserialization process by providing the name.
       * 
       * @param name this is the name of the label to be registered
       * @param label this is the label that is to be registered
       */
      public void register(String name, Label label) {
         elements.put(name, label);
      }
      
      /**
       * This is used to register a label based on the type. This is
       * done to ensure the label instance can be dynamically found
       * during the deserialization process by providing the type.
       * 
       * @param type this is the type of the label to be registered
       * @param label this is the label that is to be registered
       */
      public void register(Class type, Label label) {
         if(!containsKey(type)) {
            put(type, label);
         }
      }
   }
} 