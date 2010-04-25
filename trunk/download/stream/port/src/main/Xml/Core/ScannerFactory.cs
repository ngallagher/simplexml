#region License
//
// ScannerFactory.cs July 2006
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
   /// The <c>ScannerFactory</c> is used to create scanner objects
   /// that will scan a class for its XML class schema. Caching is done
   /// by this factory so that repeat retrievals of a <c>Scanner</c>
   /// will not require repeat scanning of the class for its XML schema.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Context
   /// </seealso>
   class ScannerFactory {
      /// <summary>
      /// This is used to cache all schemas built to represent a class.
      /// </summary>
      private readonly ScannerCache cache;
      /// <summary>
      /// Constructor for the <c>ScannerFactory</c> object. This is
      /// used to create a factory that will create and cache scanned
      /// data for a given class. Scanning the class is required to find
      /// the fields and methods that have been annotated.
      /// </summary>
      public ScannerFactory() {
         this.cache = new ScannerCache();
      }
      /// <summary>
      /// This creates a <c>Scanner</c> object that can be used to
      /// examine the fields within the XML class schema. The scanner
      /// maintains information when a field from within the scanner is
      /// visited, this allows the serialization and deserialization
      /// process to determine if all required XML annotations are used.
      /// </summary>
      /// <param name="type">
      /// the schema class the scanner is created for
      /// </param>
      /// <returns>
      /// a scanner that can maintains information on the type
      /// </returns>
      public Scanner GetInstance(Class type) {
         Scanner schema = cache.get(type);
         if(schema == null) {
            schema = new Scanner(type);
            cache.put(type, schema);
         }
         return schema;
      }
   }
}
