#region License
//
// Decorator.cs July 2008
//
// Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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
using SimpleFramework.Xml.Stream;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Decorator</c> interface is used to describe an object
   /// that is used to add decorations to an output node. A decoration is
   /// a object that adds information to the output node without any
   /// change to the structure of the node. Decorations can include extra
   /// information like comments and namespaces.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Label
   /// </seealso>
   interface Decorator {
      /// <summary>
      /// This method is used to decorate the provided node. This node
      /// can be either an XML element or an attribute. Decorations that
      /// can be applied to the node by invoking this method include
      /// things like comments and namespaces.
      /// </summary>
      /// <param name="node">
      /// this is the node that is to be decorated by this
      /// </param>
      void Decorate(OutputNode node);
      /// <summary>
      /// This method is used to decorate the provided node. This node
      /// can be either an XML element or an attribute. Decorations that
      /// can be applied to the node by invoking this method include
      /// things like comments and namespaces. This can also be given
      /// another <c>Decorator</c> which is applied before this
      /// decorator, any common data can then be overwritten.
      /// </summary>
      /// <param name="node">
      /// this is the node that is to be decorated by this
      /// </param>
      /// <param name="secondary">
      /// this is a secondary decorator to be applied
      /// </param>
      void Decorate(OutputNode node, Decorator secondary);
   }
}
