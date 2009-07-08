/*
 * ElementListLabel.java July 2006
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

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.stream.Style;

/**
 * The <code>ElementListLabel</code> represents a label that is used
 * to represent an XML element list in a class schema. This element 
 * list label can be used to convert an XML node into a collection of
 * composite objects. Each element converted with the converter this
 * creates must be an XML serializable element.
 * 
 * @author Niall Gallagher
 * 
 *  @see org.simpleframework.xml.ElementList
 */
class ElementListLabel implements Label {
   
   /**
    * This is the decorator that is associated with the element.
    */
   private Decorator decorator;

   /**
    * This references the annotation that the field uses.
    */
   private ElementList label;
   
   /**
    * This contains the details of the annotated contact object.
    */
   private Signature detail;
   
   /**
    * This is the type of collection this list will instantiate.
    */
   private Class type;
   
   /**
    * Represents the type of objects this list will hold.
    */
   private Class item;
   
   /**
    * This is the name of the XML entry from the annotation.
    */
   private String entry;
   
   /**
    * This is the name of the element for this label instance.
    */
   private String name;  

   /**
    * Constructor for the <code>ElementListLabel</code> object. This
    * creates a label object, which can be used to convert an XML 
    * node to a <code>Collection</code> of XML serializable objects.
    * 
    * @param contact this is the contact that this label represents
    * @param label the annotation that contains the schema details
    */
   public ElementListLabel(Contact contact, ElementList label) {
      this.detail = new Signature(contact, this);
      this.decorator = new Qualifier(contact);
      this.type = contact.getType();
      this.entry = label.entry();
      this.item = label.type();
      this.name = label.name();      
      this.label = label;
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
    * This will create a <code>Converter</code> for transforming an XML
    * element into a collection of XML serializable objects. The XML
    * schema class for these objects must be present the element list
    * annotation. 
    * 
    * @param context this is the context object used for serialization
    * 
    * @return this returns the converter for creating a collection 
    */
   public Converter getConverter(Context context) throws Exception {
      String entry = getEntry(context);
      
      if(!label.inline()) {
         return getConverter(context, entry);
      }
      return getInlineConverter(context, entry);      
   }

   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into a collection of XML serializable objects. The XML
    * schema class for these objects must be present the element list
    * annotation. 
    * 
    * @param context this is the context object used for serialization
    * @param name this is the name of the XML entry element to use
    * 
    * @return this returns the converter for creating a collection 
    */
   private Converter getConverter(Context context, String name) throws Exception {    
      Class item = getDependant();
      
      if(!context.isPrimitive(item)) {
         return new CompositeList(context, type, item, name);
      }
      return new PrimitiveList(context, type, item, name);      
   }
   
   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into a collection of XML serializable objects. The XML
    * schema class for these objects must be present the element list
    * annotation. 
    * 
    * @param context this is the context object used for serialization
    * 
    * @return this returns the converter for creating a collection 
    */
   private Converter getInlineConverter(Context context, String name) throws Exception {      
      Class item = getDependant();
      
      if(!context.isPrimitive(item)) {
         return new CompositeInlineList(context, type, item, name);
      }
      return new PrimitiveInlineList(context, type, item, name);      
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
   public String getName(Context context) throws Exception{
      Style style = context.getStyle();
      String name = detail.getName();
      
      return style.getElement(name);
   }
   
   /**
    * This is used to either provide the entry value provided within
    * the annotation or compute a entry value. If the entry string
    * is not provided the the entry value is calculated as the type
    * of primitive the object is as a simplified class name.
    * 
    * @param context this is the context used to style the entry
    * 
    * @return this returns the name of the XML entry element used 
    */
   private String getEntry(Context context) throws Exception {
      Style style = context.getStyle();
      String entry = getEntry();
      
      return style.getElement(entry);
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
      Factory factory = new CollectionFactory(context, type);
      
      if(!label.empty()) {
         return factory.getInstance();
      }
      return null;
   }
   
   /**
    * This is used to acquire the dependant type for the annotated
    * list. This will simply return the type that the collection is
    * composed to hold. This must be a serializable type, that is,
    * a type that is annotated with the <code>Root</code> class.
    * 
    * @return this returns the component type for the collection
    */
   public Class getDependant() throws Exception  {      
      Contact contact = getContact();
     
      if(item == void.class) {
         item = contact.getDependant();
      }        
      if(item == null) {
         throw new ElementException("Unable to determine type for %s", label);           
      }     
      return item;
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
      if(detail.isEmpty(entry)) {
         entry = detail.getEntry();
      }
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
   public String getName() throws Exception{
      return detail.getName();
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
    * This is used to determine whether the annotation requires it
    * and its children to be written as a CDATA block. This is done
    * when a primitive or other such element requires a text value
    * and that value needs to be encapsulated within a CDATA block.
    * 
    * @return currently the element list does not require CDATA
    */
   public boolean isData() {
      return label.data();
   }
   
   /**
    * This method is used to determine if the label represents an
    * attribute. This is used to style the name so that elements
    * are styled as elements and attributes are styled as required.
    * 
    * @return this is used to determine if this is an attribute
    */
   public boolean isAttribute() {
      return false;
   }
   
   /**
    * This is used to determine if the label is a collection. If the
    * label represents a collection then any original assignment to
    * the field or method can be written to without the need to 
    * create a new collection. This allows obscure collections to be
    * used and also allows initial entries to be maintained.
    * 
    * @return true if the label represents a collection value
    */
   public boolean isCollection() {
      return true;
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
    * This is used to determine whether the list has been specified
    * as inline. If the list is inline then no overrides are needed
    * and the outer XML element for the list is not used.
    * 
    * @return this returns whether the annotation is inline
    */
   public boolean isInline() {
      return label.inline();
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