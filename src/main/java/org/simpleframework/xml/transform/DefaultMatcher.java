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
 * The <code>DefaultMatcher</code> is a delegation object that uses
 * several matcher implementations to correctly resolve both the
 * stock <code>Transform</code> implementations and implementations
 * that have been overridden by the user with a custom matcher. This
 * will perform the resolution of the transform using the specified 
 * matcher, if this results in no transform then this will look for
 * a transform within the collection of implementations.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.transform.Transformer
 */
class DefaultMatcher implements Matcher {
   
   /**
    * Matcher used to resolve stock transforms for primitive types.
    */
   private Matcher primitive;   
   
   /**
    * Matcher used to resolve user specified transform overrides.
    */
   private Matcher matcher;
   
   /**
    * Matcher used to resolve all the core Java object transforms.
    */
   private Matcher stock;
   
   /**
    * Matcher used to resolve transforms for array type objects.
    */
   private Matcher array; 
   
   /**
    * Constructor for the <code>DefaultMatcher</code> object. This
    * performs resolution of <code>Transform</code> implementations 
    * using the specified matcher. If that matcher fails to resolve
    * a suitable transform then the stock implementations are used.
    * 
    * @param matcher this is the user specified matcher object
    */
   public DefaultMatcher(Matcher matcher) {
      this.primitive = new PrimitiveMatcher();
      this.stock = new PackageMatcher();
      this.array = new ArrayMatcher(this);
      this.matcher = matcher;
   }
   
   /**
    * This is used to match a <code>Transform</code> for the given
    * type. If a transform cannot be resolved this this will throw an
    * exception to indicate that resolution of a transform failed. A
    * transform is resolved by first searching for a transform within
    * the user specified matcher then searching the stock transforms.
    * 
    * @param type this is the type to resolve a transform object for
    * 
    * @return this returns a transform used to transform the type
    */
   public Transform match(Class type) throws Exception {
      Transform value = matcher.match(type);
      
      if(value != null) {
         return value;
      }
      return matchType(type);
   }
   
   /**
    * This is used to match a <code>Transform</code> for the given
    * type. If a transform cannot be resolved this this will throw an
    * exception to indicate that resolution of a transform failed. A
    * transform is resolved by first searching for a transform within
    * the user specified matcher then searching the stock transforms.
    * 
    * @param type this is the type to resolve a transform object for
    * 
    * @return this returns a transform used to transform the type
    */
   private Transform matchType(Class type) throws Exception {
      if(type.isArray()) {
         return array.match(type);
      }
      if(type.isPrimitive()) {
         return primitive.match(type);
      }
      return stock.match(type); 
   }
}
 