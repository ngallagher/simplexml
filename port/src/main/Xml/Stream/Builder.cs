#region License
//
// Builder.cs July 2008
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>Builder</c> class is used to represent an XML style
   /// that can be applied to a serialized object. A style can be used to
   /// modify the element and attribute names for the generated document.
   /// Styles can be used to generate hyphenated or camel case XML.
   /// </code>
   ///    &lt;example-element&gt;
   ///        &lt;child-element example-attribute='example'&gt;
   ///           &lt;inner-element&gt;example&lt;/inner-element&gt;
   ///        &lt;/child-element&gt;
   ///     &lt;/example-element&gt;
   /// </code>
   /// Above the hyphenated XML elements and attributes can be generated
   /// from a style implementation. Styles enable the same objects to be
   /// serialized in different ways, generating different styles of XML
   /// without having to modify the class schema for that object.
   /// </summary>
   internal class Builder : Style {
      /// <summary>
      /// This is the cache for the constructed attribute values.
      /// </summary>
      private readonly Cache attributes;
      /// <summary>
      /// This is the cache for the constructed element values.
      /// </summary>
      private readonly Cache elements;
      /// <summary>
      /// This is the style object used to create the values used.
      /// </summary>
      private readonly Style style;
      /// <summary>
      /// Constructor for the <c>Builder</c> object. This will cache
      /// values constructed from the inner style object, which allows the
      /// results from the style to retrieved quickly the second time.
      /// </summary>
      /// <param name="style">
      /// this is the internal style object to be used
      /// </param>
      public Builder(Style style) {
         this.attributes = new Cache();
         this.elements = new Cache();
         this.style = style;
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
         String value = attributes[name];
         if(value != null) {
            return value;
         }
         value = style.GetAttribute(name);
         if(value != null) {
            attributes[name] = value;
         }
         return value;
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
         String value = elements[name];
         if(value != null) {
            return value;
         }
         value = style.GetElement(name);
         if(value != null) {
            elements[name] = value;
         }
         return value;
      }
      /// <summary>
      /// This is used to set the attribute values within this builder.
      /// Overriding the attribute values ensures that the default
      /// algorithm does not need to determine each of the values. It
      /// allows special behaviour that the user may require for XML.
      /// </summary>
      /// <param name="name">
      /// the name of the XML attribute to be overridden
      /// </param>
      /// <param name="value">
      /// the value that is to be used for that attribute
      /// </param>
      public void SetAttribute(String name, String value) {
         attributes[name] = value;
      }
      /// This is used to set the element values within this builder.
      /// Overriding the element values ensures that the default
      /// algorithm does not need to determine each of the values. It
      /// allows special behaviour that the user may require for XML.
      /// </summary>
      /// <param name="name">
      /// the name of the XML element to be overridden
      /// </param>
      /// <param name="value">
      /// the value that is to be used for that element
      /// </param>
      public void SetElement(String name, String value) {
         elements[name] = value;
      }
      /// The <c>Cache</c> object is used to cache the values
      /// used to represent the styled attributes and elements. This
      /// is a concurrent hash map so that styles can be used by more
      /// than one thread simultaneously.
      /// </summary>
      private class Cache : Dictionary<String, String> {
         /// <summary>
         /// Constructor for the <c>Cache</c> object. This will
         /// create a concurrent cache that can translate between the
         /// XML attributes and elements and the styled values.
         /// </summary>
         public Cache()  {
         }
      }
   }
}
