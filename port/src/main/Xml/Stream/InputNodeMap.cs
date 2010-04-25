#region License
//
// InputNodeMap.cs July 2006
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
   /// The <c>InputNodeMap</c> object represents a map to contain
   /// attributes used by an input node. This can be used as an empty
   /// node map, it can be used to extract its values from a start
   /// element. This creates <c>InputAttribute</c> objects for
   /// each node added to the map, these can then be used by an element
   /// input node to represent attributes as input nodes.
   /// </summary>
   class InputNodeMap : LinkedHashMap<String, InputNode> : NodeMap<InputNode> {
      /// <summary>
      /// This is the source node that this node map belongs to.
      /// </summary>
      private readonly InputNode source;
      /// <summary>
      /// Constructor for the <c>InputNodeMap</c> object. This
      /// is used to create an empty input node map, which will create
      /// <c>InputAttribute</c> object for each inserted node.
      /// </summary>
      /// <param name="source">
      /// this is the node this node map belongs to
      /// </param>
      protected InputNodeMap(InputNode source) {
         this.source = source;
      }
      /// <summary>
      /// Constructor for the <c>InputNodeMap</c> object. This
      /// is used to create an input node map, which will be populated
      /// with the attributes from the <c>StartElement</c> that
      /// is specified.
      /// </summary>
      /// <param name="source">
      /// this is the node this node map belongs to
      /// </param>
      /// <param name="element">
      /// the element to populate the node map with
      /// </param>
      public InputNodeMap(InputNode source, EventNode element) {
         this.source = source;
         this.Build(element);
      }
      /// <summary>
      /// This is used to insert all attributes belonging to the start
      /// element to the map. All attributes acquired from the element
      /// are converted into <c>InputAttribute</c> objects so
      /// that they can be used as input nodes by an input node.
      /// </summary>
      /// <param name="element">
      /// the element to acquire attributes from
      /// </param>
      public void Build(EventNode element) {
         for(Attribute entry : element) {
            InputAttribute value = new InputAttribute(source, entry);
            if(!entry.isReserved()) {
               Put(value.Name, value);
            }
         }
      }
      /// <summary>
      /// This is used to acquire the actual node this map represents.
      /// The source node provides further details on the context of
      /// the node, such as the parent name, the namespace, and even
      /// the value in the node. Care should be taken when using this.
      /// </summary>
      /// <returns>
      /// this returns the node that this map represents
      /// </returns>
      public InputNode Node {
         get {
             return source;
         }
      }
      //public InputNode GetNode() {
      //    return source;
      //}
      /// This is used to get the name of the element that owns the
      /// nodes for the specified map. This can be used to determine
      /// which element the node map belongs to.
      /// </summary>
      /// <returns>
      /// this returns the name of the owning element
      /// </returns>
      public String Name {
         get {
            return source.Name;
         }
      }
      //public String GetName() {
      //   return source.Name;
      //}
      /// This is used to add a new <c>InputAttribute</c> node to
      /// the map. The created node can be used by an input node to
      /// to represent the attribute as another input node. Once the
      /// node is created it can be acquired using the specified name.
      /// </summary>
      /// <param name="name">
      /// this is the name of the node to be created
      /// </param>
      /// <param name="value">
      /// this is the value to be given to the node
      /// </param>
      /// <returns>
      /// this returns the node that has just been added
      /// </returns>
      public InputNode Put(String name, String value) {
         InputNode node = new InputAttribute(source, name, value);
         if(name != null) {
            Put(name, node);
         }
         return node;
      }
      /// <summary>
      /// This is used to remove the <c>Node</c> mapped to the
      /// given name.  This returns a name value pair that represents
      /// an attribute. If no node is mapped to the specified name
      /// then this method will return a null value.
      /// </summary>
      /// <param name="name">
      /// this is the name of the node to remove
      /// </param>
      /// <returns>
      /// this will return the node mapped to the given name
      /// </returns>
      public InputNode Remove(String name) {
         return super.Remove(name);
      }
      /// <summary>
      /// This is used to acquire the <c>Node</c> mapped to the
      /// given name. This returns a name value pair that represents
      /// an attribute. If no node is mapped to the specified name
      /// then this method will return a null value.
      /// </summary>
      /// <param name="name">
      /// this is the name of the node to retrieve
      /// </param>
      /// <returns>
      /// this will return the node mapped to the given name
      /// </returns>
      public InputNode Get(String name) {
         return super.Get(name);
      }
      /// <summary>
      /// This returns an iterator for the names of all the nodes in
      /// this <c>NodeMap</c>. This allows the names to be
      /// iterated within a for each loop in order to extract nodes.
      /// </summary>
      /// <returns>
      /// this returns the names of the nodes in the map
      /// </returns>
      public Iterator<String> Iterator() {
         return keySet().Iterator();
      }
   }
}
