/*
 * InputAttribute.java July 2006
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

import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;

/**
 * The <code>InputAttribute</code> is used to represent an attribute
 * within an element. Rather than representing an attribute as a
 * name value pair of strings, an attribute is instead represented
 * as an input node, in the same manner as an element. The reason
 * for representing an attribute in this way is such that a uniform
 * means of extracting and parsing values can be used for inputs.
 *
 * @author Niall Gallagher
 */ 
class InputAttribute implements InputNode {
	
   /**
    * This is the parent node to this attribute instance.
    */
   private InputNode parent;
	   
   /**
    * Represents the source attribute if one was specified.
    */         
   private Attribute source;
        
   /**
    * Represents the name of this input attribute instance.
    */         
   private String name;

   /**
    * Represents the value for this input attribute instance.
    */ 
   private String value;  
        
   /**
    * Constructor for the <code>InputAttribute</code> object. This
    * is used to wrap a an attribute as an input node object. The
    * attribute can then be used in a similar manner to elements.
    *
    * @param parent this is the parent node to this attribute
    * @param source this is the attribute that this will wrap
    */   
   public InputAttribute(InputNode parent, Attribute source) {
      this(parent, source, source.getName());
   }        

   /**
    * Constructor for the <code>InputAttribute</code> object. This
    * is used to wrap a an attribute as an input node object. The
    * attribute can then be used in a similar manner to elements.
    *
    * @param parent this is the parent node to this attribute
    * @param source this is the attribute that this will wrap
    * @param name this is the name of the XML attribute
    */   
   private InputAttribute(InputNode parent, Attribute source, QName name) {
      this.name = name.getLocalPart();
      this.value = source.getValue();
      this.source = source;      
   }

   /**
    * Constructor for the <code>InputAttribute</code> object. This
    * is used to create an input attribute using the provided name
    * and value, all other values for this input node will be null.
    * 
    * @param parent this is the parent node to this attribute
    * @param name this is the name for this attribute object
    * @param value this is the value for this attribute object
    */     
   public InputAttribute(InputNode parent, String name, String value) {
	  this.parent = parent;
      this.value = value;
      this.name = name;           
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
      return new InputPosition(source);           
   }

   /**
    * Returns the name of the node that this represents. This is
    * an immutable property and will not change for this node. 
    *  
    * @return returns the name of the node that this represents
    */   
   public String getName() {
      return name;
   }

   /**
    * Returns the value for the node that this represents. This 
    * is an immutable value for the node and cannot be changed.
    * 
    * @return the name of the value for this node instance
    */   
   public String getValue() {
      return value;
   }
   
   /**
    * This method is used to determine if this node is the root 
    * node for the XML document. This will return false as this 
    * node can never be the root node because it is an attribute.
    * 
    * @return this will always return false for attribute nodes
    */
   public boolean isRoot() {
      return false;
   }

   /**
    * This is used to determine if this node is an element. This
    * node instance can not be an element so this method returns
    * false. Returning null tells the users of this node that any
    * attributes added to the node map will be permenantly lost.
    *
    * @return this returns false as this is an attribute node
    */ 
   public boolean isElement() {
      return false;           
   } 

   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return null. If nodes are added 
    * to the node map the values will not be available here.
    *
    * @return this always returns null for a requested attribute
    */ 
   public InputNode getAttribute(String name) {
      return null;
   }

   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return an empty map. If nodes are
    * added to the node map the values will not be maintained.
    *
    * @return this always returns an empty node map of attributes
    */
   public NodeMap getAttributes() {
      return new InputNodeMap(this);
   }
   
   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return null. An attribute is a
    * simple name value pair an so can not contain any child nodes.
    *
    * @return this always returns null for a requested child node
    */
   public InputNode getNext() {
      return null;           
   }
   
   /**
    * Because the <code>InputAttribute</code> object represents an
    * attribute this method will return null. An attribute is a
    * simple name value pair an so can not contain any child nodes.
    *
    * @param name this is the name of the next expected element
    *
    * @return this always returns null for a requested child node
    */
   public InputNode getNext(String name) {
      return null;           
   }
   
   /**
    * This method is used to skip all child elements from this
    * element. This allows elements to be effectively skipped such
    * that when parsing a document if an element is not required
    * then that element can be completely removed from the XML.
    */ 
   public void skip() {
      return;           
   }
   
   /**
    * This is used to determine if this input node is empty. An
    * empty node is one with no attributes or children. This can
    * be used to determine if a given node represents an empty
    * entity, with which no extra data can be extracted.
    * 
    * @return this will always return false as it has a value
    */
   public boolean isEmpty() {
      return false;
   }
}
