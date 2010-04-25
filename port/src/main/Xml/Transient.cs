#region License
//
// Transient.cs June 2007
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
   /// The <c>Transient</c> annotation is an optional annotation
   /// that can be used within an XML class schema to mark a method or
   /// field as being transient, which indicates that it does not take
   /// part in serialization or deserialization. This is particularly
   /// useful when the <c>Default</c> annotation is applied to
   /// a class, as it indicates no default serialization is to be used.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Default
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Transient : System.Attribute {
   }
}
