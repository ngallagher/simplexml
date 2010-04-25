#region License
//
// Complete.cs July 2006
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
   /// The <c>Complete</c> annotation is used to mark a method that
   /// requires a callback from the persister once the serialization of
   /// the object has completed. The complete method is typically used
   /// in combination with the persist method, which is the method that
   /// is annotated with the <c>Persist</c> annotation.
   /// <p>
   /// Typically the complete method will revert any changes made when
   /// the persist method was invoked. For example, should the persist
   /// method acquire a lock to ensure the object is serialized in a
   /// safe state then the commit method can be used to release the lock.
   /// The complete method must be a no argument public method or a
   /// method that takes a single <c>Map</c> object argument. The
   /// complete method is invoked even if deserialization terminates.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Persist
   /// </seealso>
   [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
   public class Complete : System.Attribute {
   }
}
