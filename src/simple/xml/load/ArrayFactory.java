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
    * This represents the array type from the object field.
    */
   private Class field;
        
   /**
    * Constructor for the <code>ArrayFactory</code> object. This is
    * given the field type as taken from the owning object. If the
    * field type is not an array this throws an exception for each
    * request to instantiate an array of the specified length.
    * 
    * @param field this is the class for the owning object
    */
   public ArrayFactory(Class field) {
      this.field = field;           
   }        

   /**
    * Creates the object array to use. This will check to ensure 
    * that the field type is an array before instantiating it. If
    * the field type is not an array then this throws an exception.
    * 
    * @param node this is the input node representing the list
    * 
    * @return this is the obejct array instantiated for the field
    */         
   public Object[] getInstance(int size) throws Exception {
      if(!field.isArray()) {
         throw new InstantiationException("Type is not an array %s", field);  
      }                 
      Class type = field.getComponentType();
      Object list = Array.newInstance(type, size);

      return (Object[])list;
   }     
}
