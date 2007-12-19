/*
 * SetPart.java April 2007
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
 * The <code>SetPart</code> object represents the setter method for
 * a Java Bean property. This composes the set part of the method
 * contact for an object. The set part contains the method that is
 * used to set the value on an object and the annotation that tells
 * the deserialization process how to deserialize the value.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.MethodContact
 */
class SetPart implements MethodPart {
   
   /**
    * This is the annotation for the set method provided.
    */
   private final Annotation label;
   
   /**
    * This represents the method type for the set part method.
    */
   private final MethodType type;
   
   /**
    * This method is used to set the value during deserialization. 
    */
   private final Method method;
   
   /**
    * This represents the name of this set part instance.
    */
   private final String name;
   
   /**
    * Constructor for the <code>SetPart</code> object. This is
    * used to create a method part that will provide a means for 
    * the deserialization process to set a value to a object.
    * 
    * @param method the method that is used to set the value
    * @param label this describes how to deserialize the value
    */
   public SetPart(MethodName method, Annotation label) {
      this.method = method.getMethod();
      this.name = method.getName();
      this.type = method.getType();
      this.label = label;
   }
   
   /**
    * This provdes the name of the method part as acquired from the
    * method name. The name represents the Java Bean property name
    * of the method and is used to pair getter and setter methods.
    * 
    * @return this returns the Java Bean name of the method part
    */
   public String getName() {
      return name;
   }
   
   /**
    * This is used to acquire the type for this method part. This
    * is used by the serializer to determine the schema class that
    * is used to match the XML elements to the object details.
    * 
    * @return this returns the schema class for this method
    */
   public Class getType() {
      return method.getParameterTypes()[0];
   }
   
   /**
    * This is used to acquire the dependant class for the method 
    * part. The dependant type is the type that represents the 
    * generic type of the type. This is used when collections are
    * annotated as it allows a default entry class to be taken
    * from the generic information provided.
    * 
    * @return this returns the generic dependant for the type
    */  
   public Class getDependant() {
      return Reflector.getParameterDependant(method, 0);
   }
   
   /**
    * This is used to acquire the dependant classes for the method 
    * part. The dependant types are the types that represents the 
    * generic types of the type. This is used when collections are
    * annotated as it allows a default entry classes to be taken
    * from the generic information provided.
    * 
    * @return this returns the generic dependants for the type
    */  
   public Class[] getDependants() {
      return Reflector.getParameterDependants(method, 0);
   }
   
   /**
    * This is used to acquire the annotation that was used to label
    * the method this represents. This acts as a means to match the
    * set method with the get method using an annotation comparison.
    * 
    * @return this returns the annotation used to mark the method
    */
   public Annotation getAnnotation() {
      return label;
   }
  
   /**
    * This is the method type for the method part. This is used in
    * the scanning process to determine which type of method a
    * instance represents, this allows set and get methods to be
    * paired.
    * 
    * @return the method type that this part represents
    */
   public MethodType getMethodType() {
      return type;
   }
   
   /**
    * This is used to acquire the method that can be used to invoke
    * the Java Bean method on the object. If the method represented
    * by this is inaccessible then this will set it as accessible.
    * 
    * @return returns the method used to interace with the object
    */
   public Method getMethod() {
      if(!method.isAccessible()) {
         method.setAccessible(true);              
      }           
      return method;
   }
   
   /**
    * This is used to describe the method as it exists within the
    * owning class. This is used to provide error messages that can
    * be used to debug issues that occur when processing a method.
    * This returns the method as a generic string representation.  
    * 
    * @return this returns a string representation of the method
    */
   public String toString() {
      return method.toGenericString();
   }
}
