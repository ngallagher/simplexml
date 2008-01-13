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
 * determine the transform to be used. All array transforms created
 * by this will be <code>ArrayTransform</code> object instances. 
 * These will use a type transform for the array component to add
 * values to the individual array indexes. Also such transforms are
 * typically treated as a comma separated list of individual values.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.ArrayTransform
 */
class ArrayMatcher implements Matcher {

   /**
    * This is the primary matcher that can resolve transforms.
    */
   private final Matcher primary;
   
   /**
    * Constructor for the <code>ArrayTransform</code> object. This
    * is used to match array types to their respective transform
    * using the <code>ArrayTransform</code> object. This will use
    * a comma separated list of tokens to populate the array.
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
    * name of the array component type. If a transform can not be
    * found then this method will throw an exception.
    * 
    * @param type this is the array to find the transform for
    * 
    * @throws Exception thrown if a transform can not be matched
    */
   public Transform match(Class type) throws Exception {
      Class entry = type.getComponentType();
      
      if(entry == char.class) {
         return new CharacterArrayTransform(entry);
      } 
      if(entry == Character.class) {
         return new CharacterArrayTransform(entry);
      }
      if(entry == String.class) {
         return new StringArrayTransform();
      }
      return matchArray(entry);
   }
   
   /**
    * This is used to match a <code>Transform</code> based on the
    * array component type of an object to be transformed. This will
    * attempt to match the transform using the fully qualified class
    * name of the array component type. If a transform can not be
    * found then this method will throw an exception.
    * 
    * @param entry this is the array component type to be matched
    * 
    * @throws Exception thrown if a transform can not be matched
    */
   private Transform matchArray(Class entry) throws Exception {            
      Transform transform = primary.match(entry);
     
      if(transform == null) {
         return null;
      }
      return new ArrayTransform(transform, entry);
   }
}
