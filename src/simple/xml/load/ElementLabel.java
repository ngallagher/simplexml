/*
 * ElementLabel.java July 2006
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

import java.lang.reflect.Field;
import simple.xml.Element;

/**
 * The <code>ElementLabel</code> represents a label that is used to
 * represent an XML element in a class schema. This element can be
 * used to convert an XML node into either a primitive value such as
 * a string or composite object value, which is itself a schema for
 * a section of XML within the XML document. 
 * 
 * @author Niall Gallagher
 * 
 *  @see simple.xml.Element
 */
final class ElementLabel implements Label {
   
   /**
    * References the annotation that was used by the field.
    */
   private Element label;
   
   /**
    * The field that this element label represents.
    */
   private Field field;
   
   /**
    * This is the type of the class that the field references.
    */
   private Class type;
   
   /**
    * Constructor for the <code>ElementLabel</code> object. This is
    * used to create a label that can convert a XML node into a 
    * composite object or a primitive type from an XML element. 
    * 
    * @param field this is the field that this label represents
    * @param label this is the annotation for the field 
    */
   public ElementLabel(Field field, Element label) {
      this.type = field.getType();
      this.label = label;
      this.field = field;
   }
   
   /**
    * Creates a converter that can be used to transform an XML node to
    * an object and vice versa. The converter created will handles
    * only XML elements and requires the source object to be provided. 
    * 
    * @param source this is the source object used for serialization
    * 
    * @return this returns a converter for serializing XML elements
    */
   public Converter getConverter(Source source) {
      if(isPrimitive(type)) {
         return new Primitive(source, type);
      }
      return new Composite(source, type);
   }
   
   /**
    * This is used to acquire the field object for this label. The 
    * field retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistance. All fields that
    * are retrieved from this method will be accessible. 
    * 
    * @return returns the field that this label is representing
    */
   public Field getField() {
      return field;
   }
   
   /**
    * This is used to acquire the name of the XML element as taken
    * from the field annotation. Every XML annotation must contain a
    * name, so that it can be identified from the XML source. This
    * allows the class to be used as a schema for the XML document. 
    * 
    * @return returns the name of the annotation for the field
    */   
   public String getName() {
      return label.name();
   }
   
   /**
    * This acts as a convinience method used to determine the type of
    * the field this represents. This is used when an object is written
    * to XML. It determines whether a <code>class</code> attribute
    * is required within the serialized XML element, that is, if the
    * class returned by this is different from the actual value of the
    * object to be serialized then that type needs to be remembered.
    *  
    * @return this returns the type of the field class
    */   
   public Class getType() {
      return type;
   }
   
   /**
    * This is used to determine whether the XML element is required. 
    * This ensures that if an XML element is missing from a document
    * that deserialization can continue. Also, in the process of
    * serialization, if a value is null it does not need to be 
    * written to the resulting XML document.
    * 
    * @return true if the label represents a some required data
    */   
   public boolean isRequired() {
      return label.required();
   }
   
   /**
    * This method is used to determine whether the field type is a
    * primitive or enumerated type. If it is either of these then it
    * must be a leaf element, that is, an element without any other
    * elements. If this is true a primitive converter is used.
    * 
    * @param type the type checked to determine if it is primitive
    * 
    * @return true if the type is primitive, false otherwise
    */
   private boolean isPrimitive(Class type) {
      if(type.equals(Boolean.class)) {
         return true;              
      }
      if(type.equals(Integer.class)) {
         return true;              
      }      
      if(type.equals(Float.class)) {
         return true;               
      }
      if(type.equals(Long.class)) {
         return true;              
      }
      if(type.equals(String.class)) {
         return true;              
      }
      if(type.equals(Double.class)) {
         return true;              
      }
      if(type.equals(Byte.class)) {
         return true;              
      }
      if(type.equals(Short.class)) {
         return true;              
      }
      if(type.isPrimitive()) {
         return true;                 
      }
      return type.isEnum();
   }
   
   /**
    * This provides a string describing the XML annotation this is
    * used to represent. This is used when debugging an error as
    * it can be used within stack traces for problem labels.
    * 
    * @return this returns a description of the XML annotation
    */
   public String toString() {
      return label.toString();
   }
}
