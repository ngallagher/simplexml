/*
 * ConstructorCache.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.util.WeakCache;
import java.lang.reflect.Constructor;

/**
 * The <code>ConstructorCache</code> object is a typed hash map used
 * to store the constructors used in converting a primitive type to
 * a primitive value using a string. This cache is used to reduce the
 * time taken to convert the primitives by reducing the amount of
 * reflection required and eliminate type resolution.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.PrimitiveFactory
 */
class ConstructorCache extends WeakCache<Class, Constructor> {
   
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
