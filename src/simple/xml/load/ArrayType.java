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
import java.util.List;

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
   private Class field;
   
   /**
    * This is used if the array is a reference to an instance.
    */
   private Type type;
   
   /**
    * This is used to specify the creation of an array type which
    * can be used for creating an array that can hold instances of 
    * the specified type. The type specified must be the component
    * type for the array that is to be created. 
    * 
    * @param field this is the component type for the array
    */
   public ArrayType(Class field) {
      this.field = field;      
   }
   
   /**
    * This is used to construct an array from a reference to an
    * existing array. The type specified must be a reference. If it
    * is not a reference to an existing array type type returned is
    * dependant on the implementation and could be a null value. 
   
    * @param type this is the type that references an array
    */
   public ArrayType(Type type) {
      this.type = type;
   }
   
   /**
    * This is the instance that is acquired from this type. This is
    * typically used if the <code>isReference</code> method is true.
    * If there was to type reference provided then this returns null
    * otherwise this will delegate to the <code>Type</code> given.
    * 
    *  @return this returns a reference to an existing array
    */
   public Object getInstance() throws Exception {
      if(type != null) {
        return type.getInstance();
      }
      return null;
   }
   
   /**
    * Creates the object array to use. This will use the provided
    * list of values to form the values within the array. Each of
    * the values witin the specified <code>List</code> will be
    * set into a the array, if the type of the values within the
    * list are not compatible then an exception is thrown.
    * 
    * @param list this is the list of values for the array    
    * 
    * @return this is the obejct array instantiated for the type
    */
   public Object getInstance(List list) throws Exception {
      return getInstance(list, list.size());
   }
   
   /**
    * Creates the object array to use. This will use the provided
    * list of values to form the values within the array. Each of
    * the values witin the specified <code>List</code> will be
    * set into a the array, if the type of the values within the
    * list are not compatible then an exception is thrown.
    * 
    * @param list this is the list of values for the array
    * @param size the number of values to consider for copying
    * 
    * @return this is the obejct array instantiated for the type
    */ 
   private Object getInstance(List list, int size) throws Exception {
      Object array = Array.newInstance(field, size);

      for(int i = 0; i < size; i++) {
         Array.set(array, i, list.get(i));
      }
      return array;     
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
      if(type != null) {
         return type.getType();
      }
      return field;
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
      if(type != null) {
         return type.isReference();
      }
      return false;
   }
   
}
