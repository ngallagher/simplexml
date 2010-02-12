/*
 * TransformCache.java May 2007
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

package org.simpleframework.xml.transform;

import org.simpleframework.xml.util.WeakCache;

/**
 * The <code>TransformCache</code> is used to cache transform objects. 
 * It is used so the overhead of instantiating a transform each time
 * an object of the specified type requires transformation is removed.
 * Essentially this acts as a typedef for the generic hash map.
 * 
 * @author Niall Gallagher
 */
class TransformCache extends WeakCache<Class, Transform>{
   
   /**
    * Constructor for the <code>TransformCache</code> object. This is
    * a concurrent hash table that maps class types to the transform
    * objects they represent. To enable reloading of classes by the
    * system this will drop the transform if the class in unloaded.
    */
   public TransformCache() {
      super();
   }
}
