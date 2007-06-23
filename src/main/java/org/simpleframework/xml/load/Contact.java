/*
 * Contact.java April 2007
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

/**
 * The <code>Contact</code> interface is used to provide a point of
 * contact with an object. Typically this will be used to get and
 * set to an from a field or a pair of matching bean methods. Each
 * contact must be labeled with an annotation.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.load.Label
 */ 
interface Contact {
   
   /**
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serializaed and deserialized from the object.
    *
    * @return this provides the annotation associated with this
    */
   public Annotation getAnnotation();
   
   /**
    * This will provide the contact type. The contact type is the
    * class that is to be set and get on the object. Typically the
    * type will be a serializable object or a primitive type.
    *
    * @return this returns the type that this contact represents
    */ 
   public Class getType();
   
   /**
    * This provides the dependant class for the contact. This will
    * typically represent a generic type for the actual type. For
    * contacts that use a <code>Collection</code> type this will
    * be the generic type parameter for that collection.
    * 
    * @return this returns the dependant type for the contact
    */
   public Class getDependant();
   
   /**
    * This represents the name of the object contact. If the contact
    * is a field then the name of the field is provided. If however
    * the contact is a method then the Java Bean name of the method
    * is provided, which will be the decapatilized name of the 
    * method without the get, set, or is prefix to the method.
    * 
    * @return this returns the name of the contact represented
    */
   public String getName();
   
   /**
    * This is used to set the value on the specified object through
    * this contact. Depending on the type of contact this will set
    * the value given, typically this will be done by invoking a
    * method or setting the value on the object field.
    *
    * @param source this is the object to set the value on
    * @param value this is the value to be set through the contact
    */ 
   public void set(Object source, Object value) throws Exception;
   
   /**
    * This is used to get the value from the specified object using
    * the point of contact. Typically the value is retrieved from
    * the specified object by invoking a get method of by acquiring
    * the value from a field within the specified object.
    *
    * @param source this is the object to acquire the value from
    *
    * @return this is the value acquired from the point of contact
    */ 
   public Object get(Object source) throws Exception;
   
   /**
    * This is used to describe the contact as it exists within the
    * owning class. This is used to provide error messages that can
    * be used to debug issues that occur when processing a contact.  
    * 
    * @return this returns a string representation of the contact
    */
   public String toString();
}
