#region License
//
// NamespaceMap.cs July 2008
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
using System.Collections.Generic;
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>NamespaceMap</c> object is used store the namespaces
   /// for an element. Each namespace added to this map can be added
   /// with a prefix. A prefix is added only if the associated reference
   /// has not been added to a parent element. If a parent element has
   /// the associated reference, then the parents prefix is the one that
   /// will be returned when requested from this map.
   /// </summary>
   public interface NamespaceMap  {

      /// <summary>
      /// This is the prefix that is associated with the source element.
      /// If the source element does not contain a namespace reference
      /// then this will return its parents namespace. This ensures
      /// that if a namespace has been declared its child elements will
      /// inherit its prefix.
      /// </summary>
      /// <returns>
      /// This returns the prefix that is currently in scope.
      /// </returns>
      String Prefix {
         get;
      }

      /// <summary>
      /// This returns an iterator for the namespace of all the nodes
      /// in this <c>NamespaceMap</c>. This allows the namespaces
      /// to be iterated within a for each loop in order to extract the
      /// prefix values associated with the map.
      /// </summary>
      /// <returns>
      /// This returns the namespaces contained in this map.
      /// </returns>
      List<String> References {
         get;
      }

      /// This acquires the prefix for the specified namespace reference.
      /// If the namespace reference has been set on this node with a
      /// given prefix then that prefix is returned, however if it has
      /// not been set this will search the parent elements to find the
      /// prefix that is in scope for the specified reference.
      /// </summary>
      /// <param name="reference">
      /// The reference to find a matching prefix for.
      /// </param>
      /// <returns>
      /// This will return the prefix that is is scope.
      /// </returns>
      String Get(String reference);

      /// <summary>
      /// This is used to remove the prefix that is matched to the
      /// given reference. If no prefix is matched to the reference then
      /// this will silently return. This will only remove mappings
      /// from the current map, and will ignore the parent nodes.
      /// </summary>
      /// <param name="reference">
      /// This is the reference that is to be removed.
      /// </param>
      /// <returns>
      /// This returns the prefix that was matched to this.
      /// </returns>
      String Remove(String reference);

      /// <summary>
      /// This is used to add the namespace reference to the namespace
      /// map. If the namespace has been added to a parent node then
      /// this will not add the reference. The prefix added to the map
      /// will be the default namespace, which is an empty prefix.
      /// </summary>
      /// <param name="reference">
      /// This is the reference to be added.
      /// </param>
      /// <returns>
      /// This returns the prefix that has been replaced.
      /// </returns>
      String Put(String reference);

      /// <summary>
      /// This is used to add the namespace reference to the namespace
      /// map. If the namespace has been added to a parent node then
      /// this will not add the reference.
      /// </summary>
      /// <param name="reference">
      /// This is the reference to be added.
      /// </param>
      /// <param name="prefix">
      /// This is the prefix to be added to the reference.
      /// </param>
      /// <returns>
      /// This returns the prefix that has been replaced.
      /// </returns>
      String Put(String reference, String prefix);
   }
}
