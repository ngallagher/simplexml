/*
 * ObjectFactory.java July 2006
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
import simple.xml.stream.InputNode;

/**
 * The <code>ObjectFactory</code> is the most basic factory. This will
 * basically check to see if there is an override type within the XML
 * node provided, if there is then that is instantiated, otherwise the
 * field type is instantiated. Any type created must have a default
 * no argument constructor. If the override type is an abstract class
 * or an interface then this factory throws an exception.
 *  
 * @author Niall Gallagher
 */ 
final class ObjectFactory extends Factory {

   /**
    * Constructor for the <code>ObjectFactory</code> class. This is
    * given the field class that this should create object instances
    * of. If the field type is abstract then the XML node must have
    * sufficient information for the <code>Strategy</code> object to
    * resolve the implementation class to be instantiated.
    *
    * @param source the contextual object used by the persister 
    * @param field this is the field type of the object 
    */
   public ObjectFactory(Source source, Class field) {
      super(source, field);           
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
   public Object getInstance(InputNode node) throws Exception {
      Type type = getOverride(node);
    
      if(type == null) { 
         if(!isInstantiable(field)) {
            throw new InstantiationException("Cannot instantiate %s", field);              
         }
         return getInstance(field);
      }
      return type.getInstance();
   }

   /**
    * This method will instantiate an object of the provided type. If
    * the object or constructor does not have public access then this
    * will ensure the constructor is accessible and can be used.
    * 
    * @param type this is used to ensure the object is accessible
    *
    * @return this returns an instance of the specifiec class type
    */ 
   protected Object getInstance(Class type) throws Exception {
      Constructor method = type.getDeclaredConstructor();

      if(!method.isAccessible()) {
         method.setAccessible(true);              
      }
      return method.newInstance();   
   }      
}
