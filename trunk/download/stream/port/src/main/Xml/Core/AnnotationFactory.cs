#region License
//
// AnnotationFactory.cs January 2010
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
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>AnnotationFactory</c> is used to create annotations
   /// using a given class. This will classify the provided type as
   /// either a list, map, array, or a default object. Depending on the
   /// type provided a suitable annotation will be created. Annotations
   /// produced by this will have default attribute values.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.AnnotationHandler
   /// </seealso>
   class AnnotationFactory {
      /// <summary>
      /// This is used to create an annotation for the provided type.
      /// Annotations created are used to match the type provided. So
      /// a <c>List</c> will have an <c>ElementList</c>
      /// annotation for example. Matching the annotation to the
      /// type ensures the best serialization for that type.
      /// </summary>
      /// <param name="type">
      /// the type to create the annotation for
      /// </param>
      /// <returns>
      /// this returns the synthetic annotation to be used
      /// </returns>
      public Annotation GetInstance(Class type) {
         ClassLoader loader = ClassLoader;
         if(Map.class.isAssignableFrom(type)) {
            return GetInstance(loader, ElementMap.class);
         }
         if(Collection.class.isAssignableFrom(type)) {
            return GetInstance(loader, ElementList.class);
         }
         if(type.isArray()) {
            return GetInstance(loader, ElementArray.class);
         }
         return GetInstance(loader, Element.class);
      }
      /// <summary>
      /// This will create a synthetic annotation using the provided
      /// interface. All attributes for the provided annotation will
      /// have their default values.
      /// </summary>
      /// <param name="loader">
      /// this is the class loader to load the annotation
      /// </param>
      /// <param name="label">
      /// this is the annotation interface to be used
      /// </param>
      /// <returns>
      /// this returns the synthetic annotation to be used
      /// </returns>
      public Annotation GetInstance(ClassLoader loader, Class label) {
         AnnotationHandler handler = new AnnotationHandler(label);
         Class[] list = new Class[] {label};
         return (Annotation) Proxy.newProxyInstance(loader, list, handler);
      }
      /// <summary>
      /// This is used to create a suitable class loader to be used to
      /// load the synthetic annotation classes. The class loader
      /// provided will be the same as the class loader that was used
      /// to load this class.
      /// </summary>
      /// <returns>
      /// this returns the class loader that is to be used
      /// </returns>
      public ClassLoader ClassLoader {
         get {
            return AnnotationFactory.class.ClassLoader;
         }
      }
      //public ClassLoader GetClassLoader() {
      //   return AnnotationFactory.class.ClassLoader;
      //}
}
