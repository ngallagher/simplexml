#region License
//
// ClassInstance.cs January 2007
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
   /// The <c>ClassInstance</c> object is used to create an object
   /// by using a <c>Class</c> to determine the type. If the given
   /// class can not be instantiated then this throws an exception when
   /// the instance is requested. For performance an instantiator is
   /// given as it contains a reflection cache for constructors.
   /// </summary>
   class ClassInstance : Instance {
      /// <summary>
      /// This is the instantiator used to create the objects.
      /// </summary>
      private Instantiator creator;
      /// <summary>
      /// This represents the value of the instance if it is set.
      /// </summary>
      private Object value;
      /// <summary>
      /// This is the type of the instance that is to be created.
      /// </summary>
      private Class type;
      /// <summary>
      /// Constructor for the <c>ClassInstance</c> object. This is
      /// used to create an instance of the specified type. If the given
      /// type can not be instantiated then an exception is thrown.
      /// </summary>
      /// <param name="creator">
      /// this is the creator used for the instantiation
      /// </param>
      /// <param name="type">
      /// this is the type that is to be instantiated
      /// </param>
      public ClassInstance(Instantiator creator, Class type) {
         this.creator = creator;
         this.type = type;
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
            if(value == null) {
               value = creator.GetObject(type);
            }
            return value;
         }
      }
      //public Object GetInstance() {
      //   if(value == null) {
      //      value = creator.GetObject(type);
      //   }
      //   return value;
      //}
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
      public Object SetInstance(Object value) {
         return this.value = value;
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
      /// references could be substitutes objects or existing ones.
      /// </summary>
      /// <returns>
      /// this returns true if the object is a reference
      /// </returns>
      public bool IsReference() {
         return false;
      }
   }
}
