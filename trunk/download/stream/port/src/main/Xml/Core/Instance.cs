#region License
//
// Instance.cs January 2007
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
   /// The <c>Instance</c> object creates a type that is represented
   /// by an XML element. Typically the <c>getInstance</c> method
   /// acts as a proxy to the classes new instance method, which takes no
   /// arguments. Simply delegating to <c>Class.newInstance</c> will
   /// sometimes not be sufficient, is such cases reflectively acquiring
   /// the classes constructor may be required in order to pass arguments.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Value
   /// </seealso>
   interface Instance {
      /// <summary>
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If for some reason the type can
      /// not be instantiated an exception is thrown from this.
      /// </summary>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object Instance {
         get;
      }
      //public Object GetInstance();
      /// This method is used acquire the value from the type and if
      /// possible replace the value for the type. If the value can
      /// not be replaced then an exception should be thrown. This
      /// is used to allow primitives to be inserted into a graph.
      /// </summary>
      /// <param name="value">
      /// this is the value to insert as the type
      /// </param>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object SetInstance(Object value);
      /// <summary>
      /// This is used to determine if the type is a reference type.
      /// A reference type is a type that does not require any XML
      /// deserialization based on its annotations. Values that are
      /// references could be substitutes objects or existing ones.
      /// </summary>
      /// <returns>
      /// this returns true if the object is a reference
      /// </returns>
      public bool IsReference();
      /// <summary>
      /// This is the type of the object instance that will be created
      /// by the <c>getInstance</c> method. This allows the
      /// deserialization process to perform checks against the field.
      /// </summary>
      /// <returns>
      /// the type of the object that will be instantiated
      /// </returns>
      public Class Type {
         get;
      }
      //public Class GetType();
}
