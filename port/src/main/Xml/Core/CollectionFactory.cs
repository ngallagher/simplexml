#region License
//
// CollectionFactory.cs July 2006
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>CollectionFactory</c> is used to create collection
   /// instances that are compatible with the field type. This performs
   /// resolution of the collection class by firstly consulting the
   /// specified <c>Strategy</c> implementation. If the strategy
   /// cannot resolve the collection class then this will select a type
   /// from the Java Collections framework, if a compatible one exists.
   /// </summary>
   class CollectionFactory : Factory {
      /// <summary>
      /// Constructor for the <c>CollectionFactory</c> object. This
      /// is given the field type as taken from the owning object. The
      /// given type is used to determine the collection instance created.
      /// </summary>
      /// <param name="context">
      /// this is the context associated with this factory
      /// </param>
      /// <param name="type">
      /// this is the class for the owning object
      /// </param>
      public CollectionFactory(Context context, Type type) {
         super(context, type);
      }
      /// <summary>
      /// Creates a collection that is determined from the field type.
      /// This is used for the <c>ElementList</c> to get a
      /// collection that does not have any overrides. This must be
      /// done as the inline list does not contain an outer element.
      /// </summary>
      /// <returns>
      /// a type which is used to instantiate the collection
      /// </returns>
      @Override
      public Object Instance {
         get {
            Class type = getType();
            Class real = type;
            if(!isInstantiable(real)) {
               real = GetConversion(type);
            }
            if(!IsCollection(real)) {
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
      //   if(!IsCollection(real)) {
      //      throw new InstantiationException("Type is not a collection %s", type);
      //   }
      //   return real.newInstance();
      //}
      /// Creates the collection to use. The <c>Strategy</c> object
      /// is consulted for the collection class, if one is not resolved
      /// by the strategy implementation or if the collection resolved is
      /// abstract then the Java Collections framework is consulted.
      /// </summary>
      /// <param name="node">
      /// this is the input node representing the list
      /// </param>
      /// <returns>
      /// this is the collection instantiated for the field
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
         if(!IsCollection(type)) {
            throw new InstantiationException("Type is not a collection %s", type);
         }
         return context.GetInstance(type);
      }
      /// <summary>
      /// This creates a <c>Collection</c> instance from the type
      /// provided. If the type provided is abstract or an interface then
      /// this can promote the type to a collection type that can be
      /// instantiated. This is done by asking the type to convert itself.
      /// </summary>
      /// <param name="value">
      /// the type used to instantiate the collection
      /// </param>
      /// <returns>
      /// this returns a compatible collection instance
      /// </returns>
      public Instance GetInstance(Value value) {
         Class type = value.getType();
         if(!isInstantiable(type)) {
            type = GetConversion(type);
         }
         if(!IsCollection(type)) {
            throw new InstantiationException("Type is not a collection %s", type);
         }
         return new ConversionInstance(context, value, type);
      }
      /// <summary>
      /// This is used to convert the provided type to a collection type
      /// from the Java Collections framework. This will check to see if
      /// the type is a <c>List</c> or <c>Set</c> and return
      /// an <c>ArrayList</c> or <c>HashSet</c> type. If no
      /// suitable match can be found this throws an exception.
      /// </summary>
      /// <param name="type">
      /// this is the type that is to be converted
      /// </param>
      /// <returns>
      /// a collection that is assignable to the provided type
      /// </returns>
      public Class GetConversion(Class type) {
         if(type.isAssignableFrom(ArrayList.class)) {
            return ArrayList.class;
         }
         if(type.isAssignableFrom(HashSet.class)) {
            return HashSet.class;
         }
         if(type.isAssignableFrom(TreeSet.class)) {
            return TreeSet.class;
         }
         throw new InstantiationException("Cannot instantiate %s", type);
      }
      /// <summary>
      /// This determines whether the type provided is a collection type.
      /// If the type is assignable to a <c>Collection</c> then
      /// this returns true, otherwise this returns false.
      /// </summary>
      /// <param name="type">
      /// given to determine whether it is a collection
      /// </param>
      /// <returns>
      /// true if the provided type is a collection type
      /// </returns>
      public bool IsCollection(Class type) {
         return Collection.class.isAssignableFrom(type);
      }
   }
}
