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

package org.simpleframework.xml.load;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.stream.Style;

/**
 * The <code>ElementLabel</code> represents a label that is used to
 * represent an XML element in a class schema. This element can be
 * used to convert an XML node into either a primitive value such as
 * a string or composite object value, which is itself a schema for
 * a section of XML within the XML document. 
 * 
 * @author Niall Gallagher
 * 
 *  @see org.simpleframework.xml.Element
 */
class ElementLabel implements Label {
   
   /**
    * The contact that this element label represents.
    */
   private Signature detail;
   
   /**
    * References the annotation that was used by the field.
    */
   private Element label;
   
   /**
    * This is the type of the class that the field references.
    */
   private Class type;
   
   /**
    * This is the name of the element for this label instance.
    */
   private String name;
   
   /**
    * Constructor for the <code>ElementLabel</code> object. This is
    * used to create a label that can convert a XML node into a 
    * composite object or a primitive type from an XML element. 
    * 
    * @param contact this is the field that this label represents
    * @param label this is the annotation for the contact 
    */
   public ElementLabel(Contact contact, Element label) {
      this.detail = new Signature(contact, this);
      this.type = contact.getType();
      this.name = label.name();     
      this.label = label;      
   }
   
   /**
    * Creates a converter that can be used to transform an XML node to
    * an object and vice versa. The converter created will handles
    * only XML elements and requires the source object to be provided. 
    * 
    * @param root this is the source object used for serialization
    * 
    * @return this returns a converter for serializing XML elements
    */
   public Converter getConverter(Source root) throws Exception {
      if(Factory.isPrimitive(type)) {
         return new Primitive(root, type);
      }
      return new Composite(root, type);
   }
   
   /**
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    * 
    * @param source this is used to provide a styled name
    * 
    * @return returns the name that is used for the XML property
    */
   public String getName(Source source) throws Exception{
      Style style = source.getStyle();
      String name = detail.getName();
      
      return style.getElement(name);
   }
   
   /**
    * This is used to provide a configured empty value used when the
    * annotated value is null. This ensures that XML can be created
    * with required details regardless of whether values are null or
    * not. It also provides a means for sensible default values.
    * 
    * @param root this is the source object for the serialization
    * 
    * @return this returns the string to use for default values
    */
   public Object getEmpty(Source root) {
      return null;
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
    * This is typically used to acquire the entry value as acquired
    * from the annotation. However given that the annotation this
    * represents does not have a entry attribute this will always
    * provide a null value for the entry string.
    * 
    * @return this will always return null for the entry value 
    */
   public String getEntry() {
      return null;
   }
   
   /**
    * This is used to acquire the dependant class for this label. 
    * This returns null as there are no dependants to the element
    * annotation as it can only hold primitives with no dependants.
    * 
    * @return this is used to return the dependant type of null
    */
   public Class getDependant() {
      return null;
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
    * This method is used by the deserialization process to check
    * to see if an annotation is inline or not. If an annotation
    * represents an inline XML entity then the deserialization
    * and serialization process ignores overrides and special 
    * attributes. By default all XML elements are not inline.
    * 
    * @return this always returns false for element labels
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
