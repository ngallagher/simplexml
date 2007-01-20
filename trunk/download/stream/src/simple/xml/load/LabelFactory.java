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

package simple.xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import simple.xml.ElementList;
import simple.xml.Element;
import simple.xml.Attribute;

/**
 * The <code>LabelFactory</code> object is used to create instances of
 * the <code>Label</code> object that can be used to convert an XML
 * node into a Java object. Each label created requires the field it
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
    * Creates a <code>Label</code> using the provided field and XML
    * annotation. The label produced contains all information related
    * to an object member. It knows the name of the XML entity, as
    * well as whether it is required. Once created the converter can
    * transform an XML node into Java object and vice versa.
    * 
    * @param field this is the field that the label is produced for
    * @param label represents the XML annotation for the field
    * 
    * @return returns the label instantiated for the field
    */
   public static Label getInstance(Field field, Annotation label) {
      try {                
         Constructor factory = getConstructor(label);

         if(!field.isAccessible()) {
            field.setAccessible(true);                 
         }         
         return (Label)factory.newInstance(field, label);
      }catch(Exception e) {
         return null;
      }
    } 
    
    /**
     * Creates a constructor that can be used to instantiate the label
     * used to represent the specified annotation. The constructor
     * created by this method takes two arguments, a field object and
     * an <code>Annotation</code> of the type specified.
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
     * field and the specific XML annotation for that field. If the
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
       if(label instanceof Attribute) {
          return new Entry(AttributeLabel.class, Attribute.class);
       }
       throw new RuntimeException("Annotation not supported");
    }
    
    /**
     * The <code>Entry<code> object is used to create a constructor 
     * that can be used to instantiate the correct label for the XML
     * annotation specified. The constructor requires two arguments
     * a <code>Field</code> and the specified XML annotation.
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
        * arguments, a field and the XML annotation type. 
        * 
        * @return returns the constructor for the label object
        */
       public Constructor getConstructor() throws Exception {
          return getConstructor(Field.class);
       }
       
       /**
        * Creates the constructor used to instantiate the label for
        * the XML annotation. The constructor returned will take two
        * arguments, a field and the XML annotation type.
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
