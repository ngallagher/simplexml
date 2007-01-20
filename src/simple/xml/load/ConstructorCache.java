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

package simple.xml.load;

import java.util.concurrent.ConcurrentHashMap;
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
 * @see simple.xml.load.PrimitiveFactory
 */
final class ConstructorCache extends ConcurrentHashMap<Class, Constructor> {
   
   /**
    * Constructor for the <code>ConstructorCache</code> object. It
    * is used to create a typed hash table that can be used to map
    * the constructors used to convert strings to primitive types.
    */
   public ConstructorCache() {
      super();           
   }
   
   /**
    * This method is used to cache the <code>Constructor</code> with
    * the type that constructor represents. The cached constructor
    * can then be looked up via its type when a primitive needs to 
    * be converted from a string to a suitable type for a field. 
    * 
    * @param type the type to cache the constructor under
    * @param method this is the constructor that is to be cached
    * 
    * @return the previous constructor stored in the cache
    */
   public Constructor cache(Class type, Constructor method) {
      return put(type, method);
   }
}
