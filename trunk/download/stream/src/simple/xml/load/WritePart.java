/*
 * WritePart.java April 2007
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
 * The <code>WritePart</code> object represents the setter method for
 * a Java Bean property. This composes the set part of the method
 * contact for an object. The write part contains the method that is
 * used to set the value on an object and the annotation that tells
 * the deserialization process how to deserialize the value.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.MethodContact
 */
final class WritePart implements MethodPart {
   
   /**
    * This is the annotation for the set method provided.
    */
   private Annotation label;
   
   /**
    * This method is used to set the value during deserialization. 
    */
   private Method method;
   
   /**
    * Constructor for the <code>WritePart</code> object. This is
    * used to create a method part that will provide a means for 
    * the deserialization process to set a value to a object.
    * 
    * @param method the method that is used to set the value
    * @param label this describes how to deserialize the value
    */
   public WritePart(Method method, Annotation label) {
      this.method = method;
      this.label = label;
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
    * This is used to acquire the method that can be used to invoke
    * the Java Bean method on the object. If the method represented
    * by this is inaccessible then this will set it as accessible.
    * 
    * @retun returns the method used to interace with the object
    */
   public Method getMethod() {
      if(!method.isAccessible()) {
         method.setAccessible(true);              
      }           
      return method;
   }
}
