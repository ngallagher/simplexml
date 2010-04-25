#region License
//
// ConverterFactory.cs January 2010
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
   /// The <c>ConverterFactory</c> is used to instantiate objects
   /// based on a provided type or annotation. This provides a single
   /// point of creation for all converters within the framework. For
   /// performance all the instantiated converters are cached against
   /// the class for that converter. This ensures the converters can
   /// be acquired without the overhead of instantiation.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.ConverterCache
   /// </seealso>
   class ConverterFactory {
      /// <summary>
      /// This is the cache that is used to cache converter instances.
      /// </summary>
      private readonly ConverterCache cache;
      /// <summary>
      /// Constructor for the <c>ConverterFactory</c> object.
      /// This will create an internal cache which is used to cache all
      /// instantiations made by the factory. Caching the converters
      /// ensures there is no overhead with instantiations.
      /// </summary>
      public ConverterFactory() {
         this.cache = new ConverterCache();
      }
      /// <summary>
      /// This is used to instantiate the converter based on the type
      /// provided. If the type provided can not be instantiated for
      /// some reason then an exception is thrown from this method.
      /// </summary>
      /// <param name="type">
      /// this is the converter type to be instantiated
      /// </param>
      /// <returns>
      /// this returns an instance of the provided type
      /// </returns>
      public Converter GetInstance(Class type) {
         Converter converter = cache.fetch(type);
         if(converter == null) {
            return GetConverter(type);
         }
         return converter;
      }
      /// <summary>
      /// This is used to instantiate the converter based on the type
      /// of the <c>Convert</c> annotation provided. If the type
      /// can not be instantiated for some reason then an exception is
      /// thrown from this method.
      /// </summary>
      /// <param name="convert">
      /// this is the annotation containing the type
      /// </param>
      /// <returns>
      /// this returns an instance of the provided type
      /// </returns>
      public Converter GetInstance(Convert convert) {
         Class type = convert.value();
         if(type.isInterface()) {
            throw new ConvertException("Can not instantiate %s", type);
         }
         return GetInstance(type);
      }
      /// <summary>
      /// This is used to instantiate the converter based on the type
      /// provided. If the type provided can not be instantiated for
      /// some reason then an exception is thrown from this method.
      /// </summary>
      /// <param name="type">
      /// this is the converter type to be instantiated
      /// </param>
      /// <returns>
      /// this returns an instance of the provided type
      /// </returns>
      public Converter GetConverter(Class type) {
         Constructor factory = GetConstructor(type);
         if(factory == null){
            throw new ConvertException("No default constructor for %s", type);
         }
         return GetConverter(type, factory);
      }
      /// <summary>
      /// This is used to instantiate the converter based on the type
      /// provided. If the type provided can not be instantiated for
      /// some reason then an exception is thrown from this method.
      /// </summary>
      /// <param name="type">
      /// this is the converter type to be instantiated
      /// </param>
      /// <param name="factory">
      /// this is the constructor used to instantiate
      /// </param>
      /// <returns>
      /// this returns an instance of the provided type
      /// </returns>
      public Converter GetConverter(Class type, Constructor factory) {
         Converter converter = (Converter)factory.newInstance();
         if(converter != null){
            cache.cache(type, converter);
         }
         return converter;
      }
      /// <summary>
      /// This is used to acquire the default no argument constructor
      /// for the the provided type. If the constructor is not accessible
      /// then it will be made accessible so that it can be instantiated.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the constructor for
      /// </param>
      /// <returns>
      /// this returns the constructor for the type provided
      /// </returns>
      public Constructor GetConstructor(Class type) {
         Constructor factory = type.getDeclaredConstructor();
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         return factory;
      }
   }
}
