/*
 * MethodPart.java April 2007
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
 * The <code>MethodPart</code> interface is used to provide a point 
 * of contact with an object. Typically this will be used to get a
 * method from an object which is contains an XML annotation. This
 * provides the type the method is associated with, this type is
 * either the method return type or the single value parameter.
 * 
 * @author Niall Gallagher
 */ 
interface MethodPart {

   /**
    * This provides the name of the method part as acquired from the
    * method name. The name represents the Java Bean property name
    * of the method and is used to pair getter and setter methods.
    * 
    * @return this returns the Java Bean name of the method part
    */
   public String getName(); 
   
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
    * class that is either the method return type or the single
    * value parameter type associated with the method.
    *
    * @return this returns the type that this contact represents
    */ 
   public Class getType();
   
   /**
    * This is used to acquire the dependant class for the method 
    * part. The dependant type is the type that represents the 
    * generic type of the type. This is used when collections are
    * annotated as it allows a default entry class to be taken
    * from the generic information provided.
    * 
    * @return this returns the generic dependant for the type
    */
   public Class getDependant();
   
   /**
    * This is used to acquire the dependant classes for the method 
    * part. The dependant types are the types that represent the 
    * generic types of the type. This is used when collections are 
    * annotated as it allows a default entry class to be taken
    * from the generic information provided.
    * 
    * @return this returns the generic dependant for the type
    */
   public Class[] getDependants();
   
   /**
    * This is the method for this point of contact. This is what
    * will be invoked by the serialization or deserialization 
    * process when an XML element or attribute is to be used.
    * 
    * @return this returns the method associated with this
    */
   public Method getMethod();
   
   /**
    * This is the method type for the method part. This is used in
    * the scanning process to determine which type of method a
    * instance represents, this allows set and get methods to be
    * paired.
    * 
    * @return the method type that this part represents
    */
   public MethodType getMethodType();
   
   /**
    * This is used to describe the method as it exists within the
    * owning class. This is used to provide error messages that can
    * be used to debug issues that occur when processing a method.
    * This should return the method as a generic representation.  
    * 
    * @return this returns a string representation of the method
    */
   public String toString();
}