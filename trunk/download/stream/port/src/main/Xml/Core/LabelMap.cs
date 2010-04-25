#region License
//
// LabelMap.cs July 2006
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
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>LabelMap</c> object represents a map that contains
   /// string label mappings. This is used for convenience as a typedef
   /// like construct to avoid having declare the generic type whenever
   /// it is referenced. Also this allows <c>Label</c> values
   /// from the map to be iterated within for each loops.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Label
   /// </seealso>
   class LabelMap : LinkedHashMap<String, Label> : Iterable<Label> {
      /// <summary>
      /// This is the scanner object that represents the scanner used.
      /// </summary>
      private readonly Scanner source;
      /// <summary>
      /// Constructor for the <c>LabelMap</c> object is used to
      /// create an empty map. This is used for convenience as a typedef
      /// like construct which avoids having to use the generic type.
      /// </summary>
      public LabelMap(Scanner source) {
         this.source = source;
      }
      /// <summary>
      /// This allows the <c>Label</c> objects within the label map
      /// to be iterated within for each loops. This will provide all
      /// remaining label objects within the map. The iteration order is
      /// not maintained so label objects may be given in any sequence.
      /// </summary>
      /// <returns>
      /// this returns an iterator for existing label objects
      /// </returns>
      public Iterator<Label> Iterator() {
         return values().Iterator();
      }
      /// <summary>
      /// This performs a <c>remove</c> that will remove the label
      /// from the map and return that label. This method allows the
      /// values within the map to be exclusively taken one at a time,
      /// which enables the user to determine which labels remain.
      /// </summary>
      /// <param name="name">
      /// this is the name of the element of attribute
      /// </param>
      /// <returns>
      /// this is the label object representing the XML node
      /// </returns>
      public Label Take(String name) {
         return remove(name);
      }
      /// <summary>
      /// This method is used to clone the label map such that mappings
      /// can be maintained in the original even if they are modified
      /// in the clone. This is used to that the <c>Schema</c> can
      /// remove mappings from the label map as they are visited.
      /// </summary>
      /// <param name="context">
      /// this is the context used to style the XML names
      /// </param>
      /// <returns>
      /// this returns a cloned representation of this map
      /// </returns>
      public LabelMap Clone(Context context) {
         LabelMap clone = new LabelMap(source);
         for(Label label : this) {
            String name = label.getName(context);
            clone.put(name, label);
         }
         return clone;
      }
      /// <summary>
      /// This method is used to determine whether strict mappings are
      /// required. Strict mapping means that all labels in the class
      /// schema must match the XML elements and attributes in the
      /// source XML document. When strict mapping is disabled, then
      /// XML elements and attributes that do not exist in the schema
      /// class will be ignored without breaking the parser.
      /// </summary>
      /// <param name="context">
      /// this is used to determine if this is strict
      /// </param>
      /// <returns>
      /// true if strict parsing is enabled, false otherwise
      /// </returns>
      public bool IsStrict(Context context) {
         return context.IsStrict() && source.IsStrict();
      }
   }
}
