#region License
//
// Converter.cs January 2010
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
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>Converter</c> object is used to convert an object
   /// to XML by intercepting the normal serialization process. When
   /// serializing an object the <c>write</c> method is invoked.
   /// This is provided with the object instance to be serialized and
   /// the <c>OutputNode</c> to use to write the XML. Values
   /// can be taken from the instance and transferred to the node.
   /// <p>
   /// For deserialization the <c>read</c> method is invoked.
   /// This is provided with the <c>InputNode</c>, which can be
   /// used to read the elements and attributes representing the
   /// member data of the object being deserialized. Once the object
   /// has been instantiated it must be returned.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.AnnotationStrategy
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.Util.RegistryStrategy
   /// </seealso>
   public interface Converter<T> {
      /// <summary>
      /// This <c>read</c> method is used to deserialize an object
      /// from the source XML. The deserialization is performed using
      /// the XML node provided. This node can be used to read the XML
      /// elements and attributes in any format required. Once all of
      /// the data has been extracted an instance must be returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to deserialize the object from
      /// </param>
      /// <returns>
      /// the object instance resulting from the deserialization
      /// </returns>
      T Read(InputNode node);
      /// <summary>
      /// This <c>write</c> method is used to serialize an object
      /// to XML. The serialization should be performed in such a way
      /// that all of the objects values are represented by an element
      /// or attribute of the provided node. This ensures that it can
      /// be fully deserialized at a later time.
      /// </summary>
      /// <param name="node">
      /// this is the node to serialized to object to
      /// </param>
      /// <param name="value">
      /// this is the value that is to be serialized
      /// </param>
      void Write(OutputNode node, T value);
   }
}
