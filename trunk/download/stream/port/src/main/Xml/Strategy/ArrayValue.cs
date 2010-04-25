#region License
//
// ArrayValue.cs April 2007
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
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>ArrayValue</c> object is a value used for describing
   /// arrays for a specified component type object. This provides the
   /// component type for the array as well as the length of the array,
   /// which allows the deserialization process to build a suitable length
   /// array from the criteria taken from the XML element.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Allocate
   /// </seealso>
   class ArrayValue : Value {
      /// <summary>
      /// This is the value that has been set within this value object.
      /// </summary>
      private Object value;
      /// <summary>
      /// This is the optional field type for the array to be created.
      /// </summary>
      private Class type;
      /// <summary>
      /// This is used to determine the size of the array to be created.
      /// </summary>
      private int size;
      /// <summary>
      /// Constructor for the <c>ArrayValue</c> object. This will
      /// provide sufficient criteria to the deserialization process
      /// to instantiate an array of the specified size an type.
      /// </summary>
      /// <param name="type">
      /// this is the component type for the array
      /// </param>
      /// <param name="size">
      /// this is the size of the array to instantiate
      /// </param>
      public ArrayValue(Class type, int size) {
         this.type = type;
         this.size = size;
      }
      /// <summary>
      /// This is the instance that is acquired from this value. This is
      /// typically used if the <c>isReference</c> method is true.
      /// If there was no value reference provided then this returns null.
      /// </summary>
      /// <returns>
      /// this returns a reference to an existing array
      /// </returns>
      public Object Value {
         get {
            return value;
         }
         set {
            this.value = _value;
         }
      }
      //public Object GetValue() {
      //   return value;
      //}
      /// This method is set the value so that future calls provide the
      /// value that was provided. Setting the value ensures that the
      /// value used is consistent across invocations of this object.
      /// </summary>
      /// <param name="value">
      /// this is the value to inserted to this object
      /// </param>
      //public void SetValue(Object value) {
      //   this.value = value;
      //}
      /// This will return the component type for the array instance
      /// that is described by this object. This is used to ensure that
      /// an array with the correct component type can be instantiated.
      /// </summary>
      /// <returns>
      /// this returns the component type for the array
      /// </returns>
      public Class Type {
         get {
            return type;
         }
      }
      //public Class GetType() {
      //   return type;
      //}
      /// This returns the length of the array that is to be allocated.
      /// For various <c>Strategy</c> implementations the length
      /// is provided as an attribute on the array XML element.
      /// </summary>
      /// <returns>
      /// this returns the number of elements for the array
      /// </returns>
      public int Length {
         get {
            return size;
         }
      }
      //public int GetLength() {
      //   return size;
      //}
      /// This will return false for the array value because the array
      /// is not a reference type. Only <c>Reference</c> values
      /// will have this set to true as they read from the graph.
      /// </summary>
      /// <returns>
      /// this returns false as this is not a reference value
      /// </returns>
      public bool IsReference() {
         return false;
      }
   }
}
