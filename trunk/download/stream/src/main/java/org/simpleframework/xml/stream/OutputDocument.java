/*
 * OutputDocument.java July 2006
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

/**
 * The <code>OutputDocument</code> object is used to represent the
 * root of an XML document. This does not actually represent anything
 * that will be written to the generated document. It is used as a
 * way to create the root document element. Once the root element has
 * been created it can be committed by using this object.
 * 
 * @author Niall Gallagher
 */ 
class OutputDocument implements OutputNode {

   /**
    * Represents a dummy output node map for the attributes.
    */ 
   private OutputNodeMap table;
        
   /**
    * Represnets the writer that is used to create the element.
    */ 
   private NodeWriter writer;
   
   /**
    * This is the output stack used by the node writer object.
    */ 
   private OutputStack stack;
   
   /**
    * Represents the value that has been set on this document.
    */ 
   private String value;
   
   /**
    * This is the output mode of this output document object.
    */
   private Mode mode;
  
   /**
    * Constructor for the <code>OutputDocument</code> object. This 
    * is used to create an empty output node object that can be
    * used to create a root element for the generated document. 
    *
    * @param writer this is the node writer to write the node to
    * @param stack this is the stack that contains the open nodes
    */ 
   public OutputDocument(NodeWriter writer, OutputStack stack) {
      this.table = new OutputNodeMap(this);    
      this.mode = Mode.INHERIT;
      this.writer = writer;
      this.stack = stack;
   }     
   
   /**
    * This is used to acquire the <code>Node</code> that is the
    * parent of this node. This will return the node that is
    * the direct parent of this node and allows for siblings to
    * make use of nodes with their parents if required.  
    *   
    * @return this will always return null for this output    
    */
   public OutputNode getParent() {
	   return null;
   }
   
   /**
    * To signify that this is the document element this method will
    * return null. Any object with a handle on an output node that
    * has been created can check the name to determine its type.
    *
    * @return this returns null for the name of the node 
    */ 
   public String getName() {
      return null;
   }
   
   /**
    * This returns the value that has been set for this document.
    * The value returned is esentially a dummy value as this node
    * is never written to the resulting XML document.
    *
    * @return the value that has been set with this document
    */ 
   public String getValue() throws Exception {
      return value;
   }
   
   /**
    * This method is used to determine if this node is the root 
    * node for the XML document. The root node is the first node
    * in the document and has no sibling nodes. This will return
    * true although the codument node is not strictly the root.
    * 
    * @return returns true although this is not really a root
    */
   public boolean isRoot() {
      return true;
   }
   
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
   public Mode getMode() {
      return mode;
   }
   
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
   public void setMode(Mode mode) {
      this.mode = mode;
   }
   
   /**
    * This method is used for convinience to add an attribute node 
    * to the attribute <code>NodeMap</code>. The attribute added
    * can be removed from the element by useing the node map.
    * 
    * @param name this is the name of the attribute to be added
    * @param value this is the value of the node to be added
    */ 
   public void setAttribute(String name, String value) {
      table.put(name, value);
   }

   /**
    * This returns a <code>NodeMap</code> which can be used to add
    * nodes to this node. The node map returned by this is a dummy
    * map, as this output node is never writteh to the XML document.
    *
    * @return returns the node map used to manipulate attributes
    */ 
   public NodeMap getAttributes() {
      return table;
   }

   /**
    * This is used to set a text value to the element. This effect
    * of adding this to the doucment node will not change what
    * is actually written to the generated XML document.
    * 
    * @param value this is the text value to add to this element
    */  
   public void setValue(String value) {
      this.value = value;
   }
   
   /**
    * This is used to set the output mode of this node to either
    * be CDATA or escaped. If this is set to true the any value
    * specified will be written in a CDATA block, if this is set
    * to false the values is escaped. If however this method is
    * never invoked then the mode is inherited from the parent.
    * 
    * @param data if true the value is written as a CDATA block
    */
   public void setData(boolean data) {
      if(data) {
         mode = Mode.DATA;
      } else {
         mode = Mode.ESCAPE;
      }     
   }
   
   /**
    * This is used to create a child element within the element that
    * this object represents. When a new child is created with this
    * method then the previous child is committed to the document.
    * The created <code>OutputNode</code> object can be used to add
    * attributes to the child element as well as other elements.
    *
    * @param name this is the name of the child element to create
    */  
   public OutputNode getChild(String name) throws Exception {
      return writer.writeElement(this, name);
   }
   
   /**
    * This is used to remove any uncommitted changes. Removal of an
    * output node can only be done if it has no siblings and has
    * not yet been committed. If the node is committed then this 
    * will throw an exception to indicate that it cannot be removed. 
    * 
    * @throws Exception thrown if the node cannot be removed
    */
   public void remove() throws Exception {
      if(stack.isEmpty()) {
         throw new NodeException("No root node");              
      }           
      stack.bottom().remove();  
   }

   /**
    * This will commit this element and any uncommitted elements
    * elements that are decendents of this node. For instance if
    * any child or grand child remains open under this element
    * then those elements will be closed before this is closed.
    *
    * @throws Exception this is thrown if there is an I/O error
    * or if a root element has not yet been created
    */ 
   public void commit() throws Exception {
      if(stack.isEmpty()) {
         throw new NodeException("No root node");              
      }           
      stack.bottom().commit();
   }

   /**
    * This is used to determine whether this node has been committed.
    * This will return true if no root element has been created or
    * if the root element for the document has already been commited.
    *
    * @return true if the node is committed or has not been created
    */  
   public boolean isCommitted() {
      return stack.isEmpty();
   }
}
