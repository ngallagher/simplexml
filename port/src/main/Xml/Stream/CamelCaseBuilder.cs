#region License
//
// CamelCaseBuilder.cs July 2008
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
   /// The <c>CamelCaseBuilder</c> is used to represent an XML style
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
   class CamelCaseBuilder : Style {
      /// <summary>
      /// If true then the attribute will start with upper case.
      /// </summary>
      private readonly bool attribute;
      /// <summary>
      /// If true then the element will start with upper case.
      /// </summary>
      private readonly bool element;
      /// <summary>
      /// Constructor for the <c>CamelCaseBuilder</c> object. This
      /// is used to create a style that will create camel case XML
      /// attributes and elements allowing a consistent format for
      /// generated XML. Both the attribute an elements are configurable.
      /// </summary>
      /// <param name="element">
      /// if true the element will start as upper case
      /// </param>
      /// <param name="attribute">
      /// if true the attribute starts as upper case
      /// </param>
      public CamelCaseBuilder(bool element, bool attribute) {
         this.attribute = attribute;
         this.element = element;
      }
      /// <summary>
      /// This is used to generate the XML attribute representation of
      /// the specified name. Attribute names should ensure to keep the
      /// uniqueness of the name such that two different names will
      /// be styled in to two different strings.
      /// </summary>
      /// <param name="name">
      /// this is the attribute name that is to be styled
      /// </param>
      /// <returns>
      /// this returns the styled name of the XML attribute
      /// </returns>
      public String GetAttribute(String name) {
         if(name != null) {
            return new Attribute(name).process();
         }
         return null;
      }
      /// <summary>
      /// This is used to generate the XML element representation of
      /// the specified name. Element names should ensure to keep the
      /// uniqueness of the name such that two different names will
      /// be styled in to two different strings.
      /// </summary>
      /// <param name="name">
      /// this is the element name that is to be styled
      /// </param>
      /// <returns>
      /// this returns the styled name of the XML element
      /// </returns>
      public String GetElement(String name) {
         if(name != null) {
            return new Element(name).process();
         }
         return null;
      }
      /// <summary>
      /// This is used to parse the style for this builder. This takes
      /// all of the words split from the original string and builds all
      /// of the processed tokens for the styles elements and attributes.
      /// </summary>
      private class Attribute : Splitter {
         /// <summary>
         /// Constructor for the <c>Attribute</c> object. This will
         /// take the original string and parse it such that all of the
         /// words are emitted and used to build the styled token.
         /// </summary>
         /// <param name="source">
         /// this is the original string to be parsed
         /// </param>
         private Attribute(String source) {
            super(source);
         }
         /// <summary>
         /// This is used to parse the provided text in to the style that
         /// is required. Manipulation of the text before committing it
         /// ensures that the text adheres to the required style.
         /// </summary>
         /// <param name="text">
         /// this is the text buffer to acquire the token from
         /// </param>
         /// <param name="off">
         /// this is the offset in the buffer token starts at
         /// </param>
         /// <param name="len">
         /// this is the length of the token to be parsed
         /// </param>
         [Override]
         public void Parse(char[] text, int off, int len) {
            if(attribute) {
               text[off] = toUpper(text[off]);
            }
         }
         /// <summary>
         /// This is used to commit the provided text in to the style that
         /// is required. Committing the text to the buffer assembles the
         /// tokens resulting in a complete token.
         /// </summary>
         /// <param name="text">
         /// this is the text buffer to acquire the token from
         /// </param>
         /// <param name="off">
         /// this is the offset in the buffer token starts at
         /// </param>
         /// <param name="len">
         /// this is the length of the token to be committed
         /// </param>
         [Override]
         public void Commit(char[] text, int off, int len) {
            builder.append(text, off, len);
         }
      }
      /// <summary>
      /// This is used to parse the style for this builder. This takes
      /// all of the words split from the original string and builds all
      /// of the processed tokens for the styles elements and attributes.
      /// </summary>
      private class Element : Attribute {
         /// <summary>
         /// Constructor for the <c>Element</c> object. This will
         /// take the original string and parse it such that all of the
         /// words are emitted and used to build the styled token.
         /// </summary>
         /// <param name="source">
         /// this is the original string to be parsed
         /// </param>
         private Element(String source) {
            super(source);
         }
         /// <summary>
         /// This is used to parse the provided text in to the style that
         /// is required. Manipulation of the text before committing it
         /// ensures that the text adheres to the required style.
         /// </summary>
         /// <param name="text">
         /// this is the text buffer to acquire the token from
         /// </param>
         /// <param name="off">
         /// this is the offset in the buffer token starts at
         /// </param>
         /// <param name="len">
         /// this is the length of the token to be parsed
         /// </param>
         [Override]
         public void Parse(char[] text, int off, int len) {
            if(element) {
               text[off] = toUpper(text[off]);
            }
         }
      }
   }
}
