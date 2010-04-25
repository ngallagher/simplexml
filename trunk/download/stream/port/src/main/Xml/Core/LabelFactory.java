/*
 * LabelFactory.java July 2006
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
import java.lang.reflect.Constructor;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;

/**
 * The <code>LabelFactory</code> object is used to create instances of
 * the <code>Label</code> object that can be used to convert an XML
 * node into a Java object. Each label created requires the contact it
 * represents and the XML annotation it is marked with.  
 * <p>
 * The <code>Label</code> objects created by this factory a selected
 * using the XML annotation type. If the annotation type is not known
 * the factory will throw an exception, otherwise a label instance
 * is created that will expose the properties of the annotation.
 * 
 * @author Niall Gallagher
 */
final class LabelFactory {

   /**
    * Creates a <code>Label</code> using the provided contact and XML
    * annotation. The label produced contains all information related
    * to an object member. It knows the name of the XML entity, as
    * well as whether it is required. Once created the converter can
    * transform an XML node into Java object and vice versa.
    * 
    * @param contact this is contact that the label is produced for
    * @param label represents the XML annotation for the contact
    * 
    * @return returns the label instantiated for the field
    */
   public static Label getInstance(Contact contact, Annotation label) throws Exception {
      Label value = getLabel(contact, label);
      
      if(value == null) {
         return value;
      }      
      return new CacheLabel(value);
   } 
   
   /**
    * Creates a <code>Label</code> using the provided contact and XML
    * annotation. The label produced contains all information related
    * to an object member. It knows the name of the XML entity, as
    * well as whether it is required. Once created the converter can
    * transform an XML node into Java object and vice versa.
    * 
    * @param contact this is contact that the label is produced for
    * @param label represents the XML annotation for the contact
    * 
    * @return returns the label instantiated for the field
    */
   private static Label getLabel(Contact contact, Annotation label) throws Exception {     
      Constructor factory = getConstructor(label);    
      
      if(!factory.isAccessible()) {
         factory.setAccessible(true);
      }
      return (Label)factory.newInstance(contact, label);
   }
    
    /**
     * Creates a constructor that can be used to instantiate the label
     * used to represent the specified annotation. The constructor
     * created by this method takes two arguments, a contact object 
     * and an <code>Annotation</code> of the type specified.
     * 
     * @param label the XML annotation representing the label
     * 
     * @return returns a constructor for instantiating the label 
     * 
     * @throws Exception thrown if the annotation is not supported
     */
    private static Constructor getConstructor(Annotation label) throws Exception {
       return getEntry(label).getConstructor();
    }
    
    /**
     * Creates an entry that is used to select the constructor for the
     * label. Each label must implement a constructor that takes a
     * contact and the specific XML annotation for that field. If the
     * annotation is not know this method throws an exception.
     * 
     * @param label the XML annotation used to create the label
     * 
     * @return this returns the entry used to create a suitable
     *         constructor for the label
     * 
     * @throws Exception thrown if the annotation is not supported
     */
    private static Entry getEntry(Annotation label) throws Exception{      
       if(label instanceof Element) {
          return new Entry(ElementLabel.class, Element.class);
       }
       if(label instanceof ElementList) {
          return new Entry(ElementListLabel.class, ElementList.class);
       }
       if(label instanceof ElementArray) {
          return new Entry(ElementArrayLabel.class, ElementArray.class);               
       }
       if(label instanceof ElementMap) {
          return new Entry(ElementMapLabel.class, ElementMap.class);
       }
       if(label instanceof Attribute) {
          return new Entry(AttributeLabel.class, Attribute.class);
       }
       if(label instanceof Version) {
          return new Entry(VersionLabel.class, Version.class);
       }
       if(label instanceof Text) {
          return new Entry(TextLabel.class, Text.class);
       }
       throw new PersistenceException("Annotation %s not supported", label);
    }
    
    /**
     * The <code>Entry<code> object is used to create a constructor 
     * that can be used to instantiate the correct label for the XML
     * annotation specified. The constructor requires two arguments
     * a <code>Contact</code> and the specified XML annotation.
     * 
     * @see java.lang.reflect.Constructor
     */
    private static class Entry {
       
       /**       
        * This is the XML annotation type within the constructor.
        */
       public Class argument;
       
       /**
        * This is the label type that is to be instantiated.
        */
       public Class label;
       
       /**
        * Constructor for the <code>Entry</code> object. This pairs
        * the label type with the XML annotation argument used within
        * the constructor. This allows constructor to be selected.
        * 
        * @param label this is the label type to be instantiated
        * @param argument type that is used within the constructor
        */
       public Entry(Class label, Class argument) {
          this.argument = argument;
          this.label = label;
       }
       
       /**
        * Creates the constructor used to instantiate the label for
        * the XML annotation. The constructor returned will take two
        * arguments, a contact and the XML annotation type. 
        * 
        * @return returns the constructor for the label object
        */
       public Constructor getConstructor() throws Exception {
          return getConstructor(Contact.class);
       }
       
       /**
        * Creates the constructor used to instantiate the label for
        * the XML annotation. The constructor returned will take two
        * arguments, a contact and the XML annotation type.
        * 
        * @param type this is the XML annotation argument type used
        * 
        * @return returns the constructor for the label object
        */
       private Constructor getConstructor(Class type) throws Exception {
          return label.getConstructor(type, argument);
       }
    }
}
