#region License
//
// Stack.cs July 2006
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
   /// The <c>Stack</c> object is used to provide a lightweight
   /// stack implementation. To ensure top performance this stack is not
   /// synchronized and keeps track of elements using an array list.
   /// A null from either a <c>pop</c> or <c>top</c> means
   /// that the stack is empty. This allows the stack to be peeked at
   /// even if it has not been populated with anything yet.
   /// </summary>
   class Stack<T> : List<T> {
      /// <summary>
      /// Constructor for the <c>Stack</c> object. This is used
      /// to create a stack that can be used to keep track of values
      /// in a first in last out manner. Typically this is used to
      /// determine if an XML element is in or out of context.
      /// </summary>
      /// <param name="size">
      /// this is the initial size of the stack to use
      /// </param>
      public Stack(int size) : base(size) {
      }
      /// <summary>
      /// This is used to remove the element from the top of this
      /// stack. If the stack is empty then this will return null, as
      /// such it is not advisable to push null elements on the stack.
      /// </summary>
      /// <returns>
      /// this returns the node element the top of the stack
      /// </returns>
      public T Pop() {
         int size = Count;
         if(Count <= 0) {
            return default(T);
         }
         T value = this[size - 1];
         RemoveAt(size - 1);
         return value;
      }
      /// <summary>
      /// This is used to peek at the element from the top of this
      /// stack. If the stack is empty then this will return null, as
      /// such it is not advisable to push null elements on the stack.
      /// </summary>
      /// <returns>
      /// this returns the node element the top of the stack
      /// </returns>
      public T Top() {
         int size = Count;
         if(size <= 0) {
            return default(T);
         }
         return this[size - 1];
      }
      /// <summary>
      /// This is used to acquire the node from the bottom of the stack.
      /// If the stack is empty then this will return null, as such it
      /// is not advisable to push null elements on the stack.
      /// </summary>
      /// <returns>
      /// this returns the element from the bottom of the stack
      /// </returns>
      public T Bottom() {
         int size = Count;
         if(size <= 0) {
            return default(T);
         }
         return this[0];
      }
      /// <summary>
      /// This method is used to add an element to the top of the stack.
      /// Although it is possible to add a null element to the stack it
      /// is not advisable, as null is returned when the stack is empty.
      /// </summary>
      /// <param name="value">
      /// this is the element to add to the stack
      /// </param>
      /// <returns>
      /// this returns the actual node that has just been added
      /// </returns>
      public T Push(T value) {
         Add(value);
         return value;
      }
   }
}
