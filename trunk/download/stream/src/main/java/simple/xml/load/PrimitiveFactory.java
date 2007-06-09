/*
 * PrimitiveFactory.java July 2006
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

package simple.xml.load;

import java.lang.reflect.Constructor;

/**
 * The <code>PrimitiveFactory</code> object is used to create objects
 * that are primitive types. This creates primitives and enumerated
 * types when given a string value. The string value is parsed using
 * the string constructors from the <code>java.lang</code> primitive
 * types like <code>Integer</code>. The field type is used to 
 * determine the resulting object instance that will be assigned as
 * the field value. 
 * 
 * @author Niall Gallagher
 */ 
class PrimitiveFactory extends Factory {
   
   /**
    * Caches the constructors used to convert primitive types.
    * 
    * @see simple.xml.load.Primitive
    */
   private static ConstructorCache cache;

   static {
      cache = new ConstructorCache();           
   }   
        
   /**
    * Constructor for the <code>PrimitiveFactory</code> object. This
    * is provided the field type that is to be instantiated. This
    * must be either a <code>java.lang</code> primitive type object
    * or one of the primitive types such as <code>int</code>. Also
    * this can be given a class for an enumerated type. 
    * 
    * @param field this is the field type to be instantiated
    */
   public PrimitiveFactory(Source root, Class field) {
      super(root, field);           
   }
        
   /**
    * This will instantiate an object of the field type using the
    * provided string. Typically this string is parsed into the type
    * provided. This is done by reflectively creating one of the 
    * <code>java.lang</code> primitive types using the provided
    * string. If the type is an enumerated type then it is created
    * using the <code>Enum.valueOf</code> method.
    * 
    * @param text this is the value to be converted
    * 
    * @return this returns an instance of the field type
    */         
   public Object getInstance(String text) throws Exception {
      if(field == String.class) {
         return text;              
      }    
      if(field.isEnum()) {
         return getEnumeration(text);              
      }           
      return getPrimitive(text);
   }  
   
   /**
    * This will construct the enumerated type from the provided text
    * value. This uses <code>Enum.valueOf</code> to determine the 
    * enumerated type value that is to be instantiated.
    * 
    * @param text this is the enumerated type value to be created
    * 
    * @return this returns an instance of the enumerated type 
    * 
    * @throws Exception thrown if there was a problem instantiating
    */
   private Object getEnumeration(String text) throws Exception {
      return Enum.valueOf(field, text);
   }
  
   /**
    * This will construct the primitive type from the provided text
    * value. This reflectively constructs a primitive object using a
    * single argument constructor that takes a string. For instance
    * for the type <code>int.class</code> the value is created using
    * the <code>Integer(String)</code> constructor.
    * 
    * @param text this is the text to be converted to a primitive
    * 
    * @return returns a primitive object representing the value
    * 
    * @throws Exception if the text value is not parsable
    */
   private Object getPrimitive(String text) throws Exception {
      Constructor method = cache.get(field);
      
      if(method != null) {
         return method.newInstance(text);              
      }
      Class type = getConversion(field);
      
      if(type == Character.class) {
         return getCharacter(text);
      }
      return getPrimitive(text, type);
   }
   
   /**
    * This will construct the primitive type from the provided text
    * value. This reflectively constructs a primitive object using a
    * single argument constructor that takes a string. For instance
    * for the type <code>int.class</code> the value is created using
    * the <code>Integer(String)</code> constructor.
    * 
    * @param text this is the text to be converted to a primitive
    * @param type this is the converted type for the primitive
    * 
    * @return returns a primitive object representing the value
    * 
    * @throws Exception if the text value is not parsable
    */
   private Object getPrimitive(String text, Class type) throws Exception {
      Constructor method = getConstructor(type);

      if(method != null) {
         cache.cache(field, method);              
      }
      return method.newInstance(text);
   }
   
   /**
    * This is used to acquire a character from the specified text 
    * value. The character is a special entity as it does not have
    * a constructor which will accept a string value for creation.
    * As a result this method is required to extract the character.
    *  
    * @param text the text value that represents the character 
    * 
    * @return this returns the autoboxed value for the character
    * 
    * @throws Exception if the length of the string is not one
    */
   private Object getCharacter(String text) throws Exception {
      if(text.length() != 1) {
         throw new InstantiationException("Cannot convert '%s' to a character", text);
      }
      return text.charAt(0);
   }

   /**
    * Creates a constructor using the provided type. This will look
    * for a single argument constructor that takes a string. The type
    * of object that is created is determined using the field type.
    * 
    * @param type the type that can be used to get the constructor
    * 
    * @return this returns a constructor for a primitive type
    * 
    * @throws Exception if a suitable constructor was not found
    */
   private Constructor getConstructor(Class type) throws Exception {
      return type.getConstructor(String.class);
   }

   /**
    * This performs a conversion from primitive type such as the
    * <code>int.class</code> or <code>float.class</code> type to the
    * suitable primitive object types. If no mapping can be made then
    * this returns the type provided.
    * 
    * @param type this is the type to convert to a primitive type
    * 
    * @return this returns the primitive object type to be used
    */
   public Class getConversion(Class type) {
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
