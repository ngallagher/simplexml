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

package simple.xml.load;

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
 * @see simple.xml.load.MethodScanner
 */ 
class MethodContact implements Contact {
   
   /**
    * This is the label that marks both the set and get methods.
    */         
   private Annotation label;
   
   /**
    * This is the read method which is used to get the value.
    */
   private Method read;
   
   /**
    * This is the write method which is used to set the value.
    */ 
   private Method write;

   /**
    * This is the type associated with this point of contact.
    */ 
   private Class type;
   
   /**
    * This is the dependant type as taken from the read method.
    */
   private Class item;
   
   /**
    * This represents the name of the method for this contact.
    */
   private String name;
   
   /**
    * Constructor for the <code>MethodContact</code> object. This is
    * used to compose a point of contact that makes use of a get and
    * set method on a class. The specified methods will be invoked
    * during the serialization process to read and write values.
    *
    * @param read this forms the get method for the object
    * @param write this forms the get method for the object
    */ 
   public MethodContact(MethodPart read, MethodPart write) {
      this.label = read.getAnnotation();   
      this.item = read.getDependant();
      this.write = write.getMethod();
      this.read = read.getMethod();
      this.type = read.getType();   
      this.name = read.getName();

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
   public void set(Object source, Object value) throws Exception{
      write.invoke(source, value);
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
      return read.invoke(source);
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
