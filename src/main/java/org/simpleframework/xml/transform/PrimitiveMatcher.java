/*
 * PrimitiveMatcher.java May 2007
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

/**
 * The <code>PrimitiveMatcher</code> object is used to resolve the
 * primitive types to a stock transform. This will basically use
 * a transform that is used with the primitives language object.
 * This will always return a suitable transform for a primitive.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.DefaultMatcher
 */
class PrimitiveMatcher implements Matcher { 
   
   /**
    * Constructor for the <code>PrimitiveMatcher</code> object. The
    * primitive matcher is used to resolve a transform instance to
    * convert primitive types to an from strings. If a match is not
    * found with this matcher then an exception is thrown.
    */
   public PrimitiveMatcher() {
      super();
   }
   
   /**
    * This method is used to match the specified type to primitive
    * transform implementations. If this is given a primitive then
    * it will always return a suitable <code>Transform</code>. If
    * however it is given an object type an exception is thrown.
    * 
    * @param type this is the primitive type to be transformed
    * 
    * @return this returns a stock transform for the primitive
    */
   public Transform match(Class type) throws Exception {     
      if(type == int.class) {
         return new IntegerTransform();
      }
      if(type == boolean.class) {
         return new BooleanTransform();
      }
      if(type == long.class) {
         return new LongTransform();
      }
      if(type == double.class) {
         return new DoubleTransform();
      }
      if(type == float.class) {
         return new FloatTransform();
      }
      if(type == short.class) {
         return new ShortTransform();
      }
      if(type == byte.class) {
         return new ByteTransform();
      }
      if(type == char.class) {
         return new CharacterTransform();
      }     
      throw new TransformException("Transform of '%s' not supported", type);
   }
}