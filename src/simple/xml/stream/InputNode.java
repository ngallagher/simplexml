/*
 * InputNode.java July 2006
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

/**
 * The <code>InputNode</code> object represents an iterator for the
 * elements within an element. This allows the input node object to
 * become a self contained iterator for an element and its children.
 * Each child taken from the input node object, is itself an input
 * node, and can be used to exlpore its sub elements without having
 * any affect on its outer elements.
 *
 * @author Niall Gallagher
 */ 
public interface InputNode extends Node {

   /**
    * This method is used to determine if this node is the root 
    * node for the XML document. The root node is the first node
    * in the document and has no sibling nodes. This is false
    * if the node has a parent node or a sibling node.
    * 
    * @return true if this is the root node within the document
    */
   public boolean isRoot();
   
   /**
    * This provides the position of this node within the document.
    * This allows the user of this node to report problems with
    * the location within the document, allowing the XML to be
    * debugged if it does not match the class schema.
    *
    * @return this returns the position of the XML read cursor
    */         
   public Position getPosition();
        
   /**
    * Provides an attribute from the element represented. If an
    * attribute for the specified name does not exist within the
    * element represented then this method will return null.
    *
    * @param name this is the name of the attribute to retrieve
    *
    * @return this returns the value for the named attribute
    */ 
   public InputNode getAttribute(String name);

   /**
    * This returns a map of the attributes contained within the
    * element. If no elements exist within the element then this
    * returns an empty map. 
    * 
    * @return this returns a map of attributes for the element
    */  
   public NodeMap getAttributes();
   
   /**
    * This returns the next child element within this element if
    * one exists. If all children have been read, or if there are
    * no child elements for this element then this returns null.
    *
    * @return this returns an input node for the next child
    *
    * @exception Exception thrown if there was a parse error
    */ 
   public InputNode getNext() throws Exception;   

   /**
    * This method is used to skip all child elements from this
    * element. This allows elements to be effectively skipped such
    * that when parsing a document if an element is not required
    * then that element can be completely removed from the XML.
    *
    * @exception Exception thrown if there was a parse error
    */ 
   public void skip() throws Exception;
}
