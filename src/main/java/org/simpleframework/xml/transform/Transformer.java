/*
 * Transformer.java May 2007
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

import org.simpleframework.xml.util.WeakCache;
import org.simpleframework.xml.util.Cache;

/**
 * The <code>Transformer</code> object is used to convert strings to
 * and from object instances. This is used during the serialization
 * and deserialization process to transform types from the Java class
 * libraries, as well as other types which do not contain XML schema
 * annotations. Typically this will be used to transform primitive
 * types to and from strings, such as <code>int</code> values.
 * <pre>
 * 
 *    &#64;Element
 *    private String[] value;
 *    
 * </pre>
 * For example taking the above value the array of strings needs to 
 * be converted in to a single string value that can be inserted in 
 * to the element in such a way that in can be read later. In this
 * case the serialized value of the string array would be as follows.
 * <pre>
 * 
 *    &lt;value&gt;one, two, three&lt;/value&gt;
 * 
 * </pre>
 * Here each non-null string is inserted in to a comma separated  
 * list of values, which can later be deserialized. Just to note the
 * above array could be annotated with <code>ElementList</code> just
 * as easily, in which case each entry would have its own element.
 * The choice of which annotation to use is up to the developer. A
 * more obvious benefit to transformations like this can be seen for
 * values annotated with the <code>Attribute</code> annotation.
 * 
 * @author Niall Gallagher
 */
public class Transformer {

   /**
    * This is used to cache all transforms matched to a given type.
    */
   private TransformCache cache;

   /**
    * This is used to perform the matching of types to transforms.
    */
   private Matcher matcher;
   
   /**
    * This is used to cache the types that to not have a transform.
    */ 
   private Cache error;

   /**
    * Constructor for the <code>Transformer</code> object. This is
    * used to create a transformer which will transform specified
    * types using transforms loaded from the class path. By default
    * this will match transforms by convention.
    */
   public Transformer() {
      this(new EmptyMatcher());
   }
   
   /**
    * Constructor for the <code>Transformer</code> object. This is
    * used to create a transformer which will transform specified
    * types using transforms loaded from the class path. Tramsforms
    * are matched to types using the specified matcher object.
    * 
    * @param matcher this is used to match types to transforms
    */
   private Transformer(Matcher matcher) {  
      this.matcher = new DefaultMatcher(matcher);
      this.cache = new TransformCache();
      this.error = new WeakCache();
   }
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param value this is the string representation of the value
    * @param type this is the type to convert the string value to
    * 
    * @return this returns an appropriate instanced to be used
    */
   public Object read(String value, Class type) throws Exception {
      Transform transform = lookup(type);

      if(transform == null) {
         throw new TransformException("Transform of %s not supported", type);
      }      
      return transform.read(value);
   }
   
   /**
    * This method is used to convert the provided value into an XML
    * usable format. This is used in the serialization process when
    * there is a need to convert a field value in to a string so 
    * that that value can be written as a valid XML entity.
    * 
    * @param value this is the value to be converted to a string
    * @param type this is the type to convert to a string value
    * 
    * @return this is the string representation of the given value
    */
   public String write(Object value, Class type) throws Exception {
      Transform transform = lookup(type);

      if(transform == null) {
         throw new TransformException("Transform of %s not supported", type);
      }
      return transform.write(value);
   }

   /**
    * This method is used to determine if the type specified can be
    * transformed. This will use the <code>Matcher</code> to find a
    * suitable transform, if one exists then this returns true, if
    * not then this returns false. This is used during serialization
    * to determine how to convert a field or method parameter. 
    *
    * @param type the type to determine whether its transformable
    * 
    * @return true if the type specified can be transformed by this
    */ 
   public boolean valid(Class type) throws Exception {      
      return lookup(type) != null;
   }

   /**
    * This method is used to acquire a <code>Transform</code> for 
    * the the specified type. If there is no transform for the type
    * then this will return null. Once acquired once the transform
    * is cached so that subsequent lookups will be performed faster.
    *
    * @param type the type to determine whether its transformable
    *
    * @return this will return a transform for the specified type
    */ 
   private Transform lookup(Class type) throws Exception {
      Transform transform = cache.fetch(type);            

      if(transform != null) {
         return transform;
      }
      if(error.contains(type)) {
         return null;              
      }           
      return match(type);
   }

   /**
    * This method is used to acquire a <code>Transform</code> for 
    * the the specified type. If there is no transform for the type
    * then this will return null. Once acquired once the transform
    * is cached so that subsequent lookups will be performed faster.
    *
    * @param type the type to determine whether its transformable
    *
    * @return this will return a transform for the specified type
    */ 
   private Transform match(Class type) throws Exception {
      Transform transform = matcher.match(type);
      
      if(transform != null) {
         cache.cache(type, transform);
      } else {
         error.cache(type, this);               
      }
      return transform;
   }
}
