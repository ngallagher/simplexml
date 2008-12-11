/*
 * Support.java May 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.core;

import java.beans.Introspector;

import org.simpleframework.xml.filter.Filter;
import org.simpleframework.xml.filter.PlatformFilter;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;
import org.simpleframework.xml.transform.Transformer;

/**
 * The <code>Support</code> object is used to provide support to the
 * serialization engine for processing and transforming strings. This
 * contains a <code>Transformer</code> which will create objects from
 * strings and will also reverse this process converting an object
 * to a string. This is used in the conversion of primitive types.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.Transformer
 */
class Support implements Filter {
   
   /**
    * This will perform the scanning of types are provide scanners.
    */
   private final ScannerFactory factory;
   
   /**
    * This is the factory that is used to create the scanners.
    */
   private final Instantiator creator;
   
   /**
    * This is the transformer used to transform objects to text.
    */
   private final Transformer transform;
   
   /**
    * This is the matcher used to acquire the transform objects.
    */
   private final Matcher matcher;
   
   /**
    * This is the filter used to transform the template variables.
    */
   private final Filter filter;
   
   /**
    * Constructor for the <code>Support</code> object. This will
    * create a support object with a default matcher and default
    * platform filter. This ensures it contains enough information
    * to process a template and transform basic primitive types.
    */
   public Support() {
      this(new PlatformFilter());
   }

   /**
    * Constructor for the <code>Support</code> object. This will
    * create a support object with a default matcher and the filter
    * provided. This ensures it contains enough information to 
    * process a template and transform basic primitive types.
    * 
    * @param filter this is the filter to use with this support
    */
   public Support(Filter filter) {
      this(filter, new EmptyMatcher());
   }
   
   /**
    * Constructor for the <code>Support</code> object. This will
    * create a support object with the matcher and filter provided.
    * This allows the user to override the transformations that
    * are used to convert types to strings and back again.
    * 
    * @param filter this is the filter to use with this support
    * @param matcher this is the matcher used for transformations
    */
   public Support(Filter filter, Matcher matcher) {
      this.transform = new Transformer(matcher);
      this.factory = new ScannerFactory();
      this.creator = new Instantiator();
      this.matcher = matcher;
      this.filter = filter;
   }
   
   /**
    * Replaces the text provided with some property. This method 
    * acts much like a the get method of the <code>Map</code>
    * object, in that it uses the provided text as a key to some 
    * value. However it can also be used to evaluate expressions
    * and output the result for inclusion in the generated XML.
    *
    * @param text this is the text value that is to be replaced
    * @return returns a replacement for the provided text value
    */
   public String replace(String text) {
      return filter.replace(text);
   }
   
   /**
    * This is used to create a <code>Type</code> object for the class
    * specified. This will allow instances of the specified type to
    * be instantiated and also allows reflective information to be
    * cached internally within the context object.
    * 
    * @param type this is the type that is to be instantiated
    * 
    * @return this returns a type that can be used for instantiation
    */
   public Type getType(Class type) throws Exception {
      return creator.getType(type);
   }
   
   /**
    * This is used to match a <code>Transform</code> using the type
    * specified. If no transform can be acquired then this returns
    * a null value indicating that no transform could be found.
    * 
    * @param type this is the type to acquire the transform for
    * 
    * @return returns a transform for processing the type given
    */ 
   public Transform getTransform(Class type) throws Exception {
      return matcher.match(type);
   }

   /**
    * This creates a <code>Scanner</code> object that can be used to
    * examine the fields within the XML class schema. The scanner
    * maintains information when a field from within the scanner is
    * visited, this allows the serialization and deserialization
    * process to determine if all required XML annotations are used.
    * 
    * @param type the schema class the scanner is created for
    * 
    * @return a scanner that can maintains information on the type
    */ 
   public Scanner getScanner(Class type) throws Exception {
      return factory.getInstance(type);
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
      return transform.read(value, type);
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
      return transform.write(value, type);
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
      return transform.valid(type);
   }
   
   /**
    * This is used to acquire the name of the specified type using
    * the <code>Root</code> annotation for the class. This will 
    * use either the name explicitly provided by the annotation or
    * it will use the name of the class that the annotation was
    * placed on if there is no explicit name for the root.
    * 
    * @param type this is the type to acquire the root name for
    * 
    * @return this returns the name of the type from the root
    * 
    * @throws Exception if the class contains an illegal schema
    */
   public String getName(Class type) throws Exception {
      Scanner schema = getScanner(type);
      String name = schema.getName();
      
      if(name != null) {
         return name;
      }
      return getClassName(type);
   }
   
   /**
    * This returns the name of the class specified. If there is a root
    * annotation on the type, then this is ignored in favor of the 
    * actual class name. This is typically used when the type is a
    * primitive or if there is no <code>Root</code> annotation present. 
    * 
    * @param type this is the type to acquire the root name for
    * 
    * @return this returns the name of the type from the root
    */
   private String getClassName(Class type) throws Exception {
      if(type.isArray()) {
         type = type.getComponentType();
      }      
      String name = type.getSimpleName();
      
      if(type.isPrimitive()) {
         return name;
      }
      return Introspector.decapitalize(name);
   }
   
   /**
    * This is used to determine whether the scanned class represents
    * a primitive type. A primitive type is a type that contains no
    * XML annotations and so cannot be serialized with an XML form.
    * Instead primitives a serialized using transformations.
    *
    * @param type this is the type to determine if it is primitive
    * 
    * @return this returns true if no XML annotations were found
    */
   public boolean isPrimitive(Class type) throws Exception{
      if(type == String.class) {
         return true;
      }
      if(type.isEnum()) {
         return true;
      }
      if(type.isPrimitive()) {
         return true;
      }
      return transform.valid(type);
   }
   
   /**
    * This is used to determine if the type specified is a floating
    * point type. Types that are floating point are the double and
    * float primitives as well as the java types for this primitives.
    * 
    * @param type this is the type to determine if it is a float
    * 
    * @return this returns true if the type is a floating point
    */
   public boolean isFloat(Class type) throws Exception {
      if(type == Double.class) {
         return true;
      }
      if(type == Float.class) {
         return true;
      }
      if(type == float.class) {
         return true;
      }
      if(type == double.class) {
         return true;
      }
      return false;
   }
}
