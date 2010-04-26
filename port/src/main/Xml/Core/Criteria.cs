#region License
//
// Criteria.cs December 2009
//
// Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>Criteria</c> object represents the criteria used to
   /// create an object and populate its methods and fields. This allows
   /// all deserialized information for a single object to be stored in
   /// a single location. All deserialized variables are accessible from
   /// the <c>get</c> method.
   /// </summary>
   interface Criteria {
      /// <summary>
      /// This is used to get the <c>Variable</c> that represents
      /// a deserialized object. The variable contains all the meta
      /// data for the field or method and the value that is to be set
      /// on the method or field.
      /// </summary>
      /// <param name="name">
      /// this is the name of the variable to be acquired
      /// </param>
      /// <returns>
      /// this returns the named variable if it exists
      /// </returns>
      Variable Get(String name);
      /// <summary>
      /// This is used to remove the <c>Variable</c> from this
      /// criteria object. When removed, the variable will no longer be
      /// used to set the method or field when the <c>commit</c>
      /// method is invoked.
      /// </summary>
      /// <param name="name">
      /// this is the name of the variable to be removed
      /// </param>
      /// <returns>
      /// this returns the named variable if it exists
      /// </returns>
      Variable Remove(String name);
      /// <summary>
      /// This is used to create a <c>Variable</c> and set it for
      /// this criteria. The variable can be retrieved at a later stage
      /// using the name of the label. This allows for repeat reads as
      /// the variable can be used to acquire the labels converter.
      /// </summary>
      /// <param name="label">
      /// this is the label used to create the pointer
      /// </param>
      /// <param name="value">
      /// this is the value of the object to be read
      /// </param>
      void Set(Label label, Object value);
      /// <summary>
      /// This is used to set the values for the methods and fields of
      /// the specified object. Invoking this performs the population
      /// of an object being deserialized. It ensures that each value
      /// is set after the XML element has been fully read.
      /// </summary>
      /// <param name="source">
      /// this is the object that is to be populated
      /// </param>
      void Commit(Object source);
   }
}
