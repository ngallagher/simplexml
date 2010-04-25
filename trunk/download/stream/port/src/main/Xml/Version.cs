#region License
//
// Version.cs July 2008
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
namespace SimpleFramework.Xml {
   /// <summary>
   /// The <c>Version</c> annotation is used to specify an attribute
   /// that is used to represent a revision of the class XML schema. This
   /// annotation can annotate only floating point types such as double,
   /// float, and the java primitive object types. This can not be used to
   /// annotate strings, enumerations or other primitive types.
   /// </summary>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Version : System.Attribute {
      private String name;
      private double revision;
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
      /// This represents the revision of the class. A revision is used
      /// by the deserialization process to determine how to match the
      /// annotated fields and methods to the XML elements and attributes.
      /// If the version deserialized is different to the annotated
      /// revision then annotated fields and methods are not required
      /// and if there are excessive XML nodes they are ignored.
      /// </summary>
      /// <returns>
      /// this returns the version of the XML class schema
      /// </returns>
      public double Revision {
         get {
            return revision;
         }
         set {
            revision = value;
         }
      }
      /// <summary>
      /// Determines whether the version is required within an XML
      /// element. Any field marked as not required will not have its
      /// value set when the object is deserialized. This is written
      /// only if the version is not the same as the default version.
      /// </summary>
      /// <returns>
      /// true if the version is required, false otherwise
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
