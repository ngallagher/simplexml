/*
 * TextLabel.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

import simple.xml.Text;

/**
 * The <code>TextLabel</code> represents a label that is used to get
 * a converter for a text entry within an XML element. This label is
 * used to convert an XML text entry into a primitive value such as 
 * a string or an integer, this will throw an exception if the field
 * value does not represent a primitive object.
 * 
 * @author Niall Gallagher
 * 
 *  @see simple.xml.Text
 */
class TextLabel implements Label {
   
   /**
    * This represents the signature of the annotated contact.
    */
   private Signature detail;
   
   /**
    * The contact that this annotation label represents.
    */
   private Contact contact;
   
   /**
    * References the annotation that was used by the contact.
    */
   private Text label;
   
   /**
    * This is the type of the class that the field references.
    */
   private Class type;
   
   /**
    * This is the default value to use if the real value is null.
    */
   private String empty;
   
   /**
    * Constructor for the <code>TextLabel</code> object. This is
    * used to create a label that can convert a XML node into a 
    * primitive value from an XML element text value.
    * 
    * @param contact this is the contact this label represents
    * @param label this is the annotation for the contact 
    */
   public TextLabel(Contact contact, Text label) {
      this.detail = new Signature(contact, this);
      this.type = contact.getType();
      this.empty = label.empty();
      this.contact = contact;
      this.label = label;      
   }
   
   /**
    * Creates a converter that can be used to transform an XML node to
    * an object and vice versa. The converter created will handles
    * only XML text and requires the source object to be provided. 
    * 
    * @param source this is the source object used for serialization
    * 
    * @return this returns a converter for serializing XML elements
    */
   public Converter getConverter(Source source) throws Exception {
      String ignore = getEmpty();
      
      if(!Factory.isPrimitive(type)) {
         throw new TextException("Cannot use %s to represent %s", label, type);
      }
      return new Primitive(source, type, ignore);
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
      if(detail.isEmpty(empty)) {
         return null;
      }
      return empty;
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
    * This is used to acquire the name of the element or attribute
    * that is used by the class schema. The name is determined by
    * checking for an override within the annotation. If it contains
    * a name then that is used, if however the annotation does not
    * specify a name the the field or method name is used instead.
    * 
    * @return returns the name that is used for the XML property
    */
   public String getName() {
      return contact.toString();
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
   public String getOverride(){
      return contact.toString();
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
    * This is typically used to acquire the parent value as acquired
    * from the annotation. However given that the annotation this
    * represents does not have a parent attribute this will always
    * provide a null value for the parent string.
    * 
    * @return this will always return null for the parent value 
    */
   public String getParent() {
      return null;
   }
   
   /**
    * This is used to acquire the dependant class for this label. 
    * This returns null as there are no dependants to the XML text
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
    * This is used to determine if the <code>Text</code> method or
    * field is to have its value written as a CDATA block. This will
    * set the output node to CDATA mode if this returns true, if it
    * is false data will be written according to an inherited mode.
    * By default inherited mode results in escaped XML text.
    * 
    * @return this returns true if the text is to be a CDATA block
    */
   public boolean isData() {
      return label.data();
   }
   
   /**
    * This method is used by the deserialization process to check
    * to see if an annotation is inline or not. If an annotation
    * represents an inline XML entity then the deserialization
    * and serialization process ignores overrides and special 
    * attributes. By default all text entities are inline.
    * 
    * @return this always returns true for text labels
    */
   public boolean isInline() {
      return true;
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
