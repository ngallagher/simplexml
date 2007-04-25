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

package simple.xml.graph;

import java.util.IdentityHashMap;
import simple.xml.stream.NodeMap;

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
final class WriteGraph extends IdentityHashMap<Object, String> {
   
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
    * document. The specified strings represent attributes that 
    * are inserted in to the XML during the serialization.
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing instance
    * @param label this is used to represent the objects type
    */
   public WriteGraph(String mark, String refer, String label) {
      this.label = label;
      this.refer = refer;
      this.mark = mark;
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
         real = type.getComponentType();
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
         put(refer, name);
         return true;
      } 
      String unique = getKey();      
      
      node.put(mark, unique);
      put(value, unique);
      
      return false;     
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
