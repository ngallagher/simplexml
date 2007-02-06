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

package simple.xml.stream;

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
 * @see simple.xml.stream.NodeReader
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
    * Constructor for the <code>InputElement</code> object. This 
    * is used to create an input node that will provide access to 
    * an XML element. All attributes associated with the element 
    * given are extracted and exposed via the attribute node map.
    *
    * @param element this is the XML element wrapped
    * @param reader this is the reader used to read XML elements
    */ 
   public InputElement(NodeReader reader, StartElement element) throws Exception {
      this.map = new InputNodeMap(this, element);
      this.element = element;
      this.reader = reader;           
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
}


