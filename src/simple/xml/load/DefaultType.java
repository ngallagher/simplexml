/*
 * DefaultType.java January 2007
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

package simple.xml.load;

import java.lang.reflect.Constructor;

/**
 * The <code>DefaultType</code> is an implementation of the type that
 * is used to instantiate an object using its default no argument
 * constructor. This will simply ensure that the constructor is an
 * accessible method before invoking the types new instance method.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.load.DefaultStrategy
 */
final class DefaultType implements Type {

   /**
    * This is the type that this object is used to represent.
    */
   private Class type;

   /**
    * Constructor for the <code>DefaultType</code> object. This is
    * used to create a type object that can be used to instantiate
    * and object with that objects default no argument constructor.
    * 
    * @param type this is the type of object that is created
    */
   public DefaultType(Class type) {
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
      return getInstance(type);
   }
   
   /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this.
    * 
    * @param type this is the type of the object to construct
    * 
    * @return an instance of the type this object represents
    */
   private Object getInstance(Class type) throws Exception {
      Constructor method = type.getDeclaredConstructor();

      if(!method.isAccessible()) {
         method.setAccessible(true);              
      }
      return method.newInstance();   
   }  
   
   /**
    * This is the type of the object instance that will be created
    * by the <code>getInstance</code> method. This allows the 
    * deserialization process to perform checks against the field.
    * 
    * @return the type of the object that will be instantiated
    */
   public Class getType() {
      return type;
   }   
}
