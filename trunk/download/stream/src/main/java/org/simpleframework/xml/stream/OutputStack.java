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

package org.simpleframework.xml.stream;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;

/**
 * The <code>OutputStack</code> is used to keep track of the nodes 
 * that have been written to the document. This ensures that when
 * nodes are written to  the XML document that the writer can tell
 * whether a child node for a given <code>OutputNode</code> can be
 * created. Each created node is pushed, and popped when ended.
 *
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.stream.OutputNode
 */ 
class OutputStack extends ArrayList<OutputNode> {

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
      int size = size();
      
      if(size <= 0) {
         return null;
      }
      return purge(size - 1);
   }
   
   /**
    * This is used to acquire the <code>OutputNode</code> from the
    * top of the output stack. This is used when the writer wants to
    * determine the current element written to the XML document.
    *
    * @return this returns the node from the top of the stack
    */    
   public OutputNode top() {
      int size = size();
      
      if(size <= 0) {
         return null;              
      }           
      return get(size - 1);
   }

   /**
    * This is used to acquire the <code>OutputNode</code> from the
    * bottom of the output stack. This is used when the writer wants
    * to determine the root element for the written XML document.
    *
    * @return this returns the node from the bottom of the stack
    */ 
   public OutputNode bottom() {
      int size = size();
      
      if(size <= 0) {
         return null;              
      }           
      return get(0);           
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
   
   /**
    * The <code>purge</code> method is used to purge a match from
    * the provided position. This also ensures that the active set
    * has the node removed so that it is no longer relevant.
    *
    * @param index the index of the node that is to be removed
    * 
    * @return returns the node removed from the specified index
    */ 
   public OutputNode purge(int index) {      
      OutputNode node = remove(index);  
      
      if(node != null){
         active.remove(node);
      }    
      return node;
   }
   
   /**
    * This is returns an <code>Iterator</code> that is used to loop
    * through the ouptut nodes from the top down. This allows the
    * node writer to determine what <code>Mode</code> should be used
    * by an output node. This reverses the iteration of the list.
    * 
    * @return returns an iterator to iterate from the top down
    */ 
   public Iterator<OutputNode> iterator() {
      return new Sequence();              
   }

   /**
    * The is used to order the <code>OutputNode</code> objects from
    * the top down. This is basically used to reverse the order of
    * the linked list so that the stack can be iterated within a
    * for each loop easily. This can also be used to remove a node.
    *
    * @author Niall Gallagher
    */
   private class Sequence implements Iterator<OutputNode> {

      /**
       * The cursor used to acquire objects from the stack.
       */               
      private int cursor;

      /**
       * Constructor for the <code>Sequence</code> object. This is
       * used to position the cursor at the end of the list so the
       * last inserted output node is the first returned from this.
       */ 
      public Sequence() {
         this.cursor = size();                 
      }

      /**
       * Returns the <code>OutputNode</code> object at the cursor
       * position. If the cursor has reached the start of the list 
       * then this returns null instead of the first output node.
       * 
       * @return this returns the node from the cursor position
       */ 
      public OutputNode next() {
         if(hasNext()) {
             return get(--cursor);
         }           
         return null;     
      }    

      /**
       * This is used to determine if the cursor has reached the
       * start of the list. When the cursor reaches the start of
       * the list then this method returns false.
       * 
       * @return this returns true if there are more nodes left
       */ 
      public boolean hasNext() {
         return cursor > 0;
      }

      /**
       * Removes the match from the cursor position. This also
       * ensures that the node is removed from the active set so
       * that it is not longer considered a relevant output node.
       */ 
      public void remove() {                    
         purge(cursor);                
      }        
   }
}
