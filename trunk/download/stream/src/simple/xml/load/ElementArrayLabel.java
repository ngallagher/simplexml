/*
 * ElementArrayLabel.java July 2006
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
import simple.xml.ElementArray;

/**
 * The <code>ElementArrayLabel</code> represents a label that is used
 * to represent an XML element array in a class schema. This element 
 * array label can be used to convert an XML node into an array of
 * composite objects. Each element converted with the converter this
 * creates must be an XML serializable element.
 * 
 * @author Niall Gallagher
 * 
 *  @see simple.xml.ElementArray
 */
final class ElementArrayLabel implements Label {

   /**
    * This references the annotation that the field uses.
    */
   private ElementArray label;
   
   /**
    * This references the field from the source object.
    */
   private Field field;
   
   /**
    * This is the type of array this label will represent.
    */
   private Class type;
	
   /**
    * Constructor for the <code>ElementArrayLabel</code> object. This
    * creates a label object, which can be used to convert an element
    * node to an array of XML serializable objects.
    * 
    * @param field this is the field that this label represents
    * @param label the annotation that contains the schema details
    */
   public ElementArrayLabel(Field field, ElementArray label) {
      this.type = field.getType();
      this.field = field;
      this.label = label;
   }
	
   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into an array of XML serializable objects. The XML schema
    * class for these objects must present the element list annotation. 
    * 
    * @param root this is the source object used for serialization
    * 
    * @return this returns the converter for creating a collection 
    */
   public Converter getConverter(Source root) throws Exception {
      if(!type.isArray()) {
         throw new InstantiationException("Type is not an array %s", type);
      }
      Class entry = type.getComponentType();

      return new CompositeArray(root, type, entry);      
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
