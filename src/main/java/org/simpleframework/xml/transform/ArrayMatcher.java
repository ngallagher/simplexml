/*
 * ArrayMatcher.java May 2007
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
 * The <code>ArrayMatcher</code> object performs matching of array
 * types to array transforms. This uses the array component type to
 * determine the transform to be used by appending the class name
 * of the array component type with "ArrayTransform". If a class
 * can be loaded for the generated array transform class name then
 * an instance of that type is used to perform the transformation.
 * If no then this will thrown an exception. 
 * 
 * @author Niall Gallagher
 */
class ArrayMatcher extends PackageMatcher {

   /**
    * This is the primary matcher that can resolve transforms.
    */
   private final Matcher primary;
   
   /**
    * Constructor for the <code>ArrayTransform</code> object. This
    * is used to match array types to their respective transform
    * using a convention where the fully qualified class name of 
    * the array component type is appended with "ArrayTransform".
    * 
    * @param primary this is the primary matcher to be used 
    */
   public ArrayMatcher(Matcher primary) {
      this.primary = primary;
   }
   
   /**
    * This is used to match a <code>Transform</code> based on the
    * array component type of an object to be transformed. This will
    * attempt to match the transform using the fully qualified class
    * name of the array component type. If a trasform can not be
    * found then this method will throw an exception.
    * 
    * @param type this is the array to find the transform for
    * 
    * @throws Exception thrown if a transform can not be matched
    */
   public Transform match(Class type) throws Exception {
      Class entry = type.getComponentType();
      
      if(entry.isPrimitive()) {
         return matchPrimitive(entry);
      }
      return matchArray(entry);
   }
   
   /**
    * This is used to match a <code>Transform</code> based on the
    * array component type of an object to be transformed. This will
    * attempt to match the transform using the fully qualified class
    * name of the array component type. If a trasform can not be
    * found then this method will throw an exception.
    * 
    * @param entry this is the array component type to be matched
    * 
    * @throws Exception thrown if a transform can not be matched
    */
   private Transform matchPrimitive(Class entry) throws Exception {            
      Transform transform = primary.match(entry);

      if(entry == char.class) {
         return new CharacterArrayTransform();
      }      
      if(transform == null) {
         throw new TransformRequiredException("Transform for %s not found", entry);
      }
      return new PrimitiveArrayTransform(transform, entry);
   }
   
   /**
    * This is used to match a <code>Transform</code> based on the
    * array component type of an object to be transformed. This will
    * attempt to match the transform using the fully qualified class
    * name of the array component type. If a trasform can not be
    * found then this method will throw an exception.
    * 
    * @param entry this is the array component type to be matched
    * 
    * @throws Exception thrown if a transform can not be matched
    */
   private Transform matchArray(Class entry) throws Exception {
      Class type = getArrayClass(entry);      
      
      return (Transform)type.newInstance();
   }
}
