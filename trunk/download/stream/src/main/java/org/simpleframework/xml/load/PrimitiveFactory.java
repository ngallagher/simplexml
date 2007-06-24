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
}
