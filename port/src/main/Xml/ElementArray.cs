#region License
//
// ElementArray.cs July 2006
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
   /// The <c>ElementArray</c> annotation represents a method or
   /// field that is an array of elements. The array deserialized is the
   /// same type as the field or method, all entries within the array
   /// must be a compatible type. However, a <c>class</c> attribute
   /// can be used to override an entry, this must be an assignable type.
   /// </code>
   ///    &lt;array length="3"&gt;
   ///       &lt;entry&gt;
   ///          &lt;value&gt;example text value&lt;/value&gt;
   ///       &lt;/entry&gt;
   ///       &lt;entry&gt;
   ///          &lt;value&gt;some other value&lt;/value&gt;
   ///       &lt;/entry&gt;
   ///       &lt;entry/&gt;
   ///    &lt;/array&gt;
   /// </code>
   /// All null objects within the array are represented as an empty XML
   /// element so that they can be deserialized accurately. This ensures
   /// that the length attribute of the array is respected, as well as
   /// the index position of all serialized entries. The length of the
   /// array must be specified for deserialization to instantiate the
   /// array before the array values are instantiated. This is required
   /// to account for cyclical references in the object graph.
   /// </summary>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class ElementArray : System.Attribute {
      private String name;
      private String entry;
      private bool data;
      private bool required;
      private bool empty;
      /// <summary>
      /// This represents the name of the XML element. Annotated fields
      /// or methods can optionally provide the name of the element. If
      /// no name is provided then the name of the annotated field or
      /// method will be used in its place. The name is provided if the
      /// field or method name is not suitable as an XML element name.
      /// </summary>
      /// <returns>
      /// the name of the XML element this represents
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
      /// This is used to provide a name of the XML element representing
      /// the entry within the array. An entry name is optional and is
      /// used when the name needs to be overridden. This also ensures
      /// that entry, regardless of type has the same root name.
      /// </summary>
      /// <returns>
      /// this returns the entry XML element for each value
      /// </returns>
      public String Entry {
         get {
            return entry;
         }
         set {
            entry = value;
         }
      }
      /// <summary>
      /// This is used to determine whether the element data is written
      /// in a CDATA block or not. If this is set to true then the text
      /// is written within a CDATA block, by default the text is output
      /// as escaped XML. Typically this is useful when this annotation
      /// is applied to an array of primitives, such as strings.
      /// </summary>
      /// <returns>
      /// true if entries are to be wrapped in a CDATA block
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
      /// Determines whether the element is required within the XML
      /// document. Any field marked as not required will not have its
      /// value set when the object is deserialized. If an object is to
      /// be serialized only a null attribute will not appear as XML.
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
      /// <summary>
      /// This is used to determine if an optional field or method can
      /// remain null if it does not exist. If this is false then the
      /// optional element is given an empty array. This is a convenience
      /// attribute which avoids having to check if the element is null
      /// before providing it with a suitable default instance.
      /// </summary>
      /// <returns>
      /// false if an optional element is always instantiated
      /// </returns>
      public bool Empty {
         get {
            return empty;
         }
         set {
            empty = value;
         }
      }
   }
}
