#region License
//
// MapFactory.cs July 2007
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
using SimpleFramework.Xml.Stream;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>MapFactory</c> is used to create map instances that
   /// are compatible with the field type. This performs resolution of
   /// the map class by consulting the specified <c>Strategy</c>
   /// implementation. If the strategy cannot resolve the map class
   /// then this will select a type from the Java Collections framework,
   /// if a compatible one exists.
   /// </summary>
   class MapFactory : Factory {
      /// <summary>
      /// Constructor for the <c>MapFactory</c> object. This is
      /// given the field type as taken from the owning object. The
      /// given type is used to determine the map instance created.
      /// </summary>
      /// <param name="context">
      /// this is the context object for this factory
      /// </param>
      /// <param name="type">
      /// this is the class for the owning object
      /// </param>
      public MapFactory(Context context, Type type) {
         super(context, type);
      }
      /// <summary>
      /// Creates a map object that is determined from the field type.
      /// This is used for the <c>ElementMap</c> to get a map
      /// that does not have any overrides. This must be done as the
      /// inline list does not contain an outer element.
      /// </summary>
      /// <returns>
      /// a type which is used to instantiate the map
      /// </returns>
      public Object Instance {
         get {
            Class type = getType();
            Class real = type;
            if(!isInstantiable(real)) {
               real = GetConversion(type);
            }
            if(!IsMap(real)) {
               throw new InstantiationException("Type is not a collection %s", type);
            }
            return real.newInstance();
         }
      }
      //public Object GetInstance() {
      //   Class type = getType();
      //   Class real = type;
      //   if(!isInstantiable(real)) {
      //      real = GetConversion(type);
      //   }
      //   if(!IsMap(real)) {
      //      throw new InstantiationException("Type is not a collection %s", type);
      //   }
      //   return real.newInstance();
      //}
      /// Creates the map object to use. The <c>Strategy</c> object
      /// is consulted for the map object class, if one is not resolved
      /// by the strategy implementation or if the collection resolved is
      /// abstract then the Java Collections framework is consulted.
      /// </summary>
      /// <param name="node">
      /// this is the input node representing the list
      /// </param>
      /// <returns>
      /// this is the map object instantiated for the field
      /// </returns>
      public Instance GetInstance(InputNode node) {
         Value value = getOverride(node);
         Class type = getType();
         if(value != null) {
            return GetInstance(value);
         }
         if(!isInstantiable(type)) {
            type = GetConversion(type);
         }
         if(!IsMap(type)) {
            throw new InstantiationException("Type is not a map %s", type);
         }
         return context.GetInstance(type);
      }
      /// <summary>
      /// This creates a <c>Map</c> object instance from the type
      /// provided. If the type provided is abstract or an interface then
      /// this can promote the type to a map object type that can be
      /// instantiated. This is done by asking the type to convert itself.
      /// </summary>
      /// <param name="value">
      /// the type used to instantiate the map object
      /// </param>
      /// <returns>
      /// this returns a compatible map object instance
      /// </returns>
      public Instance GetInstance(Value value) {
         Class type = value.getType();
         if(!isInstantiable(type)) {
            type = GetConversion(type);
         }
         if(!IsMap(type)) {
            throw new InstantiationException("Type is not a map %s", type);
         }
         return new ConversionInstance(context, value, type);
      }
      /// <summary>
      /// This is used to convert the provided type to a map object type
      /// from the Java Collections framework. This will check to see if
      /// the type is a <c>Map</c> or <c>SortedMap</c> and
      /// return a <c>HashMap</c> or <c>TreeSet</c> type. If
      /// no suitable match can be found this throws an exception.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be converted
      /// </param>
      /// <returns>
      /// a collection that is assignable to the provided type
      /// </returns>
      public Class GetConversion(Class type) {
         if(type.isAssignableFrom(HashMap.class)) {
            return HashMap.class;
         }
         if(type.isAssignableFrom(TreeMap.class)) {
            return TreeMap.class;
         }
         throw new InstantiationException("Cannot instantiate %s", type);
      }
      /// <summary>
      /// This determines whether the type provided is a object map type.
      /// If the type is assignable to a <c> Map</c> object then
      /// this returns true, otherwise this returns false.
      /// </summary>
      /// <param name="type">
      /// given to determine whether it is a map type
      /// </param>
      /// <returns>
      /// true if the provided type is a map object type
      /// </returns>
      public bool IsMap(Class type) {
         return Map.class.isAssignableFrom(type);
      }
   }
}
