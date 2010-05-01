#region License
//
// NodeBuilder.cs July 2006
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
using InputStream = System.IO.Stream;
using System.IO;
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>NodeBuilder</c> object is used to create either an
   /// input node or an output node for a given source or destination.
   /// If an <c>InputNode</c> is required for reading an XML
   /// document then a reader must be provided to read the content from.
   /// <p>
   /// If an <c>OutputNode</c> is required then a destination is
   /// required. The provided output node can be used to generate well
   /// formed XML to the specified writer.
   /// </summary>
   public sealed class NodeBuilder {

      /// <summary>
      /// This is the XML provider implementation that creates readers.
      /// </summary>
      private static Provider provider = ProviderFactory.Instance;

      /// <summary>
      /// This is used to create an <c>InputNode</c> that can be
      /// used to read XML from the specified stream. The stream will
      /// be positioned at the root element in the XML document.
      /// </summary>
      /// <param name="source">
      /// This contains the contents of the XML source.
      /// </param>
      public static InputNode Read(InputStream source) {
         return Read(provider.Provide(source));
      }

      /// <summary>
      /// This is used to create an <c>InputNode</c> that can be
      /// used to read XML from the specified reader. The reader will
      /// be positioned at the root element in the XML document.
      /// </summary>
      /// <param name="source">
      /// This contains the contents of the XML source.
      /// </param>
      public static InputNode Read(TextReader source) {
         return Read(provider.Provide(source));
      }

      /// <summary>
      /// This is used to create an <c>InputNode</c> that can be
      /// used to read XML from the specified reader. The reader will
      /// be positioned at the root element in the XML document.
      /// </summary>
      /// <param name="source">
      /// This contains the contents of the XML source.
      /// </param>
      private static InputNode Read(EventReader source) {
         return new NodeReader(source).ReadRoot();
      }

      /// <summary>
      /// This is used to create an <c>OutputNode</c> that can be
      /// used to write a well formed XML document. The writer specified
      /// will have XML elements, attributes, and text written to it as
      /// output nodes are created and populated.
      /// </summary>
      /// <param name="result">
      /// This contains the result of the generated XML.
      /// </param>
      public static OutputNode Write(TextWriter result) {
         return Write(result, new Format());
      }

      /// <summary>
      /// This is used to create an <c>OutputNode</c> that can be
      /// used to write a well formed XML document. The writer specified
      /// will have XML elements, attributes, and text written to it as
      /// output nodes are created and populated.
      /// </summary>
      /// <param name="result">
      /// This contains the result of the generated XML.
      /// </param>
      /// <param name="format">
      /// This is the format to use for the document.
      /// </param>
      public static OutputNode Write(TextWriter result, Format format) {
         return new NodeWriter(result, format).WriteRoot();
      }
   }
}
