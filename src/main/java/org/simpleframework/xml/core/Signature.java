/*
 * Signature.java February 2005
 *
 * Copyright (C) 2005, Niall Gallagher <niallg@users.sf.net>
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

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Root;
import java.beans.Introspector;

/**
 * The <code>Signature</code> object is used to determine the details
 * to use for an annotated field or method using both the field an
 * annotation details. This allows defaults to be picked up from the
 * method or field type if that have not been explicitly overridden
 * in the annotation. 
 * 
 * @author Niall Gallagher
 */
class Signature {
   
   /**
    * This is the actual annotation from the specified contact.
    */
   private Annotation marker;
   
   /**
    * This is the field or method contact that has been annotated.
    */
   private Contact contact;
   
   /**
    * This is the label used to expose the annotation details.
    */
   private Label label;
   
   /**
    * Constructor for the <code>Signature</code> object. This is 
    * used to create an object that will use information available
    * within the field and annotation to determine exactly what 
    * the name of the XML element is to be and the type to use.
    * 
    * @param contact this is the method or field contact used
    * @param label this is the annotation on the contact object
    */
   public Signature(Contact contact, Label label) {
      this.marker = contact.getAnnotation();
      this.contact = contact;
      this.label = label;
   }
   
   /**
    * This is used to acquire the <code>Contact</code> for this. The
    * contact is the actual method or field that has been annotated
    * and is used to set or get information from the object instance.
    * 
    * @return the method or field that this signature represents
    */
   public Contact getContact() {
      return contact;
   }
   
   /**
    * This returns the dependant type for the annotation. This type
    * is the type other than the annotated field or method type that
    * the label depends on. For the <code>ElementList</code> this 
    * can be the generic parameter to an annotated collection type.
    * 
    * @return this is the type that the annotation depends on
    */
   public Class getDependant() throws Exception {
      return label.getDependant();
   }

   /**
    * This method is used to get the entry name of a label using 
    * the type of the label. This ensures that if there is no
    * entry XML element name declared by the annotation that a
    * suitable name can be calculated from the annotated type.
    * 
    * @return this returns a suitable XML entry element name
    */
   public String getEntry() throws Exception {
      Class type = getDependant();   
      
      if(type.isArray()) {
         type = type.getComponentType();
      }
      String name = getName(type);
      
      if(name == null) {
         return null;
      }
      return name.intern();
   }
   
   /**
    * This is used to acquire the name of the specified type using
    * the <code>Root</code> annotation for the class. This will 
    * use either the name explicitly provided by the annotation or
    * it will use the name of the class that the annotation was
    * placed on if there is no explicit name for the root.
    * 
    * @param type this is the type to acquire the root name for
    * 
    * @return this returns the name of the type from the root
    * 
    * @throws Exception if the class contains an illegal schema
    */
   private String getName(Class type) throws Exception {
      String name = getRoot(type);
      
      if(name != null) {
         return name;
      } else {
         name = type.getSimpleName();
      }
      return Introspector.decapitalize(name);
   }
   
   /**
    * This will acquire the name of the <code>Root</code> annotation
    * for the specified class. This will traverse the inheritance
    * heirarchy looking for the root annotation, when it is found it
    * is used to acquire a name for the XML element it represents.
    *  
    * @param type this is the type to acquire the root name with
    * 
    * @return the root name for the specified type if it exists
    */
   private String getRoot(Class type) { 
      Class real = type;
         
      while(type != null) {
         String name = getRoot(real, type);
         
         if(name != null) {
           return name;
         }
         type = type.getSuperclass();
      }
      return null;     
   }
   
   /**
    * This will acquire the name of the <code>Root</code> annotation
    * for the specified class. This will traverse the inheritance
    * heirarchy looking for the root annotation, when it is found it
    * is used to acquire a name for the XML element it represents.
    *  
    * @param real the actual type of the object being searched
    * @param type this is the type to acquire the root name with    
    * 
    * @return the root name for the specified type if it exists
    */
   private String getRoot(Class<?> real, Class<?> type) {
      String name = type.getSimpleName();
      
      if(type.isAnnotationPresent(Root.class)) {
         Root root = type.getAnnotation(Root.class);
         String text = root.name();
          
         if(!isEmpty(text)) {
            return text;
         }
         return Introspector.decapitalize(name);
      }
      return null;
   }
   
   /**
    * This is used to determine the name of the XML element that the
    * annotated field or method represents. This will determine based
    * on the annotation attributes and the dependent type required
    * what the name of the XML element this represents is. 
    * 
    * @return this returns the name of the XML element expected
    */
   public String getName() throws Exception {
      String entry = label.getEntry(); 
         
      if(!label.isInline()) {
         entry = getDefault();
      }
      return entry.intern();
   }
   
   /**
    * This is used to acquire the name for an element by firstly
    * checking for an override in the annotation. If one exists
    * then this is returned if not then the name of the field
    * or method contact is returned. 
    * 
    * @return this returns the XML element name to be used
    */
   private String getDefault() throws Exception {
      String name = label.getOverride();

      if(!isEmpty(name)) {
         return name;
      }
      return contact.getName();
   }  
   
   /**
    * This method is used to determine if a root annotation value is
    * an empty value. Rather than determining if a string is empty
    * be comparing it to an empty string this method allows for the
    * value an empty string represents to be changed in future.
    * 
    * @param value this is the value to determine if it is empty
    * 
    * @return true if the string value specified is an empty value
    */
   public boolean isEmpty(String value) {
      if(value != null) {
         return value.length() == 0;
      }
      return true;      
   }
   
   /**
    * This method is used to construct a string that describes the
    * signature of an XML annotated field or method. This will use
    * the <code>Contact</code> object and the annotation used for
    * that contact to construct a string that has sufficient
    * information such that it can be used in error reporting.
    * 
    * @return returns a string used to represent this signature 
    */
   public String toString() {
      return String.format("%s on %s", marker, contact);
   }
}
