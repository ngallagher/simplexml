#region License
//
// AnnotationHandler.cs December 2009
//
// Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>AnnotationHandler</c> object is used to handle all
   /// invocation made on a synthetic annotation. This is required so
   /// that annotations can be created without an implementation. The
   /// <c>java.lang.reflect.Proxy</c> object is used to wrap this
   /// invocation handler with the annotation interface.
   /// </summary>
   class AnnotationHandler : InvocationHandler {
      /// <summary>
      /// This is the method used to acquire the associated type.
      /// </summary>
      private const String CLASS = "annotationType";
      /// <summary>
      /// This is used to acquire a string value for the annotation.
      /// </summary>
      private const String STRING = "toString";
      /// <summary>
      /// This is used to ensure that all annotations are optional.
      /// </summary>
      private const String REQUIRED = "required";
      /// <summary>
      /// This is used to perform a comparison of the annotations.
      /// </summary>
      private const String EQUAL = "equals";
      /// <summary>
      /// This is used to perform a comparison of the annotations.
      /// </summary>
      private readonly Comparer comparer;
      /// <summary>
      /// This is annotation type associated with this handler.
      /// </summary>
      private readonly Class type;
      /// <summary>
      /// Constructor for the <c>AnnotationHandler</c> object. This
      /// is used to create a handler for invocations on a synthetic
      /// annotation. The annotation type wrapped must be provided.
      /// </summary>
      /// <param name="type">
      /// this is the annotation type that this is wrapping
      /// </param>
      public AnnotationHandler(Class type) {
         this.comparer = new Comparer();
         this.type = type;
      }
      /// <summary>
      /// This is used to handle all invocations on the wrapped annotation.
      /// Typically the response to an invocation will result in the
      /// default value of the annotation attribute being returned. If the
      /// method is an <c>equals</c> or <c>toString</c> then
      /// this will be handled by an internal implementation.
      /// </summary>
      /// <param name="proxy">
      /// this is the proxy object the invocation was made on
      /// </param>
      /// <param name="method">
      /// this is the method that was invoked on the proxy
      /// </param>
      /// <param name="list">
      /// this is the list of parameters to be used
      /// </param>
      /// <returns>
      /// this is used to return the result of the invocation
      /// </returns>
      public Object Invoke(Object proxy, Method method, Object[] list) {
         String name = method.getName();
         if(name.Equals(STRING)) {
            return ToString();
         }
         if(name.Equals(EQUAL)) {
            return Equals(proxy, list);
         }
         if(name.Equals(CLASS)) {
            return type;
         }
         if(name.Equals(REQUIRED)) {
            return false;
         }
         return method.getDefaultValue();
      }
      /// <summary>
      /// This is used to determine if two annotations are equals based
      /// on the attributes of the annotation. The comparison done can
      /// ignore specific attributes, for instance the name attribute.
      /// </summary>
      /// <param name="proxy">
      /// this is the annotation the invocation was made on
      /// </param>
      /// <param name="list">
      /// this is the parameters provided to the invocation
      /// </param>
      /// <returns>
      /// this returns true if the annotations are equals
      /// </returns>
      public bool Equals(Object proxy, Object[] list) {
         Annotation left = (Annotation) proxy;
         Annotation right = (Annotation) list[0];
         return comparer.Equals(left, right);
      }
      /// <summary>
      /// This is used to build a string from the annotation. The string
      /// produces adheres to the typical string representation of a
      /// normal annotation. This ensures that an exceptions that are
      /// thrown with a string representation of the annotation are
      /// identical to those thrown with a normal annotation.
      /// </summary>
      /// <returns>
      /// returns a string representation of the annotation
      /// </returns>
      public String ToString() {
         StringBuilder builder = new StringBuilder();
         if(type != null) {
            Name(builder);
            Attributes(builder);
         }
         return builder.ToString();
      }
      /// <summary>
      /// This is used to build a string from the annotation. The string
      /// produces adheres to the typical string representation of a
      /// normal annotation. This ensures that an exceptions that are
      /// thrown with a string representation of the annotation are
      /// identical to those thrown with a normal annotation.
      /// </summary>
      /// <param name="builder">
      /// this is the builder used to compose the text
      /// </param>
      public void Name(StringBuilder builder) {
         String name = type.getName();
         if(name != null) {
            builder.append('@');
            builder.append(name);
            builder.append('(');
         }
      }
      /// <summary>
      /// This is used to build a string from the annotation. The string
      /// produces adheres to the typical string representation of a
      /// normal annotation. This ensures that an exceptions that are
      /// thrown with a string representation of the annotation are
      /// identical to those thrown with a normal annotation.
      /// </summary>
      /// <param name="builder">
      /// this is the builder used to compose the text
      /// </param>
      public void Attributes(StringBuilder builder) {
         Method[] list = type.getDeclaredMethods();
         for(int i = 0; i < list.length; i++) {
            String attribute = list[i].getName();
            Object value = Value(list[i]);
            if(i > 0) {
               builder.append(',');
               builder.append(' ');
            }
            builder.append(attribute);
            builder.append('=');
            builder.append(value);
         }
         builder.append(')');
      }
      /// <summary>
      /// This is used to extract the default value used for the provided
      /// annotation attribute. This will return the default value for
      /// all attributes except that it makes the requirement optional.
      /// Making the requirement optional provides better functionality.
      /// </summary>
      /// <param name="method">
      /// this is the annotation representing the attribute
      /// </param>
      /// <returns>
      /// this returns the default value for the attribute
      /// </returns>
      public Object Value(Method method) {
         String name = method.getName();
         if(name.Equals(REQUIRED)) {
            return  false;
         }
         return method.getDefaultValue();
      }
   }
}
