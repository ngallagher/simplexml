#region License
//
// Type.cs January 2010
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
   /// The <c>Type</c> interface is used to represent a method or
   /// field that has been annotated for serialization. Representing
   /// methods and fields as a generic type object allows various
   /// common details to be extracted in a uniform way. It allows all
   /// annotations on the method or field to be exposed. This can
   /// also wrap classes that represent entries to a list or map.
   /// </summary>
   public interface Type {
      /// <summary>
      /// This will provide the method or field type. The type is the
      /// class that is to be read and written on the object. Typically
      /// the type will be a serializable object or a primitive type.
      /// </summary>
      /// <returns>
      /// this returns the type for this method o field
      /// </returns>
      public Class Type {
         get;
      }
      //public Class GetType();
      /// This is the annotation associated with the method or field
      /// that has been annotated. If this represents an entry to a
      /// Java collection such as a <c>java.util.List</c> then
      /// this will return null for any annotation requested.
      /// </summary>
      /// <param name="type">
      /// this is the type of the annotation to acquire
      /// </param>
      /// <returns>
      /// this provides the annotation associated with this
      /// </returns>
      <T : Annotation> T getAnnotation(Class<T> type);
      /// <summary>
      /// This is used to describe the type as it exists within the
      /// owning class. This is used to provide error messages that can
      /// be used to debug issues that occur when processing.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the type
      /// </returns>
      String ToString();
   }
}
