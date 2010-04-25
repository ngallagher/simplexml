#region License
//
// MethodPartFactory.cs April 2007
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
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>MethodPartFactory</c> is used to create method parts
   /// based on the method signature and the XML annotation. This is
   /// effectively where a method is classified as either a getter or a
   /// setter method within an object. In order to determine the type of
   /// method the method name is checked to see if it is prefixed with
   /// either the "get", "is", or "set" tokens.
   /// <p>
   /// Once the method is determined to be a Java Bean method according
   /// to conventions the method signature is validated. If the method
   /// signature does not follow a return type with no arguments for the
   /// get method, and a single argument for the set method then this
   /// will throw an exception.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.MethodScanner
   /// </seealso>
   class MethodPartFactory {
      /// <summary>
      /// This is used to create the synthetic annotations for methods.
      /// </summary>
      private readonly AnnotationFactory factory;
      /// <summary>
      /// Constructor for the <c>MethodPartFactory</c> object. This
      /// is used to create method parts based on the method signature
      /// and the XML annotation is uses. The created part can be used to
      /// either set or get values depending on its type.
      /// </summary>
      public MethodPartFactory() {
         this.factory = new AnnotationFactory();
      }
      /// <summary>
      /// This is used to acquire a <c>MethodPart</c> for the method
      /// provided. This will synthesize an XML annotation to be used for
      /// the method. If the method provided is not a setter or a getter
      /// then this will return null, otherwise it will return a part with
      /// a synthetic XML annotation. In order to be considered a valid
      /// method the Java Bean conventions must be followed by the method.
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the part for
      /// </param>
      /// <returns>
      /// this is the method part object for the method
      /// </returns>
      public MethodPart GetInstance(Method method) {
         Annotation label = GetAnnotation(method);
         if(label != null) {
            return GetInstance(method, label);
         }
         return null;
      }
      /// <summary>
      /// This is used to acquire a <c>MethodPart</c> for the name
      /// and annotation of the provided method. This will determine the
      /// method type by examining its signature. If the method follows
      /// Java Bean conventions then either a setter method part or a
      /// getter method part is returned. If the method does not comply
      /// with the conventions an exception is thrown.
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the part for
      /// </param>
      /// <param name="label">
      /// this is the annotation associated with the method
      /// </param>
      /// <returns>
      /// this is the method part object for the method
      /// </returns>
      public MethodPart GetInstance(Method method, Annotation label) {
         MethodName name = GetName(method, label);
         MethodType type = name.GetType();
         if(type == MethodType.SET) {
            return new SetPart(name, label);
         }
         return new GetPart(name, label);
      }
      /// <summary>
      /// This is used to acquire a <c>MethodName</c> for the name
      /// and annotation of the provided method. This will determine the
      /// method type by examining its signature. If the method follows
      /// Java Bean conventions then either a setter method name or a
      /// getter method name is returned. If the method does not comply
      /// with the conventions an exception is thrown.
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the name for
      /// </param>
      /// <param name="label">
      /// this is the annotation associated with the method
      /// </param>
      /// <returns>
      /// this is the method name object for the method
      /// </returns>
      public MethodName GetName(Method method, Annotation label) {
         MethodType type = GetMethodType(method);
         if(type == MethodType.GET) {
            return GetRead(method, type);
         }
         if(type == MethodType.IS) {
            return GetRead(method, type);
         }
         if(type == MethodType.SET) {
            return GetWrite(method, type);
         }
         throw new MethodException("Annotation %s must mark a set or get method", label);
      }
      /// <summary>
      /// This is used to acquire a <c>MethodType</c> for the name
      /// of the method provided. This will determine the method type by
      /// examining its prefix. If the name follows Java Bean conventions
      /// then either a setter method type is returned. If the name does
      /// not comply with the naming conventions then null is returned.
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the type for
      /// </param>
      /// <returns>
      /// this is the method name object for the method
      /// </returns>
      public MethodType GetMethodType(Method method) {
         String name = method.GetName();
         if(name.startsWith("get")) {
            return MethodType.GET;
         }
         if(name.startsWith("is")) {
            return MethodType.IS;
         }
         if(name.startsWith("set")) {
            return MethodType.SET;
         }
         return MethodType.NONE;
      }
      /// <summary>
      /// This is used to synthesize an XML annotation given a method. The
      /// provided method must follow the Java Bean conventions and either
      /// be a getter or a setter. If this criteria is satisfied then a
      /// suitable XML annotation is created to be used. Typically a match
      /// is performed on whether the method type is a Java collection or
      /// an array, if neither criteria are true a normal XML element is
      /// used. Synthesizing in this way ensures the best results.
      /// </summary>
      /// <param name="method">
      /// this is the method to extract the annotation for
      /// </param>
      /// <returns>
      /// an XML annotation or null if the method is not suitable
      /// </returns>
      public Annotation GetAnnotation(Method method) {
         Class type = GetType(method);
         if(type != null) {
            return factory.GetInstance(type);
         }
         return null;
      }
      /// <summary>
      /// This is used to extract the type from a method. Type type of a
      /// method is the return type for a getter and a parameter type for
      /// a setter. Such a parameter will only be returned if the method
      /// observes the Java Bean conventions for a property method.
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the type for
      /// </param>
      /// <returns>
      /// this returns the type associated with the method
      /// </returns>
      public Class GetType(Method method) {
         MethodType type = GetMethodType(method);
         if(type == MethodType.SET) {
            return GetParameterType(method);
         }
         if(type == MethodType.GET) {
            return GetReturnType(method);
         }
         if(type == MethodType.IS) {
            return GetReturnType(method);
         }
         return null;
      }
      /// <summary>
      /// This is the parameter type associated with the provided method.
      /// The first parameter is returned if the provided method is a
      /// setter. If the method takes more than one parameter or if it
      /// takes no parameters then null is returned from this.
      /// </summary>
      /// <param name="method">
      /// this is the method to get the parameter type for
      /// </param>
      /// <returns>
      /// this returns the parameter type associated with it
      /// </returns>
      public Class GetParameterType(Method method) {
         Class[] list = method.getParameterTypes();
         if(list.length == 1) {
            return method.getParameterTypes()[0];
         }
         return null;
      }
      /// <summary>
      /// This is the return type associated with the provided method.
      /// The return type of the method is provided only if the method
      /// adheres to the Java Bean conventions regarding getter methods.
      /// If the method takes a parameter then this will return null.
      /// </summary>
      /// <param name="method">
      /// this is the method to get the return type for
      /// </param>
      /// <returns>
      /// this returns the return type associated with it
      /// </returns>
      public Class GetReturnType(Method method) {
         Class[] list = method.getParameterTypes();
         if(list.length == 0) {
            return method.GetReturnType();
         }
         return null;
      }
      /// <summary>
      /// This is used to acquire a <c>MethodName</c> for the name
      /// and annotation of the provided method. This must be a getter
      /// method, and so must have a return type that is not void and
      /// have not arguments. If the method has arguments an exception
      /// is thrown, if not the Java Bean method name is provided.
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the name for
      /// </param>
      /// <param name="type">
      /// this is the method type to acquire the name for
      /// </param>
      /// <returns>
      /// this is the method name object for the method
      /// </returns>
      public MethodName GetRead(Method method, MethodType type) {
         Class[] list = method.getParameterTypes();
         String real = method.GetName();
         if(list.length != 0) {
            throw new MethodException("Get method %s is not a valid property", method);
         }
         String name = GetTypeName(real, type);
         if(name == null) {
            throw new MethodException("Could not get name for %s", method);
         }
         return new MethodName(method, type, name);
      }
      /// <summary>
      /// This is used to acquire a <c>MethodName</c> for the name
      /// and annotation of the provided method. This must be a setter
      /// method, and so must accept a single argument, if it contains
      /// more or less than one argument an exception is thrown.
      /// return type that is not void and
      /// </summary>
      /// <param name="method">
      /// this is the method to acquire the name for
      /// </param>
      /// <param name="type">
      /// this is the method type to acquire the name for
      /// </param>
      /// <returns>
      /// this is the method name object for the method
      /// </returns>
      public MethodName GetWrite(Method method, MethodType type) {
         Class[] list = method.getParameterTypes();
         String real = method.GetName();
         if(list.length != 1) {
            throw new MethodException("Set method %s os not a valid property", method);
         }
         String name = GetTypeName(real, type);
         if(name == null) {
            throw new MethodException("Could not get name for %s", method);
         }
         return new MethodName(method, type, name);
      }
      /// <summary>
      /// This is used to acquire the name of the method in a Java Bean
      /// property style. Thus any "get", "is", or "set" prefix is
      /// removed from the name and the following character is changed
      /// to lower case if it does not represent an acronym.
      /// </summary>
      /// <param name="name">
      /// this is the name of the method to be converted
      /// </param>
      /// <param name="type">
      /// this is the type of method the name represents
      /// </param>
      /// <returns>
      /// this returns the Java Bean name for the method
      /// </returns>
      public String GetTypeName(String name, MethodType type) {
         int prefix = type.getPrefix();
         int size = name.length();
         if(size > prefix) {
            name = name.substring(prefix, size);
         }
         return Reflector.GetName(name);
      }
   }
}
