/*
 * MethodContact.java April 2007
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
import java.lang.reflect.Method;

/**
 * The <code>MethodContact</code> object is acts as a contact that
 * can set and get data to and from an object using methods. This 
 * requires a get method and a set method that share the same class
 * type for the return and parameter respectively.
 *
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.load.MethodScanner
 */ 
class MethodContact implements Contact {
   
   /**
    * This is the label that marks both the set and get methods.
    */         
   private Annotation label;

   /**
    * This is the dependant types as taken from the get method.
    */
   private Class[] items;
   
   /**
    * This is the dependent type as taken from the get method.
    */
   private Class item;
   
   /**
    * This is the type associated with this point of contact.
    */ 
   private Class type; 
   
   /**
    * This is the get method which is used to get the value.
    */
   private Method get;
   
   /**
    * This is the set method which is used to set the value.
    */ 
   private Method set;
   
   /**
    * This represents the name of the method for this contact.
    */
   private String name;
   
   /**
    * Constructor for the <code>MethodContact</code> object. This is
    * used to compose a point of contact that makes use of a get and
    * set method on a class. The specified methods will be invoked
    * during the serialization process to get and set values.
    *
    * @param get this forms the get method for the object
    * @param set this forms the get method for the object
    */ 
   public MethodContact(MethodPart get, MethodPart set) {
      this.label = get.getAnnotation();   
      this.items = get.getDependants();
      this.item = get.getDependant();
      this.set = set.getMethod();
      this.get = get.getMethod();
      this.type = get.getType();   
      this.name = get.getName();
   }
   
   /**
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serialized and deserialized from the object.
    *
    * @return this provides the annotation associated with this
    */
   public Annotation getAnnotation() {
      return label;
   }
   
   /**
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serializaed and deserialized from the object.
    * 
    * @param type this is the type of the annotation to acquire
    *
    * @return this provides the annotation associated with this
    */
   public <T extends Annotation> T getAnnotation(Class<T> type) {
      T label = get.getAnnotation(type);
      
      if(label == null) {
         label = set.getAnnotation(type);
      }
      return label;
   }

   /**
    * This will provide the contact type. The contact type is the
    * class that is to be set and get on the object. This represents
    * the return type for the get and the parameter for the set.
    *
    * @return this returns the type that this contact represents
    */
   public Class getType() {
      return type;
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
      return item;
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
      return items;
   } 
   
   /**
    * This is used to acquire the name of the method. This returns
    * the name of the method without the get, set or is prefix that
    * represents the Java Bean method type. Also this decaptitalizes
    * the resulting name. The result is used to represent the XML
    * attribute of element within the class schema represented.
    * 
    *  @return this returns the name of the method represented
    */
   public String getName() {
      return name;
   }   

   /**
    * This is used to set the specified value on the provided object.
    * The value provided must be an instance of the contact class so
    * that it can be set without a runtime class compatibility error.
    *
    * @param source this is the object to set the value on
    * @param value this is the value that is to be set on the object
    */    
   public void set(Object source, Object value) throws Exception{
      set.invoke(source, value);
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
      return get.invoke(source);
   }
   
   /**
    * This is used to describe the contact as it exists within the
    * owning class. It is used to provide error messages that can
    * be used to debug issues that occur when processing a contact.
    * The string provided contains both the set and get methods.
    * 
    * @return this returns a string representation of the contact
    */
   public String toString() {
      return String.format("method '%s'", name);
   }
}
