/*
 * OutputNode.java July 2006
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
 * The <code>OutputNode</code> object is used to represent a cursor
 * which can be used to write XML elements and attributes. Each of
 * the output node objects represents a element, and can be used 
 * to add attributes to that element as well as child elements.
 *
 * @author Niall Gallagher
 */ 
public interface OutputNode extends Node {
   
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
    * This returns a <code>NodeMap</code> which can be used to add
    * nodes to the element before that element has been committed. 
    * Nodes can be removed or added to the map and will appear as
    * attributes on the written element when it is committed.
    *
    * @return returns the node map used to manipulate attributes
    */ 
   public NodeMap getAttributes();

   /**
    * This is used to set a text value to the element. This should
    * be added to the element if the element contains no child
    * elements. If the value cannot be added an exception is thrown.
    * 
    * @param value this is the text value to add to this element
    *
    * @throws Exception thrown if the text value cannot be added
    */ 
   public void setValue(String value) throws Exception;

   /**
    * This method is used for convinience to add an attribute node 
    * to the attribute <code>NodeMap</code>. The attribute added
    * can be removed from the element by useing the node map.
    * 
    * @param name this is the name of the attribute to be added
    * @param value this is the value of the node to be added
    */ 
   public void setAttribute(String name, String value);
   
   /**
    * This is used to create a child element within the element that
    * this object represents. When a new child is created with this
    * method then the previous child is committed to the document.
    * The created <code>OutputNode</code> object can be used to add
    * attributes to the child element as well as other elements.
    *
    * @param name this is the name of the child element to create
    */ 
   public OutputNode getChild(String name) throws Exception;        

   /**
    * The <code>commit</code> method is used flush and commit any 
    * child nodes that have been created by this node. This allows
    * the output to be completed when building of the XML document
    * has been completed. If output fails an exception is thrown.
    * 
    * @throws Exception thrown if the node cannot be committed
    */ 
   public void commit() throws Exception;

   /**
    * This is used to determine whether the node has been committed.
    * If the node has been committed, then this will return true.
    * When committed the node can no longer produce chile nodes.
    * 
    * @return true if this node has already been committed
    */
   public boolean isCommitted();
}
