#region License
//
// Value.cs January 2007
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
   /// The <c>Value</c> object describes a type that is represented
   /// by an XML element. This enables a <c>Strategy</c> to define
   /// not only the type an element represents, but also defines if that
   /// type needs to be created. This allows arrays as well as standard
   /// object types to be described. When instantiated the instance should
   /// be set on the value object for use by the strategy to detect cycles.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Strategy
   /// </seealso>
   public interface Value {
      /// <summary>
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If the value has not been set
      /// then this method will return null if this is not a reference.
      /// </summary>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object Value {
         get;
         set;
      }
      //public Object GetValue();
      /// This method is used set the value within this object. Once
      /// this is set then the <c>getValue</c> method will return
      /// the object that has been provided for consistency.
      /// </summary>
      /// <param name="value">
      /// this is the value to insert as the type
      /// </param>
      //public void SetValue(Object value);
      /// This is the type of the object instance this represents. The
      /// type returned by this is used to instantiate an object which
      /// will be set on this value and the internal graph maintained.
      /// </summary>
      /// <returns>
      /// the type of the object that must be instantiated
      /// </returns>
      public Class Type {
         get;
      }
      //public Class GetType();
      /// This returns the length of the array that is to be allocated.
      /// If this value does not represent an array then this should
      /// return zero to indicate that it is not an array object.
      /// </summary>
      /// <returns>
      /// this returns the number of elements for the array
      /// </returns>
      public int Length {
         get;
      }
      //public int GetLength();
      /// This will return true if the object represents a reference.
      /// A reference will provide a valid instance when this objects
      /// getter is invoked. A valid instance can be a null.
      /// </summary>
      /// <returns>
      /// this returns true if this represents a reference
      /// </returns>
      public bool IsReference();
   }
}
