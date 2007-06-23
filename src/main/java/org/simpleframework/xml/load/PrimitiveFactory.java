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

package org.simpleframework.xml.load;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.transform.Transformer;

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
    * Caches the constructors used to transform primitive types.
    * 
    * @see org.simpleframework.xml.load.Primitive
    */
   private static Transformer transform;

   static {
      transform = new Transformer();           
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
    * This method will instantiate an object of the field type, or if
    * the <code>Strategy</code> object can resolve a class from the
    * XML element then this is used instead. If the resulting type is
    * abstract or an interface then this method throws an exception.
    * 
    * @param node this is the node to check for the override
    * 
    * @return this returns an instance of the resulting type
    */         
   public Type getInstance(InputNode node) throws Exception {
      Type type = getOverride(node);
    
      if(type == null) { 
         return new ClassType(field);         
      }
      return type;      
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
      return transform.read(text, field);
   }
   
   /**
    * This is used to acquire a text value for the specified object.
    * This will convert the object to a string using the transformer
    * so that it can be deserialized from the generate XML document.
    * 
    * @param value this is the object instance to get the value of
    * @param type this is the type of the object instance
    * 
    * @return this returns a string representation of the object
    * 
    * @throws Exception if the object could not be transformed
    */
   public String getValue(Object value, Class type) throws Exception {
      return transform.write(value, type);
   }
}
