/*
 * AttributeParameter.java July 2009
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.Attribute;

/**
 * The <code>AttributeParameter</code> represents a constructor 
 * parameter. It contains the XML annotation used on the parameter
 * as well as the name of the parameter and its position index.
 * A parameter is used to validate against the annotated methods 
 * and fields and also to determine the deserialized values that
 * should be injected in to the constructor to instantiate it.
 * 
 * @author Niall Gallagher
 */
class AttributeParameter implements Parameter {
   
   /**
    * This is the constructor the parameter was declared in.
    */
   private final Constructor factory;
   
   /**
    * This is the contact used to determine the parameter name.
    */
   private final Contact contact;
   
   /**
    * This is the label that will create the parameter name. 
    */
   private final Label label;
   
   /**
    * This is the actual name that has been determined.
    */
   private final String name;
   
   /**
    * This is the type of the label represented by this.
    */
   private final Class type;
   
   /**
    * This is the index that the parameter was declared at.
    */
   private final int index;
   
   /**   
    * Constructor for the <code>AttributeParameter</code> object.
    * This is used to create a parameter that can be used to 
    * determine a consistent name using the provided XML annotation.
    * 
    * @param factory this is the constructor the parameter is in
    * @param value this is the annotation used for the parameter
    * @param index this is the index the parameter appears at
    */
   public AttributeParameter(Constructor factory, Attribute value, int index) throws Exception {
      this.contact = new Contact(value, factory, index);
      this.label = new AttributeLabel(contact, value);
      this.type = label.getType();
      this.name = label.getName();
      this.factory = factory;
      this.index = index;
   }
   
   /**
    * This is used to acquire the name of the parameter that this
    * represents. The name is determined using annotation and 
    * the name attribute of that annotation, if one is provided.    
    * 
    * @return this returns the name of the annotated parameter
    */
   public String getName() throws Exception {
      return name;
   }
   
   /**
    * This is used to acquire the name of the parameter that this
    * represents. The name is determined using annotation and 
    * the name attribute of that annotation, if one is provided.
    * 
    * @param context this is the context used to style the name
    * 
    * @return this returns the name of the annotated parameter
    */
   public String getName(Context context) throws Exception {
      return label.getName(context);
   }
   
   /**
    * This is used to acquire the annotated type class. The class
    * is the type that is to be deserialized from the XML. This
    * is used to validate against annotated fields and methods.
    * 
    * @return this returns the type used for the parameter
    */
   public Class getType() {
      return factory.getParameterTypes()[index];
   } 
   
   /**
    * This is used to acquire the annotation that is used for the
    * parameter. The annotation provided will be an XML annotation
    * such as the <code>Element</code> or <code>Attribute</code>
    * annotation.
    * 
    * @return this returns the annotation used on the parameter
    */
   public Annotation getAnnotation() {
      return contact.getAnnotation();
   }
   
   /**
    * This returns the index position of the parameter in the
    * constructor. This is used to determine the order of values
    * that are to be injected in to the constructor.
    * 
    * @return this returns the index for the parameter
    */
   public int getIndex() {
      return index;
   }
   
   /**
    * This is used to determine if the parameter is required. If 
    * an attribute is not required then it can be null. Which 
    * means that we can inject a null value. Also, this means we
    * can match constructors in a more flexible manner.
    * 
    * @return this returns true if the parameter is required
    */
   public boolean isRequired() {
      return label.isRequired();
   }
   
   /**
    * This is used to determine if the parameter is primitive. A
    * primitive parameter must not be null. As there is no way to
    * provide the value to the constructor. A default value is 
    * not a good solution as it affects the constructor score.
    * 
    * @return this returns true if the parameter is primitive
    */
   public boolean isPrimitive() {
      return type.isPrimitive();
   }
   
   /**
    * This is used to provide a textual representation of the 
    * parameter. Providing a string describing the parameter is
    * useful for debugging and for exception messages.
    * 
    * @return this returns the string representation for this
    */
   public String toString() {
      return contact.toString();
   }
   
   /**
    * The <code>Contact</code> represents a contact object that is
    * to be used for a label in order to extract a parameter name.
    * The parameter name is taken from the XML annotation.
    * 
    * @author Niall Gallagher
    */
   private static class Contact extends ParameterContact<Attribute>  {
      
      /**
       * Constructor for the <code>Contact</code> object. This is 
       * used to create an object that acts like an adapter so that
       * the label can create a consistent name for the parameter.
       * 
       * @param label this is the annotation for the parameter
       * @param factory this is the constructor the parameter is in
       * @param index this is the index for the parameter
       */
      public Contact(Attribute label, Constructor factory, int index) {
         super(label, factory, index);
      }
      
      /**
       * This returns the name of the parameter as taken from the XML
       * annotation. The name provided here is taken by the label
       * and used to compose a name consistent with how fields and
       * methods are named by the system.
       * 
       * @return this returns the name of the annotated parameter
       */
      public String getName() {
         return label.name();
      }
   }
}