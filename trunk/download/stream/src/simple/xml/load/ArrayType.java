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

final class ArrayType implements Type {
   
   private Class field;
   
   private Type type;
   
   public ArrayType(Class field) {
      this.field = field;      
   }
   
   public ArrayType(Type type) {
      this.type = type;
   }
   
   public Object getInstance() throws Exception {
      if(type != null) {
        return type.getInstance();
      }
      return null;
   }
   
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
   
   public Class getType() {
      if(type != null) {
         return type.getType();
      }
      return null;
   }
   
   
   public boolean isReference() {
      if(type != null) {
         return type.isReference();
      }
      return false;
   }
   
}
