/*
 * DefaultMatcher.java May 2007
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
 * The <code>DefaultMatcher</code> object is used to match types to
 * their transform types. This makes use of a convention in order to
 * map the specified type to its transform. The convention used is
 * to append "Transform" to the fully qualified name of the type to
 * be transformed, or "ArrayType" if the type is an array.
 * 
 * @author Niall Gallagher
 */
class DefaultMatcher extends PackageMatcher {
   
   /**
    * This is a matcher used to resolve the transforms for arrays.
    */
   private final Matcher array;
   
   /**
    * Constructor for the <code>DefaultMatcher</code> object. This 
    * is used to create matcher instance that matches transforms for
    * types using conventions where the fully qualified class name
    * for the type to be matched is appended with a known suffix.
    */
   public DefaultMatcher() {
      this.array = new ArrayMatcher(this);
   }
   
   /**
    * This is used to match a <code>Transform</code> using the type
    * specified. If no transform can be acquired then an exception
    * is thrown indicating that no transform could be found.
    * 
    * @param type this is the type to acquire the transform for
    * 
    * @return returns a transform for processing the type given
    * 
    * @throws Exception thrown if a transform could not be found
    */    
   public Transform match(Class type) throws Exception {
      if(type.isArray()) {
         return array.match(type);
      }
      return matchType(type);
   }
   
   /**
    * This is used to match a <code>Transform</code> using the type
    * specified. If no transform can be acquired then an exception
    * is thrown indicating that no transform could be found.
    * 
    * @param type this is the type to acquire the transform for
    * 
    * @return returns a transform for processing the type given
    * 
    * @throws Exception thrown if a transform could not be found
    */   
   private Transform matchType(Class type) throws Exception {
      Class real = getConversion(type);
      Class match = getClass(real);
      
      return (Transform)match.newInstance();      
   }
   
   /**
    * Primitive types are transformed using their respective Java 
    * language types. So in order to figure out the transform that
    * is required for the type it needs to be converted so that it
    * can be resolved using the name convention scheme. 
    * 
    * @param type this is the primitive type to be converted 
    * 
    * @return returns the Java language type for the primitive
    */
   private Class getConversion(Class type) {
      if(type == int.class) {
         return Integer.class;              
      }           
      if(type == boolean.class) {
         return Boolean.class;               
      }
      if(type == float.class) {
         return Float.class;                       
      }
      if(type == long.class) {
         return Long.class;                   
      }
      if(type == double.class) {
         return Double.class;              
      }
      if(type == byte.class) {
         return Byte.class;              
      }        
      if(type == short.class) {
         return Short.class;              
      }
      if(type == char.class) {
         return Character.class;
      }
      return type;
   }
}