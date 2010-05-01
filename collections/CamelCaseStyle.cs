#region License
//
// CamelCaseStyle.cs July 2008
//
// Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>CamelCaseStyle</c> is used to represent an XML style
   /// that can be applied to a serialized object. A style can be used to
   /// modify the element and attribute names for the generated document.
   /// This styles can be used to generate camel case XML.
   /// </code>
   ///    &lt;ExampleElement&gt;
   ///        &lt;ChildElement exampleAttribute='example'&gt;
   ///           &lt;InnerElement&gt;example&lt;/InnerElement&gt;
   ///        &lt;/ChildElement&gt;
   ///     &lt;/ExampleElement&gt;
   /// </code>
   /// Above the camel case XML elements and attributes can be generated
   /// from a style implementation. Styles enable the same objects to be
   /// serialized in different ways, generating different styles of XML
   /// without having to modify the class schema for that object.
   /// </summary>
   public class CamelCaseStyle : Style {

      /// <summary>
      /// This is used to perform the actual building of tokens.
      /// </summary>
      private readonly Builder builder;

      /// <summary>
      /// This is the strategy used to generate the style tokens.
      /// </summary>
      private readonly Style style;

      /// <summary>
      /// Constructor for the <c>CamelCaseStyle</c> object. This
      /// is used to create a style that will create camel case XML
      /// attributes and elements allowing a consistent format for
      /// generated XML. By default the elements have an upper case
      /// initial character and a lower case attribute.
      /// </summary>
      public CamelCaseStyle() : this(true, false) {
      }

      /// <summary>
      /// Constructor for the <c>CamelCaseStyle</c> object. This
      /// is used to create a style that will create camel case XML
      /// attributes and elements allowing a consistent format for
      /// generated XML. By default the attributes have a lower case
      /// initial character and an configurable element.
      /// </summary>
      /// <param name="element">
      /// If true the element will start as upper case.
      /// </param>
      public CamelCaseStyle(bool element) : this(element, false) {
      }

      /// <summary>
      /// Constructor for the <c>CamelCaseStyle</c> object. This
      /// is used to create a style that will create camel case XML
      /// attributes and elements allowing a consistent format for
      /// generated XML. Both the attribute an elements are configurable.
      /// </summary>
      /// <param name="element">
      /// If true the element will start as upper case.
      /// </param>
      /// <param name="attribute">
      /// If true the attribute starts as upper case.
      /// </param>
      public CamelCaseStyle(bool element, bool attribute) {
         this.style = new CamelCaseBuilder(element, attribute);
         this.builder = new Builder(style);
      }

      /// <summary>
      /// This is used to generate the XML attribute representation of
      /// the specified name. Attribute names should ensure to keep the
      /// uniqueness of the name such that two different names will
      /// be styled in to two different strings.
      /// </summary>
      /// <param name="name">
      /// This is the attribute name that is to be styled.
      /// </param>
      /// <returns>
      /// This returns the styled name of the XML attribute.
      /// </returns>
      public virtual String GetAttribute(String name) {
         return builder.GetAttribute(name);
      }

      /// <summary>
      /// This is used to set the attribute values within this builder.
      /// Overriding the attribute values ensures that the default
      /// algorithm does not need to determine each of the values. It
      /// allows special behaviour that the user may require for XML.
      /// </summary>
      /// <param name="name">
      /// The name of the XML attribute to be overridden.
      /// </param>
      /// <param name="value">
      /// The value that is to be used for that attribute.
      /// </param>
      public void SetAttribute(String name, String value) {
         builder.SetAttribute(name, value);
      }

      /// This is used to generate the XML element representation of
      /// the specified name. Element names should ensure to keep the
      /// uniqueness of the name such that two different names will
      /// be styled in to two different strings.
      /// </summary>
      /// <param name="name">
      /// This is the element name that is to be styled.
      /// </param>
      /// <returns>
      /// This returns the styled name of the XML element.
      /// </returns>
      public virtual String GetElement(String name) {
         return builder.GetElement(name);
      }

      /// <summary>
      /// This is used to set the element values within this builder.
      /// Overriding the element values ensures that the default
      /// algorithm does not need to determine each of the values. It
      /// allows special behaviour that the user may require for XML.
      /// </summary>
      /// <param name="name">
      /// The name of the XML element to be overridden.
      /// </param>
      /// <param name="value">
      /// The value that is to be used for that element.
      /// </param>
      public void SetElement(String name, String value) {
         builder.SetElement(name, value);
      }
   }
}
