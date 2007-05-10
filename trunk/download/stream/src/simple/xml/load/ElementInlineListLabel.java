/*
 * ElementInlineListLabel.java July 2006
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

import simple.xml.ElementInlineList;
import simple.xml.Root;

/**
 * The <code>ElementListLabel</code> represents a label that is used
 * to represent an XML element list in a class schema. This element 
 * list label can be used to convert an XML node into a collection of
 * composite objects. Each element converted with the converter this
 * creates must be an XML serializable element.
 * 
 * @author Niall Gallagher
 * 
 *  @see simple.xml.ElementInlineList
 */
final class ElementInlineListLabel implements Label {

   /**
    * This references the annotation that the field uses.
    */
   private ElementInlineList label;
   
   /**
    * This references the contact from the source object.
    */
   private Contact contact;
   
   /**
    * This is the type of collection this list will instantiate.
    */
   private Class type;
   
   /**
    * Represents the type of objects this list will hold.
    */
   private Class item;
	
   /**
    * This is the root annotation for the specified type.
    */
   private Root root;
   
   /**
    * Constructor for the <code>ElementListLabel</code> object. This
    * creates a label object, which can be used to convert an XML 
    * node to a <code>Collection</code> of XML serializable objects.
    * 
    * @param field this is the field that this label represents
    * @param label the annotation that contains the schema details
    */
   public ElementInlineListLabel(Contact contact, ElementInlineList label) throws Exception {
      this.type = contact.getType();  
      this.item = label.type();
      this.label = label; 
      this.root = getRoot(item);
      this.contact = contact;     
   }
   
   /**
    * This is used to acquire the <code>Root</code> annotation for the
    * specified type. This is requires so that the inline list knows
    * what the name of the elements it contains is. This allows the
    * deserialization process to collect the correct elements.
    * 
    * @param type this is the type to acquire the root annotation for
    * 
    * @return this returns the root annotation for the specified type
    */
   private Root getRoot(Class<?> type) throws Exception {
      Class<?> entry = item;
      
      if(!type.isAnnotationPresent(Root.class)) {
         throw new RootException("Entry %s specified by %s has no root", entry, label);
      }
      return entry.getAnnotation(Root.class);      
   }
   
   /**
    * This will create a <code>Converter</code> for transforming an XML
    * element into a collection of XML serializable objects. The XML
    * schema class for these objects must be present the element list
    * annotation. 
    * 
    * @param root this is the source object used for serialization
    * 
    * @return this returns the converter for creating a collection 
    */
   public Converter getConverter(Source root) throws Exception {
      return new CompositeInlineList(root, type, item);      
   }   
   
   /**
    * This is used to acquire the name of the XML element as taken
    * from the contact annotation. Every XML annotation must contain 
    * a name, so that it can be identified from the XML source. This
    * allows the class to be used as a schema for the XML document. 
    * 
    * @return returns the name of the annotation for the contact
    */   
   public String getName() {
      return root.name();
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
