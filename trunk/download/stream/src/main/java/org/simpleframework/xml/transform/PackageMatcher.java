/*
 * PackageMatcher.java May 2007
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
 * The <code>PackageMatcher</code> object is used to perform matches
 * based on the package type of the object to be transformed. This
 * acts as a convention over configuration means of mapping a type
 * to a transform without statically mapping the types to respective
 * transform implementations. 
 * <p>
 * There are two forms of transformation matching peformed by this
 * object. Matching can be done for array types and for object types.
 * For an array the transform will have "ArrayTransform" appended to
 * the end of the fully qualified class name, for a normal type the
 * "Transform" string is appended.  
 * 
 * @author Niall Gallagher
 */
abstract class PackageMatcher implements Matcher {
   
   /**
    * This is the package that forms the base of all Java transforms.
    */
   private static final String PACKAGE = "org.simpleframework.xml.transform";
      
   /**
    * This is used to acquire the fully qualified class name for the
    * transform representing the specified type. As with all types
    * matched with this matcher, the fully qualified class name for
    * the type is appended with "Transform" to match the transform. 
    * 
    * @param type this is the type to acquire the transform class
    *  
    * @return this returns the class for the required transform  
    * 
    * @throws Exception thrown if a transform could not be loaded
    */
   protected Class getClass(Class type) throws Exception {      
      return getClass("%sTransform", type);
   }
   
   /**
    * This is used to acquire the fully qualified class name for the
    * transform representing the specified type. As with all types
    * matched with this matcher, the fully qualified class name for
    * the type is appended with "ArrayTransform" for the transform. 
    * 
    * @param entry this is the type to acquire the transform class
    *  
    * @return this returns the class for the required transform  
    * 
    * @throws Exception thrown if a transform could not be loaded
    */
   protected Class getArrayClass(Class entry) throws Exception {
      return getClass("%sArrayTransform", entry);       
   }
   
   /**
    * This is used to acquire the fully qualified class name for the
    * transform representing the specified type. As with all types
    * matched with this matcher, the fully qualified class name for
    * the type is appended with specified suffix for the transform. 
    * 
    * @param type this is the type to acquire the transform class
    * @param suffix this is appended to the name of the class
    *  
    * @return this returns the class for the required transform  
    * 
    * @throws Exception thrown if a transform could not be loaded
    */
   protected Class getClass(String suffix, Class type) throws Exception {
      String name = getConversion(suffix, type);      
    
      try {  
         return Class.forName(name);
      } catch(Exception e) {
         throw new TransformRequiredException(e, "Transform %s is required for %s", name, type);
      }
      
   }
   /**
    * This is used to acquire the fully qualified class name for the
    * transform representing the specified type. As with all types
    * matched with this matcher, the fully qualified class name for
    * the type is appended with specified suffix for the transform.
    * Also for Java types the package is replaced for the class. 
    * 
    * @param suffix this is appended to the name of the class 
    * @param type this is the type to acquire the transform class
    *  
    * @return this returns the class for the required transform  
    * 
    * @throws Exception thrown if a transform could not be loaded
    */
   protected String getConversion(String suffix, Class type) {
      String name = type.getName();
      
      if(name.startsWith("javax")) {
         name = name.replace("javax", PACKAGE);
      } 
      if(name.startsWith("java")) {
         name = name.replace("java", PACKAGE);
      }
      return String.format(suffix, name); 
   }
}
