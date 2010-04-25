#region License
//
// Default.cs January 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>Default</c> annotation is used to specify that all
   /// fields or methods should be serialized in a default manner. This
   /// basically allows an objects fields or properties to be serialized
   /// without the need to annotate them. This has advantages if the
   /// format of the serialized object is not important, as it allows
   /// the object to be serialized with a minimal use of annotations.
   /// </code>
   ///    &#64;Root
   ///    &#64;Default(DefaultType.FIELD)
   ///    public class Example {
   ///       ...
   ///    }
   /// </code>
   /// Defaults can be applied to either fields or property methods. If
   /// this annotation is applied to a class, certain fields or methods
   /// can be ignored using the <c>Transient</c> annotation. If a
   /// member is marked as transient then it will not be serialized. The
   /// defaults are applied only to those members that are not otherwise
   /// annotated with an XML annotation.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Transient
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Default : System.Attribute {
      private DefaultType value;
      /// <summary>
      /// This method is used to return the type of default that is to
      /// be applied to the class. Defaults can be applied to either
      /// fields or property methods. Any member with an XML annotation
      /// will not be treated as a default.
      /// </summary>
      /// <returns>
      /// this returns the type of defaults to be applied
      /// </returns>
      public DefaultType Value {
         get {
            return value;
         }
         set {
            value = value;
         }
      }
   }
}
