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

import org.simpleframework.xml.transform.Transformer;
import org.simpleframework.xml.stream.InputNode;

/**
 * The <code>PrimitiveFactory</code> object is used to create objects
 * that are primitive types. This creates primitives and enumerated
 * types when given a string value. The string value is parsed using
 * a matched <code>Transform</code> implementation. The transform is
 * then used to convert the object instance to an from a suitable XML
 * representation. Only enumerated types are not transformed using 
 * a transform, instead they use <code>Enum.name</code>. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.transform.Transformer
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
    * must be a type that contains a <code>Transform</code> object,
    * typically this ia a <code>java.lang</code> primitive object
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
    * provided string. Typically this string is transformed in to the
    * type using a <code>Transform</code> object. However, if the
    * values is an enumeration then its value is created using the
    * <code>Enum.valueOf</code> method. Also string values typically
    * do not require conversion of any form and are just returned.
    * 
    * @param text this is the value to be transformed to an object
    * 
    * @return this returns an instance of the field type
    */         
   public Object getInstance(String text) throws Exception {
      if(field == String.class) {
         return text;              
      }    
      if(field.isEnum()) {
         return Enum.valueOf(field, text);              
      }           
      return transform.read(text, field);
   }
   
   /**
    * This is used to acquire a text value for the specified object.
    * This will convert the object to a string using the transformer
    * so that it can be deserialized from the generate XML document.
    * However if the type is an <code>Enum</code> type then the text
    * value is taken from <code>Enum.name</code> so it can later be
    * deserialized easily using the enumeration class and name.
    * 
    * @param source this is the object instance to get the value of
    * 
    * @return this returns a string representation of the object
    * 
    * @throws Exception if the object could not be transformed
    */
   public String getText(Object source) throws Exception {
      Class type = source.getClass();      

      if(type.isEnum()) {
         Enum value = (Enum)source;
         return value.name();
      }
      return transform.write(source, type);     
   }
}
