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

import simple.xml.stream.InputNode;
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
final class ArrayFactory extends Factory {
        
   /**
    * Constructor for the <code>ArrayFactory</code> object. This is
    * given the array component type as taken from the field type 
    * of the source object. Each request for an array will return 
    * an array which uses the specified component type.
    * 
    * @param root this is the context object for serialization
    * @param field the array component type for the field object
    */
   public ArrayFactory(Source root, Class field) {
      super(root, field);                
   }        

   /**
    * Creates the object array to use. This will use the provided
    * list of values to form the values within the array. Each of
    * the values witin the specified <code>List</code> will be
    * set into a the array, if the type of the values within the
    * list are not compatible then an exception is thrown.
    * 
    * @param node this is the input node for the array element
    * @param list this is the list of values for the array
    * 
    * @return this is the obejct array instantiated for the type
    */         
   public Object getArray(InputNode node, List list) throws Exception {
      Type type = getOverride(node);
      int size = list.size();
      
      if(type == null) {
         return getArray(field, list, size);         
      }      
      return getArray(type, list, size);
   }
   
   /**
    * Creates the object array to use. This will use the provided
    * list of values to form the values within the array. Each of
    * the values witin the specified <code>List</code> will be
    * set into a the array, if the type of the values within the
    * list are not compatible then an exception is thrown.
    * 
    * @param type this is the type used to create the new array
    * @param list this is the list of values for the array
    * @param size the number of values to consider for copying
    * 
    * @return this is the obejct array instantiated for the type
    */ 
   private Object getArray(Type type, List list, int size) throws Exception {
      Object array = type.getArray(size);

      for(int i = 0; i < size; i++) {
         Array.set(array, i, list.get(i));
      }
      return array;     
   }   
   
   /**
    * Creates the object array to use. This will use the provided
    * list of values to form the values within the array. Each of
    * the values witin the specified <code>List</code> will be
    * set into a the array, if the type of the values within the
    * list are not compatible then an exception is thrown.
    * 
    * @param type this is the type used to create the new array
    * @param list this is the list of values for the array
    * @param size the number of values to consider for copying
    * 
    * @return this is the obejct array instantiated for the type
    */ 
   private Object getArray(Class type, List list, int size) {
      Object array = Array.newInstance(type, size);
      
      for(int i = 0; i < size; i++) {
         Array.set(array, i, list.get(i));
      }
      return array;     
   }
   
   /**
    * This method is used to convert the specified array to a list
    * that can be iterated in a generic way. This is used when an
    * array is to be converted into a sequence of XML elements.
    * 
    * @param source this is the source array to be converted
    * 
    * @return this returns a list containing the array values
    * 
    * @throws Exception thrown if the array could not be used
    */
   public List getList(Object source) throws Exception {
      return getList(source, Array.getLength(source));
   }
   
   /**
    * This method is used to convert the specified array to a list
    * that can be iterated in a generic way. This is used when an
    * array is to be converted into a sequence of XML elements.
    * 
    * @param source this is the source array to be converted
    * @param size this is the number of values to iterate over
    * 
    * @return this returns a list containing the array values
    * 
    * @throws Exception thrown if the array could not be used
    */  
   public List getList(Object source, int size) throws Exception { 
      List list = new ArrayList();
      
      for(int i = 0; i < size; i++) {
         list.add(Array.get(source, i));         
      }
      return list;
   }
}
