#region License
//
// OutputStack.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion
#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>OutputStack</c> is used to keep track of the nodes
   /// that have been written to the document. This ensures that when
   /// nodes are written to  the XML document that the writer can tell
   /// whether a child node for a given <c>OutputNode</c> can be
   /// created. Each created node is pushed, and popped when ended.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.OutputNode
   /// </seealso>
   class OutputStack : ArrayList<OutputNode> {
      /// <summary>
      /// Represents the set of nodes that have not been committed.
      /// </summary>
      private readonly Set active;
      /// <summary>
      /// Constructor for the <c>OutputStack</c> object. This is
      /// used to create a stack that can be used to keep track of the
      /// elements that have been written to the XML document.
      /// </summary>
      public OutputStack(Set active) {
         this.active = active;
      }
      /// <summary>
      /// This is used to remove the <c>OutputNode</c> from the
      /// top of the output stack. This is used when an element has been
      /// ended and the output writer wants to block child creation.
      /// </summary>
      /// <returns>
      /// this returns the node from the top of the stack
      /// </returns>
      public OutputNode Pop() {
         int size = size();
         if(size <= 0) {
            return null;
         }
         return Purge(size - 1);
      }
      /// <summary>
      /// This is used to acquire the <c>OutputNode</c> from the
      /// top of the output stack. This is used when the writer wants to
      /// determine the current element written to the XML document.
      /// </summary>
      /// <returns>
      /// this returns the node from the top of the stack
      /// </returns>
      public OutputNode Top() {
         int size = size();
         if(size <= 0) {
            return null;
         }
         return get(size - 1);
      }
      /// <summary>
      /// This is used to acquire the <c>OutputNode</c> from the
      /// bottom of the output stack. This is used when the writer wants
      /// to determine the root element for the written XML document.
      /// </summary>
      /// <returns>
      /// this returns the node from the bottom of the stack
      /// </returns>
      public OutputNode Bottom() {
         int size = size();
         if(size <= 0) {
            return null;
         }
         return get(0);
      }
      /// <summary>
      /// This method is used to add an <c>OutputNode</c> to the
      /// top of the stack. This is used when an element is written to
      /// the XML document, and allows the writer to determine if a
      /// child node can be created from a given output node.
      /// </summary>
      /// <param name="value">
      /// this is the output node to add to the stack
      /// </param>
      public OutputNode Push(OutputNode value) {
         active.add(value);
         add(value);
         return value;
      }
      /// <summary>
      /// The <c>purge</c> method is used to purge a match from
      /// the provided position. This also ensures that the active set
      /// has the node removed so that it is no longer relevant.
      /// </summary>
      /// <param name="index">
      /// the index of the node that is to be removed
      /// </param>
      /// <returns>
      /// returns the node removed from the specified index
      /// </returns>
      public OutputNode Purge(int index) {
         OutputNode node = Remove(index);
         if(node != null){
            active.Remove(node);
         }
         return node;
      }
      /// <summary>
      /// This is returns an <c>Iterator</c> that is used to loop
      /// through the ouptut nodes from the top down. This allows the
      /// node writer to determine what <c>Mode</c> should be used
      /// by an output node. This reverses the iteration of the list.
      /// </summary>
      /// <returns>
      /// returns an iterator to iterate from the top down
      /// </returns>
      public Iterator<OutputNode> Iterator() {
         return new Sequence();
      }
      /// <summary>
      /// The is used to order the <c>OutputNode</c> objects from
      /// the top down. This is basically used to reverse the order of
      /// the linked list so that the stack can be iterated within a
      /// for each loop easily. This can also be used to remove a node.
      /// </summary>
      private class Sequence : Iterator<OutputNode> {
         /// <summary>
         /// The cursor used to acquire objects from the stack.
         /// </summary>
         private int cursor;
         /// <summary>
         /// Constructor for the <c>Sequence</c> object. This is
         /// used to position the cursor at the end of the list so the
         /// last inserted output node is the first returned from this.
         /// </summary>
         public Sequence() {
            this.cursor = size();
         }
         /// <summary>
         /// Returns the <c>OutputNode</c> object at the cursor
         /// position. If the cursor has reached the start of the list
         /// then this returns null instead of the first output node.
         /// </summary>
         /// <returns>
         /// this returns the node from the cursor position
         /// </returns>
         public OutputNode Next() {
            if(HasNext()) {
                return get(--cursor);
            }
            return null;
         }
         /// <summary>
         /// This is used to determine if the cursor has reached the
         /// start of the list. When the cursor reaches the start of
         /// the list then this method returns false.
         /// </summary>
         /// <returns>
         /// this returns true if there are more nodes left
         /// </returns>
         public bool HasNext() {
            return cursor > 0;
         }
         /// <summary>
         /// Removes the match from the cursor position. This also
         /// ensures that the node is removed from the active set so
         /// that it is not longer considered a relevant output node.
         /// </summary>
         public void Remove() {
            Purge(cursor);
         }
      }
   }
}
