#region License
//
// ObjectInstance.cs April 2007
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
   /// The <c>ObjectInstance</c> is used to instantiate an object
   /// from the criteria provided in the given <c>Value</c>. If
   /// the value contains a reference then the reference is provided
   /// from this type. For performance the <c>Context</c> object
   /// is used to instantiate the object as it contains a reflection
   /// cache of constructors.
   /// </summary>
   class ObjectInstance : Instance {
      /// <summary>
      /// This is the context that is used to create the instance.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This is the value object that will be wrapped by this.
      /// </summary>
      private readonly Value value;
      /// <summary>
      /// This is the new class that is used for the instantiation.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// Constructor for the <c>ObjectInstance</c> object. This
      /// is used to create an instance of the type described by the
      /// value object. If the value object contains a reference then
      /// this will simply provide that reference.
      /// </summary>
      /// <param name="context">
      /// this is used to instantiate the object
      /// </param>
      /// <param name="value">
      /// this is the value describing the instance
      /// </param>
      public ObjectInstance(Context context, Value value) {
         this.type = value.Type;
         this.context = context;
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
            Object object = GetInstance(type);
            if(value != null) {
               value.Value = value;
            }
            return object;
         }
      }
      //public Object GetInstance() {
      //   if(value.IsReference()) {
      //      return value.Value;
      //   }
      //   Object object = GetInstance(type);
      //   if(value != null) {
      //      value.Value = value;
      //   }
      //   return object;
      //}
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If for some reason the type can
      /// not be instantiated an exception is thrown from this.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be instantiated
      /// </param>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object GetInstance(Class type) {
         Instance value = context.GetInstance(type);
         Object object = value.Instance;
         return object;
      }
      /// <summary>
      /// This method is used acquire the value from the type and if
      /// possible replace the value for the type. If the value can
      /// not be replaced then an exception should be thrown. This
      /// is used to allow primitives to be inserted into a graph.
      /// </summary>
      /// <param name="object">
      /// this is the object to insert as the value
      /// </param>
      /// <returns>
      /// an instance of the type this object represents
      /// </returns>
      public Object SetInstance(Object object) {
         if(value != null) {
            value.Value = value;
         }
         return object;
      }
      /// <summary>
      /// This is used to determine if the type is a reference type.
      /// A reference type is a type that does not require any XML
      /// deserialization based on its annotations. Types that are
      /// references could be substitutes objects are existing ones.
      /// </summary>
      /// <returns>
      /// this returns true if the object is a reference
      /// </returns>
      public bool IsReference() {
         return value.IsReference();
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
}
