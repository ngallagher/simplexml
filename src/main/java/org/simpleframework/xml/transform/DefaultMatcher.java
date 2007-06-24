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

class DefaultMatcher extends PackageMatcher {
   
   private final Matcher array;
   
   public DefaultMatcher() {
      this.array = new ArrayMatcher(this);
   }
   
   public Transform match(Class type) throws Exception {
      if(type.isArray()) {
         return array.match(type);
      }
      return matchType(type);
   }
   
   private Transform matchType(Class type) throws Exception {
      Class real = getConversion(type);
      Class match = getClass(real);
      
      return (Transform)match.newInstance();      
   }
   
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
