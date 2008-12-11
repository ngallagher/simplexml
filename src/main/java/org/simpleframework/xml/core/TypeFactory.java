/*
 * TypeFactory.java July 2006
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

/**
 * The <code>TypeFactory</code> is used to instantiate type that will
 * leverage a constructor cache to quickly instantiate objects. This
 * is used by the various object factories to return type instances
 * that can be used by converters to create the objects that will
 * later be deserialized.
 *
 * @author Niall Gallagher
 */
class TypeFactory {

   /**
    * This is used to cache the constructors for the given types.
    */
   private final ConstructorCache cache;
   
   /**
    * Constructor for the <code>TypeFactory</code> object. This will
    * create a constructor cache that can be used to cache all of 
    * the constructors instantiated for the required types. 
    */
   public TypeFactory() {
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
   public Type getInstance(Class type) {
      return new ClassType(cache, type);
   }
}