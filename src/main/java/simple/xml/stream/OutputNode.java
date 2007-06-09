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
    * The <code>Mode</code> is used to indicate the output mode
    * of this node. Three modes are possible, each determines
    * how a value, if specified, is written to the resulting XML
    * document. This is determined by the <code>setData</code>
    * method which will set the output to be CDATA or escaped, 
    * if neither is specified the mode is inherited.
    * 
    * @return this returns the mode of this output node object
    */
   public Mode getMode();
   
   /**
    * This is used to set the output mode of this node to either
    * be CDATA, escaped, or inherited. If the mode is set to data
    * then any value specified will be written in a CDATA block, 
    * if this is set to escaped values are escaped. If however 
    * this method is set to inherited then the mode is inherited
    * from the parent node.
    * 
    * @param mode this is the output mode to set the node to 
    */
   public void setMode(Mode mode);
   
   /**
    * This is used to set the output mode of this node to either
    * be CDATA or escaped. If this is set to true the any value
    * specified will be written in a CDATA block, if this is set
    * to false the values is escaped. If however this method is
    * never invoked then the mode is inherited from the parent.
    * 
    * @param data if true the value is written as a CDATA block
    */
   public void setData(boolean data);
   
   /**
    * This is used to set a text value to the element. This should
    * be added to the element if the element contains no child
    * elements. If the value cannot be added an exception is thrown.
    * 
    * @param value this is the text value to add to this element
    *
    * @throws Exception thrown if the text value cannot be added
    */ 
   public void setValue(String value);
   
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
    * This is used to acquire the <code>Node</code> that is the
    * parent of this node. This will return the node that is
    * the direct parent of this node and allows for siblings to
    * make use of nodes with their parents if required.  
    *   
    * @return this returns the parent node for this node
    */
   public OutputNode getParent();
   
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
    * This is used to remove any uncommitted changes. Removal of an
    * output node can only be done if it has no siblings and has
    * not yet been committed. If the node is committed then this 
    * will throw an exception to indicate that it cannot be removed. 
    * 
    * @throws Exception thrown if the node cannot be removed
    */
   public void remove() throws Exception;
   
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
