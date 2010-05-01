#region License
//
// OutputNodeMap.cs July 2006
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
   /// The <c>OutputNodeMap</c> is used to collect attribute nodes
   /// for an output node. This will create a generic node to add to the
   /// map. The nodes created will be used by the output node to write
   /// attributes for an element.
   /// </summary>
   class OutputNodeMap : LinkedHashMap<String, OutputNode>, NodeMap<OutputNode> {

      /// <summary>
      /// This is the source node that this node map belongs to.
      /// </summary>
      private readonly OutputNode source;

      /// <summary>
      /// Constructor for the <c>OutputNodeMap</c> object. This is
      /// used to create a node map that is used to create and collect
      /// nodes, which will be used as attributes for an output element.
      /// </summary>
      public OutputNodeMap(OutputNode source) {
         this.source = source;
      }

      /// <summary>
      /// This is used to acquire the actual node this map represents.
      /// The source node provides further details on the context of
      /// the node, such as the parent name, the namespace, and even
      /// the value in the node. Care should be taken when using this.
      /// </summary>
      /// <returns>
      /// This returns the node that this map represents.
      /// </returns>
      public OutputNode Node {
         get {
             return source;
         }
      }

      /// This is used to get the name of the element that owns the
      /// nodes for the specified map. This can be used to determine
      /// which element the node map belongs to.
      /// </summary>
      /// <returns>
      /// This returns the name of the owning element.
      /// </returns>
      public String Name {
         get {
            return source.Name;
         }
      }

      /// <summary>
      /// This returns an iterator for the names of all the nodes in
      /// this <c>OutputNodeMap</c>. This allows the names to be
      /// iterated within a for each loop in order to extract nodes.
      /// </summary>
      /// <returns>
      /// this returns the names of the nodes in the map
      /// </returns>
      public List<String> Names {
         get {
            return Keys;
         }
      }

      /// This is used to add a new <c>Node</c> to the map. The
      /// node that is created is a simple name value pair. Once the
      /// node is created it can be retrieved by its given name.
      /// </summary>
      /// <param name="name">
      /// This is the name of the node to be created.
      /// </param>
      /// <param name="value">
      /// This is the value to be given to the node.
      /// </param>
      /// <returns>
      /// This is the node that has been added to the map.
      /// </returns>
      public OutputNode Put(String name, String value) {
         OutputNode node = new OutputAttribute(source, name, value);

         if(source != null) {
            Put(name, node);
         }
         return node;
      }

      /// <summary>
      /// This is used to remove the <c>Node</c> mapped to the
      /// given name.  This returns a name value pair that represents
      /// an attribute. If no node is mapped to the specified name
      /// then this method will a return null value.
      /// </summary>
      /// <param name="name">
      /// This is the name of the node to remove.
      /// </param>
      /// <returns>
      /// This will return the node mapped to the given name.
      /// </returns>
      public override OutputNode Remove(String name) {
         return base.Remove(name);
      }

      /// <summary>
      /// This is used to acquire the <c>Node</c> mapped to the
      /// given name. This returns a name value pair that represents
      /// an element. If no node is mapped to the specified name then
      /// this method will return a null value.
      /// </summary>
      /// <param name="name">
      /// This is the name of the node to retrieve.
      /// </param>
      /// <returns>
      /// This will return the node mapped to the given name.
      /// </returns>
      public override OutputNode Get(String name) {
         return base.Get(name);
      }
   }
}
