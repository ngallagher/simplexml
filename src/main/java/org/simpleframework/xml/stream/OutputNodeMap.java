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
class OutputNodeMap extends LinkedHashMap<String, OutputNode> implements NodeMap<OutputNode> {

   /**
    * This is the source node that this node map belongs to.
    */         
   private OutputNode source;
        
   /**
    * Constructor for the <code>OutputNodeMap</code> object. This is
    * used to create a node map that is used to create and collect
    * nodes, which will be used as attributes for an output element.
    */         
   public OutputNodeMap(OutputNode source) {
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
    * 
    * @return this is the node that has been added to the map
    */    
   public OutputNode put(String name, String value) {
      OutputNode node = new OutputAttribute(source, name, value);
      
      if(source != null) {
         put(name, node);
      }
      return node;
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
   public OutputNode remove(String name) {
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
   public OutputNode get(String name) {
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
}
