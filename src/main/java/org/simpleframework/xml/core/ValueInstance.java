/*
 * ValueInstance.java January 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.strategy.Value;

/**
 * The <code>ValueInstance</code> object is used to create an object
 * by using a <code>Value</code> instance to determine the type. If
 * the provided value instance represents a reference then this will
 * simply provide the value of the reference, otherwise it will
 * instantiate a new object and return that.
 * 
 * @author Niall Gallagher
 */
class ValueInstance implements Instance {
   
   /**
    * This is the instantiator used to create the objects.
    */
   private final Instantiator creator;
   
   /**
    * This is the internal value that contains the criteria.
    */
   private final Value value;
   
   /**
    * This is the type that is to be instantiated by this.
    */
   private final Class type;
   
   /**
    * Constructor for the <code>ValueInstance</code> object. This 
    * is used to represent an instance that delegates to the given
    * value object in order to acquire the object. 
    * 
    * @param creator this is the instantiator used to create objects
    * @param value this is the value object that contains the data
    */
   public ValueInstance(Instantiator creator, Value value) {
      this.type = value.getType();
      this.creator = creator;
      this.value = value;
   }
   
   /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this.
    * 
    * @return an instance of the type this object represents
    */
   public Object getInstance() throws Exception {
      if(value.isReference()) {
         return value.getValue();
      }
      Object object = creator.getObject(type);
      
      if(value != null) {
         value.setValue(object);
      }
      return object;
   }
   
   /**
    * This method is used acquire the value from the type and if
    * possible replace the value for the type. If the value can
    * not be replaced then an exception should be thrown. This 
    * is used to allow primitives to be inserted into a graph.
    * 
    * @param object this is the object to insert as the value
    * 
    * @return an instance of the type this object represents
    */
   public Object setInstance(Object object) {
      if(value != null) {
         value.setValue(object);
      }
      return object;
   }

   /**
    * This is used to determine if the type is a reference type.
    * A reference type is a type that does not require any XML
    * deserialization based on its annotations. Values that are
    * references could be substitutes objects or existing ones. 
    * 
    * @return this returns true if the object is a reference
    */
   public boolean isReference() {
      return value.isReference();
   }

   /**
    * This is the type of the object instance that will be created
    * by the <code>getInstance</code> method. This allows the 
    * deserialization process to perform checks against the field.
    * 
    * @return the type of the object that will be instantiated
    */
   public Class getType() {
      return type;
   }
}
