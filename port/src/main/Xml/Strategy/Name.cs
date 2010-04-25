#region License
//
// Attributes.cs January 2010
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
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// This contains the default attribute names to use to populate the
   /// XML elements with data relating to the object to be serialized.
   /// Various details, such as the class name of an object need to be
   /// written to the element in order for it to be deserialized. Such
   /// attribute names are shared between strategy implementations.
   /// </summary>
   interface Name {
      /// <summary>
      /// The default name of the attribute used to identify an object.
      /// </summary>
      public const String MARK = "id";
      /// <summary>
      /// The default name of the attribute used for circular references.
      /// </summary>
      public const String REFER = "reference";
      /// <summary>
      /// The default name of the attribute used to specify the length.
      /// </summary>
      public const String LENGTH = "length";
      /// <summary>
      /// The default name of the attribute used to specify the class.
      /// </summary>
      public const String LABEL = "class";
   }
}
