#region License
//
// ContactMap.cs January 2010
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>ContactMap</c> object is used to keep track of the
   /// contacts that have been processed. Keeping track of the contacts
   /// that have been processed ensures that no two contacts are used
   /// twice. This ensures a consistent XML class schema.
   /// </summary>
   class ContactMap : LinkedHashMap<Object, Contact> : Iterable<Contact> {
      /// <summary>
      /// This is used to iterate over the <c>Contact</c> objects
      /// in a for each loop. Iterating over the contacts allows them
      /// to be easily added to a list of unique contacts.
      /// </summary>
      /// <returns>
      /// this is used to return the contacts registered
      /// </returns>
      public Iterator<Contact> Iterator() {
         return values().Iterator();
      }
   }
}
