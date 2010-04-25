#region License
//
// ObjectValue.cs January 2007
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
   /// The <c>ObjectValue</c> is an implementation of a value
   /// that represents a object other than an array. Objects described
   /// by this can be instantiated and set in to the internal graph
   /// so that they can be later retrieved.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Allocate
   /// </seealso>
   class ObjectValue : Value {
      /// <summary>
      /// This is the value that has been set for this instance.
      /// </summary>
      private Object value;
      /// <summary>
      /// This is the type that this object is used to represent.
      /// </summary>
      private Class type;
      /// <summary>
      /// Constructor for the <c>ObjectValue</c> object. This is
      /// used to describe an object that can be instantiated by the
      /// deserialization process and set on the internal graph used.
      /// </summary>
      /// <param name="type">
      /// this is the type of object that is described
      /// </param>
      public ObjectValue(Class type) {
         this.type = type;
      }
      /// <summary>
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If the value has not been set
      /// then this method will return null as this is not a reference.
      /// </summary>
      /// <returns>
      /// an instance of the type this object represents
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
      /// This method is used set the value within this object. Once
      /// this is set then the <c>getValue</c> method will return
      /// the object that has been provided for consistency.
      /// </summary>
      /// <param name="value">
      /// this is the value to insert as the type
      /// </param>
      //public void SetValue(Object value) {
      //   this.value = value;
      //}
      /// This is the type of the object instance this represents. The
      /// type returned by this is used to instantiate an object which
      /// will be set on this value and the internal graph maintained.
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
      /// This returns zero as this is an object and will typically
      /// not be used to instantiate anything. If the reference is an
      /// an array then this can not be used to instantiate it.
      /// </summary>
      /// <returns>
      /// this returns zero regardless of the value type
      /// </returns>
      public int Length {
         get {
            return 0;
         }
      }
      //public int GetLength() {
      //   return 0;
      //}
      /// This method always returns false for the default type. This
      /// is because by default all elements encountered within the
      /// XML are to be deserialized based on there XML annotations.
      /// </summary>
      /// <returns>
      /// this returns false for each type encountered
      /// </returns>
      public bool IsReference() {
         return false;
      }
   }
}
