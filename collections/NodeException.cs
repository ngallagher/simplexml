#region License
//
// NodeException.cs July 2006
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
   /// The <c>NodeException</c> is thrown to indicate the state of
   /// either the input node or output node being invalid. Typically
   /// this is thrown if some illegal operation is requested.
   /// </summary>
   public class NodeException : Exception {

      /// <summary>
      /// Constructor for the <c>NodeException</c> object. This is
      /// given the message to be reported when the exception is thrown.
      /// </summary>
      /// <param name="text">
      /// A message describing the exception that has occured
      /// </param>
      public NodeException(String text) : base(text) {
      }
   }
}
