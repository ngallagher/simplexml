/*
 * ExtractorFactory.java March 2011
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
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Variant;
import org.simpleframework.xml.VariantList;
import org.simpleframework.xml.VariantMap;

/**
 * The <code>ExtractorFactory</code> is used to create an extractor 
 * object that can be used to build a label for each annotation in
 * the variant group. In order to build an extractor the factory
 * requires the <code>Contact</code> and the variant annotation.
 * Each extractor created by this factory can be used to extract
 * the constituent parts of each label defined in the variant.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Extractor
 */
class ExtractorFactory {   

   /**
    * This is the variant annotation this creates extractors for.
    */
   private final Annotation label;
   
   /**
    * This is the contact that has been annotated as a variant.
    */
   private final Contact contact;
   
   /**
    * Constructor for the <code>ExtractorFactory</code> object. This
    * requires the contact that was annotated as a variant and the
    * actual variant annotation, which is used to build individual
    * labels based on the declarations.
    * 
    * @param contact this is the field or method annotated
    * @param label this is the variant annotation to extract from
    */
   public ExtractorFactory(Contact contact, Annotation label) {
      this.contact = contact;
      this.label = label;
   }
   
   /**
    * This is used to instantiate an <code>Extractor</code> based on
    * the variant annotation provided. Each extractor provides a 
    * uniform interface to the constituent parts of the variant.
    * 
    * @return this returns an extractor for the variant
    */
   public Extractor getInstance() throws Exception {
      return (Extractor)getInstance(label);     
   }
   
   /**
    * This is used to instantiate an <code>Extractor</code> based on
    * the variant annotation provided. Each extractor provides a 
    * uniform interface to the constituent parts of the variant.
    * 
    * @param label this is the variant annotation to be used
    * 
    * @return this returns an extractor for the variant
    */
   private Object getInstance(Annotation label) throws Exception {
      ExtractorBuilder builder = getBuilder(label);
      Constructor factory = builder.getConstructor();
      
      if(!factory.isAccessible()) {
         factory.setAccessible(true);
      }
      return factory.newInstance(contact, label); 
   }
   
   /**
    * This returns a builder used to instantiate an extractor based
    * on a particular variant annotation. If the annotation provided
    * does not represent a valid variant an exception is thrown.
    * 
    * @param label this is the variant annotation to build for
    * 
    * @return this returns a builder used to create an extractor
    */
   private ExtractorBuilder getBuilder(Annotation label) throws Exception {
      if(label instanceof Variant) {
         return new ExtractorBuilder(Variant.class, ElementExtractor.class);
      }
      if(label instanceof VariantList) {
         return new ExtractorBuilder(VariantList.class, ElementListExtractor.class);
      }
      if(label instanceof VariantMap) {
         return new ExtractorBuilder(VariantMap.class, ElementMapExtractor.class);
      }
      throw new PersistenceException("Annotation %s is not a variant", label);
   }
   
   /**
    * The <code>ExtractorBuilder</code> object is used to instantiate
    * an extractor based an a particular variant annotation. Each 
    * builder has a known constructor signature which can be used to
    * reflectively instantiate the builder instance.
    * 
    * @author Niall Gallagher
    */
   private static class ExtractorBuilder {
      
      /**
       * This is the variant annotation to build the extractor for.
       */
      private final Class label;
      
      /**
       * This is the actual extractor that is to be instantianted.
       */
      private final Class type;
      
      /**
       * Constructor for the <code>ExtractorBuilder</code> object. This
       * requires the variant annotation to instantiate the builder for.
       * Also, the actual builder type is required.
       * 
       * @param label this is the variant annotation to be used
       * @param type this is the actual extractor implementation
       */
      public ExtractorBuilder(Class label, Class type) {
         this.label = label;
         this.type = type;
      }
      
      /**
       * Returns a <code>Constructor</code> that can be used to create
       * an extractor based on a known constructor signature. The 
       * provided constructor is then used to instantiated the object.
       * 
       * @return this returns the constructor for the extractor
       */
      private Constructor getConstructor() throws Exception {
         return type.getConstructor(Contact.class, label);
      }
   }
   
   /**
    * The <code>ElementExtractor</code> object is used extract the
    * constituent parts of the provided variant annotation. This can
    * also be used to create a </code>Label</code> object for each
    * of the declared annotation for dynamic serialization.
    * 
    * @author Niall Gallagher
    */
   private static class ElementExtractor implements Extractor<Element> {
      
      /**
       * This is the variant annotation to extract the labels for.
       */
      private final Variant variant;
      
      /**
       * This is the contact that has been annotated as a variant.
       */
      private final Contact contact;
      
      /**
       * Constructor for the <code>ElementExtractor</code> object. This
       * is used to create an extractor that can be used to extract
       * the various labels used to serialize and deserialize objects.
       * 
       * @param contact this is the contact annotated as a variant
       * @param variant this is the variant annotation to extract from
       */
      public ElementExtractor(Contact contact, Variant variant) throws Exception {
         this.variant = variant;
         this.contact = contact;
      }
      
      /**
       * This is used to acquire each annotation that forms part of the
       * variant group. Extracting the annotations in this way allows
       * the extractor to build a <code>Label</code> which can be used
       * to represent the annotation. Each label can then provide a
       * converter implementation to serialize objects.
       * 
       * @return this returns each annotation within the variant group
       */
      public List<Element> getAnnotations() {
         Element[] list = variant.value();
         
         if(list.length > 0) {
            return Arrays.asList(list);
         }
         return Collections.emptyList();
      }
      
      /**
       * This creates a <code>Label</code> object used to represent the
       * annotation provided. Creating the label in this way ensures
       * that each variant has access to the serialization methods 
       * defined for each type an XML element name.
       * 
       * @param label this is the annotation to create the label for
       * 
       * @return this is the label created for the annotation
       */
      public Label getLabel(Element element) {
         return new ElementLabel(contact, element);
      }
      
      /**
       * Each annotation can provide a class which is used to determine
       * which label is used to serialize an object. This ensures that
       * the correct label is selected whenever serialization occurs.
       * 
       * @param label this is the annotation to extract the type for
       * 
       * @return this returns the class associated with the annotation
       */
      public Class getType(Element element) {
         Class type = element.type();
         
         if(type == void.class) {
            return contact.getType();
         }
         return type;
      }
   }
   
   /**
    * The <code>ElementListExtractor</code> object is used extract the
    * constituent parts of the provided variant annotation. This can
    * also be used to create a </code>Label</code> object for each
    * of the declared annotation for dynamic serialization.
    * 
    * @author Niall Gallagher
    */
   private static class ElementListExtractor implements Extractor<ElementList>{
      
      /**
       * This is the variant annotation to extract the labels for.
       */
      private final VariantList variant;
      
      /**
       * This is the contact that has been annotated as a variant.
       */
      private final Contact contact;
      
      /**
       * Constructor for the <code>ElementListExtractor</code> object. 
       * This is used to create an extractor that can be used to extract
       * the various labels used to serialize and deserialize objects.
       * 
       * @param contact this is the contact annotated as a variant
       * @param variant this is the variant annotation to extract from
       */
      public ElementListExtractor(Contact contact, VariantList variant) throws Exception {
         this.variant = variant;
         this.contact = contact;
      }
      
      /**
       * This is used to acquire each annotation that forms part of the
       * variant group. Extracting the annotations in this way allows
       * the extractor to build a <code>Label</code> which can be used
       * to represent the annotation. Each label can then provide a
       * converter implementation to serialize objects.
       * 
       * @return this returns each annotation within the variant group
       */
      public List<ElementList> getAnnotations() {
         ElementList[] list = variant.value();
         
         if(list.length > 0) {
            return Arrays.asList(list);
         }
         return Collections.emptyList();
      }
      
      /**
       * This creates a <code>Label</code> object used to represent the
       * annotation provided. Creating the label in this way ensures
       * that each variant has access to the serialization methods 
       * defined for each type an XML element name.
       * 
       * @param label this is the annotation to create the label for
       * 
       * @return this is the label created for the annotation
       */
      public Label getLabel(ElementList element) {
         return new ElementListLabel(contact, element);
      }
      
      /**
       * Each annotation can provide a class which is used to determine
       * which label is used to serialize an object. This ensures that
       * the correct label is selected whenever serialization occurs.
       * 
       * @param label this is the annotation to extract the type for
       * 
       * @return this returns the class associated with the annotation
       */
      public Class getType(ElementList element) {
         return element.type();
      }
   }
   
   /**
    * The <code>ElementListExtractor</code> object is used extract the
    * constituent parts of the provided variant annotation. This can
    * also be used to create a </code>Label</code> object for each
    * of the declared annotation for dynamic serialization.
    * 
    * @author Niall Gallagher
    */
   private static class ElementMapExtractor implements Extractor<ElementMap>{
      
      /**
       * This is the variant annotation to extract the labels for.
       */
      private final VariantMap variant;
      
      /**
       * This is the contact that has been annotated as a variant.
       */
      private final Contact contact;
      
      /**
       * Constructor for the <code>ElementMapExtractor</code> object. 
       * This is used to create an extractor that can be used to extract
       * the various labels used to serialize and deserialize objects.
       * 
       * @param contact this is the contact annotated as a variant
       * @param variant this is the variant annotation to extract from
       */
      public ElementMapExtractor(Contact contact, VariantMap variant) throws Exception {
         this.variant = variant;
         this.contact = contact;
      }
      
      /**
       * This is used to acquire each annotation that forms part of the
       * variant group. Extracting the annotations in this way allows
       * the extractor to build a <code>Label</code> which can be used
       * to represent the annotation. Each label can then provide a
       * converter implementation to serialize objects.
       * 
       * @return this returns each annotation within the variant group
       */
      public List<ElementMap> getAnnotations() {
         ElementMap[] list = variant.value();
         
         if(list.length > 0) {
            return Arrays.asList(list);
         }
         return Collections.emptyList();
      }
      
      /**
       * This creates a <code>Label</code> object used to represent the
       * annotation provided. Creating the label in this way ensures
       * that each variant has access to the serialization methods 
       * defined for each type an XML element name.
       * 
       * @param label this is the annotation to create the label for
       * 
       * @return this is the label created for the annotation
       */
      public Label getLabel(ElementMap element) {
         return new ElementMapLabel(contact, element);
      }
      
      /**
       * Each annotation can provide a class which is used to determine
       * which label is used to serialize an object. This ensures that
       * the correct label is selected whenever serialization occurs.
       * 
       * @param label this is the annotation to extract the type for
       * 
       * @return this returns the class associated with the annotation
       */
      public Class getType(ElementMap element) {
         return element.valueType();
      }
   }
}
