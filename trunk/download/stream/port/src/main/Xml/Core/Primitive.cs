#region License
//
// Primitive.cs July 2006
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
   /// The <c>Primitive</c> object is used to provide serialization
   /// for primitive objects. This can serialize and deserialize any
   /// primitive object and enumerations. Primitive values are converted
   /// to text using the <c>String.valueOf</c> method. Enumerated
   /// types are converted using the <c>Enum.valueOf</c> method.
   /// <p>
   /// Text within attributes and elements can contain template variables
   /// similar to those found in Apache <cite>Ant</cite>. This allows
   /// values such as system properties, environment variables, and user
   /// specified mappings to be inserted into the text in place of the
   /// template reference variables.
   /// </code>
   ///    &lt;example attribute="${value}&gt;
   ///       &lt;text&gt;Text with a ${variable}&lt;/text&gt;
   ///    &lt;/example&gt;
   /// </code>
   /// In the above XML element the template variable references will be
   /// checked against the <c>Filter</c> object used by the context
   /// serialization object. If they corrospond to a filtered value then
   /// they are replaced, if not the text remains unchanged.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Filter.Filter
   /// </seealso>
   class Primitive : Converter {
      /// <summary>
      /// This is used to convert the string values to primitives.
      /// </summary>
      private readonly PrimitiveFactory factory;
      /// <summary>
      /// The context object is used to perform text value filtering.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This the value used to represent a null primitive value.
      /// </summary>
      private readonly String empty;
      /// <summary>
      /// This is the type that this primitive expects to represent.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// Constructor for the <c>Primitive</c> object. This is used
      /// to convert an XML node to a primitive object and vice versa. To
      /// perform deserialization the primitive object requires the context
      /// object used for the instance of serialization to performed.
      /// </summary>
      /// <param name="context">
      /// the context object used for the serialization
      /// </param>
      /// <param name="type">
      /// this is the type of primitive this represents
      /// </param>
      public Primitive(Context context, Type type) {
         this(context, type, null);
      }
      /// <summary>
      /// Constructor for the <c>Primitive</c> object. This is used
      /// to convert an XML node to a primitive object and vice versa. To
      /// perform deserialization the primitive object requires the context
      /// object used for the instance of serialization to performed.
      /// </summary>
      /// <param name="context">
      /// the context object used for the serialization
      /// </param>
      /// <param name="type">
      /// this is the type of primitive this represents
      /// </param>
      /// <param name="empty">
      /// this is the value used to represent a null value
      /// </param>
      public Primitive(Context context, Type type, String empty) {
         this.factory = new PrimitiveFactory(context, type);
         this.type = type.getType();
         this.context = context;
         this.empty = empty;
      }
      /// <summary>
      /// This <c>read</c> method will extract the text value from
      /// the node and replace any template variables before converting
      /// it to a primitive value. This uses the <c>Context</c>
      /// object used for this instance of serialization to replace all
      /// template variables with values from the context filter.
      /// </summary>
      /// <param name="node">
      /// this is the node to be converted to a primitive
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object Read(InputNode node) {
         if(node.isElement()) {
            return ReadElement(node);
         }
         return Read(node, type);
      }
      /// <summary>
      /// This <c>read</c> method will extract the text value from
      /// the node and replace any template variables before converting
      /// it to a primitive value. This uses the <c>Context</c>
      /// object used for this instance of serialization to replace all
      /// template variables with values from the context filter.
      /// </summary>
      /// <param name="node">
      /// this is the node to be converted to a primitive
      /// </param>
      /// <param name="value">
      /// this is the original primitive value used
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object Read(InputNode node, Object value) {
         if(value != null) {
            throw new PersistenceException("Can not read existing %s", type);
         }
         return Read(node);
      }
      /// <summary>
      /// This <c>read</c> method will extract the text value from
      /// the node and replace any template variables before converting
      /// it to a primitive value. This uses the <c>Context</c>
      /// object used for this instance of serialization to replace all
      /// template variables with values from the context filter.
      /// </summary>
      /// <param name="node">
      /// this is the node to be converted to a primitive
      /// </param>
      /// <param name="type">
      /// this is the type to read the primitive with
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object Read(InputNode node, Class type) {
         String value = node.getValue();
         if(value == null) {
            return null;
         }
         if(empty != null && value.equals(empty)) {
            return empty;
         }
         return ReadTemplate(value, type);
      }
      /// <summary>
      /// This <c>read</c> method will extract the text value from
      /// the node and replace any template variables before converting
      /// it to a primitive value. This uses the <c>Context</c>
      /// object used for this instance of serialization to replace all
      /// template variables with values from the context filter.
      /// </summary>
      /// <param name="node">
      /// this is the node to be converted to a primitive
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object ReadElement(InputNode node) {
         Instance value = factory.GetInstance(node);
         if(!value.isReference()) {
            return ReadElement(node, value);
         }
         return value.GetInstance();
      }
      /// <summary>
      /// This <c>read</c> method will extract the text value from
      /// the node and replace any template variables before converting
      /// it to a primitive value. This uses the <c>Context</c>
      /// object used for this instance of serialization to replace all
      /// template variables with values from the context filter.
      /// </summary>
      /// <param name="node">
      /// this is the node to be converted to a primitive
      /// </param>
      /// <param name="value">
      /// this is the instance to set the result to
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object ReadElement(InputNode node, Instance value) {
         Object result = Read(node, type);
         if(value != null) {
            value.setInstance(result);
         }
         return result;
      }
      /// <summary>
      /// This <c>read</c> method will extract the text value from
      /// the node and replace any template variables before converting
      /// it to a primitive value. This uses the <c>Context</c>
      /// object used for this instance of serialization to replace all
      /// template variables with values from the context filter.
      /// </summary>
      /// <param name="value">
      /// this is the value to be processed as a template
      /// </param>
      /// <param name="type">
      /// this is the type that that the primitive is
      /// </param>
      /// <returns>
      /// this returns the primitive that has been deserialized
      /// </returns>
      public Object ReadTemplate(String value, Class type) {
         String text = context.GetProperty(value);
         if(text != null) {
            return factory.GetInstance(text, type);
         }
         return null;
      }
      /// <summary>
      /// This <c>validate</c> method will validate the primitive
      /// by checking the node text. If the value is a reference then
      /// this will not extract any value from the node. Transformation
      /// of the extracted value is not done as it can not account for
      /// template variables. Thus any text extracted is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to be validated as a primitive
      /// </param>
      /// <returns>
      /// this returns the primitive that has been validated
      /// </returns>
      public bool Validate(InputNode node) {
         if(node.isElement()) {
            ValidateElement(node);
         } else {
            node.getValue();
         }
         return true;
      }
      /// <summary>
      /// This <c>validateElement</c> method validates a primitive
      /// by checking the node text. If the value is a reference then
      /// this will not extract any value from the node. Transformation
      /// of the extracted value is not done as it can not account for
      /// template variables. Thus any text extracted is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to be validated as a primitive
      /// </param>
      /// <returns>
      /// this returns the primitive that has been validated
      /// </returns>
      public bool ValidateElement(InputNode node) {
         Instance type = factory.GetInstance(node);
         if(!type.isReference()) {
            type.setInstance(null);
         }
         return true;
      }
      /// <summary>
      /// This <c>write</c> method will serialize the contents of
      /// the provided object to the given XML element. This will use
      /// the <c>String.valueOf</c> method to convert the object to
      /// a string if the object represents a primitive, if however the
      /// object represents an enumerated type then the text value is
      /// created using <c>Enum.name</c>.
      /// </summary>
      /// <param name="source">
      /// this is the object to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element to have its text set
      /// </param>
      public void Write(OutputNode node, Object source) {
         String text = factory.GetText(source);
         if(text != null) {
            node.setValue(text);
         }
      }
   }
}
