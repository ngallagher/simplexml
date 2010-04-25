#region License
//
// CoversionInstance.cs April 2007
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
   /// The <c>ConversionInstance</c> object is used to promote the
   /// type to some more specialized type. For example if a field or
   /// method that represents a <c>List</c> is annotated then this
   /// might create a specialized type such as a <c>Vector</c>. It
   /// typically used to promote a type either because it is abstract
   /// or because another type is required.
   /// <p>
   /// This is used by the <c>CollectionFactory</c> to convert the
   /// type of a collection field from an abstract type to a instantiable
   /// type. This is used to simplify strategy implementations.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.CollectionFactory
   /// </seealso>
   class ConversionInstance : Instance {
      /// <summary>
      /// This is the context that is used to create the instance.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This is the new class that is used for the type conversion.
      /// </summary>
      private readonly Class convert;
      /// <summary>
      /// This is the value object that will be wrapped by this.
      /// </summary>
      private readonly Value value;
      /// <summary>
      /// This is used to specify the creation of a conversion type that
      /// can be used for creating an instance with a class other than
      /// the default class specified by the <c>Value</c> object.
      /// </summary>
      /// <param name="context">
      /// this is the context used for instantiation
      /// </param>
      /// <param name="value">
      /// this is the type used to create the instance
      /// </param>
      /// <param name="convert">
      /// this is the class the type is converted to
      /// </param>
      public ConversionInstance(Context context, Value value, Class convert) {
         this.context = context;
         this.convert = convert;
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
            Object created = GetInstance(convert);
            if(created != null) {
               SetInstance(created);
            }
            return created;
         }
      }
      //public Object GetInstance() {
      //   if(value.IsReference()) {
      //      return value.Value;
      //   }
      //   Object created = GetInstance(convert);
      //   if(created != null) {
      //      SetInstance(created);
      //   }
      //   return created;
      //}
      /// This method is used to acquire an instance of the type that
      /// is defined by this object. If for some reason the type can
      /// not be instantiated an exception is thrown from this.
      /// </summary>
      /// <param name="type">
      /// this is the type of the instance to create
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
      /// This is the type of the object instance that will be created
      /// by the <c>getInstance</c> method. This allows the
      /// deserialization process to perform checks against the field.
      /// </summary>
      /// <returns>
      /// the type of the object that will be instantiated
      /// </returns>
      public Class Type {
         get {
            return convert;
         }
      }
      //public Class GetType() {
      //   return convert;
      //}
      /// This will return true if the <c>Value</c> object provided
      /// is a reference type. Typically a reference type refers to a
      /// type that is substituted during the deserialization process
      /// and so constitutes an object that does not need initialization.
      /// </summary>
      /// <returns>
      /// this returns true if the type is a reference type
      /// </returns>
      public bool IsReference() {
         return value.IsReference();
      }
   }
}
