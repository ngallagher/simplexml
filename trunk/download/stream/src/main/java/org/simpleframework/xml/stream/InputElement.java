/*
 * InputElement.java July 2006
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

/**
 * The <code>InputElement</code> represents a self contained element
 * that will allow access to its child elements. If the next element
 * read from the <code>NodeReader</code> is not a child then this
 * will return null. The input element node also allows the attribute
 * values associated with the node to be accessed.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.stream.NodeReader
 */ 
class InputElement implements InputNode {
   
   /**
    * This is the XML element that this node provides access to.
    */         
   private StartElement element;
   
   /**
    * This contains all the attributes associated with the element.
    */ 
   private InputNodeMap map;

   /**
    * This is the node reader that reads from the XML document.
    */ 
   private NodeReader reader;
   
   /**
    * This is the parent node for this XML input element node.
    */
   private InputNode parent;
 
   /**
    * Constructor for the <code>InputElement</code> object. This 
    * is used to create an input node that will provide access to 
    * an XML element. All attributes associated with the element 
    * given are extracted and exposed via the attribute node map.
    *
    * @param parent this is the parent XML element for this 
    * @param element this is the XML element wrapped
    * @param reader this is the reader used to read XML elements
    */ 
   public InputElement(InputNode parent, NodeReader reader, StartElement element) {
      this.map = new InputNodeMap(this, element);      
      this.element = element;
      this.reader = reader;           
      this.parent = parent;
   }  
   
   /**
    * This is used to acquire the <code>Node</code> that is the
    * parent of this node. This will return the node that is
    * the direct parent of this node and allows for siblings to
    * make use of nodes with their parents if required.  
    *   
    * @return this returns the parent node for this node
    */
   public InputNode getParent() {
	   return parent;
   }
   
   /**
    * This provides the position of this node within the document.
    * This allows the user of this node to report problems with
    * the location within the document, allowing the XML to be
    * debugged if it does not match the class schema.
    *
    * @return this returns the position of the XML read cursor
    */      
   public Position getPosition() {
      return new InputPosition(element);           
   }   

   /**
    * Returns the name of the node that this represents. This is
    * an immutable property and should not change for any node.  
    * This provides the name without the name space part.
    *  
    * @return returns the name of the node that this represents
    */   
   public String getName() {
      return element.getName().getLocalPart();           
   }
   
   /**
    * This method is used to determine if this node is the root 
    * node for the XML document. The root node is the first node
    * in the document and has no sibling nodes. This is false
    * if the node has a parent node or a sibling node.
    * 
    * @return true if this is the root node within the document
    */
   public boolean isRoot() {
      return reader.isRoot(this);
   }

   /**
    * This is used to determine if this node is an element. This
    * allows users of the framework to make a distinction between
    * nodes that represent attributes and nodes that represent
    * elements. This is particularly useful given that attribute
    * nodes do not maintain a node map of attributes.
    *
    * @return this returns true as this instance is an element
    */ 
   public boolean isElement() {
      return true;           
   } 

   /**
    * Provides an attribute from the element represented. If an
    * attribute for the specified name does not exist within the
    * element represented then this method will return null.
    *
    * @param name this is the name of the attribute to retrieve
    *
    * @return this returns the value for the named attribute
    */    
   public InputNode getAttribute(String name) {
      return map.get(name);
   }

   /**
    * This returns a map of the attributes contained within the
    * element. If no elements exist within the element then this
    * returns an empty map. 
    * 
    * @return this returns a map of attributes for the element
    */    
   public NodeMap getAttributes() {
      return map;
   }

   /**
    * Returns the value for the node that this represents. This 
    * is an immutable value for the node and cannot be changed.
    * If there is a problem reading an exception is thrown.
    * 
    * @return the name of the value for this node instance
    */     
   public String getValue() throws Exception {
      return reader.readValue(this);           
   }
  
   /**
    * The method is used to acquire the next child attribute of this 
    * element. If the next element from the <code>NodeReader</code> 
    * is not a child node to the element that this represents then
    * this will return null, which ensures each element represents
    * a self contained collection of child nodes.
    *
    * @return this returns the next child element of this node
    *
    * @exception Exception thrown if there is a problem reading
    */  
   public InputNode getNext() throws Exception {
      return reader.readElement(this);
   }
   
   /**
    * The method is used to acquire the next child attribute of this 
    * element. If the next element from the <code>NodeReader</code> 
    * is not a child node to the element that this represents then
    * this will return null, also if the next element does not match
    * the specified name then this will return null.
    *
    * @param name this is the name expected fromt he next element
    *
    * @return this returns the next child element of this node
    *
    * @exception Exception thrown if there is a problem reading
    */  
   public InputNode getNext(String name) throws Exception {
      return reader.readElement(this, name);
   }
   
   /**
    * This method is used to skip all child elements from this
    * element. This allows elements to be effectively skipped such
    * that when parsing a document if an element is not required
    * then that element can be completely removed from the XML.
    *
    * @exception Exception thrown if there was a parse error
    */ 
   public void skip() throws Exception {
      reader.skipElement(this);           
   }
}


