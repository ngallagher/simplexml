#region License
//
// NodeMap.cs July 2006
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
   /// The <c>NodeMap</c> object represents a map of nodes that
   /// can be set as name value pairs. This typically represents the
   /// attributes that belong to an element and is used as an neutral
   /// way to access an element for either an input or output event.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.Node
   /// </seealso>
   public abstract class NodeMap<T> where T : Node {

       /// <summary>
       /// This is used to acquire the actual node this map represents.
       /// The source node provides further details on the context of
       /// the node, such as the parent name, the namespace, and even
       /// the value in the node. Care should be taken when using this.
       /// </summary>
       /// <returns>
       /// This returns the node that this map represents.
       /// </returns>
       public abstract T Node {
          get;
       }

      /// This is used to get the name of the element that owns the
      /// nodes for the specified map. This can be used to determine
      /// which element the node map belongs to.
      /// </summary>
      /// <returns>
      /// This returns the name of the owning element.
      /// </returns>
      public abstract String Name {
         get;
      }

      /// This is used to acquire the <c>Node</c> mapped to the
      /// given name. This returns a name value pair that represents
      /// either an attribute or element. If no node is mapped to the
      /// specified name then this method will return null.
      /// </summary>
      /// <param name="name">
      /// This is the name of the node to retrieve.
      /// </param>
      /// <returns>
      /// This will return the node mapped to the given name.
      /// </returns>
      public abstract T Get(String name);

      /// <summary>
      /// This is used to remove the <c>Node</c> mapped to the
      /// given name.  This returns a name value pair that represents
      /// either an attribute or element. If no node is mapped to the
      /// specified name then this method will return null.
      /// </summary>
      /// <param name="name">
      /// This is the name of the node to remove.
      /// </param>
      /// <returns>
      /// This will return the node mapped to the given name.
      /// </returns>
      public abstract T Remove(String name);

      /// <summary>
      /// This is used to add a new <c>Node</c> to the map. The
      /// type of node that is created an added is left up to the map
      /// implementation. Once a node is created with the name value
      /// pair it can be retrieved and used.
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
      public abstract T Put(String name, String value);
   }
}
