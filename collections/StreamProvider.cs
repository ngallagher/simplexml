#region License
//
// StreamProvider.cs January 2010
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
using InputStream = System.IO.Stream;
using System.IO;
using System.Xml;
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>StreamProvider</c> object is used to provide event
   /// reader implementations for StAX. Wrapping the mechanics of the
   /// StAX framework within a <c>Provider</c> ensures that it can
   /// be plugged in without any dependencies. This allows other parsers
   /// to be swapped in should there be such a requirement.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.StreamProvider
   /// </seealso>
   class StreamProvider : Provider {

      /// <summary>
      /// Constructor for the <c>StreamProvider</c> object. This
      /// is used to instantiate a parser factory that will be used to
      /// create parsers when requested. Instantiating the factory up
      /// front also checks that the framework is fully supported.
      /// </summary>
      public StreamProvider() {
      }

      /// <summary>
      /// This provides an <c>EventReader</c> that will read from
      /// the specified input stream. When reading from an input stream
      /// the character encoding should be taken from the XML prolog or
      /// it should default to the UTF-8 character encoding.
      /// </summary>
      /// <param name="source">
      /// This is the stream to read the document with.
      /// </param>
      /// <returns>
      /// This is used to return the event reader implementation.
      /// </returns>
      public EventReader Provide(InputStream source) {
         return Provide(XmlReader.Create(source));
      }

      /// <summary>
      /// This provides an <c>EventReader</c> that will read from
      /// the specified reader. When reading from a reader the character
      /// encoding should be the same as the source XML document.
      /// </summary>
      /// <param name="source">
      /// This is the reader to read the document with.
      /// </param>
      /// <returns>
      /// This is used to return the event reader implementation.
      /// </returns>
      public EventReader Provide(TextReader source) {
         return Provide(XmlReader.Create(source));
      }

      /// <summary>
      /// This provides an <c>EventReader</c> that will read from
      /// the specified reader. The returned event reader is basically
      /// a wrapper for the provided StAX implementation.
      /// </summary>
      /// <param name="source">
      /// This is the reader to read the document with.
      /// </param>
      /// <returns>
      /// This is used to return the event reader implementation.
      /// </returns>
      public EventReader Provide(XmlReader source) {
         return new StreamReader(source);
      }
   }
}