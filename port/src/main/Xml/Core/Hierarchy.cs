#region License
//
// Hierarchy.cs April 2007
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Hierarchy</c> object is used to acquire the hierarchy
   /// of a specified class. This ensures that the iteration order of the
   /// hierarchy is from the base class to the most specialized class.
   /// It is used during scanning to ensure that the order of methods and
   /// fields written as XML is in declaration order from the most
   /// basic to the most specialized.
   /// </summary>
   class Hierarchy : LinkedList<Class> {
      /// <summary>
      /// Constructor for the <c>Hierarchy</c> object. This is used
      /// to create the hierarchy of the specified class. It enables the
      /// scanning process to evaluate methods and fields in the order of
      /// most basic to most specialized.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be scanned
      /// </param>
      public Hierarchy(Class type) {
         Scan(type);
      }
      /// <summary>
      /// This is used to scan the specified <c>Class</c> in such a
      /// way that the most basic type is at the head of the list and the
      /// most specialized is at the last, ensuring correct iteration.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be scanned
      /// </param>
      public void Scan(Class type) {
         while(type != null) {
            addFirst(type);
            type = type.getSuperclass();
         }
         remove(Object.class);
      }
   }
}
