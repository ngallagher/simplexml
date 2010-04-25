#region License
//
// Entry.cs July 2006
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
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>Entry</c> object represents entries to the dictionary
   /// object. Every entry must have a name attribute, which is used to
   /// establish mappings within the <c>Dictionary</c> object. Each
   /// entry entered into the dictionary can be retrieved using its name.
   /// <p>
   /// The entry can be serialzed with the dictionary to an XML document.
   /// Items stored within the dictionary need to extend this entry
   /// object to ensure that they can be mapped and serialized with the
   /// dictionary. Implementations should override the root annotation.
   /// </summary>
   public interface Entry {
      /// <summary>
      /// Represents the name of the entry instance used for mappings.
      /// This will be used to map the object to the internal map in
      /// the <c>Dictionary</c>. This allows serialized objects
      /// to be added to the dictionary transparently.
      /// </summary>
      /// <returns>
      /// this returns the name of the entry that is used
      /// </returns>
      public String Name {
         get;
      }
}
