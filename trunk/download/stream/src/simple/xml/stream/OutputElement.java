/*
 * OutputElement.java July 2006
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
 * The <code>OutputElement</code> object represents an XML element.  
 * Attributes can be added to this before ant child element has been
 * acquired from it. Once a child element has been acquired the
 * attributes will be written an can no longer be manipulated, the
 * same applies to any text value set for the element.
 * 
 * @author Niall Gallagher
 */ 
class OutputElement implements OutputNode {
   
   /**
    * Represents the attributes that have been set for the element.
    */         
   protected OutputNodeMap table;
   
   /**
    * Used to write the start tag and attributes for the document.
    */ 
   protected NodeWriter writer;
   
   /**
    * Represents the value that has been set for the element.
    */ 
   private String value;

   /**
    * Represents the name of the element for this output node.
    */ 
   private String name;
   
   /**
    * Constructor for the <code>OutputElement</code> object. This is
    * used to create an output element that can create elements for
    * an XML document. This requires the writer that is used to 
    * generate the actual document and the name of this node.
    *
    * @param writer this is the writer used to generate the file
    * @param name this is the name of the element this represents
    */ 
   public OutputElement(NodeWriter writer, String name) {
      this.table = new OutputNodeMap(this);           
      this.writer = writer;           
      this.name = name;
   }     

   /**
    * Returns the name of the node that this represents. This is
    * an immutable property and cannot be changed. This will be
    * written as the tag name when this has been committed.
    *  
    * @return returns the name of the node that this represents
    */   
   public String getName() {
      return name;           
   }
  
   /**
    * Returns the value for the node that this represents. This 
    * is a modifiable property for the node and can be changed,
    * however once committed any change will be irrelevent.
    * 
    * @return the name of the value for this node instance
    */   
   public String getValue() {
      return value;
   }

   /**
    * This returns a <code>NodeMap</code> which can be used to add
    * nodes to the element before that element has been committed. 
    * Nodes can be removed or added to the map and will appear as
    * attributes on the written element when it is committed.
    *
    * @return returns the node map used to manipulate attributes
    */    
   public NodeMap getAttributes() {
      return table;
   }

   /**
    * This is used to set a text value to the element. This should
    * be added to the element if the element contains no child
    * elements. If the value cannot be added an exception is thrown.
    * 
    * @param value this is the text value to add to this element
    */    
   public void setValue(String value) {
      this.value = value;
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
    * This will commit this element and any uncommitted elements
    * elements that are decendents of this node. For instance if
    * any child or grand child remains open under this element
    * then those elements will be closed before this is closed.
    *
    * @throws Exception this is thrown if there is an I/O error
    */ 
   public void commit() throws Exception{
      writer.commit(this);
   }
  
   /**
    * This is used to determine whether this node has been committed.
    * If the node is committed then no further child elements can
    * be created from this node instance. A node is considered to
    * be committed if a parent creates another child element or if
    * the <code>commit</code> method is invoked.
    *
    * @return true if the node has been committed
    */  
   public boolean isCommitted() {
      return writer.isCommitted(this);
   }
}
