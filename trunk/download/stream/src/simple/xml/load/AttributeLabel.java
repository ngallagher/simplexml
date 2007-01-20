/*
 * AttributeLabel.java July 2006
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
import simple.xml.Attribute;

/**
 * The <code>AttributeLabel</code> object is used convert any value
 * retrieved from an XML attribute to a primitive object. This is
 * also used to convert from a primitive object to an XML attribute
 * using the <code>String.valueOf</code> method. 
 * 
 * @author Niall Gallagher
 */
final class AttributeLabel implements Label {

   /**
    * Represents the annotation used to label the field.
    */
   private Attribute label;
	
   /**
    * This is the field that this label object represents.
    */
   private Field field;
   
   /**
    * This is the type that the field object references. 
    */
   private Class type;
   
   /**
    * Constructor for the <code>AttributeLabel</code> object. This 
    * is used to create a label that can convert from an object to an
    * XML attribute and vice versa. This requires the annotation and
    * field extracted from the XML schema class.
    * 
    * @param field this is the field from the XML schema class
    * @param label represents the annotation for the field
    */
   public AttributeLabel(Field field, Attribute label) {
      this.type = field.getType();
      this.field = field;
      this.label = label; 
   }   
   
   /**
    * Creates a <code>Converter</code> that can convert an attribute
    * to a primitive object. This requires the source object used
    * for the current instance of XML serialization being performed.
    * 
    * @param root this is source object used for serialization
    */
   public Converter getConverter(Source root) {
      return new Primitive(root, type);
   }
   
   /**
    * This acts as a convinience method used to determine the type of
    * the field this represents. This will be a primitive type of a
    * primitive type from the <code>java.lang</code> primitives.
    * 
    * @return this returns the type of the field class
    */  
   public Class getType() {
      return type;
   }  
   
   /**
    * This is used to acquire the name of the XML attribute as taken
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
    * This is used to determine whether the attribute is required. 
    * This ensures that if an attribute is missing from a document
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
