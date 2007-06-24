/*
 * TransformCache.java May 2007
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

package org.simpleframework.xml.transform;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>TransformCache</code> is used to cache transform objects. 
 * It is used so the overhead of instantiating a transform each time
 * an object of the specified type requires transformation is removed.
 * Essentially this acts as a typedef for the generic has map.
 * 
 * @author Niall Gallagher
 */
class TransformCache extends ConcurrentHashMap<Class, Transform>{
   
   /**
    * Constructor for the <code>TransformCache</code> object. This is
    * a concurrent hash table that maps class types to the transform
    * objects they represent. To ensure that the cache can be used by
    * multiple threads this extends the concurrent hash map.
    */
   public TransformCache() {
      super();
   }
   
   /**
    * This method will cache the provided transform object with the
    * provided class object. Once cached the transform object can be
    * reused to transform string values to object instances that are
    * assignable to the specified type.
    *
    * @param type this is the class the transform is mapped to
    * @param transform this is the transform object that is cached
    *
    * @return this is the scanner instance that has been cached
    */ 
   public Transform cache(Class type, Transform transform) {
      return put(type, transform);
   }
}
