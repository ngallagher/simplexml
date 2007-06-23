/*
 * WriteGraph.java April 2007
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

package org.simpleframework.xml.graph;

import org.simpleframework.xml.stream.NodeMap;
import java.util.IdentityHashMap;
import java.lang.reflect.Array;

/**
 * The <code>WriteGraph</code> object is used to build the graph that
 * is used to represent the serialized object and its references. The
 * graph is stored in an <code>IdentityHashMap</code> which will 
 * store the objects in such a way that this graph object can tell if
 * it has allready been written to the XML document. If an object has
 * already been written to the XML document an reference attribute
 * is added to the element representing the object and serialization
 * of that object is complete, that is, no more elements are written.
 * <p>
 * The attribute values written by this are unique strings, which 
 * allows the deserialization process to identify object references
 * easily. By default these references are incrementing integers 
 * however for deserialization they can be any unique string value.
 * 
 * @author Niall Gallagher
 */
class WriteGraph extends IdentityHashMap<Object, String> {
   
   /**
    * This is used to specify the length of array instances.
    */
   private String length;
   
   /**
    * This is the label used to mark the type of an object.
    */
   private String label;
   
   /**
    * This is the attribute used to mark the identity of an object.
    */
   private String mark;
   
   /**
    * Thsi is the attribute used to refer to an existing instance.
    */
   private String refer;
   
   /**
    * Constructor for the <code>WriteGraph</code> object. This is
    * used to build the graph used for writing objects to the XML 
    * document. The specified strategy is used to acquire the names
    * of the special attributes used during the serialization.
    * 
    * @param contract this is the name scheme used by the strategy 
    */
   public WriteGraph(Contract contract) {
      this.refer = contract.getReference();
      this.mark = contract.getIdentity();
      this.length = contract.getLength();
      this.label = contract.getLabel();
   }
   
   /**
    * This is used to write the XML element attributes representing
    * the serialized object instance. If the object has already been
    * serialized to the XML document then a reference attribute is
    * inserted and this returns true, if not, then this will write
    * a unique identity marker attribute and return false.
    * 
    * @param field this is the type of the object to be serialized
    * @param value this is the instance that is to be serialized    
    * @param node this is the node that contains the attributes
    * 
    * @return returns true if the element has been fully written
    */
   public boolean setElement(Class field, Object value, NodeMap node){
      Class type = value.getClass();
      Class real = type;
      
      if(type.isArray()) {
         real = setArray(type, value, node);
      }
      if(type != field) {
         node.put(label, real.getName());
      }       
      return setReference(value, node);
   }
   
   /**
    * This is used to write the XML element attributes representing
    * the serialized object instance. If the object has already been
    * serialized to the XML document then a reference attribute is
    * inserted and this returns true, if not, then this will write
    * a unique identity marker attribute and return false.
    *
    * @param value this is the instance that is to be serialized    
    * @param node this is the node that contains the attributes
    * 
    * @return returns true if the element has been fully written
    */   
   private boolean setReference(Object value, NodeMap node) {
      String name = get(value);
      
      if(name != null) {
         node.put(refer, name);
         return true;
      } 
      String unique = getKey();      
      
      node.put(mark, unique);
      put(value, unique);
      
      return false;     
   }
   
   /**
    * This is used to add a length attribute to the element due to
    * the fact that the serialized value is an array. The length
    * of the array is acquired and inserted in to the attributes.
    * 
    * @param field this is the field type for the array to set
    * @param value this is the actual value for the array to set
    * @param node this is the map of attributes for the element
    * 
    * @return returns thr array component type that is set
    */
   private Class setArray(Class field, Object value, NodeMap node){
      int size = Array.getLength(value);
      
      if(!containsKey(value)) {       
         node.put(length, String.valueOf(size));
      }
      return field.getComponentType();
   }
   
   /**
    * This is used to generate a unique key, which is used to mark 
    * the serialized object. This currently returns an incrementing 
    * integer value as a string, but it can be any unique value.
    * 
    * @return a unique key used to identify an object instance
    */
   private String getKey() {      
      return String.valueOf(size());      
   }
}
