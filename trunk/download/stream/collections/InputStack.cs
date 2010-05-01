#region License
//
// InputStack.cs July 2006
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
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>InputStack</c> is used to keep track of the nodes
   /// that have been read from the document. This ensures that when
   /// nodes are read from the source document that the reader can tell
   /// whether a child node for a given <c>InputNode</c> can be
   /// created. Each created node is pushed, and popped when ended.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.InputNode
   /// </seealso>
   class InputStack : Stack<InputNode> {

      /// <summary>
      /// Constructor for the <c>InputStack</c> object. This is
      /// used to create a stack that can be used to keep track of the
      /// elements that have been read from the source XML document.
      /// </summary>
      public InputStack() : base(6) {
      }

      /// <summary>
      /// This is used to determine if the input stack is empty. The
      /// stack is considered empty if it has no elements in it. This
      /// is used to determine the relevance of a particular node.
      /// </summary>
      /// <returns>
      /// This will return true if there are no nodes in the stack.
      /// </returns>
      public bool IsEmpty() {
         return Count == 0;
      }

      /// <summary>
      /// This is used to determine if the specified node is relevant
      /// with respect to the state of the input stack. This returns
      /// true if there are no elements in the stack, which accounts
      /// for a new root node. Also this returns true if the specified
      /// node exists within the stack and is thus an active node.
      /// </summary>
      /// <param name="value">
      /// This is the input node value to be checked.
      /// </param>
      /// <returns>
      /// Returns true if the node is relevant in the stack.
      /// </returns>
      public bool IsRelevant(InputNode value) {
         return Contains(value) || IsEmpty();
      }
   }
}
