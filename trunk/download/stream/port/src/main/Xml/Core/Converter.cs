#region License
//
// Converter.cs July 2006
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
   /// The <c>Converter</c> object serializes and deserializes XML
   /// elements. Serialization of lists, primitives, and compound types
   /// are performed using a converter. Any object read from a converter
   /// will produce a fully deserialized object will all its fields.
   /// The objects written to an XML element populate that element with
   /// attributes an elements according to the objects annotations.
   /// </summary>
   interface Converter {
      /// <summary>
      /// The <c>read</c> method reads an object to a specific type
      /// from the provided node. If the node provided is an attribute
      /// then the object must be a primitive such as a string, integer,
      /// bool, or any of the other Java primitive types.
      /// </summary>
      /// <param name="node">
      /// contains the details used to deserialize the object
      /// </param>
      /// <returns>
      /// a fully deserialized object will all its fields
      /// </returns>
      public Object Read(InputNode node);
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
      /// this is an existing value to deserialize in to
      /// </param>
      /// <returns>
      /// a fully deserialized object will all its fields
      /// </returns>
      public Object Read(InputNode node, Object value);
      /// <summary>
      /// The <c>validate</c> method is used to validate the class
      /// XML schema against an input source. This will traverse the class
      /// fields and methods ensuring that the input XML document contains
      /// a valid structure when compared against the class XML schema.
      /// </summary>
      /// <param name="node">
      /// contains the details used to validate the object
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(InputNode node);
      /// <summary>
      /// The <c>write</c> method writes the fields from the given
      /// object to the XML element. After this has finished the element
      /// contains all attributes and sub-elements from the object.
      /// </summary>
      /// <param name="object">
      /// this is the object to be written to the element
      /// </param>
      /// <param name="node">
      /// this is the element that is to be populated
      /// </param>
      public void Write(OutputNode node, Object object);
   }
}
