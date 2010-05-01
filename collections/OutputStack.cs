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
using System.Collections;
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
   class OutputStack : List<OutputNode> {

      /// <summary>
      /// Represents the set of nodes that have not been committed.
      /// </summary>
      private readonly ArrayList active;

      /// <summary>
      /// Constructor for the <c>OutputStack</c> object. This is
      /// used to create a stack that can be used to keep track of the
      /// elements that have been written to the XML document.
      /// </summary>
      public OutputStack(ArrayList active) {
         this.active = active;
      }

      /// <summary>
      /// This is used to determine if there are any nodes in this stack.
      /// If there are no nodes in the stack then this will return true.
      /// </summary>
      /// <returns>
      /// This returns true if there no more nodes in the stack.
      /// </returns>
      public bool IsEmpty() {
         return Count == 0;
      }

      /// <summary>
      /// This is used to remove the <c>OutputNode</c> from the
      /// top of the output stack. This is used when an element has been
      /// ended and the output writer wants to block child creation.
      /// </summary>
      /// <returns>
      /// This returns the node from the top of the stack.
      /// </returns>
      public OutputNode Pop() {
         int size = Count;

         if(size <= 0) {
            return null;
         }
         return Purge(size - 1);
      }

      /// <summary>
      /// This is used to acquire the <c>OutputNode</c> from the top
      /// of the output stack. This is used when the writer wants to
      /// determine the current element written to the XML document.
      /// </summary>
      /// <returns>
      /// This returns the node from the top of the stack.
      /// </returns>
      public OutputNode Top() {
         int size = Count;

         if(size <= 0) {
            return null;
         }
         return this[size - 1];
      }

      /// <summary>
      /// This is used to acquire the <c>OutputNode</c> from the
      /// bottom of the output stack. This is used when the writer wants
      /// to determine the root element for the written XML document.
      /// </summary>
      /// <returns>
      /// This returns the node from the bottom of the stack.
      /// </returns>
      public OutputNode Bottom() {
         int size = Count;

         if(size <= 0) {
            return null;
         }
         return this[0];
      }

      /// <summary>
      /// This method is used to add an <c>OutputNode</c> to the top
      /// of the stack. This is used when an element is written to
      /// the XML document, and allows the writer to determine if a
      /// child node can be created from a given output node.
      /// </summary>
      /// <param name="value">
      /// This is the output node to add to the stack.
      /// </param>
      public OutputNode Push(OutputNode value) {
         active.Add(value);
         Add(value);
         return value;
      }

      /// <summary>
      /// The <c>Remove</c> method is used to remove a match from the
      /// provided position. This will check to see if there is a node
      /// at the specified index, if there is then it is removed.
      /// </summary>
      /// <param name="index">
      /// The index of the node that is to be removed.
      /// </param>
      /// <returns>
      /// Returns the node removed from the specified index.
      /// </returns>
      public OutputNode Remove(int index) {
         OutputNode node = this[index];

         if(node != null) {
            RemoveAt(index);
         }
         return node;
      }

      /// <summary>
      /// The <c>Purge</c> method is used to purge a match from the
      /// provided position. This also ensures that the active set
      /// has the node removed so that it is no longer relevant.
      /// </summary>
      /// <param name="index">
      /// The index of the node that is to be removed.
      /// </param>
      /// <returns>
      /// Returns the node removed from the specified index.
      /// </returns>
      public OutputNode Purge(int index) {
         OutputNode node = Remove(index);

         if(node != null){
            active.Remove(node);
         }
         return node;
      }
   }
}
