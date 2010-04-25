#region License
//
// Registry.cs January 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>Registry</c> represents an object that is used to
   /// register bindings between a class and a converter implementation.
   /// Converter instances created by this registry are lazily created
   /// and cached so that they are instantiated only once. This ensures
   /// that the overhead of serialization is reduced.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.RegistryStrategy
   /// </seealso>
   public class Registry {
      /// <summary>
      /// This is used to bind converter types to serializable types.
      /// </summary>
      private readonly RegistryBinder binder;
      /// <summary>
      /// This is used to cache the converters based on object types.
      /// </summary>
      private readonly ConverterCache cache;
      /// <summary>
      /// Constructor for the <c>Registry</c> object. This is used
      /// to create a registry between classes and the converters that
      /// should be used to serialize and deserialize the instances. All
      /// converters are instantiated once and cached for reuse.
      /// </summary>
      public Registry() {
         this.binder = new RegistryBinder();
         this.cache = new ConverterCache();
      }
      /// <summary>
      /// This is used to acquire a <c>Converter</c> instance from
      /// the registry. All instances are cache to reduce the overhead
      /// of lookups during the serialization process. Converters are
      /// lazily instantiated and so are only created if demanded.
      /// </summary>
      /// <param name="type">
      /// this is the type to find the converter for
      /// </param>
      /// <returns>
      /// this returns the converter instance for the type
      /// </returns>
      public Converter Lookup(Class type) {
         Converter converter = cache.fetch(type);
         if(converter == null) {
            return Create(type);
         }
         return converter;
      }
      /// <summary>
      /// This is used to acquire a <c>Converter</c> instance from
      /// the registry. All instances are cached to reduce the overhead
      /// of lookups during the serialization process. Converters are
      /// lazily instantiated and so are only created if demanded.
      /// </summary>
      /// <param name="type">
      /// this is the type to find the converter for
      /// </param>
      /// <returns>
      /// this returns the converter instance for the type
      /// </returns>
      public Converter Create(Class type) {
         Converter converter = binder.Lookup(type);
         if(converter != null) {
            cache.cache(type, converter);
         }
         return converter;
      }
      /// <summary>
      /// This is used to register a binding between a type and the
      /// converter used to serialize and deserialize it. During the
      /// serialization process the converters are retrieved and
      /// used to convert the object members to XML.
      /// </summary>
      /// <param name="type">
      /// this is the object type to bind to a converter
      /// </param>
      /// <param name="converter">
      /// this is the converter class to be used
      /// </param>
      /// <returns>
      /// this will return this registry instance to use
      /// </returns>
      public Registry Bind(Class type, Class converter) {
         if(type != null) {
            binder.Bind(type, converter);
         }
         return this;
      }
      /// <summary>
      /// This is used to register a binding between a type and the
      /// converter used to serialize and deserialize it. During the
      /// serialization process the converters are retrieved and
      /// used to convert the object properties to XML.
      /// </summary>
      /// <param name="type">
      /// this is the object type to bind to a converter
      /// </param>
      /// <param name="converter">
      /// this is the converter instance to be used
      /// </param>
      /// <returns>
      /// this will return this registry instance to use
      /// </returns>
      public Registry Bind(Class type, Converter converter) {
         if(type != null) {
            cache.cache(type, converter);
         }
         return this;
      }
   }
}
