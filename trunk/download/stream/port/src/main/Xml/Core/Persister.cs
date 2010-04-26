#region License
//
// Persister.cs July 2006
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
using SimpleFramework.Xml.Filter;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml.Transform;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Persister</c> object is used to provide an implementation
   /// of a serializer. This : the <c>Serializer</c> interface
   /// and enables objects to be persisted and loaded from various sources.
   /// This implementation makes use of <c>Filter</c> objects to
   /// replace template variables within the source XML document. It is fully
   /// thread safe and can be shared by multiple threads without concerns.
   /// <p>
   /// Deserialization is performed by passing an XML schema class into one
   /// of the <c>read</c> methods along with the source of an XML stream.
   /// The read method then reads the contents of the XML stream and builds
   /// the object using annotations within the XML schema class.
   /// <p>
   /// Serialization is performed by passing an object and an XML stream into
   /// one of the <c>write</c> methods. The serialization process will
   /// use the class of the provided object as the schema class. The object
   /// is traversed and all fields are marshalled to the result stream.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Serializer
   /// </seealso>
   public class Persister : Serializer {
      /// <summary>
      /// This is the strategy object used to load and resolve classes.
      /// </summary>
      private readonly Strategy strategy;
      /// <summary>
      /// This support is used to convert the strings encountered.
      /// </summary>
      private readonly Support support;
      /// <summary>
      /// This object is used to format the the generated XML document.
      /// </summary>
      private readonly Format format;
      /// <summary>
      /// This is the style that is used for the serialization process.
      /// </summary>
      private readonly Style style;
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use an empty filter.
      /// This means that template variables will remain unchanged within
      /// the XML document parsed when an object is deserialized.
      /// </summary>
      public Persister() {
         this(new HashMap());
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided format
      /// instructions. The persister uses the <c>Format</c> object
      /// to structure the generated XML. It determines the indent size
      /// of the document and whether it should contain a prolog.
      /// </summary>
      /// <param name="format">
      /// this is used to structure the generated XML
      /// </param>
      public Persister(Format format) {
         this(new TreeStrategy(), format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use a platform filter
      /// object using the overrides within the provided map. This means
      /// that template variables will be replaced firstly with mappings
      /// from within the provided map, followed by system properties.
      /// </summary>
      /// <param name="filter">
      /// this is the map that contains the overrides
      /// </param>
      public Persister(Dictionary filter) {
         this(new PlatformFilter(filter));
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use a platform filter
      /// object using the overrides within the provided map. This means
      /// that template variables will be replaced firstly with mappings
      /// from within the provided map, followed by system properties.
      /// </summary>
      /// <param name="filter">
      /// this is the map that contains the overrides
      /// </param>
      /// <param name="format">
      /// this is the format used to format the documents
      /// </param>
      public Persister(Dictionary filter, Format format) {
         this(new PlatformFilter(filter));
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// </summary>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      public Persister(Filter filter) {
         this(new TreeStrategy(), filter);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// </summary>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      /// <param name="format">
      /// this is used to structure the generated XML
      /// </param>
      public Persister(Filter filter, Format format) {
         this(new TreeStrategy(), filter, format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided matcher
      /// for customizable transformations. The <c>Matcher</c> will
      /// enable the persister to determine the correct way to transform
      /// the types that are not annotated and considered primitives.
      /// </summary>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      public Persister(Matcher matcher) {
         this(new TreeStrategy(), matcher);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided matcher
      /// for customizable transformations. The <c>Matcher</c> will
      /// enable the persister to determine the correct way to transform
      /// the types that are not annotated and considered primitives.
      /// </summary>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      /// <param name="format">
      /// this is used to structure the generated XML
      /// </param>
      public Persister(Matcher matcher, Format format) {
         this(new TreeStrategy(), matcher, format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use a strategy object.
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      public Persister(Strategy strategy) {
         this(strategy, new HashMap());
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use a strategy object.
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="format">
      /// this is used to structure the generated XML
      /// </param>
      public Persister(Strategy strategy, Format format) {
         this(strategy, new HashMap(), format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// </summary>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      public Persister(Filter filter, Matcher matcher) {
         this(new TreeStrategy(), filter, matcher);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// </summary>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      /// <param name="format">
      /// this is used to structure the generated XML
      /// </param>
      public Persister(Filter filter, Matcher matcher, Format format) {
         this(new TreeStrategy(), filter, matcher, format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use a platform filter
      /// object using the overrides within the provided map. This means
      /// that template variables will be replaced firstly with mappings
      /// from within the provided map, followed by system properties.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="data">
      /// this is the map that contains the overrides
      /// </param>
      public Persister(Strategy strategy, Dictionary data) {
         this(strategy, new PlatformFilter(data));
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="data">
      /// the filter data used to replace template variables
      /// </param>
      /// <param name="format">
      /// this is used to format the generated XML document
      /// </param>
      public Persister(Strategy strategy, Dictionary data, Format format) {
         this(strategy, new PlatformFilter(data), format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      public Persister(Strategy strategy, Filter filter) {
         this(strategy, filter, new Format());
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided filter.
      /// This persister will replace all variables encountered when
      /// deserializing an object with mappings found in the filter.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      /// <param name="format">
      /// this is used to format the generated XML document
      /// </param>
      public Persister(Strategy strategy, Filter filter, Format format) {
         this(strategy, filter, new EmptyMatcher(), format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided matcher
      /// for customizable transformations. The <c>Matcher</c> will
      /// enable the persister to determine the correct way to transform
      /// the types that are not annotated and considered primitives.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      public Persister(Strategy strategy, Matcher matcher) {
         this(strategy, new PlatformFilter(), matcher);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided matcher
      /// for customizable transformations. The <c>Matcher</c> will
      /// enable the persister to determine the correct way to transform
      /// the types that are not annotated and considered primitives.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      /// <param name="format">
      /// this is used to format the generated XML document
      /// </param>
      public Persister(Strategy strategy, Matcher matcher, Format format) {
         this(strategy, new PlatformFilter(), matcher, format);
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided matcher
      /// for customizable transformations. The <c>Matcher</c> will
      /// enable the persister to determine the correct way to transform
      /// the types that are not annotated and considered primitives.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      public Persister(Strategy strategy, Filter filter, Matcher matcher) {
         this(strategy, filter, matcher, new Format());
      }
      /// <summary>
      /// Constructor for the <c>Persister</c> object. This is used
      /// to create a serializer object that will use the provided matcher
      /// for customizable transformations. The <c>Matcher</c> will
      /// enable the persister to determine the correct way to transform
      /// the types that are not annotated and considered primitives.
      /// <p>
      /// This persister will use the provided <c>Strategy</c> to
      /// intercept the XML elements in order to read and write persistent
      /// data, such as the class name or version of the document.
      /// </summary>
      /// <param name="strategy">
      /// this is the strategy used to resolve classes
      /// </param>
      /// <param name="matcher">
      /// this is used to customize the transformations
      /// </param>
      /// <param name="filter">
      /// the filter used to replace template variables
      /// </param>
      public Persister(Strategy strategy, Filter filter, Matcher matcher, Format format) {
         this.support = new Support(filter, matcher);
         this.style = format.Style;
         this.strategy = strategy;
         this.format = format;
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, String source) {
         return Read(type, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, File source) {
         return Read(type, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, InputStream source) {
         return Read(type, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, Reader source) {
         return Read(type, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, InputNode source) {
         return Read(type, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, String source, bool strict) {
         return Read(type, new StringReader(source), strict);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, File source, bool strict) {
         InputStream file = new FileInputStream(source);
         try {
            return Read(type, file, strict);
         } finally {
            file.close();
         }
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, InputStream source, bool strict) {
         return Read(type, NodeBuilder.Read(source), strict);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, Reader source, bool strict) {
         return Read(type, NodeBuilder.Read(source), strict);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and convert it into an object
      /// of the specified type. If the XML source cannot be deserialized
      /// or there is a problem building the object graph an exception
      /// is thrown. The instance deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be deserialized from XML
      /// </param>
      /// <param name="node">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document
      /// </returns>
      public <T> T Read(Class<? : T> type, InputNode node, bool strict) {
         return Read(type, node, new Source(strategy, support, style, strict));
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document provided and convert it to an object of the specified
      /// type. If the XML document cannot be deserialized or there is a
      /// problem building the object graph an exception is thrown. The
      /// object graph deserialized is returned.
      /// </summary>
      /// <param name="type">
      /// this is the XML schema class to be deserialized
      /// </param>
      /// <param name="node">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="context">
      /// the contextual object used for deserialization
      /// </param>
      /// <returns>
      /// the object deserialized from the XML document given
      /// </returns>
      public <T> T Read(Class<? : T> type, InputNode node, Context context) {
         return (T)new Traverser(context).Read(node, type);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, String source) {
         return Read(value, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, File source) {
         return Read(value, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, InputStream source) {
         return Read(value, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, Reader source) {
         return Read(value, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, InputNode source) {
         return Read(value, source, true);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, String source, bool strict) {
         return Read(value, new StringReader(source), strict);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, File source, bool strict) {
         InputStream file = new FileInputStream(source);
         try {
            return Read(value, file, strict);
         }finally {
            file.close();
         }
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, InputStream source, bool strict) {
         return Read(value, NodeBuilder.Read(source), strict);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, Reader source, bool strict) {
         return Read(value, NodeBuilder.Read(source), strict);
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="node">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, InputNode node, bool strict) {
         return Read(value, node, new Source(strategy, support, style, strict));
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the object to deserialize the XML in to
      /// </param>
      /// <param name="node">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="context">
      /// the contextual object used for deserialization
      /// </param>
      /// <returns>
      /// the same instance provided is returned when finished
      /// </returns>
      public <T> T Read(T value, InputNode node, Context context) {
         return (T)new Traverser(context).Read(node, value);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, String source) {
         return Validate(type, source, true);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, File source) {
         return Validate(type, source, true);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, InputStream source) {
         return Validate(type, source, true);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, Reader source) {
         return Validate(type, source, true);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, InputNode source) {
         return Validate(type, source, true);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, String source, bool strict) {
         return Validate(type, new StringReader(source), strict);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, File source, bool strict) {
         InputStream file = new FileInputStream(source);
         try {
            return Validate(type, file, strict);
         } finally {
            file.close();
         }
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, InputStream source, bool strict) {
         return Validate(type, NodeBuilder.Read(source), strict);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="source">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, Reader source, bool strict) {
         return Validate(type, NodeBuilder.Read(source), strict);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="node">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="strict">
      /// this determines whether to read in strict mode
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, InputNode node, bool strict) {
         return Validate(type, node, new Source(strategy, support, style, strict));
      }
      /// <summary>
      /// This <c>validate</c> method will validate the contents of
      /// the XML document against the specified XML class schema. This is
      /// used to perform a read traversal of the class schema such that
      /// the document can be tested against it. This is preferred to
      /// reading the document as it does not instantiate the objects or
      /// invoke any callback methods, thus making it a safe validation.
      /// </summary>
      /// <param name="type">
      /// this is the class type to be validated against XML
      /// </param>
      /// <param name="node">
      /// this provides the source of the XML document
      /// </param>
      /// <param name="context">
      /// the contextual object used for deserialization
      /// </param>
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(Class type, InputNode node, Context context) {
         return new Traverser(context).Validate(node, type);
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="root">
      /// this is where the serialized XML is written to
      /// </param>
      public void Write(Object source, OutputNode root) {
         Write(source, root, support);
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="root">
      /// this is where the serialized XML is written to
      /// </param>
      /// <param name="support">
      /// this is the support used to process strings
      /// </param>
      public void Write(Object source, OutputNode root, Support support) {
         Write(source, root, new Source(strategy, support, style));
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="context">
      /// this is a contextual object used for serialization
      /// </param>
      public void Write(Object source, OutputNode node, Context context) {
         new Traverser(context).Write(node, source);
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="out">
      /// this is where the serialized XML is written to
      /// </param>
      public void Write(Object source, File out) {
         OutputStream file = new FileOutputStream(out);
         try {
            Write(source, file);
         }finally {
            file.close();
         }
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="out">
      /// this is where the serialized XML is written to
      /// </param>
      public void Write(Object source, OutputStream out) {
         Write(source, out, "utf-8");
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="out">
      /// this is where the serialized XML is written to
      /// </param>
      /// <param name="charset">
      /// this is the character encoding to be used
      /// </param>
      public void Write(Object source, OutputStream out, String charset) {
         Write(source, new OutputStreamWriter(out, charset));
      }
      /// <summary>
      /// This <c>write</c> method will traverse the provided object
      /// checking for field annotations in order to compose the XML data.
      /// This uses the <c>getClass</c> method on the object to
      /// determine the class file that will be used to compose the schema.
      /// If there is no <c>Root</c> annotation for the class then
      /// this will throw an exception. The root annotation is the only
      /// annotation required for an object to be serialized.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be serialized
      /// </param>
      /// <param name="out">
      /// this is where the serialized XML is written to
      /// </param>
      public void Write(Object source, Writer out) {
         Write(source, NodeBuilder.Write(out, format));
      }
   }
}
