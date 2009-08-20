/*
 * GetPart.java April 2007
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

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The <code>GetPart</code> object represents the getter method for
 * a Java Bean property. This composes the get part of the method
 * contact for an object. The get part contains the method that is
 * used to get the value in an object and the annotation that tells
 * the serialization process how to serialize the value.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.MethodContact
 */
class GetPart implements MethodPart {
   
   /**
    * This is the annotation for the get method provided.
    */
   private final Annotation label;
   
   /**
    * This represents the method type for the get part method.
    */
   private final MethodType type;
   
   /**
    * This method is used to get the value during serialization. 
    */
   private final Method method;
   
   /**
    * This is the Java Bean name representation of the method.
    */
   private final String name;
   
   /**
    * Constructor for the <code>GetPart</code> object. This is
    * used to create a method part that will provide a means for 
    * the serialization process to set a value to a object.
    * 
    * @param method the method that is used to get the value
    * @param label this describes how to serialize the value
    */   
   public GetPart(MethodName method, Annotation label) {      
      this.method = method.getMethod();      
      this.name = method.getName();
      this.type = method.getType();
      this.label = label;
   }
   
   /**
    * This provides the name of the method part as acquired from the
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
      return method.getReturnType();
   }
   
   /**
    * This is used to acquire the dependent class for the method 
    * part. The dependent type is the type that represents the 
    * generic type of the type. This is used when collections are
    * annotated as it allows a default entry class to be taken
    * from the generic information provided.
    * 
    * @return this returns the generic dependent for the type
    */
   public Class getDependent() {
      return Reflector.getReturnDependent(method);
   }
   
   /**
    * This is used to acquire the dependent classes for the method 
    * part. The dependent types are the types that represent the 
    * generic types of the type. This is used when collections are 
    * annotated as it allows a default entry class to be taken
    * from the generic information provided.
    * 
    * @return this returns the generic dependent for the type
    */
   public Class[] getDependents() {
      return Reflector.getReturnDependents(method);
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
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serialized and deserialized from the object.
    * 
    * @param type this is the type of the annotation to acquire
    *
    * @return this provides the annotation associated with this
    */
   public <T extends Annotation> T getAnnotation(Class<T> type) {
      return method.getAnnotation(type);
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