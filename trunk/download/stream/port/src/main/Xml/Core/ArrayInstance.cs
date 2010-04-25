#region License
//
// ArrayInstance.cs January 2007
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
using SimpleFramework.Xml.Strategy;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>ArrayInstance</c> object is used for creating arrays
   /// from a specified <c>Value</c> object. This allows primitive
   /// and composite arrays to be acquired either by reference or by value
   /// from the given value object. This must be  given the length of the
   /// array so that it can be allocated correctly.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Instance
   /// </seealso>
   class ArrayInstance : Instance {
      /// <summary>
      /// This is the value object that contains the criteria.
      /// </summary>
      private readonly Value value;
      /// <summary>
      /// This is the array component type for the created array.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// This is the length of the array to be instantiated.
      /// </summary>
      private readonly int length;
      /// <summary>
      /// Constructor for the <c>ArrayInstance</c> object. This
      /// is used to create an object that can create an array of the
      /// given length and specified component type.
      /// </summary>
      /// <param name="value">
      /// this is the value object describing the instance
      /// </param>
      public ArrayInstance(Value value) {
         this.length = value.Length;
         this.type = value.Type;
         this.value = value;
      }
      /// <summary>
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If for some reason the type can
      /// not be instantiated an exception is thrown from this.
      /// </summary>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object Instance {
         get {
            if(value.IsReference()) {
               return value.Value;
            }
            Object array = Array.newInstance(type, length);
            if(value != null) {
               value.Value = value;
            }
            return array;
         }
      }
      //public Object GetInstance() {
      //   if(value.IsReference()) {
      //      return value.Value;
      //   }
      //   Object array = Array.newInstance(type, length);
      //   if(value != null) {
      //      value.Value = value;
      //   }
      //   return array;
      //}
      /// This method is used acquire the value from the type and if
      /// possible replace the value for the type. If the value can
      /// not be replaced then an exception should be thrown. This
      /// is used to allow primitives to be inserted into a graph.
      /// </summary>
      /// <param name="array">
      /// this is the array to insert as the value
      /// </param>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object SetInstance(Object array) {
         if(value != null) {
            value.Value = value;
         }
         return array;
      }
      /// <summary>
      /// This is the type of the object instance that will be created
      /// by the <c>getInstance</c> method. This allows the
      /// deserialization process to perform checks against the field.
      /// </summary>
      /// <returns>
      /// the type of the object that will be instantiated
      /// </returns>
      public Class Type {
         get {
            return type;
         }
      }
      //public Class GetType() {
      //   return type;
      //}
      /// This is used to determine if the type is a reference type.
      /// A reference type is a type that does not require any XML
      /// deserialization based on its annotations. Values that are
      /// references could be substitutes objects of existing ones.
      /// </summary>
      /// <returns>
      /// this returns true if the object is a reference
      /// </returns>
      public bool IsReference() {
         return value.IsReference();
      }
   }
}
