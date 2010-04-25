#region License
//
// Replace.cs June 2007
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
   /// The <c>Replace</c> method is used to replace an object that
   /// is about to be serialized to an XML document. This is used to so
   /// that an object can provide a substitute to itself. Scenarios such
   /// as serializing an object to an external file or location can be
   /// accommodated using a write replacement method.
   /// <p>
   /// This is similar to the <c>writeReplace</c> method used within
   /// Java Object Serialization in that it is used to plug a replacement
   /// in to the resulting stream during the serialization process. Care
   /// should be taken to provide a suitable type from the replacement so
   /// that the object can be deserialized at a later time.
   /// </summary>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Replace : System.Attribute {
   }
}
