/*
 * ParameterContact.java April 2007
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
import java.lang.reflect.Constructor;

/**
 * The <code>ParameterContact</code> object is used to represent
 * a contact that is provided so that a <code>Label</code> can be
 * used to determine a consistent name for the parameter. Unlike
 * field and method contacts this is essentially an adapter that
 * is used so that the parameter name can be determined in a 
 * similar way to a method or field.
 * 
 * @author Niall Gallagher
 */
abstract class ParameterContact<T extends Annotation> implements Contact {

   /**
    * This is the constructor the parameter was declared within. 
    */
   protected final Constructor factory;
   
   /**
    * This is the index of the parameter within the constructor.
    */
   protected final int index;
   
   /**
    * This is the annotation used to label the parameter.
    */
   protected final T label;

   /**
    * Constructor for the <code>ParameterContact</code> object. This
    * is used to create a contact that can be used to determine a
    * consistent name for the parameter. It requires the annotation,
    * the constructor, and the parameter declaration index.
    * 
    * @param label this is the annotation used for the parameter
    * @param factory this is the constructor that is represented
    * @param index this is the index for the parameter
    */
   public ParameterContact(T label, Constructor factory, int index) {
      this.factory = factory;
      this.index = index;
      this.label = label;
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
    * This will provide the contact type. The contact type is the
    * class that is to be set and get on the object. Typically the
    * type will be a serializable object or a primitive type.
    *
    * @return this returns the type that this contact represents
    */ 
   public Class getType() {
      return factory.getParameterTypes()[index];
   }

   /**
    * This provides the dependent class for the contact. This will
    * typically represent a generic type for the actual type. For
    * contacts that use a <code>Collection</code> type this will
    * be the generic type parameter for that collection.
    * 
    * @return this returns the dependent type for the contact
    */
   public Class getDependent() {
      return Reflector.getParameterDependent(factory, index);
   }

   /**
    * This provides the dependent classes for the contact. This will
    * typically represent a generic types for the actual type. For
    * contacts that use a <code>Map</code> type this will be the 
    * generic type parameter for that map type declaration.
    * 
    * @return this returns the dependent types for the contact
    */
   public Class[] getDependents() {
      return Reflector.getParameterDependents(factory, index);
   }
   
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
   public Object get(Object source) {
      return null;
   }  

   /**
    * This is used to set the value on the specified object through
    * this contact. Depending on the type of contact this will set
    * the value given, typically this will be done by invoking a
    * method or setting the value on the object field.
    *
    * @param source this is the object to set the value on
    * @param value this is the value to be set through the contact
    */ 
   public void set(Object source, Object value) {  
      return;
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
      return null;
   }
   
   /**
    * This is used to identify annotated methods are fields that
    * can not be modified. Such field will require that there is 
    * a constructor that can have the value injected in to it.
    * 
    * @return this returns true if the field or method is final
    */
   public boolean isFinal() {
      return true;
   }

   /**
    * This represents the name of the parameter. Because the name
    * of the parameter does not exist at runtime the name must
    * be taken directly from the annotation and the parameter type.
    * Each XML annotation must provide their own unique way of
    * providing a name for the parameter contact.
    * 
    * @return this returns the name of the contact represented
    */
   public abstract String getName();
}
