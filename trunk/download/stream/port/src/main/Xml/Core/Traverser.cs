#region License
//
// Traverser.cs July 2006
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
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Traverser</c> object is used to traverse the XML class
   /// schema and either serialize or deserialize an object. This is the
   /// root of all serialization and deserialization operations. It uses
   /// the <c>Root</c> annotation to ensure that the XML schema
   /// matches the provided XML element. If no root element is defined the
   /// serialization and deserialization cannot be performed.
   /// </summary>
   class Traverser {
      /// <summary>
      /// This is the context object used for the traversal performed.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This is the style that is used to style the XML roots.
      /// </summary>
      private readonly Style style;
      /// <summary>
      /// Constructor for the <c>Traverser</c> object. This creates
      /// a traverser that can be used to perform serialization or
      /// or deserialization of an object. This requires a source object.
      /// </summary>
      /// <param name="context">
      /// the context object used for the traversal
      /// </param>
      public Traverser(Context context) {
         this.style = context.Style;
         this.context = context;
      }
      /// <summary>
      /// This will acquire the <c>Decorator</c> for the type.
      /// A decorator is an object that adds various details to the
      /// node without changing the overall structure of the node. For
      /// example comments and namespaces can be added to the node with
      /// a decorator as they do not affect the deserialization.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the decorator for
      /// </param>
      /// <returns>
      /// this returns the decorator associated with this
      /// </returns>
      public Decorator GetDecorator(Class type) {
         return context.GetDecorator(type);
      }
      /// <summary>
      /// This <c>read</c> method is used to deserialize an object
      /// from the provided XML element. The class provided acts as the
      /// XML schema definition used to control the deserialization. If
      /// the XML schema does not have a <c>Root</c> annotation
      /// this throws an exception. Also if the root annotation name is
      /// not the same as the XML element name an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the node that is to be deserialized
      /// </param>
      /// <param name="type">
      /// this is the XML schema class to be used
      /// </param>
      /// <returns>
      /// an object deserialized from the XML element
      /// </returns>
      public Object Read(InputNode node, Class type) {
         Composite factory = GetComposite(type);
         Object value = factory.Read(node);
         if(value != null) {
            Class real = value.getClass();
            return Read(node, real, value);
         }
         return null;
      }
      /// <summary>
      /// This <c>read</c> method will read the contents of the XML
      /// document from the provided source and populate the object with
      /// the values deserialized. This is used as a means of injecting an
      /// object with values deserialized from an XML document. If the
      /// XML source cannot be deserialized or there is a problem building
      /// the object graph an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the node that is to be deserialized
      /// </param>
      /// <param name="value">
      /// this is the value that is to be deserialized
      /// </param>
      /// <returns>
      /// an object deserialized from the XML element
      /// </returns>
      public Object Read(InputNode node, Object value) {
         Class type = value.getClass();
         Composite factory = GetComposite(type);
         Object real = factory.Read(node, value);
         return Read(node, type, real);
      }
      /// <summary>
      /// This <c>read</c> method is used to deserialize an object
      /// from the provided XML element. The class provided acts as the
      /// XML schema definition used to control the deserialization. If
      /// the XML schema does not have a <c>Root</c> annotation
      /// this throws an exception. Also if the root annotation name is
      /// not the same as the XML element name an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the node that is to be deserialized
      /// </param>
      /// <param name="value">
      /// this is the XML schema object to be used
      /// </param>
      /// <returns>
      /// an object deserialized from the XML element
      /// </returns>
      public Object Read(InputNode node, Class type, Object value) {
         String root = GetName(type);
         if(root == null) {
            throw new RootException("Root annotation required for %s", type);
         }
         return value;
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
      /// <returns>
      /// true if the document matches the class XML schema
      /// </returns>
      public bool Validate(InputNode node, Class type) {
         Composite factory = GetComposite(type);
         String root = GetName(type);
         if(root == null) {
            throw new RootException("Root annotation required for %s", type);
         }
         return factory.Validate(node);
      }
      /// <summary>
      /// This <c>write</c> method is used to convert the provided
      /// object to an XML element. This creates a child node from the
      /// given <c>OutputNode</c> object. Once this child element
      /// is created it is populated with the fields of the source object
      /// in accordance with the XML schema class.
      /// </summary>
      /// <param name="source">
      /// this is the object to be serialized to XML
      /// </param>
      public void Write(OutputNode node, Object source) {
         Write(node, source, source.getClass());
      }
      /// <summary>
      /// This <c>write</c> method is used to convert the provided
      /// object to an XML element. This creates a child node from the
      /// given <c>OutputNode</c> object. Once this child element
      /// is created it is populated with the fields of the source object
      /// in accordance with the XML schema class.
      /// </summary>
      /// <param name="source">
      /// this is the object to be serialized to XML
      /// </param>
      /// <param name="expect">
      /// this is the class that is expected to be written
      /// </param>
      public void Write(OutputNode node, Object source, Class expect) {
         Class type = source.getClass();
         String root = GetName(type);
         if(root == null) {
            throw new RootException("Root annotation required for %s", type);
         }
         Write(node, source, expect, root);
      }
      /// <summary>
      /// This <c>write</c> method is used to convert the provided
      /// object to an XML element. This creates a child node from the
      /// given <c>OutputNode</c> object. Once this child element
      /// is created it is populated with the fields of the source object
      /// in accordance with the XML schema class.
      /// </summary>
      /// <param name="source">
      /// this is the object to be serialized to XML
      /// </param>
      /// <param name="expect">
      /// this is the class that is expected to be written
      /// </param>
      /// <param name="name">
      /// this is the name of the root annotation used
      /// </param>
      public void Write(OutputNode node, Object source, Class expect, String name) {
         OutputNode child = node.getChild(name);
         Type type = GetType(expect);
         if(source != null) {
            Class actual = source.getClass();
            if(!context.SetOverride(type, source, child)) {
               Converter convert = GetComposite(actual);
               Decorator decorator = GetDecorator(actual);
               decorator.decorate(child);
               convert.Write(child, source);
            }
         }
         child.commit();
      }
      /// <summary>
      /// This will create a <c>Composite</c> object using the XML
      /// schema class provided. This makes use of the source object that
      /// this traverser has been given to create a composite converter.
      /// </summary>
      /// <param name="expect">
      /// this is the XML schema class to be used
      /// </param>
      /// <returns>
      /// a converter for the specified XML schema class
      /// </returns>
      public Composite GetComposite(Class expect) {
         Type type = GetType(expect);
         if(expect == null) {
            throw new RootException("Can not instantiate null class");
         }
         return new Composite(context, type);
      }
      /// <summary>
      /// This is used to acquire a type for the provided class. This will
      /// wrap the class in a <c>Type</c> wrapper object. Wrapping
      /// the class allows it to be used within the framework.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be wrapped for use
      /// </param>
      /// <returns>
      /// this returns the type that wraps the specified class
      /// </returns>
      public Type GetType(Class type) {
         return new ClassType(type);
      }
      /// <summary>
      /// Extracts the <c>Root</c> annotation from the provided XML
      /// schema class. If no annotation exists in the provided class the
      /// super class is checked and so on until the <c>Object</c>
      /// is encountered, if no annotation is found this returns null.
      /// </summary>
      /// <param name="type">
      /// this is the XML schema class to use
      /// </param>
      /// <returns>
      /// this returns the root annotation for the XML schema
      /// </returns>
      public String GetName(Class type) {
         String root = context.GetName(type);
         String name = style.GetElement(root);
         return name;
      }
   }
}
