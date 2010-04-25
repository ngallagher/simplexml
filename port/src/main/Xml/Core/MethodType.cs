#region License
//
// MethodType.cs May 2007
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
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>MethodType</c> enumeration is used to specify a
   /// set of types that can be used to classify Java Beans methods.
   /// This creates three types for the get, is, and set methods. The
   /// method types allow the <c>MethodScanner</c> to determine
   /// what function the method has in creating a contact point for
   /// the object. This also enables methods to be parsed correctly.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.MethodScanner
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.Core.MethodPart
   /// </seealso>
   enum MethodType {
      /// <summary>
      /// This is used to represent a method that acts as a getter.
      /// </summary>
      GET(3),
      /// <summary>
      /// This is used to represent a method that acts as a getter.
      /// </summary>
      IS(2),
      /// <summary>
      /// This is used to represent a method that acts as a setter.
      /// </summary>
      SET(3),
      /// <summary>
      /// This is used to represent a a normal method to be ignored.
      /// </summary>
      NONE(0);
      /// <summary>
      /// This is the length of the prefix the method type uses.
      /// </summary>
      private int prefix;
      /// <summary>
      /// Constructor for the <c>MethodType</c> object. This is
      /// used to create a method type specifying the length of the
      /// prefix. This allows the method name to be parsed easily.
      /// </summary>
      /// <param name="prefix">
      /// this is the length of the method name prefix
      /// </param>
      private MethodType(int prefix) {
         this.prefix = prefix;
      }
      /// <summary>
      /// This is used to acquire the prefix for the method type. The
      /// prefix allows the method name to be extracted easily as it
      /// is used to determine the character range that forms the name.
      /// </summary>
      /// <returns>
      /// this returns the method name prefix for the type
      /// </returns>
      public int GetPrefix() {
         return prefix;
      }
   }
}
