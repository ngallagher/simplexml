/*
 * NodeWriter.java July 2006
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

import java.util.HashSet;
import java.util.Set;
import java.io.Writer;

/**
 * The <code>NodeWriter</code> object is used to create a writer that
 * will write well formed indented XML for a given output node. This
 * is used in the serialization process to convert an object into an
 * XML document.
 * <p>
 * This keeps a stack of all the active output nodes so that if an
 * output node has been committed it cannot write any further data to
 * the XML document. This allows all output nodes to be independant
 * of each other as the node write organizes the write access.
 *
 * @author Niall Gallagher
 */  
final class NodeWriter {

   /**
    * Represents the stack of output nodes that are not yet ended.
    */          
   private OutputStack stack;
   
   /**
    * Formatter used to indent the XML elements and escape text.
    */ 
   private Formatter writer;
   
   /**
    * Contains the set of as yet uncommitted elements blocks.
    */ 
   private Set active;

   /**
    * Constructor for the <code>NodeWriter</code> object. This will
    * create the object that is used to control an output elements
    * access to the generated XML document. This keeps a stack of
    * active and uncommitted elements.
    *
    * @param result this is the output for the resulting document
    */ 
   public NodeWriter(Writer result) {
      this(result, new Format());
   }  
   
   /**
    * Constructor for the <code>NodeWriter</code> object. This will
    * create the object that is used to control an output elements
    * access to the generated XML document. This keeps a stack of
    * active and uncommitted elements.
    *
    * @param result this is the output for the resulting document
    * @param format this is used to format the generated document
    */ 
   public NodeWriter(Writer result, Format format) {
      this.writer = new Formatter(result, format);
      this.active = new HashSet();
      this.stack = new OutputStack(active);           
   }  
   
   /**
    * This is used to acquire the root output node for the document.
    * This will create an empty node that can be used to generate
    * the root document element as a child to the document.
    * <p>
    * Depending on whether or not an encoding has been specified 
    * this method will write a prolog to the generated XML document.
    * Each prolog written uses an XML version of "1.0".
    *
    * @return this returns an output element for the document
    */ 
   public OutputNode writeRoot() throws Exception {
      OutputDocument root = new OutputDocument(this, stack);

      if(stack.isEmpty()) {
         writer.writeProlog();              
      }
      return root;
   }
   
   /**
    * This method is used to determine if the node is the root 
    * node for the XML document. The root node is the first node
    * in the document and has no sibling nodes. This is false
    * if the node has a parent node or a sibling node.
    *
    * @param node this is the node that is check as the root 
    *
    * @return true if the node is the root node for the document
    */
   public boolean isRoot(OutputNode node) {
      return stack.bottom() == node;        
   }
   
   /**
    * This is used to determine if the specified node has been 
    * committed. If this returns tre then the node is committed
    * and cannot be used to add further child elements.
    *
    * @param node this is the node to check for commit status
    * 
    * @return this returns true if the node has been committed
    */ 
   public boolean isCommitted(OutputNode node) {
      return !active.contains(node);
   }
  
   /**
    * This method is used to commit all nodes on the stack up to and
    * including the specified node. This will effectively create end 
    * tags for any nodes that are currently open up to the specified
    * element. Once committed the output node can no longer be used
    * to create child elements, nor can any of its child elements.
    *
    * @param parent this is the node that is to be committed
    */ 
   public void commit(OutputNode parent) throws Exception {
      if(stack.contains(parent)) {
         OutputNode top = stack.top();
         
         if(!isCommitted(top)) {
            writeAttributes(top);
         }         
         while(stack.top() != parent) {
            writeEnd(stack.pop());
         }
         writeEnd(parent);
         stack.pop();
      }
   } 

   /**
    * This is used to create a new element under the specified node.
    * This will effectively commit all nodes that are open until this
    * node is encountered. Once the specified node is encountered on
    * the stack a new element is created with the specified name.
    *
    * @param parent this is the node that is to be committed
    * @param name this is the name of the start element to create
    *
    * @return this will return a child node for the given parent
    */ 
   public OutputNode writeElement(OutputNode parent, String name) throws Exception {
      if(stack.isEmpty()) {
         return writeStart(name);       
      }
      if(stack.contains(parent)) {
         OutputNode top = stack.top();
      
         if(!isCommitted(top)) {
            writeAttributes(top);
         }
         while(stack.top() != parent) {
            writeEnd(stack.pop());
         }       
         return writeStart(name);
      }
      return null;
   }

    /**
     * This is used to write a new start element to the resulting XML
     * document. This will create an output node of the specified
     * name before writing the start tag. Once the tag is written 
     * the node is pushed on to the head of the output node stack.
     *
     * @param name this is the name of the node that is to be written
     *
     * @return this returns an output node used for writing content
     */       
   private OutputNode writeStart(String name) throws Exception {
      OutputNode node = new OutputElement(this, name);

      if(name != null) {
          writer.writeStart(name);
      }          
      return stack.push(node);
   }
 
   /**
    * This is used to write a new end element to the resulting XML
    * document. This will acquire the name and value of the given
    * node, if the node has a value that is written. Finally a new
    * end tag is written to the document and the output is flushed.
    *
    * @param node this is the node that is to have an end tag
    */  
   private void writeEnd(OutputNode node) throws Exception {
      String value = node.getValue();
      String name = node.getName();

      if(value != null) { 
         writer.writeText(value);
      }         
      writer.writeEnd(name);
      writer.flush();
   }
   
   /**
    * This is used to write the attributes of the specified node to
    * the output. This will iterate over each node entered on to
    * the node. Once written the node is considered inactive.
    *
    * @param this is the node to have is attributes written
    */ 
   private void writeAttributes(OutputNode node) throws Exception {
      NodeMap map = node.getAttributes();
      
      for(String name : map) {
         Node entry = map.get(name);
         String value = entry.getValue();
         
         writer.writeAttribute(name, value);
      }
      active.remove(node);
   }
}

