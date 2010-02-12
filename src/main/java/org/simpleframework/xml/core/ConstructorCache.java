/*
 * ConstructorCache.java July 2006
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

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>ConstructorCache</code> object is a typed hash map used
 * to store the constructors used in converting a primitive type to
 * a primitive value using a string. This cache is used to reduce the
 * time taken to convert the primitives by reducing the amount of
 * reflection required and eliminate type resolution.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.PrimitiveFactory
 */
class ConstructorCache extends ConcurrentHashMap<Class, Constructor> {
   
   /**
    * Constructor for the <code>ConstructorCache</code> object. It
    * is used to create a typed hash table that can be used to map
    * the constructors used to convert strings to primitive types.
    * If the class is unloaded then the cached constructor is lost.
    */
   public ConstructorCache() {
      super();           
   }
}
