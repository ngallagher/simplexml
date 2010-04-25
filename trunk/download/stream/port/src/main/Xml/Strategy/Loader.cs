#region License
//
// Loader.cs January 2010
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
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>Loader</c> object is used to provide class loading
   /// for the strategies. This will attempt to load the class using
   /// the thread context class loader, if this loader is set it will
   /// be used to load the class. If not then the class will be loaded
   /// using the caller class loader. Loading in this way ensures
   /// that a custom loader can be provided using the current thread.
   /// </summary>
   class Loader {
      /// <summary>
      /// This method is used to acquire the class of the specified name.
      /// Loading is performed by the thread context class loader as this
      /// will ensure that the class loading strategy can be changed as
      /// requirements dictate. Typically the thread context class loader
      /// can handle all serialization requirements.
      /// </summary>
      /// <param name="type">
      /// this is the name of the class that is to be loaded
      /// </param>
      /// <returns>
      /// this returns the class that has been loaded by this
      /// </returns>
      public Class Load(String type) {
         ClassLoader loader = ClassLoader;
         if(loader == null) {
            loader = CallerClassLoader;
         }
         return loader.loadClass(type);
      }
      /// <summary>
      /// This is used to acquire the caller class loader for this object.
      /// Typically this is only used if the thread context class loader
      /// is set to null. This ensures that there is at least some class
      /// loader available to the strategy to load the class.
      /// </summary>
      /// <returns>
      /// this returns the loader that loaded this class
      /// </returns>
      public ClassLoader CallerClassLoader {
         get {
            return Loader.class.ClassLoader;
         }
      }
      //public ClassLoader GetCallerClassLoader() {
      //   return Loader.class.ClassLoader;
      //}
      /// This is used to acquire the thread context class loader. This
      /// is the default class loader used by the cycle strategy. When
      /// using the thread context class loader the caller can switch the
      /// class loader in use, which allows class loading customization.
      /// </summary>
      /// <returns>
      /// this returns the loader used by the calling thread
      /// </returns>
      public ClassLoader ClassLoader {
         get {
            return Thread.currentThread().getContextClassLoader();
         }
      }
      //public ClassLoader GetClassLoader() {
      //   return Thread.currentThread().getContextClassLoader();
      //}
}
