/*
 * ReadGraph.java April 2007
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

import simple.xml.stream.NodeMap;
import simple.xml.stream.Node;
import simple.xml.load.ElementException;
import simple.xml.load.Type;
import java.util.HashMap;

/**
 * The <code>ReadGraph</code> object is used to build a graph of the
 * objects that have been deserialized from the XML document. This is
 * required so that cycles in the object graph can be recreated such
 * that the deserialized object is an exact duplicate of the object
 * that was serialized. Objects are stored in the graph using unique
 * keys, which for this implementation are unique strings.
 * 
 * @author Niall Gallagher
 * 
 * @see simple.xml.graph.WriteGraph
 */
final class ReadGraph extends HashMap {
   
   /**
    * This is the attribute used to specify the array length.
    */
   private static final String SIZE = "size";   
   
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
    * Constructor for the <code>ReadGraph</code> object. This is used
    * to create graphs that are used for reading objects from the XML
    * document. The specified strings represent special attributes
    * that are inserted in to the XML during the serialization.
    * 
    * @param mark this is used to mark the identity of an object
    * @param refer this is used to refer to an existing instance
    * @param label this is used to represent the objects type
    */
   public ReadGraph(String mark, String refer, String label) {
      this.label = label;
      this.refer = refer;
      this.mark = mark;
   }
   
   /**
    * This is used to recover the object references from the document
    * using the special attributes specified. This allows the element
    * specified by the <code>NodeMap</code> to be used to discover
    * exactly which node in the object graph the element represents.
    * 
    * @param field the type of the field or method in the instance
    * @param node this is the XML element to be deserialized
    * 
    * @return this is used to return the type to acquire the value
    */
   public Type getElement(Class field, NodeMap node) throws Exception {
      Node entry = node.remove(label);
         
      if(entry != null) {
         String name = entry.getValue();
         field = Class.forName(name);
      }         
      return getInstance(field, node); 
   }
   
   /**
    * This is used to recover the object references from the document
    * using the special attributes specified. This allows the element
    * specified by the <code>NodeMap</code> to be used to discover
    * exactly which node in the object graph the element represents.
    * 
    * @param field the type of the field or method in the instance
    * @param node this is the XML element to be deserialized
    * 
    * @return this is used to return the type to acquire the value
    */
   private Type getInstance(Class field, NodeMap node) throws Exception {      
      Node entry = node.remove(mark);
      
      if(entry == null) {
         return getReference(field, node);
      }      
      String key = entry.getValue();
      
      if(containsKey(key)) {
         throw new CycleException("Element '%s' already exists", key);
      }
      return getType(field, node, key);
   }
   
   /**
    * This is used to recover the object references from the document
    * using the special attributes specified. This allows the element
    * specified by the <code>NodeMap</code> to be used to discover
    * exactly which node in the object graph the element represents.
    * 
    * @param field the type of the field or method in the instance
    * @param node this is the XML element to be deserialized
    * 
    * @return this is used to return the type to acquire the value
    */ 
   private Type getReference(Class field, NodeMap node) throws Exception {
      Node entry = node.remove(refer);
      
      if(entry == null) {
         return getType(field, node);
      }
      String key = entry.getValue();
      Object value = get(key); 
         
      if(value == null) {        
         throw new CycleException("Invalid reference '%s' found", key);
      }
      return new ReferenceType(value);
   }
   
   private Type getType(Class field, NodeMap node) throws Exception {
      String size = SIZE;
      
      if(field.isArray()) {
         return getArray(field, node, size);
      }
      return new ClassType(field);
   }
   
   private Type getType(Class field, NodeMap node, String key) throws Exception {
      Type type = getType(field, node);
      
      if(key != null) {
         return new NewType(type, this, key);
      }
      return type;      
   }
   
   private Type getArray(Class field, NodeMap node, String size) throws Exception {
      Node entry = node.remove(size);
      
      if(entry == null) {
         throw new ElementException("Array %s requires a %s attribute", field, size);
      }
      String value = entry.getValue();
      int length = Integer.parseInt(value);
      
      return new ArrayType(field, length);      
   }
}