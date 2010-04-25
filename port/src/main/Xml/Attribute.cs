#region License
//
// Attribute.cs July 2006
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
using System;
#endregion
namespace SimpleFramework.Xml {
   /// <summary>
   /// The <c>Attribute</c> annotation represents a serializable XML
   /// attribute within an XML element. An object annotated with this is
   /// typically a primitive or enumerated type. Conversion from the
   /// attribute to primitive type is done with a <c>Transform</c>
   /// object. If a suitable transform can be found then this will convert
   /// the attribute string value to an object instance, which can be
   /// assigned to the annotated field, or passed to the annotated method.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.transform.Transformer
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Attribute : System.Attribute {
      private String name;
      private String empty;
      private bool required;
      /// <summary>
      /// This represents the name of the XML attribute. Annotated fields
      /// or methods can optionally provide the name of the XML attribute
      /// they represent. If a name is not provided then the field or
      /// method name is used in its place. A name can be specified if
      /// the field or method name is not suitable for the XML attribute.
      /// </summary>
      /// <returns>
      /// the name of the XML attribute this represents
      /// </returns>
      public String Name {
         get {
            return name;
         }
         set {
            name = value;
         }
      }
      /// <summary>
      /// This is used to provide a default value for the attribute if
      /// the annotated field or method is null. This ensures the the
      /// serialization process writes the attribute with a value even
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
      /// Determines whether the attribute is required within an XML
      /// element. Any field marked as not required will not have its
      /// value set when the object is deserialized. If an object is to
      /// be serialized only a null attribute will not appear as XML.
      /// </summary>
      /// <returns>
      /// true if the attribute is required, false otherwise
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
