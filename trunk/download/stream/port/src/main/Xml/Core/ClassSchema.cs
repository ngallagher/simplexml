#region License
//
// Schema.cs July 2006
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
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Schema</c> object is used to track which fields within
   /// an object have been visited by a converter. This object is necessary
   /// for processing <c>Composite</c> objects. In particular it is
   /// necessary to keep track of which required nodes have been visited
   /// and which have not, if a required not has not been visited then the
   /// XML source does not match the XML class schema and serialization
   /// must fail before processing any further.
   /// </summary>
   class ClassSchema : Schema {
      /// <summary>
      /// This is the decorator associated with this schema object.
      /// </summary>
      private readonly Decorator decorator;
      /// <summary>
      /// Contains a map of all attributes present within the schema.
      /// </summary>
      private readonly LabelMap attributes;
      /// <summary>
      /// Contains a map of all elements present within the schema.
      /// </summary>
      private readonly LabelMap elements;
      /// <summary>
      /// This is the version annotation for the XML class schema.
      /// </summary>
      private readonly Version revision;
      /// <summary>
      /// This is the scanner that is used to acquire the constructor.
      /// </summary>
      private readonly Creator factory;
      /// <summary>
      /// This is the pointer to the schema class replace method.
      /// </summary>
      private readonly Caller caller;
      /// <summary>
      /// This is the version label used to read the version attribute.
      /// </summary>
      private readonly Label version;
      /// <summary>
      /// This is used to represent a text value within the schema.
      /// </summary>
      private readonly Label text;
      /// <summary>
      /// This is the type that this class schema is representing.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// This is used to specify whether the type is a primitive class.
      /// </summary>
      private readonly bool primitive;
      /// <summary>
      /// Constructor for the <c>Schema</c> object. This is used
      /// to wrap the element and attribute XML annotations scanned from
      /// a class schema. The schema tracks all fields visited so that
      /// a converter can determine if all fields have been serialized.
      /// </summary>
      /// <param name="schema">
      /// this contains all labels scanned from the class
      /// </param>
      /// <param name="context">
      /// this is the context object for serialization
      /// </param>
      public ClassSchema(Scanner schema, Context context) {
         this.attributes = schema.getAttributes(context);
         this.elements = schema.getElements(context);
         this.caller = schema.getCaller(context);
         this.factory = schema.Creator;
         this.revision = schema.Revision;
         this.decorator = schema.Decorator;
         this.primitive = schema.IsPrimitive();
         this.version = schema.Version;
         this.text = schema.Text;
         this.type = schema.Type;
      }
      /// <summary>
      /// This is used to determine whether the scanned class represents
      /// a primitive type. A primitive type is a type that contains no
      /// XML annotations and so cannot be serialized with an XML form.
      /// Instead primitives a serialized using transformations.
      /// </summary>
      /// <returns>
      /// this returns true if no XML annotations were found
      /// </returns>
      public bool IsPrimitive() {
         return primitive;
      }
      /// <summary>
      /// This is used to create the object instance. It does this by
      /// either delegating to the default no argument constructor or by
      /// using one of the annotated constructors for the object. This
      /// allows deserialized values to be injected in to the created
      /// object if that is required by the class schema.
      /// </summary>
      /// <returns>
      /// this returns the creator for the class object
      /// </returns>
      public Creator Creator {
         get {
            return factory;
         }
      }
      //public Creator GetCreator() {
      //   return factory;
      //}
      /// This returns the <c>Label</c> that represents the version
      /// annotation for the scanned class. Only a single version can
      /// exist within the class if more than one exists an exception is
      /// thrown. This will read only floating point types such as double.
      /// </summary>
      /// <returns>
      /// this returns the label used for reading the version
      /// </returns>
      public Label Version {
         get {
            return version;
         }
      }
      //public Label GetVersion() {
      //   return version;
      //}
      /// This is the <c>Version</c> for the scanned class. It
      /// allows the deserialization process to be configured such that
      /// if the version is different from the schema class none of
      /// the fields and methods are required and unmatched elements
      /// and attributes will be ignored.
      /// </summary>
      /// <returns>
      /// this returns the version of the class that is scanned
      /// </returns>
      public Version Revision {
         get {
            return revision;
         }
      }
      //public Version GetRevision() {
      //   return revision;
      //}
      /// This is used to acquire the <c>Decorator</c> for this.
      /// A decorator is an object that adds various details to the
      /// node without changing the overall structure of the node. For
      /// example comments and namespaces can be added to the node with
      /// a decorator as they do not affect the deserialization.
      /// </summary>
      /// <returns>
      /// this returns the decorator associated with this
      /// </returns>
      public Decorator Decorator {
         get {
            return decorator;
         }
      }
      //public Decorator GetDecorator() {
      //   return decorator;
      //}
      /// This is used to acquire the <c>Caller</c> object. This
      /// is used to call the callback methods within the object. If the
      /// object contains no callback methods then this will return an
      /// object that does not invoke any methods that are invoked.
      /// </summary>
      /// <returns>
      /// this returns the caller for the specified type
      /// </returns>
      public Caller Caller {
         get {
            return caller;
         }
      }
      //public Caller GetCaller() {
      //   return caller;
      //}
      /// Returns a <c>LabelMap</c> that contains the details for
      /// all fields marked as XML attributes. Labels contained within
      /// this map are used to convert primitive types only.
      /// </summary>
      /// <returns>
      /// map with the details extracted from the schema class
      /// </returns>
      public LabelMap Attributes {
         get {
            return attributes;
         }
      }
      //public LabelMap GetAttributes() {
      //   return attributes;
      //}
      /// Returns a <c>LabelMap</c> that contains the details for
      /// all fields marked as XML elements. The annotations that are
      /// considered elements are the <c>ElementList</c> and the
      /// <c>Element</c> annotations.
      /// </summary>
      /// <returns>
      /// a map containing the details for XML elements
      /// </returns>
      public LabelMap Elements {
         get {
            return elements;
         }
      }
      //public LabelMap GetElements() {
      //   return elements;
      //}
      /// This returns the <c>Label</c> that represents the text
      /// annotation for the scanned class. Only a single text annotation
      /// can be used per class, so this returns only a single label
      /// rather than a <c>LabelMap</c> object. Also if this is
      /// not null then the elements label map will be empty.
      /// </summary>
      /// <returns>
      /// this returns the text label for the scanned class
      /// </returns>
      public Label Text {
         get {
            return text;
         }
      }
      //public Label GetText() {
      //   return text;
      //}
      /// This is used to acquire a description of the schema. This is
      /// useful when debugging an issue as it allows a representation
      /// of the instance to be viewed with the class it represents.
      /// </summary>
      /// <returns>
      /// this returns a visible description of the schema
      /// </returns>
      public String ToString() {
         return String.format("schema for %s", type);
      }
   }
}
