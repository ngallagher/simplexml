/*
 * PrimitiveArrayTransform.java May 2007
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

package org.simpleframework.xml.transform;

import org.simpleframework.xml.transform.StringArrayTransform;
import java.lang.reflect.Array;

/**
 * The <code>PrimitiveArrayTransform</code> is used to transform 
 * arrays to and from string representations, which will be inserted
 * in the generated XML document as the value place holder. The
 * value must be readable and writable in the same format. Fields
 * and methods annotated with the XML attribute annotation will use
 * this to persist and retrieve the value to and from the XML source.
 * <pre>
 * 
 *    &#64;Attribute
 *    private int[] text;
 *    
 * </pre>
 * As well as the XML attribute values using transforms, fields and
 * methods annotated with the XML element annotation will use this.
 * Aside from the obvious difference, the element annotation has an
 * advantage over the attribute annotation in that it can maintain
 * any references using the <code>CycleStrategy</code> object. 
 * 
 * @author Niall Gallagher
 */
class ArrayTransform implements Transform {           

   /**
    * This transform is used to split the comma separated values. 
    */
   private final StringArrayTransform split;        

   /**
    * This is the transform that performs the individual transform.
    */
   private final Transform delegate;

   /**
    * This is the entry type for the primitive array to be created.
    */
   private final Class entry;

   /**
    * Constructor for the <code>PrimitiveArrayTransform</code> object.
    * This is used to create a transform that will create primitive
    * arrays and populate the values of the array with values from a
    * comma separated list of individula values for the entry type.
    * 
    * @param delegate this is used to perform individual transforms
    * @param entry this is the entry component type for the array
    */
   public ArrayTransform(Transform delegate, Class entry) {
      this.split = new StringArrayTransform();
      this.delegate = delegate;
      this.entry = entry;
   }       
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param value this is the string representation of the value
    * 
    * @return this returns an appropriate instanced to be used
    */
   public Object read(String value) throws Exception {
      String[] list = split.read(value);      
      int length = list.length;

      return read(list, length);
   }
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param list this is the string representation of the value
    * @param length this is the number of string values to use
    * 
    * @return this returns an appropriate instanced to be used
    */
   private Object read(String[] list, int length) throws Exception {
      Object array = Array.newInstance(entry, length);

      for(int i = 0; i < length; i++) {
         Object item = delegate.read(list[i]);

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
    * 
    * @return this is the string representation of the given value
    */
   public String write(Object value) throws Exception {
      int length = Array.getLength(value);

      return write(value, length);      
   }
   
   /**
    * This method is used to convert the provided value into an XML
    * usable format. This is used in the serialization process when
    * there is a need to convert a field value in to a string so 
    * that that value can be written as a valid XML entity.
    * 
    * @param value this is the value to be converted to a string
    * 
    * @return this is the string representation of the given value
    */
   private String write(Object value, int length) throws Exception {
      String[] list = new String[length];

      for(int i = 0; i < length; i++) {
         Object entry = Array.get(value, i);

         if(entry != null) {
            list[i] = delegate.write(entry);                             
         }         
      }      
      return split.write(list);
   }
}
