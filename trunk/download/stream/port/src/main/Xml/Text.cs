#region License
//
// Text.cs April 2007
//
// Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml {
   /// <summary>
   /// The <c>Text</c> annotation is used to represent a field or
   /// method that appears as text within an XML element. Methods and
   /// fields annotated with this must represent primitive values, which
   /// means that the type is converted to and from an XML representation
   /// using a <c>Transform</c> object. For example, the primitive
   /// types typically annotated could be strings, integers, or dates.
   /// <p>
   /// One restriction on this annotation is that it can only appear once
   /// within a schema class, and it can not appear with the another XML
   /// element annotations, such as the <c>Element</c> annotation.
   /// It can however appear with any number of <c>Attribute</c>
   /// annotations.
   /// </code>
   ///    &lt;example one="value" two="value"&gt;
   ///       Example text value
   ///    &lt;example&gt;
   /// </code>
   /// Text values are used when an element containing attributes is
   /// used to wrap a text value with no child elements. This can be
   /// used in place of an element annotation to represent a primitive
   /// which is wrapped in a surrounding XML element.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.transform.Transformer
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Text : System.Attribute {
      private String empty;
      private bool data;
      private bool required;
      /// <summary>
      /// This is used to provide a default value for the text data if
      /// the annotated field or method is null. This ensures the the
      /// serialization process writes the text data with a value even
      /// if the value is null, and allows deserialization to determine
      /// whether the value within the object was null or not.
      /// </summary>
      /// <returns>
      /// this returns the default attribute value to use
      /// </returns>
      public String Empty {
         get {
            return empty;
         }
         set {
            empty = value;
         }
      }
      /// <summary>
      /// This is used to determine whether the text is written within
      /// CDATA block or not. If this is set to true then the text is
      /// written within a CDATA block, by default the text is output
      /// as escaped XML. Typically this is used for large text values.
      /// </summary>
      /// <returns>
      /// true if the data is to be wrapped in a CDATA block
      /// </returns>
      public bool Data {
         get {
            return data;
         }
         set {
            data = value;
         }
      }
      /// <summary>
      /// Determines whether the text value is required within the XML
      /// document. Any field marked as not required may not have its
      /// value set when the object is deserialized. If an object is to
      /// be serialized only a null attribute will not appear in XML.
      /// </summary>
      /// <returns>
      /// true if the element is required, false otherwise
      /// </returns>
      public bool Required {
         get {
            return required;
         }
         set {
            required = value;
         }
      }
   }
}
