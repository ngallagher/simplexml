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

import simple.xml.ElementArray;

/**
 * The <code>ElementArrayLabel</code> represents a label that is used
 * to represent an XML element array in a class schema. This element 
 * array label can be used to convert an XML node into an array of
 * composite or primitive objects. If the array is of primitive types
 * then the <code>parent</code> attribute must be specified so that 
 * the primitive values can be serialized in a structured manner.
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
   private Contact contact;
   
   /**
    * This is the type of array this label will represent.
    */
   private Class type;
   
   /**
    * This is the name of the XML parent from the annotation.
    */
   private String parent;
   
   /**
    * This is the name of the element from the annotation.
    */
   private String name;
	
   /**
    * Constructor for the <code>ElementArrayLabel</code> object. This
    * creates a label object, which can be used to convert an element
    * node to an array of XML serializable objects.
    * 
    * @param contact this is the contact that this label represents
    * @param label the annotation that contains the schema details
    */
   public ElementArrayLabel(Contact contact, ElementArray label) {
      this.type = contact.getType();      
      this.parent = label.parent();
      this.name = label.name();
      this.contact = contact;
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
         throw new InstantiationException("Type is not an array %s for %s", type, label);
      }
      return getConverter(root, parent);
   }
      
   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into an array of XML serializable objects. The XML schema
    * class for these objects must present the element list annotation. 
    * 
    * @param root this is the source object used for serialization
    * @param parent this is the name of the parent XML element to use
    * 
    * @return this returns the converter for creating a collection 
    */      
   private Converter getConverter(Source root, String parent) throws Exception {
      Class entry = type.getComponentType();
      
      if(!Factory.isPrimitive(entry)) {
         return new CompositeArray(root, type, entry, parent);        
      }
      return new PrimitiveArray(root, type, entry, parent);            
   }

   /**
    * This acts as a convinience method used to determine the type of
    * contact this represents. This is used when an object is written
    * to XML. It determines whether a <code>class</code> attribute
    * is required within the serialized XML element, that is, if the
    * class returned by this is different from the actual value of the
    * object to be serialized then that type needs to be remembered.
    *  
    * @return this returns the type of the contact class
    */
   public Class getType() {
      return type;      
   }
   
   /**
    * This is used to acquire the contact object for this label. The 
    * contact retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistance. All contacts
    * that are retrieved from this method will be accessible. 
    * 
    * @return returns the contact that this label is representing
    */   
   public Contact getContact() {
      return contact;
   }
   
   /**
    * This is used to acquire the name of the XML element as taken
    * from the contact annotation. Every XML annotation must contain 
    * a name, so that it can be identified from the XML source. This
    * allows the class to be used as a schema for the XML document. 
    * 
    * @return returns the name of the annotation for the field
    */    
   public String getName() {
      return name;
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
