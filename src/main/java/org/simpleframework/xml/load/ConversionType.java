/*
 * CoversionType.java April 2007
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

package org.simpleframework.xml.load;

/**
 * The <code>ConversionType</code> object is used to convert the type
 * of an instance. This is typically used to promote a type either
 * because it is not instantiable or because another type is required.
 * This is used by the <code>CollectionFactory</code> to convert the
 * type of a collection field from an abstract type to a instantiable
 * type. This is used to simplify strategy implementations. 
 *
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.CollectionFactory
 */
class ConversionType implements Type {
   
   /**
    * This is the new class that is used for the type conversion. 
    */
   private Class convert;
   
   /**
    * This is the type object that will be wrapped by this type.
    */
   private Type type;
   
   /**
    * This is used to specify the creation of a conversion type that
    * can be used for creating an instance with a class other than 
    * the default class specified by the <code>Type</code> object.
    * 
    * @param type this is the type used to create the instance
    * @param convert this is the class the type is converted to
    */
   public ConversionType(Type type, Class convert) {
      this.convert = convert;      
      this.type = type;
   }
   
   /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this.
    * 
    * @return an instance of the type this object represents
    */
   public Object getInstance() throws Exception {
      return getInstance(convert);
   }
   
   /**
    * This method will instantiate an object of the provided type. If
    * the object or constructor does not have public access then this
    * will ensure the constructor is accessible and can be used.
    * 
    * @param convert this is used to ensure the object is accessible
    *
    * @return this returns an instance of the specifiec class type
    */ 
   public Object getInstance(Class convert) throws Exception {  
      return type.getInstance(convert);
   }
   
   /**
    * This method is used acquire the value from the type and if
    * possible replace the value for the type. If the value can
    * not be replaced then an exception should be thrown. This 
    * is used to allow primitives to be inserted into a graph.
    * 
    * @param value this is the value to insert as the type
    * 
    * @return an instance of the type this object represents
    */
   public Object getInstance(Object value) throws Exception {
      return type.getInstance(value);
   }
   
   /**
    * This is the type of the object instance that will be created
    * by the <code>getInstance</code> method. This allows the 
    * deserialization process to perform checks against the field.
    * 
    * @return the type of the object that will be instantiated
    */
   public Class getType() {
      return convert;
   }
   
   /**
    * This will return true if the <code>Type</code> object provided
    * is a reference type. Typically a reference type refers to a 
    * type that is substituted during the deserialization process 
    * and so constitutes an object that does not need initialization.
    * 
    * @return this returns true if the type is a reference type
    */
   public boolean isReference() {
      return type.isReference();
   }   
}
