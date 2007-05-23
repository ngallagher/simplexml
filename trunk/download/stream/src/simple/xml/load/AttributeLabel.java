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
    * This is the contact that this label object represents.
    */
   private Signature sign;
   
   /**
    * This is the type that the field object references. 
    */
   private Class type;
   
   /**
    * This is the name of the element from the annotation.
    */
   private String name;
   
   /**
    * Constructor for the <code>AttributeLabel</code> object. This 
    * is used to create a label that can convert from an object to an
    * XML attribute and vice versa. This requires the annotation and
    * contact extracted from the XML schema class.
    * 
    * @param contact this is the field from the XML schema class
    * @param label represents the annotation for the field
    */
   public AttributeLabel(Contact contact, Attribute label) {
      this.sign = new Signature(contact, this);
      this.type = contact.getType();
      this.name = label.name();      
      this.label = label; 
   }   
   
   /**
    * Creates a <code>Converter</code> that can convert an attribute
    * to a primitive object. This requires the source object used
    * for the current instance of XML serialization being performed.
    * 
    * @param root this is source object used for serialization
    */
   public Converter getConverter(Source root) throws Exception {
      return new Primitive(root, type);
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
   public String getName() throws Exception {
      return sign.getName();
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
      return name;
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
      return sign.getContact();
   }
   
   /**
    * This acts as a convinience method used to determine the type of
    * the contact this represents. This will be a primitive type of a
    * primitive type from the <code>java.lang</code> primitives.
    * 
    * @return this returns the type of the contact class
    */  
   public Class getType() {
      return type;
   }
   
   /**
    * This is used to acquire the dependant class for this label. 
    * This returns null as there are no dependants to the attribute
    * annotation as it can only hold primitives with no dependants.
    * 
    * @return this is used to return the dependant type of null
    */
   public Class getDependant() {
      return null;
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
    * Because the attribute can contain only simple text values it
    * is never required to specified as anything other than text.
    * Therefore this will always return false as CDATA does not
    * apply to the attribute values.
    *
    * @return this will always return false for XML attributes
    */
   public boolean isData() {
      return false;
   }
   
   /**
    * This method is used by the deserialization process to check
    * to see if an annotation is inline or not. If an annotation
    * represents an inline XML entity then the deserialization
    * and serialization process ignores overrides and special 
    * attributes. By default all attributes are not inline items.
    * 
    * @return this always returns false for attribute labels
    */
   public boolean isInline() {
      return false;
   }
   
   /**
    * This provides a string describing the XML annotation this is
    * used to represent. This is used when debugging an error as
    * it can be used within stack traces for problem labels.
    * 
    * @return this returns a description of the XML annotation
    */
   public String toString() {
      return sign.toString();
   }
}
