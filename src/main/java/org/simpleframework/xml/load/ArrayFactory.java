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

package org.simpleframework.xml.load;

import java.lang.reflect.Array;

import org.simpleframework.xml.stream.InputNode;

/**
 * The <code>ArrayFactory</code> is used to create object array
 * types that are compatible with the field type. This simply
 * requires the type of the array in order to instantiate that
 * array. However, this also performs a check on the field type 
 * to ensure that the array component types are compatible.
 * 
 * @author Niall Gallagher
 */ 
class ArrayFactory extends Factory {
        
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
    * This is used to create a default instance of the field type. It
    * is up to the subclass to determine how to best instantiate an
    * object of the field type that best suits. This is used when the
    * empty value is required or to create the default type instance.
    * 
    * @return a type which is used to instantiate the collection     
    */
   @Override
   public Object getInstance() throws Exception {
      Class type = field.getComponentType();
      
      if(type != null) {
         return Array.newInstance(type, 0);
      }
      return null;
   }

   /**
    * Creates the array type to use. This will use the provided
    * XML element to determine the array type and provide a means
    * for creating an array with the <code>Type</code> object. If
    * the array size cannot be determined an exception is thrown.
    * 
    * @param node this is the input node for the array element
    * 
    * @return the object array type used for the instantiation
    */         
   public Type getInstance(InputNode node) throws Exception {
      Type type = getOverride(node);    
      
      if(type == null) {
         throw new ElementException("Array length required for %s", field);         
      }      
      Class entry = type.getType();
      
      return getInstance(type, entry);
   }

   /**
    * Creates the array type to use. This will use the provided
    * XML element to determine the array type and provide a means
    * for creating an array with the <code>Type</code> object. If
    * the array types are not compatible an exception is thrown.
    * 
    * @param type this is the type object with the array details
    * @param entry this is the entry type for the array instance    
    * 
    * @return this object array type used for the instantiation  
    */
   private Type getInstance(Type type, Class entry) throws Exception {
      Class expect = field.getComponentType();

      if(!expect.isAssignableFrom(entry)) {
         throw new InstantiationException("Array of type %s cannot hold %s", expect, entry);
      }
      return type;      
   }   
}
