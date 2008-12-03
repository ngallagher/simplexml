/*
 * OutputAttribute.java July 2006
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
 * The <code>OutputAttribute</code> object is used to represent a 
 * node added to the output node map. It represents a simple name 
 * value pair that is used as an attribute by an output element.
 * This shares its namespaces with the parent element so that any
 * namespaces added to the attribute are actually added to the
 * parent element, which ensures correct scoping.
 *
 * @see org.simpleframework.xml.stream.Node
 */ 
class OutputAttribute implements OutputNode {
   
   /**
    * This contains the namespaces for the parent element.
    */         
   private NamespaceMap scope;
   
   /**
    * Represents the output node that this node requires.
    */
   private OutputNode source;

   /**
    * Represents the namespace reference for this node.
    */
   private String reference;
   
   /**
    * Represents the name of this node object instance.
    */            
   private String name;

   /**
    * Represents the value of this node object instance.
    */  
   private String value;
        
   /**
    * Constructor for the <code>OutputAttribute</code> object. This 
    * is used to create a simple name value pair attribute holder.
    *
    * @param name this is the name that is used for the node
    * @param value this is the value used for the node
    */ 
   public OutputAttribute(OutputNode source, String name, String value) {
      this.scope = source.getNamespaces();
      this.source = source;
      this.value = value;              
      this.name = name;              
   }         
   
   /**
    * Returns the value for the node that this represents. This   
    * is a modifiable property for the node and can be changed.
    * When set this forms the value the attribute contains in
    * the parent XML element, which is written in quotations.
    *    
    * @return the name of the value for this node instance
    */      
   public String getValue() {
      return value;              
   }

   /**
    * This is used to set a text value to the attribute. This should
    * be added to the attribute if the attribute is to be written 
    * to the parent element. Without a value this is invalid.
    *
    * @param value this is the text value to add to this attribute
    */
   public void setValue(String value) {
      this.value = value;           
   }

   /**
    * Returns the name of the node that this represents. This is
    * an immutable property and should not change for any node.  
    * If this is null then the attribute will not be added to 
    * the node map, all attributes must have a valid key.
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
   public OutputNode getParent() {
     return source;
   }      

   /**
    * This returns a <code>NodeMap</code> which can be used to add
    * nodes to the element before that element has been committed. 
    * Nodes can be removed or added to the map and will appear as
    * attributes on the written element when it is committed.
    *
    * @return returns the node map used to manipulate attributes
    */ 
   public NodeMap<OutputNode> getAttributes() {
      return new OutputNodeMap(this);
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
   public OutputNode getChild(String name) {
      return null;
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
      return Mode.INHERIT;
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
      return;           
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
      return;           
   }

   /**
    * This is used to acquire the prefix for this output node. If
    * the output node is an element then this will search its parent
    * nodes until the prefix that is currently in scope is found. 
    * If however this node is an attribute then the hierarchy of 
    * nodes is not searched as attributes to not inherit namespaces.
    *
    * @return this returns the prefix associated with this node
    */  
   public String getPrefix() {
      return scope.get(reference);           
   }
   
   /**
    * This is used to acquire the namespace URI reference associated
    * with this node. Although it is recommended that the namespace
    * reference is a URI it does not have to be, it can be any unique
    * identifier that can be used to distinguish the qualified names.
    *
    * @return this returns the nanmespace URI reference for this
    */
   public String getReference() {
      return reference;           
   }
  
   /**
    * This is used to set the reference for the node. Setting the
    * reference implies that the node is a qualified node within the
    * XML document. Both elements and attributes can be qualified.
    * Depending on the prefix set on this node or, failing that, any
    * parent node for the reference, the element will appear in the
    * XML document with that string prefixed to the node name.
    *
    * @param reference this is used to set the reference for the node
    */  
   public void setReference(String reference) {
      this.reference = reference;           
   }
  
   /**
    * This returns the <code>NamespaceMap</code> for this node. Only
    * an element can have namespaces, so if this node represents an
    * attribute the elements namespaces will be provided when this is
    * requested. By adding a namespace it becomes in scope for the
    * current element all all child elements of that element.
    *
    * @return this returns the namespaces associated with the node
    */  
   public NamespaceMap getNamespaces() {
      return scope;           
   }

   /**
    * This method is used for convinience to add an attribute node 
    * to the attribute <code>NodeMap</code>. The attribute added
    * can be removed from the element by useing the node map.
    * 
    * @param name this is the name of the attribute to be added
    * @param value this is the value of the node to be added
    * 
    * @return this returns the node that has just been added
    */ 
   public OutputNode setAttribute(String name, String value) {
      return null;           
   }

   /**
    * This is used to remove any uncommitted changes. Removal of an
    * output node can only be done if it has no siblings and has
    * not yet been committed. If the node is committed then this 
    * will throw an exception to indicate that it cannot be removed. 
    */
   public void remove() {
      return;
   }
   
   /**
    * The <code>commit</code> method is used flush and commit any 
    * child nodes that have been created by this node. This allows
    * the output to be completed when building of the XML document
    * has been completed. If output fails an exception is thrown.
    */ 
   public void commit() {
      return;           
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
      return false;           
   }

   /**
    * This is used to determine whether the node has been committed.
    * If the node has been committed, then this will return true.
    * When committed the node can no longer produce chile nodes.
    *
    * @return true if this node has already been committed
    */
   public boolean isCommitted() {
      return true;           
   }
}
