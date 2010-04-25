#region License
//
// ClassTransform.cs May 2007
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
using System;
#endregion
package SimpleFramework.Xml.transform;
/// <summary>
/// The <c>ClassTransform</c> object is used to transform class
/// values to and from string representations, which will be inserted
/// in the generated XML document as the value place holder. The
/// value must be readable and writable in the same format. Fields
/// and methods annotated with the XML attribute annotation will use
/// this to persist and retrieve the value to and from the XML source.
/// </code>
///    &#64;Attribute
///    private Class target;
/// </code>
/// As well as the XML attribute values using transforms, fields and
/// methods annotated with the XML element annotation will use this.
/// Aside from the obvious difference, the element annotation has an
/// advantage over the attribute annotation in that it can maintain
/// any references using the <c>CycleStrategy</c> object.
/// </summary>
/// @author Ben Wolfe
/// @author Niall Gallagher
class ClassTransform : Transform<Class> {
   /// <summary>
   /// This method is used to convert the string value given to an
   /// appropriate representation. This is used when an object is
   /// being deserialized from the XML document and the value for
   /// the string representation is required.
   /// </summary>
   /// <param name="target">
   /// this is the string representation of the class
   /// </param>
   /// <returns>
   /// this returns an appropriate instanced to be used
   /// </returns>
   public Class Read(String target) {
      ClassLoader loader = ClassLoader;
      if(loader == null) {
         loader = CallerClassLoader;
      }
      return loader.loadClass(target);
   }
   /// <summary>
   /// This method is used to convert the provided value into an XML
   /// usable format. This is used in the serialization process when
   /// there is a need to convert a field value in to a string so
   /// that that value can be written as a valid XML entity.
   /// </summary>
   /// <param name="target">
   /// this is the value to be converted to a string
   /// </param>
   /// <returns>
   /// this is the string representation of the given value
   /// </returns>
   public String Write(Class target) {
      return target.getName();
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
         return getClass().ClassLoader;
      }
   }
   //public ClassLoader GetCallerClassLoader() {
   //   return getClass().ClassLoader;
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
         return Thread.currentThRead().getContextClassLoader();
      }
   }
   //public ClassLoader GetClassLoader() {
   //   return Thread.currentThRead().getContextClassLoader();
   //}
}
