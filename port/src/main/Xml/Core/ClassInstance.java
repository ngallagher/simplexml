/*
 * ClassInstance.java January 2007
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

/**
 * The <code>ClassInstance</code> object is used to create an object
 * by using a <code>Class</code> to determine the type. If the given
 * class can not be instantiated then this throws an exception when
 * the instance is requested. For performance an instantiator is
 * given as it contains a reflection cache for constructors.
 * 
 * @author Niall Gallagher
 */
class ClassInstance implements Instance {
   
   /**
    * This is the instantiator used to create the objects.
    */
   private Instantiator creator;
   
   /**
    * This represents the value of the instance if it is set.
    */
   private Object value;
   
   /**
    * This is the type of the instance that is to be created.
    */
   private Class type;
   
   /**
    * Constructor for the <code>ClassInstance</code> object. This is
    * used to create an instance of the specified type. If the given
    * type can not be instantiated then an exception is thrown.
    * 
    * @param creator this is the creator used for the instantiation
    * @param type this is the type that is to be instantiated
    */
   public ClassInstance(Instantiator creator, Class type) {
      this.creator = creator;
      this.type = type;
   }

   /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this.
    * 
    * @return an instance of the type this object represents
    */
   public Object getInstance() throws Exception {
      if(value == null) {
         value = creator.getObject(type);
      }
      return value;
   }
   
   /**
    * This method is used acquire the value from the type and if
    * possible replace the value for the type. If the value can
    * not be replaced then an exception should be thrown. This 
    * is used to allow primitives to be inserted into a graph.
    * 
    * @param value this is the value to insert as the type
    * 
    * @return an instance of the type this object represents
    */
   public Object setInstance(Object value) throws Exception {
      return this.value = value;
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

   /**
    * This is used to determine if the type is a reference type.
    * A reference type is a type that does not require any XML
    * deserialization based on its annotations. Values that are
    * references could be substitutes objects or existing ones. 
    * 
    * @return this returns true if the object is a reference
    */
   public boolean isReference() {
      return false;
   }
}
