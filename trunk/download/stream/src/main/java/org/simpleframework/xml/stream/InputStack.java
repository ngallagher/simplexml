/*
 * InputStack.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.stream;

import java.util.ArrayList;

/**
 * The <code>InputStack</code> is used to keep track of the nodes 
 * that have been read from the document. This ensures that when
 * nodes are read from the source document that the reader can tell
 * whether a child node for a given <code>InputNode</code> can be
 * created. Each created node is pushed, and popped when ended.
 *
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.stream.InputNode
 */ 
class InputStack extends ArrayList<InputNode> {

   /**
    * Constructor for the <code>InputStack</code> object. This is
    * used to create a stack that can be used to keep track of the
    * elements that have been read from the source XML document.
    */         
   public InputStack() {
      super(6);
   }

   /**
    * This is used to remove the <code>InputNode</code> from the
    * top of the input stack. This is used when an element has been
    * ended and the input reader wants to block child creation.
    *
    * @return this returns the node from the top of the stack
    */ 
   public InputNode pop() {
      int size = size();
      
      if(size <= 0) {
         return null;               
      }           
      return remove(size - 1);
   }
   
   /**
    * This is used to acquired the <code>InputNode</code> from the
    * top of the input stack. This is used when the reader wants to
    * determine the current element read from the XML document.
    *
    * @return this returns the node from the top of the stack
    */ 
   public InputNode top() {
      int size = size();
      
      if(size <= 0) {
         return null;              
      }           
      return get(size - 1);
   }
   
   /**
    * This is used to acquire the <code>InputNode</code> from the
    * bottom of the input stack. This is used when the reader wants
    * to determine the root element for the read XML document.
    *
    * @return this returns the node from the bottom of the stack
    */ 
   public InputNode bottom() {
      int size = size();
      
      if(size <= 0) {
         return null;              
      }           
      return get(0);           
   }
   
   /**
    * This method is used to add an <code>InputNode</code> to the
    * top of the stack. This is used when an element has been read
    * from XML document, and allows the reader to determine if a
    * child node can be added from a given input node.
    *
    * @param value this is the input node to add to the stack
    */ 
   public InputNode push(InputNode value) {
      add(value);
      return value;
   }
   
   /**
    * This is used to determine if the specified node is relevant
    * with respect to the state of the input stack. This returns
    * true if there are no elements in the stack, which accounts
    * for a new root node. Also this returns true if the specified
    * node exists within the stack and is thus an active node.
    * 
    * @param value this is the input node value to be checked
    * 
    * @return returns true if the node is relevant in the stack
    */
   public boolean isRelevant(InputNode value) {
	   return contains(value) || isEmpty();
   }
}