#region License
//
// VisitorStrategy.cs January 2010
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>VisitorStrategy</c> object is a simplification of a
   /// strategy, which allows manipulation of the serialization process.
   /// Typically implementing a <c>Strategy</c> is impractical as
   /// it requires the implementation to determine the type a node
   /// represents. Instead it is often easier to visit each node that
   /// is being serialized or deserialized and manipulate it so that
   /// the resulting XML can be customized.
   /// <p>
   /// To perform customization in this way a <c>Visitor</c> can
   /// be implemented. This can be passed to this strategy which will
   /// ensure the visitor is given each XML element as it is either
   /// being serialized or deserialized. Such an inversion of control
   /// allows the nodes to be manipulated with little effort. By
   /// default this used <c>TreeStrategy</c> object as a default
   /// strategy to delegate to. However, any strategy can be used.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Visitor
   /// </seealso>
   public class VisitorStrategy : Strategy {
      /// <summary>
      /// This is the strategy that is delegated to by this strategy.
      /// </summary>
      private readonly Strategy strategy;
      /// <summary>
      /// This is the visitor that is used to intercept serialization.
      /// </summary>
      private readonly Visitor visitor;
      /// <summary>
      /// Constructor for the <c>VisitorStrategy</c> object. This
      /// strategy requires a visitor implementation that can be used
      /// to intercept the serialization and deserialization process.
      /// </summary>
      /// <param name="visitor">
      /// this is the visitor used for interception
      /// </param>
      public VisitorStrategy(Visitor visitor) {
         this(visitor, new TreeStrategy());
      }
      /// <summary>
      /// Constructor for the <c>VisitorStrategy</c> object. This
      /// strategy requires a visitor implementation that can be used
      /// to intercept the serialization and deserialization process.
      /// </summary>
      /// <param name="visitor">
      /// this is the visitor used for interception
      /// </param>
      /// <param name="strategy">
      /// this is the strategy to be delegated to
      /// </param>
      public VisitorStrategy(Visitor visitor, Strategy strategy) {
         this.strategy = strategy;
         this.visitor = visitor;
      }
      /// <summary>
      /// This method will read with  an internal strategy after it has
      /// been intercepted by the visitor. Interception of the XML node
      /// before it is delegated to the internal strategy allows the
      /// visitor to change some attributes or details before the node
      /// is interpreted by the strategy.
      /// </summary>
      /// <param name="type">
      /// this is the type of the root element expected
      /// </param>
      /// <param name="node">
      /// this is the node map used to resolve an override
      /// </param>
      /// <param name="map">
      /// this is used to maintain contextual information
      /// </param>
      /// <returns>
      /// the value that should be used to describe the instance
      /// </returns>
      public Value Read(Type type, NodeMap<InputNode> node, Dictionary map) {
         if(visitor != null) {
            visitor.Read(type, node);
         }
         return strategy.Read(type, node, map);
      }
      /// <summary>
      /// This method will write with an internal strategy before it has
      /// been intercepted by the visitor. Interception of the XML node
      /// before it is delegated to the internal strategy allows the
      /// visitor to change some attributes or details before the node
      /// is interpreted by the strategy.
      /// </summary>
      /// <param name="type">
      /// this is the type of the root element expected
      /// </param>
      /// <param name="node">
      /// this is the node map used to resolve an override
      /// </param>
      /// <param name="map">
      /// this is used to maintain contextual information
      /// </param>
      /// <returns>
      /// the value that should be used to describe the instance
      /// </returns>
      public bool Write(Type type, Object value, NodeMap<OutputNode> node, Dictionary map) {
         bool result = strategy.Write(type, value, node, map);
         if(visitor != null) {
            visitor.Write(type, node);
         }
         return result;
      }
   }
}
