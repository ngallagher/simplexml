/*
 * CreateType.java January 2007
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

package simple.xml.graph;

import simple.xml.load.Type;
import java.util.Map;

/**
 * The <code>CreateType</code> object is used to represent a type 
 * that has not yet been created and needs to be added to the object
 * graph. This is given a map that contains each node in the graph
 * keyed via a unique identifier. When an instance is created with
 * this <code>Type</code> implementation it is inserted into the
 * graph using a specified key value, so it can be reused later.
 * 
 * @author Niall Gallagher
 */
final class CreateType implements Type {
   
   /**
    * This is used to create an instance of the specified type.
    */
   private Type type;
   
   /**
    * This is the unique key that is used to store the value.
    */
   private String key;
   
   /**
    * This is used to store each instance in the object graph.
    */
   private Map map;
   
   /**
    * Constructor for the <code>CreateType</code> object. This is used
    * to create a type that can be used to establish an instance and
    * insert that instance into a map of nodes with an unique key.
    * 
    * @param type this is the type of the instance to be created
    * @param map this contains each instance mapped with a key
    * @param key this is the unique key representing this instance
    */
   public CreateType(Type type, Map map, String key) {
      this.type = type;
      this.map = map;
      this.key = key;
   }
   
   /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this. Once
    * created the instance is inserted into the map of values.
    * 
    * @return an instance of the type this object represents
    */
   public Object getInstance() throws Exception {      
      Object value = type.getInstance();
      
      if(value != null) {
         map.put(key, value);
      }
      return value;
   }
   
   /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this. Once
    * created the instance is inserted into the map of values.
    * 
    * @param convert this is the type to convert the instance to
    * 
    * @return an instance of the type this object represents
    */
   public Object getInstance(Class convert) throws Exception {      
      Object value = type.getInstance(convert);
      
      if(value != null) {
         map.put(key, value);
      }
      return value;
   }
   
   /**
    * This is the type of the object instance that will be created
    * by the <code>getInstance</code> method. This allows the 
    * deserialization process to perform checks against the field.
    * 
    * @return the type of the object that will be instantiated
    */
   public Class getType() {
      return type.getType();
   }
   
   /**
    * This method always returns false for the default type. This
    * is because by default all elements encountered within the 
    * XML are to be deserialized based on there XML annotations.
    * 
    * @return this returns false for each type encountered     
    */  
   public boolean isReference() {
      return false;
   }
}
