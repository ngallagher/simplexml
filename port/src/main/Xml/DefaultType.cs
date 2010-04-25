#region License
//
// DefaultType.cs January 2010
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
   /// The <c>DefaultType</c> enumeration is used to specify the
   /// type of defaults to apply to a class. The <c>Default</c>
   /// annotation is used to specify which type of default to apply. If
   /// applied the serializer will synthesize an XML annotation for the
   /// fields or properties of the object. The synthesized annotations
   /// will have default values for its attributes.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Default
   /// </seealso>
   public enum DefaultType {
      /// <summary>
      /// This tells the serializer to default all member fields.
      /// </summary>
      FIELD,
      /// <summary>
      /// This tells the serializer to default all property methods.
      /// </summary>
      PROPERTY
   }
}
