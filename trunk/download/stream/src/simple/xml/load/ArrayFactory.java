/*
 * ArrayFactory.java July 2006
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ArrayFactory</code> is used to create object array
 * instances that are compatible with the field type. This simply
 * requires the type of the array in order to instantiate that
 * array. However, this also performs a check on the field type 
 * to ensure that it is a valid array class before instantiation.
 * 
 * @author Niall Gallagher
 */ 
final class ArrayFactory {

   /**
    * This represents the array component type from the field.
    */
   private Class type;
        
   /**
    * Constructor for the <code>ArrayFactory</code> object. This is
    * given the array component type as taken from the field type 
    * of the source object. Each request for an array will return 
    * an array which uses the specified component type.
    * 
    * @param type the array component type for the field object
    */
   public ArrayFactory(Class type) {
      this.type = type;           
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
   public Object getArray(List list) throws Exception {
      return getArray(list, list.size());
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
   public Object getArray(List list, int size) throws Exception {
      Object array = Array.newInstance(type, size);
      
      for(int i = 0; i < size; i++) {
         Array.set(array, i, list.get(i));
      }
      return array;     
   }     
   
   public List getList(Object source) throws Exception {
      return getList(source, Array.getLength(source));
   }
   
   public List getList(Object source, int size) throws Exception { 
      List list = new ArrayList();
      
      for(int i = 0; i < size; i++) {
         list.add(Array.get(source, i));         
      }
      return list;
   }
}
