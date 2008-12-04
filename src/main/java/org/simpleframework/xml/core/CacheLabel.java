/*
 * CacheLabel.java July 2007
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

/**
 * The <code>CacheLabel</code> object is used to acquire details from an
 * inner label object so that details can be retrieved repeatedly without
 * the need to perform any logic for extracting the values. This ensures
 * that a class XML schema requires only initial processing the first
 * time the class XML schema is required. 
 * 
 * @author Niall Gallagher
 */
class CacheLabel implements Label {
   
   /**
    * This is the decorator that is associated with the label.
    */
   private final Decorator decorator;

   /**
    * This is the contact used to set and get the value for the node.
    */
   private final Contact contact;
   
   /**
    * This is used to represent the dependent class to be used.
    */
   private final Class depend;
   
   /**
    * This is used to represent the label class that this will use.
    */
   private final Class type;
   
   /**
    * This is used to represent the name of the entry item use.
    */
   private final String entry;
   
   /**
    * This is used to represent the name override for the annotation.
    */
   private final String override;
   
   /**
    * This is used to represent the name of the annotated element.
    */
   private final String name;
   
   /**
    * This is used to represent whether the data is written as data. 
    */
   private final boolean data;
   
   /**
    * This is used to represent whether the entity is required or not.
    */
   private final boolean required;
   
   /**
    * This is used to determine whether the entity is inline or not. 
    */
   private final boolean inline;
   
   /**
    * This is the label the this cache is wrapping the values for.
    */
   private final Label label;
   
   /**
    * Constructor for the <code>CacheLabel</code> object. This is used
    * to create a <code>Label</code> that acquires details from another
    * label in such a way that any logic involved in acquiring details
    * is performed only once.
    * 
    * @param label this is the label to acquire the details from  
    */
   public CacheLabel(Label label) throws Exception { 
      this.decorator = label.getDecorator();
      this.contact = label.getContact();
      this.depend = label.getDependant();
      this.required = label.isRequired();
      this.override = label.getOverride();
      this.inline = label.isInline();
      this.type = label.getType();
      this.name = label.getName();
      this.entry = label.getEntry();
      this.data = label.isData();
      this.label = label;
   }
   
   /**
    * This is used to acquire the contact object for this label. The 
    * contact retrieved can be used to set any object or primitive that
    * has been deserialized, and can also be used to acquire values to
    * be serialized in the case of object persistence. All contacts 
    * that are retrieved from this method will be accessible. 
    * 
    * @return returns the field that this label is representing
    */
   public Contact getContact() {
      return contact;
   }
   
   /**
    * This is used to acquire the <code>Decorator</code> for this.
    * A decorator is an object that adds various details to the
    * node without changing the overall structure of the node. For
    * example comments and namespaces can be added to the node with
    * a decorator as they do not affect the deserialization.
    * 
    * @return this returns the decorator associated with this
    */
   public Decorator getDecorator() throws Exception {
      return decorator;
   }
   
   /**
    * This method returns a <code>Converter</code> which can be used to
    * convert an XML node into an object value and vice versa. The 
    * converter requires only the context object in order to perform
    * serialization or deserialization of the provided XML node.
    * 
    * @param context this is the context object for the serialization
    * 
    * @return this returns an object that is used for conversion
    */
   public Converter getConverter(Context context) throws Exception {
      return label.getConverter(context);
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
   public String getName(Context context) throws Exception {
      return label.getName(context);
   }
   
   /**
    * This is used to provide a configured empty value used when the
    * annotated value is null. This ensures that XML can be created
    * with required details regardless of whether values are null or
    * not. It also provides a means for sensible default values.
    * 
    * @param context this is the context object for the serialization
    * 
    * @return this returns the string to use for default values
    */
   public Object getEmpty(Context context) throws Exception {
      return label.getEmpty(context);
   }
   
   /**
    * This returns the dependent type for the annotation. This type
    * is the type other than the annotated field or method type that
    * the label depends on. For the <code>ElementList</code> and 
    * the <code>ElementArray</code> this is the component type that
    * is deserialized individually and inserted into the container. 
    * 
    * @return this is the type that the annotation depends on
    */
   public Class getDependant() throws Exception {
      return depend;
   }
   
   /**
    * This is used to either provide the entry value provided within
    * the annotation or compute a entry value. If the entry string
    * is not provided the the entry value is calculated as the type
    * of primitive the object is as a simplified class name.
    * 
    * @return this returns the name of the XML entry element used 
    */
   public String getEntry() throws Exception {
      return entry;
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
      return name;
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
      return override;
   }
   
   /**
    * This acts as a convenience method used to determine the type of
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
    * This is used to determine whether the annotation requires it
    * and its children to be written as a CDATA block. This is done
    * when a primitive or other such element requires a text value
    * and that value needs to be encapsulated within a CDATA block.
    * 
    * @return this returns true if the element requires CDATA
    */
   public boolean isData() {
      return data;
   }
   
   /**
    * This is used to determine whether the label represents an
    * inline XML entity. The <code>ElementList</code> annotation
    * and the <code>Text</code> annotation represent inline 
    * items. This means that they contain no containing element
    * and so can not specify overrides or special attributes.
    * 
    * @return this returns true if the annotation is inline
    */
   public boolean isInline() {
      return inline;
   }
   
   /**
    * Determines whether the XML attribute or element is required. 
    * This ensures that if an XML element is missing from a document
    * that deserialization can continue. Also, in the process of
    * serialization, if a value is null it does not need to be 
    * written to the resulting XML document.
    * 
    * @return true if the label represents a some required data
    */
   public boolean isRequired() {
      return required;
   }
   
   /**
    * This is used to describe the annotation and method or field
    * that this label represents. This is used to provide error
    * messages that can be used to debug issues that occur when
    * processing a method. This should provide enough information
    * such that the problem can be isolated correctly. 
    * 
    * @return this returns a string representation of the label
    */
   public String toString() {
      return label.toString();
   }
}
