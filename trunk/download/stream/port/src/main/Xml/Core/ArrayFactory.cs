#region License
//
// ArrayFactory.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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
using SimpleFramework.Xml.Stream;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>ArrayFactory</c> is used to create object array
   /// types that are compatible with the field type. This simply
   /// requires the type of the array in order to instantiate that
   /// array. However, this also performs a check on the field type
   /// to ensure that the array component types are compatible.
   /// </summary>
   class ArrayFactory : Factory {
      /// <summary>
      /// Constructor for the <c>ArrayFactory</c> object. This is
      /// given the array component type as taken from the field type
      /// of the source object. Each request for an array will return
      /// an array which uses a compatible component type.
      /// </summary>
      /// <param name="context">
      /// this is the context object for serialization
      /// </param>
      /// <param name="type">
      /// the array component type for the field object
      /// </param>
      public ArrayFactory(Context context, Type type) {
         super(context, type);
      }
      /// <summary>
      /// This is used to create a default instance of the field type. It
      /// is up to the subclass to determine how to best instantiate an
      /// object of the field type that best suits. This is used when the
      /// empty value is required or to create the default type instance.
      /// </summary>
      /// <returns>
      /// a type which is used to instantiate the collection
      /// </returns>
      @Override
      public Object Instance {
         get {
            Class type = ComponentType;
            if(type != null) {
               return Array.newInstance(type, 0);
            }
            return null;
         }
      }
      //public Object GetInstance() {
      //   Class type = ComponentType;
      //   if(type != null) {
      //      return Array.newInstance(type, 0);
      //   }
      //   return null;
      //}
      /// Creates the array type to use. This will use the provided
      /// XML element to determine the array type and provide a means
      /// for creating an array with the <c>Value</c> object. If
      /// the array size cannot be determined an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the input node for the array element
      /// </param>
      /// <returns>
      /// the object array type used for the instantiation
      /// </returns>
      public Instance GetInstance(InputNode node) {
         Value value = getOverride(node);
         if(value == null) {
            throw new ElementException("Array length required for %s", type);
         }
         Class type = value.getType();
         return GetInstance(value, type);
      }
      /// <summary>
      /// Creates the array type to use. This will use the provided
      /// XML element to determine the array type and provide a means
      /// for creating an array with the <c>Value</c> object. If
      /// the array types are not compatible an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the type object with the array details
      /// </param>
      /// <param name="type">
      /// this is the entry type for the array instance
      /// </param>
      /// <returns>
      /// this object array type used for the instantiation
      /// </returns>
      public Instance GetInstance(Value value, Class type) {
         Class expect = ComponentType;
         if(!expect.isAssignableFrom(type)) {
            throw new InstantiationException("Array of type %s cannot hold %s", expect, type);
         }
         return new ArrayInstance(value);
      }
      /// <summary>
      /// This is used to extract the component type for the array class
      /// this factory represents. This is used when an array is to be
      /// instantiated. If the class provided to the factory is not an
      /// array then this will throw an exception.
      /// </summary>
      /// <returns>
      /// this returns the component type for the array
      /// </returns>
      public Class ComponentType {
         get {
            Class type = getType();
            if(!type.isArray()) {
               throw new InstantiationException("The %s not an array", type);
            }
            return type.ComponentType;
         }
      }
      //public Class GetComponentType() {
      //   Class type = getType();
      //   if(!type.isArray()) {
      //      throw new InstantiationException("The %s not an array", type);
      //   }
      //   return type.ComponentType;
      //}
}
