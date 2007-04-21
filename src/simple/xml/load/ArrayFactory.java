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
 * types that are compatible with the field type. This simply
 * requires the type of the array in order to instantiate that
 * array. However, this also performs a check on the field type 
 * to ensure that the array component types are compatible.
 * 
 * @author Niall Gallagher
 */ 
final class ArrayFactory extends Factory {
        
   /**
    * Constructor for the <code>ArrayFactory</code> object. This is
    * given the array component type as taken from the field type 
    * of the source object. Each request for an array will return 
    * an array which uses a compatible component type.
    * 
    * @param root this is the context object for serialization
    * @param field the array component type for the field object
    */
   public ArrayFactory(Source root, Class field) {
      super(root, field);                
   }        

   /**
    * Creates the array type to use. This will use the provided
    * XML element to determine the array type and provide a means
    * for creating an array with an <code>ArrayType</code> object.
    * This enables the array factory to determine whether the
    * XML element is a reference type or not and so allows the
    * returned type to gain access to the available reference.
    * 
    * @param node this is the input node for the array element
    * 
    * @return the object array type used for the instantiation
    */         
   public ArrayType getInstance(InputNode node) throws Exception {
      Type type = getOverride(node);    
      
      if(type != null) { 
         return getInstance(type);
      }
      return new ArrayType(field);
   }

   /**
    * Creates the array type to use. This will use the provided
    * type object to determine if the array component types are
    * compatible. If the component types are compatible then this
    * will return an <code>ArrayType</code> for the provided type.
    * list of values to form the values within the array. 
    * 
    * @param type this is the type object with the array details    
    * 
    * @return this object array type used for the instantiation  
    */
   private ArrayType getInstance(Type type) throws Exception {
      Class real = type.getType();

      if(!field.isAssignableFrom(real)) {
         throw new InstantiationException("Cannot assign %s to %s", real, field);
      }
      if(!type.isReference()) {
         return new ArrayType(real);
      }
      return new ArrayType(type);      
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
      int size = Array.getLength(source);
            
      if(size > 0) {
         return getList(source, size);
      }
      return new ArrayList();
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
   private List getList(Object source, int size) throws Exception { 
      List list = new ArrayList();
      
      for(int i = 0; i < size; i++) {
         list.add(Array.get(source, i));         
      }
      return list;
   }
}
