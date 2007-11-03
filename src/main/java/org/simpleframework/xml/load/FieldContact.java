/*
 * FieldContact.java April 2007
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

package org.simpleframework.xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * The <code>FieldContact</code> object is used to act as a contact
 * for a field within an object. This allows a value to be set on an
 * object field during deserialization and acquired from the same
 * field during serialization.
 *
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.load.FieldScanner
 */ 
class FieldContact implements Contact {
   
   /**
    * This is the label that marks the field within the object.
    */           
   private Annotation label;
   
   /**
    * This represents the field within the schema class object.
    */ 
   private Field field;
   
   /**
    * This is the name for this contact as taken from the field.
    */
   private String name;
   
   /**
    * Constructor for the <code>FieldContact</code> object. This is 
    * used as a point of contact for a field within a schema class.
    * Values can be read and written directly to the field with this.
    *
    * @param field this is the field that is the point of contact
    * @param label this is the annotation that is used by the field
    */ 
   public FieldContact(Field field, Annotation label) {
      this.label = label;
      this.field = field;
   }

   /**
    * This will provide the contact type. The contact type is the
    * class that is to be set and get on the object. This represents
    * the return type for the get and the parameter for the set.
    *
    * @return this returns the type that this contact represents
    */
   public Class getType() {
      return field.getType();
   }
   
   /**
    * This provides the dependant class for the contact. This will
    * actually represent a generic type for the actual type. For
    * contacts that use a <code>Collection</code> type this will
    * be the generic type parameter for that collection.
    * 
    * @return this returns the dependant type for the contact
    */
   public Class getDependant() {
      return Reflector.getDependant(field);
   }
   
   /**
    * This provides the dependant classes for the contact. This will
    * typically represent a generic types for the actual type. For
    * contacts that use a <code>Map</code> type this will be the 
    * generic type parameter for that map type declaration.
    * 
    * @return this returns the dependant type for the contact
    */
   public Class[] getDependants() {
      return Reflector.getDependants(field);
   }
   
   /**
    * This is used to acquire the name of the field. This will return
    * the name of the field wich can then be used to determine the 
    * XML attribute or element the contact represents. This ensures
    * that the name provided string is internalized for performance.  
    * 
    *  @return this returns the name of the field represented
    */
   public String getName() {
      if(name == null) {
         name = getName(field);
      }
      return name;
   }
   
   /**
    * This is used to acquire the name of the field such that it is
    * an internalized string. Internalization of the contact name
    * ensures that comparisons can be made to annotation names with
    * a simple reference comparison rather than a string comparison.
    * 
    * @param field the field to acquire the internalized name from
    * 
    * @return this returns the name of the string, internalized
    */
   private String getName(Field field) {
      String name = field.getName();
      
      if(name != null) {
         name = name.intern();
      }
      return name;
   }

   /**
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serializaed and deserialized from the object.
    *
    * @return this provides the annotation associated with this
    */
   public Annotation getAnnotation() {
      return label;
   }

   /**
    * This is used to set the specified value on the provided object.
    * The value provided must be an instance of the contact class so
    * that it can be set without a runtime class compatibility error.
    *
    * @param source this is the object to set the value on
    * @param value this is the value that is to be set on the object
    */ 
   public void set(Object source, Object value) throws Exception {
      field.set(source, value);
   }

   /**
    * This is used to get the specified value on the provided object.
    * The value returned from this method will be an instance of the
    * contact class type. If the returned object is of a different
    * type then the serialization process will fail.
    *
    * @param source this is the object to acquire the value from
    *
    * @return this is the value that is acquired from the object
    */
   public Object get(Object source) throws Exception {
      return field.get(source);
   }
   
   /**
    * This is used to describe the contact as it exists within the
    * owning class. It is used to provide error messages that can
    * be used to debug issues that occur when processing a contact.
    * The string provided is the generic field string.
    * 
    * @return this returns a string representation of the contact
    */
   public String toString() {
      return String.format("field '%s'", getName());
   }
}
