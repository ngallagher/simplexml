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

package org.simpleframework.xml.graph;

import org.simpleframework.xml.load.ElementException;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.Node;
import org.simpleframework.xml.load.Type;
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
 * @see org.simpleframework.xml.graph.WriteGraph
 */
class ReadGraph extends HashMap {
   
   /**
    * This is used to represent the length of array object values.
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
    * Constructor for the <code>ReadGraph</code> object. This is used
    * to create graphs that are used for reading objects from the XML
    * document. The specified strategy is used to acquire the names
    * of the special attributes used during the serialization.
    * 
    * @param contract this is the name scheme used by the strategy 
    */
   public ReadGraph(Contract contract) {
      this.refer = contract.getReference();
      this.mark = contract.getIdentity();
      this.length = contract.getLength();
      this.label = contract.getLabel();
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
      Class type = field;
      
      if(field.isArray()) {
         type = field.getComponentType();
      }
      if(entry != null) {      
         String name = entry.getValue();
         type = Class.forName(name);
      }  
      return getInstance(field, type, node); 
   }
   
   /**
    * This is used to recover the object references from the document
    * using the special attributes specified. This allows the element
    * specified by the <code>NodeMap</code> to be used to discover
    * exactly which node in the object graph the element represents.
    * 
    * @param field the type of the field or method in the instance
    * @param real this is the overridden type from the XML element
    * @param node this is the XML element to be deserialized
    * 
    * @return this is used to return the type to acquire the value
    */
   private Type getInstance(Class field, Class real, NodeMap node) throws Exception {      
      Node entry = node.remove(mark);
      
      if(entry == null) {
         return getReference(field, real, node);
      }      
      String key = entry.getValue();
      
      if(containsKey(key)) {
         throw new CycleException("Element '%s' already exists", key);
      }
      return getType(field, real, node, key);
   }
   
   /**
    * This is used to recover the object references from the document
    * using the special attributes specified. This allows the element
    * specified by the <code>NodeMap</code> to be used to discover
    * exactly which node in the object graph the element represents.
    * 
    * @param field the type of the field or method in the instance
    * @param real this is the overridden type from the XML element
    * @param node this is the XML element to be deserialized    
    * 
    * @return this is used to return the type to acquire the value
    */ 
   private Type getReference(Class field, Class real, NodeMap node) throws Exception {
      Node entry = node.remove(refer);
      
      if(entry == null) {
         return getType(field, real, node);
      }
      String key = entry.getValue();
      Object value = get(key); 
         
      if(value == null) {        
         throw new CycleException("Invalid reference '%s' found", key);
      }
      return new Reference(value, real);
   }
   
   /**
    * This is used to acquire the <code>Type</code> which can be used 
    * to represent the deserialized value. The type create cab be
    * added to the graph of created instances if the XML element has
    * an identification attribute, this allows cycles to be completed.
    *
    * @param field the type of the field or method in the instance
    * @param real this is the overridden type from the XML element
    * @param node this is the XML element to be deserialized    
    * 
    * @return this is used to return the type to acquire the value
    */
   private Type getType(Class field, Class real, NodeMap node) throws Exception {      
      if(field.isArray()) {
         return getArray(field, real, node);
      }
      return new Instance(real);
   }
   
   /**
    * This is used to acquire the <code>Type</code> which can be used 
    * to represent the deserialized value. The type create cab be
    * added to the graph of created instances if the XML element has
    * an identification attribute, this allows cycles to be completed.
    *
    * @param field the type of the field or method in the instance
    * @param real this is the overridden type from the XML element
    * @param node this is the XML element to be deserialized
    * @param key the key the instance is known as in the graph    
    * 
    * @return this is used to return the type to acquire the value
    */
   private Type getType(Class field, Class real, NodeMap node, String key) throws Exception {
      Type type = getType(field, real, node);
      
      if(key != null) {
         return new Allocate(type, this, key);
      }
      return type;      
   }
   
   /**
    * This is used to acquire the <code>Type</code> which can be used 
    * to represent the deserialized value. The type create cab be
    * added to the graph of created instances if the XML element has
    * an identification attribute, this allows cycles to be completed.
    *
    * @param field the type of the field or method in the instance
    * @param real this is the overridden type from the XML element
    * @param node this is the XML element to be deserialized  
    * 
    * @return this is used to return the type to acquire the value
    */  
   private Type getArray(Class field, Class real, NodeMap node) throws Exception {
      Node entry = node.remove(length);
      int size = 0;
      
      if(entry != null) {
         String value = entry.getValue();
         size = Integer.parseInt(value);
      }      
      return new ArrayInstance(real, size);      
   }
}