/*
 * ArrayType.java April 2007
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

package simple.xml.load;

import java.lang.reflect.Array;

/**
 * The <code>ArrayType</code> object is a type used for constructing
 * arrays from a specified component type or <code>Type</code> object.
 * This allows primitive and composite lists to be acquired either by
 * reference or by value from a converter object. This will accept a
 * list of objects which it will convert to an array object.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.ArrayFactory
 */
final class ArrayType implements Type {
   
   /**
    * This is the optional field type for the array to be created. 
    */
   private Class type;
   
   /**
    * This is used to determine the size of the array to be created.
    */
   private int size;
   
   /**
    * This is used to specify the creation of an array type which
    * can be used for creating an array that can hold instances of 
    * the specified type. The type specified must be the component
    * type for the array that is to be created. 
    * 
    * @param type this is the component type for the array
    * @param size this is the size of the array to instantiate
    */
   public ArrayType(Class type, int size) {
      this.type = type;      
      this.size = size;
   }
   
   /**
    * This is the instance that is acquired from this type. This is
    * typically used if the <code>isReference</code> method is true.
    * If there was to type reference provided then this returns null
    * otherwise this will delegate to the <code>Type</code> given.
    * 
    * @return this returns a reference to an existing array
    */
   public Object getInstance() throws Exception {
      return getInstance(type);
   }
   
   /**
    * This is the instance that is acquired from this type. This is
    * typically used if the <code>isReference</code> method is true.
    * If there was to type reference provided then this returns null
    * otherwise this will delegate to the <code>Type</code> given.
    * 
    * @param type the type to convert this array instance to
    * 
    * @return this returns a reference to an existing array
    */
   public Object getInstance(Class type) throws Exception {  
      return Array.newInstance(type, size);
   }
   
   /**
    * This will return the component type for the array instance 
    * that is produced by this object. Depending on the constructor 
    * used this will either delegate to the <code>Type</code> object 
    * specified or will return the component type class provided. 
    * 
    * @return this returns the component type for the array
    */
   public Class getType() {
      return type;
   }
   
   /**
    * This will return true if the <code>Type</code> object provided
    * is a reference type. Typically a reference type refers to a 
    * type that is substituted during the deserialization process 
    * and so constitutes an object that does not need initialization.
    * 
    * @retunr this returns true if the type is a reference type
    */
   public boolean isReference() {
      return false;
   }
   
}
