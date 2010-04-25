#region License
//
// Allocate.cs January 2007
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
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>Allocate</c> object is used to represent an entity
   /// that has not yet been created and needs to be allocated to the
   /// the object graph. This is given a map that contains each node
   /// in the graph keyed via a unique identifier. When an instance is
   /// created and set then it is added to the object graph.
   /// </summary>
   class Allocate : Value {
      /// <summary>
      /// This is used to create an instance of the specified type.
      /// </summary>
      private Value value;
      /// <summary>
      /// This is the unique key that is used to store the value.
      /// </summary>
      private String key;
      /// <summary>
      /// This is used to store each instance in the object graph.
      /// </summary>
      private Dictionary map;
      /// <summary>
      /// Constructor for the <c>Allocate</c> object. This is used
      /// to create a value that can be used to set any object in to the
      /// internal object graph so references can be discovered.
      /// </summary>
      /// <param name="value">
      /// this is the value used to describe the instance
      /// </param>
      /// <param name="map">
      /// this contains each instance mapped with a key
      /// </param>
      /// <param name="key">
      /// this is the unique key representing this instance
      /// </param>
      public Allocate(Value value, Dictionary map, String key) {
         this.value = value;
         this.map = map;
         this.key = key;
      }
      /// <summary>
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If the object is not set in the
      /// graph then this will return null.
      /// </summary>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object Value {
         get {
            return map.get(key);
         }
         set {
            if(key != null) {
               map.put(key, value);
            }
            _value.Value = value;
         }
      }
      //public Object GetValue() {
      //   return map.get(key);
      //}
      /// This method is used to set the provided object in to the graph
      /// so that it can later be retrieved. If the key for this value
      /// is null then no object is set in the object graph.
      /// </summary>
      /// <param name="object">
      /// this is the value to insert to the graph
      /// </param>
      //public void SetValue(Object object) {
      //   if(key != null) {
      //      map.put(key, object);
      //   }
      //   value.Value = value;
      //}
      /// This is the type of the object instance that will be created
      /// and set on this value. If this represents an array then this
      /// is the component type for the array to be created.
      /// </summary>
      /// <returns>
      /// the type of the object that will be instantiated
      /// </returns>
      public Class Type {
         get {
            return value.Type;
         }
      }
      //public Class GetType() {
      //   return value.Type;
      //}
      /// This returns the length of an array if this value represents
      /// an array. If this does not represent an array then this will
      /// return zero. It is up to the deserialization process to
      /// determine if the annotated field or method is an array.
      /// </summary>
      /// <returns>
      /// this returns the length of the array object
      /// </returns>
      public int Length {
         get {
            return value.Length;
         }
      }
      //public int GetLength() {
      //   return value.Length;
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
