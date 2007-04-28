/*
 * ReferenceType.java May 2006
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

package simple.xml.graph;

import simple.xml.load.Type;

/**
 * The <code>ReferenceType</code> object represents an object that 
 * is used to provide a reference to an already instantiated value.
 * This is what is used if there is a cycle in the object graph. 
 * The <code>getInstance</code> method of this object will simply
 * return the object instance that was previously created.
 * 
 * @author Niall Gallagher
 */
final class ReferenceType implements Type {
   
   /**
    * This is the object instance that has already be created.
    */
   private Object value;
   
   /**
    * This is the type of the object that this references.
    */
   private Class type;
   
   /**
    * Constructor for the <code>ReferenceType</code> object. This 
    * is used to create a type that will produce the specified 
    * value when the <code>getInstance</code> methos is invoked.
    * 
    * @param value the value for the reference this represents
    */
   public ReferenceType(Object value) {
      this.type = value.getClass();
      this.value = value;      
   }
   
   /**
    * This is used to acquire a reference to the instance that is
    * taken from the created object graph. This enables any cycles
    * in the graph to be reestablished from the persisted XML.
    * 
    * @return this returns a reference to the created instance
    */
   public Object getInstance() throws Exception {     
      return value;      
   }
   
   /**
    * This is used to acquire a reference to the instance that is
    * taken from the created object graph. This enables any cycles
    * in the graph to be reestablished from the persisted XML.
    * 
    * @param convert this is ignored as this is a reference type
    * 
    * @return this returns a reference to the created instance
    */
   public Object getInstance(Class convert) throws Exception {     
      return value;      
   }
   
   /**
    * This returns the type for the object that this references.
    * This will basically return the <code>getClass</code> class
    * from the referenced instance. This is used to ensure that
    * the type this represents is compatible to the object field.
    * 
    * @return this returns the type for the referenced object
    */
   public Class getType() {
      return type;
   }

   /**
    * This always returns true for this object. This indicates to
    * the deserialization process that there should be not further
    * deserialization of the object from the XML source stream.
    * 
    * @return because this is a reference this is always true 
    */
   public boolean isReference() {      
      return true;
   }
}
