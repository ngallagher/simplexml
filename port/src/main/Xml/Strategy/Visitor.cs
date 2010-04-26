#region License
//
// Visitor.cs January 2010
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
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>Visitor</c> interface represents an object that is
   /// used to visit each XML element during serialization. For the
   /// deserialization process each XML element is visited before
   /// control is returned to the serializer. This allows a visitor
   /// implementation to perform some operation based on the node
   /// that is being deserialized. Typically a visitor is used to
   /// edit the node, for example it may remove or insert attributes.
   /// <p>
   /// In effect this can act much like a transformer that sits
   /// between a <c>Strategy</c> implementation and the core
   /// serializer. It enables interception and manipulation of the
   /// node so that the resulting XML document can be customized in
   /// a way that can not be performed by the underlying strategy.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.VisitorStrategy
   /// </seealso>
   public interface Visitor {
      /// <summary>
      /// This is used to intercept an XML element before it is read
      /// by the underlying <c>Strategy</c> implementation. When
      /// a node is intercepted it can be manipulated in such a way
      /// that its semantics change. For example, this could be used
      /// to change the way a "class" attribute is represented, which
      /// would allow the XML to appear in a language neutral format.
      /// </summary>
      /// <param name="type">
      /// this is the type that represents the element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be intercepted
      /// </param>
      void Read(Type type, NodeMap<InputNode> node);
      /// <summary>
      /// This is used to intercept an XML element after it is written
      /// by the underlying <c>Strategy</c> implementation. When
      /// a node is intercepted it can be manipulated in such a way
      /// that its semantics change. For example, this could be used
      /// to change the way a "class" attribute is represented, which
      /// would allow the XML to appear in a language neutral format.
      /// </summary>
      /// <param name="type">
      /// this is the type that represents the element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be intercepted
      /// </param>
      void Write(Type type, NodeMap<OutputNode> node);
   }
}
