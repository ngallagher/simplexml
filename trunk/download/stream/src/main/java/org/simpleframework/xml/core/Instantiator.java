/*
 * Instantiator.java July 2006
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

package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;

/**
 * The <code>Instantiator</code> is used to instantiate types that 
 * will leverage a constructor cache to quickly create the objects.
 * This is used by the various object factories to return type 
 * instances that can be used by converters to create the objects 
 * that will later be deserialized.
 *
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.core.Instance
 */
class Instantiator {

   /**
    * This is used to cache the constructors for the given types.
    */
   private final ConstructorCache cache;
   
   /**
    * Constructor for the <code>Instantiator</code> object. This will
    * create a constructor cache that can be used to cache all of 
    * the constructors instantiated for the required types. 
    */
   public Instantiator() {
      this.cache = new ConstructorCache();
   }
   
   /**
    * This will create a <code>Type</code> object that can be used
    * to instantiate objects of the specified class. This leverages
    * an internal constructor cache to ensure creation is quicker.
    * 
    * @param type this is the type that is to be instantiated
    * 
    * @return this will return a type for instantiating objects
    */
   public Type getType(Class type) {
      return new Instance(this, type);
   }
   
   /**
    * This will create an array <code>Type</code> that can be used
    * to instantiate arrays of the specified class. This leverages
    * an internal constructor cache to ensure creation is quicker.
    * 
    * @param type this is the array type that is to be instantiated
    * @param size this is the length of the array to be created
    * 
    * @return this will return a type for instantiating objects
    */
   public Type getType(Class type, int size) {
      return new ArrayInstance(type, size);
   }
   
   /**
    * This method will instantiate an object of the provided type. If
    * the object or constructor does not have public access then this
    * will ensure the constructor is accessible and can be used.
    * 
    * @param type this is used to ensure the object is accessible
    *
    * @return this returns an instance of the specific class type
    */ 
   public Object getInstance(Class type) throws Exception {
      Constructor method = cache.fetch(type);
      
      if(method == null) {
         method = type.getDeclaredConstructor();      

         if(!method.isAccessible()) {
            method.setAccessible(true);              
         }
         cache.cache(type, method);
      }
      return method.newInstance();   
   }
}