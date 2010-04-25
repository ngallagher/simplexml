#region License
//
// NodeStack.cs January 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion
#region Using directives
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   import org.w3c.dom.Node;
   /// <summary>
   /// The <c>NodeStack</c> object is used to represent a stack
   /// of DOM nodes. Stacking DOM nodes is required to determine where
   /// within a stream of nodes you currently are. It allows a reader
   /// to produce end events when a node is popped from the stack.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.DocumentReader
   /// </seealso>
   class NodeStack : Stack<Node>{
      /// <summary>
      /// Constructor for the <c>NodeStack</c> object. This will
      /// create a stack for holding DOM nodes. To ensure that the
      /// initial size is enough to cope with typical XML documents it
      /// is set large enough to cope with reasonably deep elements.
      /// </summary>
      public NodeStack() {
         super(6);
      }
   }
}
