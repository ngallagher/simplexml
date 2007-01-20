/*
 * OutputStack.java July 2006
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

import java.util.LinkedList;
import java.util.Set;

/**
 * The <code>OutputStack</code> is used to keep track of the nodes 
 * that have been written to the document. This ensures that when
 * nodes are written to  the XML document that the writer can tell
 * whether a child node for a given <code>OutputNode</code> can be
 * created. Each created node is pushed, and popped when ended.
 *
 * @see simple.xml.stream.OutputNode
 */ 
final class OutputStack extends LinkedList<OutputNode> {

   /**
    * Represents the set of nodes that have not been committed.
    */         
   private Set active;
  
   /**
    * Constructor for the <code>OutputStack</code> object. This is
    * used to create a stack that can be used to keep track of the
    * elements that have been written to the XML document.
    */    
   public OutputStack(Set active) {
      this.active = active;
   }

   /**
    * This is used to remove the <code>OutputNode</code> from the
    * top of the output stack. This is used when an element has been
    * ended and the output writer wants to block child creation.
    *
    * @return this returns the node from the top of the stack
    */    
   public OutputNode pop() {
      OutputNode node = removeLast();
      
      if(node != null){
         active.remove(node);
      }
      return node;
   }
   
   /**
    * This is used to acquire the <code>OutputNode</code> from the
    * top of the output stack. This is used when the writer wants to
    * determine the current element written to the XML document.
    *
    * @return this returns the node from the top of the stack
    */    
   public OutputNode top() {
      if(isEmpty()) {
         return null;              
      }           
      return getLast();
   }

   /**
    * This is used to acquire the <code>OutputNode</code> from the
    * bottom of the output stack. This is used when the writer wants
    * to determine the root element for the written XML document.
    *
    * @return this returns the node from the bottom of the stack
    */ 
   public OutputNode bottom() {
      if(isEmpty()) {
         return null;              
      }           
      return getFirst();           
   }

   /**
    * This method is used to add an <code>OutputNode</code> to the
    * top of the stack. This is used when an element is written to
    * the XML document, and allows the writer to determine if a
    * child node can be created from a given output node.
    *
    * @param value this is the output node to add to the stack
    */    
   public OutputNode push(OutputNode value) {
      active.add(value);
      add(value);
      return value;
   }
}
