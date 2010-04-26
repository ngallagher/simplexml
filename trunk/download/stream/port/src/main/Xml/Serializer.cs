#region License
//
// Serializer.cs July 2006
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
namespace SimpleFramework.Xml {
   /// <summary>
   /// The <c>Serializer</c> interface is used to represent objects
   /// that can serialize and deserialize objects to an from XML. This
   /// exposes several <c>read</c> and <c>write</c> methods
   /// that can read from and write to various sources. Typically an
   /// object will be read from an XML file and written to some other
   /// file or stream.
   /// <p>
   /// An implementation of the <c>Serializer</c> interface is free
   /// to use any desired XML parsing framework. If a framework other
   /// than the Java streaming API for XML is required then it should be
   /// wrapped within the <c>SimpleFramework.Xml.Stream</c> API,
   /// which offers a framework neutral facade.
   /// </summary>
   public interface Serializer {
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
      <T> T Read(Class<? : T> type, String source);
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
      <T> T Read(Class<? : T> type, File source);
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
      <T> T Read(Class<? : T> type, InputStream source);
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
      <T> T Read(Class<? : T> type, Reader source);
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
      <T> T Read(Class<? : T> type, InputNode source);
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
      <T> T Read(Class<? : T> type, String source, bool strict);
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
      <T> T Read(Class<? : T> type, File source, bool strict);
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
      <T> T Read(Class<? : T> type, InputStream source, bool strict);
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
      <T> T Read(Class<? : T> type, Reader source, bool strict);
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
      <T> T Read(Class<? : T> type, InputNode source, bool strict);
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
      <T> T Read(T value, String source);
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
      <T> T Read(T value, File source);
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
      <T> T Read(T value, InputStream source);
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
      <T> T Read(T value, Reader source);
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
      <T> T Read(T value, InputNode source);
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
      <T> T Read(T value, String source, bool strict);
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
      <T> T Read(T value, File source, bool strict);
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
      <T> T Read(T value, InputStream source, bool strict);
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
      <T> T Read(T value, Reader source, bool strict);
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
      <T> T Read(T value, InputNode source, bool strict);
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
      bool Validate(Class type, String source);
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
      bool Validate(Class type, File source);
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
      bool Validate(Class type, InputStream source);
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
      bool Validate(Class type, Reader source);
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
      bool Validate(Class type, InputNode source);
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
      bool Validate(Class type, String source, bool strict);
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
      bool Validate(Class type, File source, bool strict);
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
      bool Validate(Class type, InputStream source, bool strict);
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
      bool Validate(Class type, Reader source, bool strict);
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
      bool Validate(Class type, InputNode source, bool strict);
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
      void Write(Object source, File out);
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
      void Write(Object source, OutputStream out);
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
      void Write(Object source, Writer out);
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
      void Write(Object source, OutputNode root);
   }
}
