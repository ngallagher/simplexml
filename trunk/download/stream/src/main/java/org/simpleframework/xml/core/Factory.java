/*
 * Factory.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import java.lang.reflect.Modifier;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * The <code>Factory</code> object provides a base class for factories 
 * used to produce field values from XML elements. The goal of this 
 * type of factory is to make use of the <code>Strategy</code> object
 * to determine the type of the field value. The strategy class must be 
 * assignable to the field class type, that is, it must extend it or
 * implement it if it represents an interface. If the strategy returns
 * a null <code>Value</code> then the subclass implementation determines 
 * the type used to populate the object field value.
 * 
 * @author Niall Gallagher
 */
abstract class Factory {
   
   /**
    * This is the context object used for the serialization process.
    */
   protected Context context;
   
   /**
    * This is used to translate all of the primitive type strings.
    */
   protected Support support;
   
   /**
    * This is the field type that the class must be assignable to.
    */
   protected Type type;  

   /**
    * Constructor for the <code>Factory</code> object. This is given 
    * the class type for the field that this factory will determine
    * the actual type for. The actual type must be assignable to the
    * field type to insure that any instance can be set. 
    * 
    * @param context the contextual object used by the persister
    * @param type this is the property representing the field 
    */
   protected Factory(Context context, Type type) {
      this.support = context.getSupport();
      this.context = context; 
      this.type = type;
   }
   
   /**
    * This is used to extract the type this factory is using. Each
    * factory represents a specific class, which it instantiates if
    * required. This method provides the represented class.
    * 
    * @return this returns the class represented by the factory
    */
   public Class getType() {
      return type.getType();
   }
   
   /**
    * This is used to create a default instance of the field type. It
    * is up to the subclass to determine how to best instantiate an
    * object of the field type that best suits. This is used when the
    * empty value is required or to create the default type instance.
    * 
    * @return a type which is used to instantiate the collection     
    */
   public Object getInstance() throws Exception {
      Class type = getType();
      
      if(!isInstantiable(type)) {
         throw new InstantiationException("Type %s can not be instantiated", type);
      }
      return type.newInstance();
   }

   /**
    * This is used to get a possible override from the provided node.
    * If the node provided is an element then this checks for a  
    * specific class override using the <code>Strategy</code> object.
    * If the strategy cannot resolve a class then this will return 
    * null. If the resolved <code>Value</code> is not assignable to 
    * the field then this will thrown an exception.
    * 
    * @param node this is the node used to search for the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception if the override type is not compatible
    */
   protected Value getOverride(InputNode node) throws Exception {
      Value value = getConversion(node);      

      if(value != null) { 
         Class type = value.getType();
         Class expect = getType();
     
         if(!isCompatible(expect, type)) {
            throw new InstantiationException("Type %s is not compatible with %s", type, expect);              
         }
      }         
      return value; 
   }
   
   /**
    * This method is used to set the override class within an element.
    * This delegates to the <code>Strategy</code> implementation, which
    * depending on the implementation may add an attribute of a child
    * element to describe the type of the object provided to this.
    * 
    * @param type this is the class of the field type being serialized
    * @param node the XML element that is to be given the details
    *
    * @throws Exception thrown if an error occurs within the strategy
    */
   public boolean setOverride(Type type, Object value, OutputNode node) throws Exception {
      Class expect = type.getType();
      
      if(!expect.isPrimitive()) {
         return context.setOverride(type, value, node);
      }
      return false;
   }

   /**
    * This performs the conversion from the element node to a type. This
    * is where the <code>Strategy</code> object is consulted and asked
    * for a class that will represent the provided XML element. This will,
    * depending on the strategy implementation, make use of attributes
    * and/or elements to determine the type for the field.
    * 
    * @param node this is the element used to extract the override
    * 
    * @return this returns null if no override type can be found
    * 
    * @throws Exception thrown if the override class cannot be loaded    
    */ 
   public Value getConversion(InputNode node) throws Exception {
      return context.getOverride(type, node);
   }
   
   /**
    * This is used to determine whether the provided base class can be
    * assigned from the issued type. For an override to be compatible
    * with the field type an instance of the override type must be 
    * assignable to the field value. 
    * 
    * @param expect this is the field value present the the object    
    * @param type this is the specialized type that will be assigned
    * 
    * @return true if the field type can be assigned the type value
    */
   public static boolean isCompatible(Class expect, Class type) {
      if(expect.isArray()) {
         expect = expect.getComponentType();
      }
      return expect.isAssignableFrom(type);           
   }

   /**
    * This is used to determine whether the type given is instantiable,
    * that is, this determines if an instance of that type can be
    * created. If the type is an interface or an abstract class then 
    * this will return false.
    * 
    * @param type this is the type to check the modifiers of
    * 
    * @return false if the type is an interface or an abstract class
    */
   public static boolean isInstantiable(Class type) {
      int modifiers = type.getModifiers();

      if(Modifier.isAbstract(modifiers)) {
         return false;              
      }              
      return !Modifier.isInterface(modifiers);
   } 
}           
