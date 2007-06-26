/*
 * ArrayTransform.java May 2007
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

package org.simpleframework.xml.transform.lang;

import org.simpleframework.xml.transform.Transform;
import java.lang.reflect.Array;

/**
 * The <code>ArrayTransform</code> object acts as a helper class that
 * can be used by individual array transforms to transform a comma
 * separated list of individual values in to an array of values. This
 * requires the component type for the array and the transform used
 * to perform the individual transformations on the values extracted.
 * 
 * @author Niall Gallagher
 */
class ArrayTransform {
   
   /**
    * This is used to split the comma separated list of values.
    */
   private final StringArrayTransform split;        

   /**
    * This represents the array component type for this transform.
    */
   private final Class entry;

   /**
    * Constructor for the <code>ArrayTransform</code> object. This
    * is used to create arrays of the specified component type. Each
    * read and write operation will then insert transformed values
    * in to an array of the specified type and return the array.
    * 
    * @param entry this is the array component type to be used
    */
   public ArrayTransform(Class entry) {
      this.split = new StringArrayTransform();
      this.entry = entry;
   }
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param value this is the string representation of the value
    * @param single this is used to perform individual transforms
    * 
    * @return this returns an appropriate instanced to be used
    */
   public <T> T read(String value, Transform single) throws Exception {
      String[] list = split.read(value);      
      int length = list.length;

      return (T)read(list, single, length);
   }
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param list this is the string representation of the value
    * @param single this is used to perform individual transforms
    * @param length this is the number of entries in the array 
    * 
    * @return this returns an appropriate instanced to be used
    */
   private Object read(String[] list, Transform single, int length) throws Exception {
      Object array = Array.newInstance(entry, length);

      for(int i = 0; i < length; i++) {
         Object item = single.read(list[i]);

         if(item != null) {
            Array.set(array, i, item);                 
         }         
      }
      return array;
   }
   
   /**
    * This method is used to convert the provided value into an XML
    * usable format. This is used in the serialization process when
    * there is a need to convert a field value in to a string so 
    * that that value can be written as a valid XML entity.
    * 
    * @param value this is the value to be converted to a string
    * @param single this is used to perform individual transforms
    * 
    * @return this is the string representation of the given value
    */ 
   public String write(Object[] value, Transform single) throws Exception {
      String[] list = new String[value.length];

      for(int i = 0; i < value.length; i++) {
         Object entry = Array.get(value, i);

         if(entry != null) {
            list[i] = single.write(entry);                             
         }         
      }      
      return split.write(list);      
   }
}