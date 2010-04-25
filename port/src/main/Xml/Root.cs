#region License
//
// Root.cs July 2006
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
   /// This <c>Root</c> annotation is used to annotate classes that
   /// need to be serialized. Also, elements within an element list, as
   /// represented by the <c>ElementList</c> annotation need this
   /// annotation so that the element names can be determined. All other
   /// field or method names can be determined using the annotation and
   /// so the <c>Root</c> annotation is not needed for such objects.
   /// </summary>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Root : System.Attribute {
      private String name;
      private bool strict;
      /// <summary>
      /// This represents the name of the XML element. This is optional
      /// an is used when the name of the class is not suitable as an
      /// element name. If this is not specified then the name of the
      /// XML element will be the name of the class. If specified the
      /// class will be serialized and deserialized with the given name.
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
      /// This is used to determine whether the object represented
      /// should be parsed in a strict manner. Strict parsing requires
      /// that each element and attribute in the XML document match a
      /// field in the class schema. If an element or attribute does
      /// not match a field then the parsing fails with an exception.
      /// Setting strict parsing to false allows details within the
      /// source XML document to be skipped during deserialization.
      /// </summary>
      /// <returns>
      /// true if strict parsing is enabled, false otherwise
      /// </returns>
      public bool Strict {
         get {
            return strict;
         }
         set {
            strict = value;
         }
      }
   }
}
