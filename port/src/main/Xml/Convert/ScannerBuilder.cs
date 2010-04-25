#region License
//
// ScannerBuilder.cs January 2010
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
using SimpleFramework.Xml.Util;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>ScannerBuilder</c> is used to build and cache each
   /// scanner requested. Building and caching scanners ensures that
   /// annotations can be acquired from a class quickly as a scan only
   /// needs to be performed once. Each scanner built scans the class
   /// provided as well as all the classes in the hierarchy.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.ConverterScanner
   /// </seealso>
   class ScannerBuilder : WeakCache<Class, Scanner> {
      /// <summary>
      /// Constructor for the <c>ScannerBuilder</c> object. This
      /// will create a builder for annotation scanners. Each of the
      /// scanners build will be cached internally to ensure that any
      /// further requests for the scanner are quicker.
      /// </summary>
      public ScannerBuilder() {
         super();
      }
      /// <summary>
      /// This is used to build <c>Scanner</c> objects that are
      /// used to scan the provided class for annotations. Each scanner
      /// instance is cached once created to ensure it does not need to
      /// be built twice, which improves the performance.
      /// </summary>
      /// <param name="type">
      /// this is the type to build a scanner object for
      /// </param>
      /// <returns>
      /// this will return a scanner instance for the given type
      /// </returns>
      public Scanner Build(Class<?> type) {
         Scanner scanner = fetch(type);
         if(scanner == null) {
            scanner = new Entry(type);
            cache(type, scanner);
         }
         return scanner;
      }
      /// <summary>
      /// The <c>Entry</c> object represents a scanner that is
      /// used to scan a specified type for annotations. All annotations
      /// scanned from the type are cached so that they do not need to
      /// be looked up twice. This ensures scanning is much quicker.
      /// </summary>
      private static class Entry : WeakCache<Class, Annotation> : Scanner {
         /// <summary>
         /// This class is the subject for all annotation scans performed.
         /// </summary>
         private readonly Class root;
         /// <summary>
         /// Constructor for the <c>Entry</c> object is used to
         /// create a scanner that will scan the specified type. All
         /// annotations that are scanned are cached to ensure that they
         /// do not need to be looked up twice. This ensures that scans
         /// are quicker including ones that result in null.
         /// </summary>
         /// <param name="root">
         /// this is the root class that is to be scanned
         /// </param>
         public Entry(Class root) {
            this.root = root;
         }
         /// <summary>
         /// This method will scan a class for the specified annotation.
         /// If the annotation is found on the class, or on one of the
         /// super types then it is returned. All scans will be cached
         /// to ensure scanning is only performed once.
         /// </summary>
         /// <param name="type">
         /// this is the annotation type to be scanned for
         /// </param>
         /// <returns>
         /// this will return the annotation if it is found
         /// </returns>
         public <T : Annotation> T scan(Class<T> type) {
            if(!contains(type)) {
               T value = find(type);
               if(type != null) {
                  cache(type, value);
               }
            }
            return (T)fetch(type);
         }
         /// <summary>
         /// This method will scan a class for the specified annotation.
         /// If the annotation is found on the class, or on one of the
         /// super types then it is returned. All scans will be cached
         /// to ensure scanning is only performed once.
         /// </summary>
         /// <param name="label">
         /// this is the annotation type to be scanned for
         /// </param>
         /// <returns>
         /// this will return the annotation if it is found
         /// </returns>
         private <T : Annotation> T find(Class<T> label) {
            Class<?> type = root;
            while(type != null) {
               T value = type.getAnnotation(label);
               if(value != null) {
                  return value;
               }
               type = type.getSuperclass();
            }
            return null;
         }
      }
   }
}
