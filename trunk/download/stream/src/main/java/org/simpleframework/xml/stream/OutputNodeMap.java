/*
 * OutputNodeMap.java July 2006
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

package org.simpleframework.xml.stream;

import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * The <code>OutputNodeMap</code> is used to collect attribute nodes
 * for an output node. This will create a generic node to add to the
 * map. The nodes created will be used by the output node to write
 * attributes for an element.
 * 
 * @author Niall Gallagher
 */ 
class OutputNodeMap extends LinkedHashMap<String, Node> implements NodeMap {

   /**
    * This is the source node that this node map belongs to.
    */         
   private Node source;
        
   /**
    * Constructor for the <code>OutputNodeMap</code> object. This is
    * used to create a node map that is used to create and collect
    * nodes, which will be used as attributes for an output element.
    */         
   public OutputNodeMap(Node source) {
      this.source = source;           
   }
   
   /**
    * This is used to get the name of the element that owns the
    * nodes for the specified map. This can be used to determine
    * which element the node map belongs to.
    * 
    * @return this returns the name of the owning element
    */  
   public String getName() {
      return source.getName();           
   }   

   /**
    * This is used to add a new <code>Node</code> to the map. The
    * node that is created is a simple name value pair. Once the
    * node is created it can be retrieved by its given name.
    *
    * @param name this is the name of the node to be created
    * @param value this is the value to be given to the node
    */    
   public void put(String name, String value) {
      put(name, new EntryNode(name, value));           
   }
   
   /**
    * This is used to remove the <code>Node</code> mapped to the
    * given name.  This returns a name value pair that represents
    * an attribute. If no node is mapped to the specified name 
    * then this method will a return null value.
    *
    * @param name this is the name of the node to remove
    * 
    * @return this will return the node mapped to the given name
    */    
   public Node remove(String name) {
      return super.remove(name);
   }

   /**
    * This is used to acquire the <code>Node</code> mapped to the
    * given name. This returns a name value pair that represents
    * an element. If no node is mapped to the specified name then 
    * this method will return a null value.
    *
    * @param name this is the name of the node to retrieve
    * 
    * @return this will return the node mapped to the given name
    */   
   public Node get(String name) {
      return super.get(name);
   }

   /**
    * This returns an iterator for the names of all the nodes in
    * this <code>OutputNodeMap</code>. This allows the names to be 
    * iterated within a for each loop in order to extract nodes.
    *
    * @return this returns the names of the nodes in the map
    */    
   public Iterator<String> iterator() {
      return keySet().iterator();           
   }

   /**
    * The <code>EntryNode</code> object is used to represent a node
    * added to the output node map. It represents a simple name 
    * value pair that is used as an attribute by an output element.
    *
    * @see org.simpleframework.xml.stream.Node
    */ 
   private class EntryNode implements Node {

      /**
       * Represents the name of this node object instance.
       */            
      private String name;

      /**
       * Represents the value of this node object instance.
       */  
      private String value;
           
      /**
       * Constructor for the <code>EntryNode</code> object. This is
       * used to create a simple name value pair attribute holder.
       *
       * @param name this is the name that is used for the node
       * @param value this is the value used for the node
       */ 
      public EntryNode(String name, String value) {
         this.value = value;              
         this.name = name;              
      }         
      
      /**
       * Returns the value for the node that this represents. This   
       * is a modifiable property for the node and can be changed.
       *    
       * @return the name of the value for this node instance
       * 
       * @throws Exception if there is a problem getting the value
       */      
      public String getValue() {
         return value;              
      }

      /**
       * Returns the name of the node that this represents. This is
       * an immutable property and should not change for any node.  
       *  
       * @return returns the name of the node that this represents
       */          
      public String getName() {
         return name;              
      }
      
      /**
       * This is used to acquire the <code>Node</code> that is the
       * parent of this node. This will return the node that is
       * the direct parent of this node and allows for siblings to
       * make use of nodes with their parents if required.  
       *   
       * @return this returns the parent node for this node
       */
      public Node getParent() {
    	  return source;
      }      
   }
}
