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
 * @see simple.xml.ElementArray
 */
class ElementArrayLabel implements Label {

   /**
    * This references the annotation that the field uses.
    */
   private ElementArray label;
   
   /**
    * This contains the details of the annotated contact object.
    */
   private Signature detail;
   
   /**
    * This is the type of array this label will represent.
    */
   private Class type;
   
   /**
    * This is the name of the XML parent from the annotation.
    */
   private String parent;
   
   /**
    * This is the name of the element for this label instance.
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
      this.detail = new Signature(contact, this);   
      this.type = contact.getType();
      this.parent = label.parent();
      this.name = label.name();
      this.label = label;
   }
	
   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into an array of XML serializable objects. The XML schema
    * class for these objects must present the element array annotation. 
    * 
    * @param root this is the source object used for serialization
    * 
    * @return this returns the converter for creating a collection 
    */
   public Converter getConverter(Source root) throws Exception {
      String parent = getParent();
      
      if(!type.isArray()) {
         throw new InstantiationException("Type is not an array %s for %s", type, label);
      }
      return getConverter(root, parent);
   }  
      
   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into an array of XML serializable objects. The XML schema
    * class for these objects must present the element array annotation. 
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
    * This is used to either provide the parent value provided within
    * the annotation or compute a parent value. If the parent string
    * is not provided the the parent value is calculated as the type
    * of primitive the object is as a simplified class name.
    * 
    * @return this returns the name of the XML parent element used 
    */
   public String getParent() throws Exception {      
      if(detail.isEmpty(parent)) {
         parent = detail.getParent();
      }
      return parent;
   }
   
   /**
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    * 
    * @return returns the name that is used for the XML property
    */
   public String getName() throws Exception{
      return detail.getName();
   }
   
   /**
    * This is used to acquire the dependant type for the annotated
    * array. This will simply return the type that the array is
    * composed to hold. This must be a serializable type, that is,
    * a type that is annotated with the <code>Root</code> class.
    * 
    * @return this returns the component type for the array
    */
   public Class getDependant() {
      return type.getComponentType();
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
      return detail.getContact();
   }
   
   /**
    * This is used to acquire the name of the element or attribute
    * as taken from the annotation. If the element or attribute
    * explicitly specifies a name then that name is used for the
    * XML element or attribute used. If however no overriding name
    * is provided then the method or field is used for the name. 
    * 
    * @return returns the name of the annotation for the contact
    */
   public String getOverride() {
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
    * This is used to determine whether the annotation requires it
    * and its children to be written as a CDATA block. This is done
    * when a primitive or other such element requires a text value
    * and that value needs to be encapsulated within a CDATA block.
    * 
    * @return this returns true if the element requires CDATA
    */
   public boolean isData() {
      return label.data();
   }
   
   /**
    * This is used to provide a configured empty value used when the
    * annotated value is null. This ensures that XML can be created
    * with required details regardless of whether values are null or
    * not. It also provides a means for sensible default values.
    * 
    * @return this returns the string to use for default values
    */
   public String getEmpty() {
      return null;
   }
   
   /**
    * This method is used by the deserialization process to check
    * to see if an annotation is inline or not. If an annotation
    * represents an inline XML entity then the deserialization
    * and serialization process ignores overrides and special 
    * attributes. By default element arrays are not inline.
    * 
    * @return this always returns false for array labels
    */
   public boolean isInline() {
      return false;
   }
   
   /**
    * This is used to describe the annotation and method or field
    * that this label represents. This is used to provide error
    * messages that can be used to debug issues that occur when
    * processing a method. This will provide enough information
    * such that the problem can be isolated correctly. 
    * 
    * @return this returns a string representation of the label
    */
   public String toString() {
      return detail.toString();
   }   
}
