#region License
//
// ConverterScanner.cs January 2010
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
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>ConverterScanner</c> is used to create a converter
   /// given a method or field representation. Creation of the converter
   /// is done using the <c>Convert</c> annotation, which may
   /// be used to annotate a field, method or class. This describes the
   /// implementation to use for object serialization. To account for
   /// polymorphism the type scanned for annotations can be overridden
   /// from type provided in the <c>Type</c> object. This ensures
   /// that if a collection of objects are serialized the correct
   /// implementation will be used for each type or subtype.
   /// </summary>
   class ConverterScanner {
      /// <summary>
      /// This is used to instantiate converters given the type.
      /// </summary>
      private readonly ConverterFactory factory;
      /// <summary>
      /// This is used to build a scanner to scan for annotations.
      /// </summary>
      private readonly ScannerBuilder builder;
      /// <summary>
      /// Constructor for the <c>ConverterScanner</c> object. This
      /// uses an internal factory to instantiate and cache all of the
      /// converters created. This will ensure that there is reduced
      /// overhead for a serialization process using converters.
      /// </summary>
      public ConverterScanner() {
         this.factory = new ConverterFactory();
         this.builder = new ScannerBuilder();
      }
      /// <summary>
      /// This method will lookup and instantiate a converter found from
      /// scanning the field or method type provided. If the type has
      /// been overridden then the <c>Value</c> object will provide
      /// the type to scan. If no annotation is found on the class, field
      /// or method then this will return null.
      /// </summary>
      /// <param name="type">
      /// this is the type to search for the annotation
      /// </param>
      /// <param name="value">
      /// this contains the type if it was overridden
      /// </param>
      /// <returns>
      /// a converter scanned from the provided field or method
      /// </returns>
      public Converter GetConverter(Type type, Value value) {
         Class real = GetType(type, value);
         Convert convert = GetConvert(type, real);
         if(convert != null) {
            return factory.GetInstance(convert);
         }
         return null;
      }
      /// <summary>
      /// This method will lookup and instantiate a converter found from
      /// scanning the field or method type provided. If the type has
      /// been overridden then the object instance will provide the type
      /// to scan. If no annotation is found on the class, field or
      /// method then this will return null.
      /// </summary>
      /// <param name="type">
      /// this is the type to search for the annotation
      /// </param>
      /// <param name="value">
      /// this contains the type if it was overridden
      /// </param>
      /// <returns>
      /// a converter scanned from the provided field or method
      /// </returns>
      public Converter GetConverter(Type type, Object value) {
         Class real = GetType(type, value);
         Convert convert = GetConvert(type, real);
         if(convert != null) {
            return factory.GetInstance(convert);
         }
         return null;
      }
      /// <summary>
      /// This method is used to scan the provided <c>Type</c> for
      /// an annotation. If the <c>Type</c> represents a field or
      /// method then the annotation can be taken directly from that
      /// field or method. If however the type represents a class then
      /// the class itself must contain the annotation.
      /// </summary>
      /// <param name="type">
      /// the field or method containing the annotation
      /// </param>
      /// <param name="real">
      /// the type that represents the field or method
      /// </param>
      /// <returns>
      /// this returns the annotation on the field or method
      /// </returns>
      public Convert GetConvert(Type type, Class real) {
         Convert convert = GetConvert(type);
         if(convert == null) {
            return GetConvert(real);
         }
         return convert;
      }
      /// <summary>
      /// This method is used to scan the provided <c>Type</c> for
      /// an annotation. If the <c>Type</c> represents a field or
      /// method then the annotation can be taken directly from that
      /// field or method. If however the type represents a class then
      /// the class itself must contain the annotation.
      /// </summary>
      /// <param name="type">
      /// the field or method containing the annotation
      /// </param>
      /// <returns>
      /// this returns the annotation on the field or method
      /// </returns>
      public Convert GetConvert(Type type) {
         Convert convert = type.getAnnotation(Convert.class);
         if(convert != null) {
            Element element = type.getAnnotation(Element.class);
            if(element == null) {
               throw new ConvertException("Element annotation required for %s", type);
            }
         }
         return convert;
      }
      /// <summary>
      /// This method is used to scan the provided <c>Type</c> for
      /// an annotation. If the <c>Type</c> represents a field or
      /// method then the annotation can be taken directly from that
      /// field or method. If however the type represents a class then
      /// the class itself must contain the annotation.
      /// </summary>
      /// <param name="real">
      /// the type that represents the field or method
      /// </param>
      /// <returns>
      /// this returns the annotation on the field or method
      /// </returns>
      public Convert GetConvert(Class real) {
         Convert convert = getAnnotation(real, Convert.class);
         if(convert != null) {
            Root root = getAnnotation(real, Root.class);
            if(root == null) {
               throw new ConvertException("Root annotation required for %s", real);
            }
         }
         return convert;
      }
      /// <summary>
      /// This is used to acquire the <c>Convert</c> annotation from
      /// the class provided. If the type does not contain the annotation
      /// then this scans all supertypes until either an annotation is
      /// found or there are no further supertypes.
      /// </summary>
      /// <param name="type">
      /// this is the type to scan for annotations
      /// </param>
      /// <param name="label">
      /// this is the annotation type that is to be found
      /// </param>
      /// <returns>
      /// this returns the annotation if found otherwise null
      /// </returns>
      private <T : Annotation> T getAnnotation(Class<?> type, Class<T> label) {
         return builder.Build(type).scan(label);
      }
      /// <summary>
      /// This is used to acquire the class that should be scanned. The
      /// type is found either on the method or field, or should there
      /// be a subtype then the class is taken from the provided value.
      /// </summary>
      /// <param name="type">
      /// this is the type representing the field or method
      /// </param>
      /// <param name="value">
      /// this contains the type if it was overridden
      /// </param>
      /// <returns>
      /// this returns the class that has been scanned
      /// </returns>
      public Class GetType(Type type, Value value) {
         Class real = type.GetType();
         if(value != null) {
            return value.GetType();
         }
         return real;
      }
      /// <summary>
      /// This is used to acquire the class that should be scanned. The
      /// type is found either on the method or field, or should there
      /// be a subtype then the class is taken from the provided value.
      /// </summary>
      /// <param name="type">
      /// this is the type representing the field or method
      /// </param>
      /// <param name="value">
      /// this contains the type if it was overridden
      /// </param>
      /// <returns>
      /// this returns the class that has been scanned
      /// </returns>
      public Class GetType(Type type, Object value) {
         Class real = type.GetType();
         if(value != null) {
            return value.getClass();
         }
         return real;
      }
   }
}
