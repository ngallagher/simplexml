/*
 * InputNodeMap.java July 2006
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

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The <code>InputNodeMap</code> object represents a map to contain
 * attributes used by an input node. This can be used as an empty
 * node map, it can be used to extract its values from a start
 * element. This creates <code>InputAttribute</code> objects for 
 * each node added to the map, these can then be used by an element
 * input node to represent attributes as input nodes.
 *
 * @author Niall Gallagher
 */ 
class InputNodeMap extends HashMap<String, InputNode> implements NodeMap {

   /**
    * This is the source node that this node map belongs to.
    */          
   private InputNode source;        
   
   /**
    * Constructor for the <code>InputNodeMap</code> object. This
    * is used to create an empty input node map, which will create
    * <code>InputAttribute</code> object for each inserted node.
    *
    * @param source this is the node this node map belongs to
    */ 
   protected InputNodeMap(InputNode source) {
      this.source = source;            
   }        

   /**
    * Constructor for the <code>InputNodeMap</code> object. This
    * is used to create an input node map, which will be populated
    * with the attributes from the <code>StartElement</code> that
    * is specified.
    *
    * @param source this is the node this node map belongs to
    * @param element the element to populate the node map with
    */ 
   public InputNodeMap(InputNode source, StartElement element) {
      this.source = source;           
      this.put(element);   
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
    * This is used to insert all attributes belonging to the start
    * element to the map. All attributes acquired from the element
    * are converted into <code>InputAttribute</code> objects so 
    * that they can be used as input nodes by an input node.
    *
    * @param element the element to acquire attributes from
    */ 
   private void put(StartElement element) {
      Iterator list = element.getAttributes();
      
      while(list.hasNext()) {
         Object event = list.next();
         
         if(event instanceof Attribute) {
            put((Attribute)event);                 
         }              
      }           
   }

   /**
    * This is used to insert an <code>Attribute</code> node to 
    * the map. The inserted attribute is converted into an input
    * node by wrapping it in an <code>InputAttribute</code> object.
    * Once the node is inserted it can be acquired by its name.
    *
    * @param event this is the attribute to add to this node map
    */     
   private void put(Attribute event) {
      put(new InputAttribute(source, event));           
   }

   /**
    * This is used to add a new <code>InputAttribute</code> node to
    * the map. The created node can be used by an input node to
    * to represent the attribute as another input node. Once the 
    * node is created it can be acquired using the specified name.
    *
    * @param name this is the name of the node to be created
    * @param value this is the value to be given to the node
    */    
   public void put(String name, String value) {
      put(new InputAttribute(source, name, value));
   }


   /**
    * This is used to insert an <code>InputAttribute</code> node 
    * to the map. The inserted node can be used by an input node to
    * to represent the attribute as another input node. Once the 
    * node is inserted it can be acquired using the attribute name.
    *
    * @param input this is the attribute to add to the node map
    */    
   private void put(InputAttribute input) {
      put(input.getName(), input);
   }
   
   /**
    * This is used to remove the <code>Node</code> mapped to the
    * given name.  This returns a name value pair that represents
    * an attribute. If no node is mapped to the specified name 
    * then this method will return a null value.
    *
    * @param name this is the name of the node to remove
    * 
    * @return this will return the node mapped to the given name
    */    
   public InputNode remove(String name) {
      return super.remove(name);
   }
   
   /**
    * This is used to acquire the <code>Node</code> mapped to the
    * given name. This returns a name value pair that represents
    * an attribute. If no node is mapped to the specified name 
    * then this method will return a null value.
    *
    * @param name this is the name of the node to retrieve
    * 
    * @return this will return the node mapped to the given name
    */       
   public InputNode get(String name) {
      return super.get(name);
   }

   /**
    * This returns an iterator for the names of all the nodes in
    * this <code>NodeMap</code>. This allows the names to be 
    * iterated within a for each loop in order to extract nodes.
    *
    * @return this returns the names of the nodes in the map
    */    
   public Iterator<String> iterator() {
      return keySet().iterator();
   }
}
