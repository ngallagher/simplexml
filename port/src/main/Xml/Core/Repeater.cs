#region License
//
// Repeater.cs July 2007
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
using SimpleFramework.Xml.Stream;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Repeater</c> interface is used to for converters that
   /// can repeat a read on a given element. This is typically used for
   /// inline lists and maps so that the elements can be mixed within the
   /// containing element. This ensures a more liberal means of writing
   /// the XML such that elements not grouped in a containing XML element
   /// can be declared throughout the document.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.CompositeInlineMap
   /// </seealso>
   interface Repeater : Converter {
      /// <summary>
      /// The <c>read</c> method reads an object to a specific type
      /// from the provided node. If the node provided is an attribute
      /// then the object must be a primitive such as a string, integer,
      /// bool, or any of the other Java primitive types.
      /// </summary>
      /// <param name="node">
      /// contains the details used to deserialize the object
      /// </param>
      /// <param name="value">
      /// this is the value to read the objects in to
      /// </param>
      /// <returns>
      /// a fully deserialized object will all its fields
      /// </returns>
      public Object Read(InputNode node, Object value);
   }
}
