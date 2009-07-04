/*
 * LabelFactory.java July 2006
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
final class ParameterFactory {

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
   public static Parameter getInstance(Constructor contact, Annotation label, int index) throws Exception {   
      Constructor factory = getConstructor(label);    
      
      if(!factory.isAccessible()) {
         factory.setAccessible(true);
      }
      return (Parameter)factory.newInstance(contact, label, index);
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
          return new Entry(ElementParameter.class, Element.class);
       }
       if(label instanceof ElementList) {
          return new Entry(ElementListParameter.class, ElementList.class);
       }
       if(label instanceof ElementArray) {
          return new Entry(ElementArrayParameter.class, ElementArray.class);               
       }
       if(label instanceof ElementMap) {
          return new Entry(ElementMapParameter.class, ElementMap.class);
       }
       if(label instanceof Attribute) {
          return new Entry(AttributeParameter.class, Attribute.class);
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
       public Class type;
       
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
       public Entry(Class label, Class type) {
          this.label = label;
          this.type = type;
       }
       
       /**
        * Creates the constructor used to instantiate the label for
        * the XML annotation. The constructor returned will take two
        * arguments, a contact and the XML annotation type. 
        * 
        * @return returns the constructor for the label object
        */
       public Constructor getConstructor() throws Exception {
          return getConstructor(Constructor.class, type, int.class);
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
       private Constructor getConstructor(Class... types) throws Exception {
          return label.getConstructor(types);
       }
    }
}
