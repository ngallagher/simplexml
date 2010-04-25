#region License
//
// Instantiator.cs July 2006
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
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Instantiator</c> is used to instantiate types that
   /// will leverage a constructor cache to quickly create the objects.
   /// This is used by the various object factories to return type
   /// instances that can be used by converters to create the objects
   /// that will later be deserialized.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Instance
   /// </seealso>
   class Instantiator {
      /// <summary>
      /// This is used to cache the constructors for the given types.
      /// </summary>
      private readonly ConstructorCache cache;
      /// <summary>
      /// Constructor for the <c>Instantiator</c> object. This will
      /// create a constructor cache that can be used to cache all of
      /// the constructors instantiated for the required types.
      /// </summary>
      public Instantiator() {
         this.cache = new ConstructorCache();
      }
      /// <summary>
      /// This will create an <c>Instance</c> that can be used
      /// to instantiate objects of the specified class. This leverages
      /// an internal constructor cache to ensure creation is quicker.
      /// </summary>
      /// <param name="value">
      /// this contains information on the object instance
      /// </param>
      /// <returns>
      /// this will return an object for instantiating objects
      /// </returns>
      public Instance GetInstance(Value value) {
         return new ValueInstance(this, value);
      }
      /// <summary>
      /// This will create an <c>Instance</c> that can be used
      /// to instantiate objects of the specified class. This leverages
      /// an internal constructor cache to ensure creation is quicker.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be instantiated
      /// </param>
      /// <returns>
      /// this will return an object for instantiating objects
      /// </returns>
      public Instance GetInstance(Class type) {
         return new ClassInstance(this, type);
      }
      /// <summary>
      /// This method will instantiate an object of the provided type. If
      /// the object or constructor does not have public access then this
      /// will ensure the constructor is accessible and can be used.
      /// </summary>
      /// <param name="type">
      /// this is used to ensure the object is accessible
      /// </param>
      /// <returns>
      /// this returns an instance of the specific class type
      /// </returns>
      public Object GetObject(Class type) {
         Constructor method = cache.get(type);
         if(method == null) {
            method = type.getDeclaredConstructor();
            if(!method.isAccessible()) {
               method.setAccessible(true);
            }
            cache.put(type, method);
         }
         return method.newInstance();
      }
   }
}
