#region License
//
// Commit.cs July 2006
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
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Commit</c> annotation is used to mark a method within
   /// a serializable object that requires a callback from the persister
   /// once the deserialization completes. The commit method is invoked
   /// by the <c>Persister</c> after all fields have been assigned
   /// and after the validation method has been invoked, if the object
   /// has a method marked with the <c>Validate</c> annotation.
   /// <p>
   /// Typically the commit method is used to complete deserialization
   /// by allowing the object to build further data structures from the
   /// fields that have been created from the deserialization process.
   /// The commit method must be a no argument method or a method that
   /// takes a single <c>Map</c> object argument, and may throw an
   /// exception, in which case the deserialization process terminates.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Validate
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Commit : System.Attribute {
   }
}
