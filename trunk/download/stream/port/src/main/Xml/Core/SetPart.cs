#region License
//
// SetPart.cs April 2007
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
   /// The <c>SetPart</c> object represents the setter method for
   /// a Java Bean property. This composes the set part of the method
   /// contact for an object. The set part contains the method that is
   /// used to set the value on an object and the annotation that tells
   /// the deserialization process how to deserialize the value.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.MethodContact
   /// </seealso>
   class SetPart : MethodPart {
      /// <summary>
      /// This is the annotation for the set method provided.
      /// </summary>
      private readonly Annotation label;
      /// <summary>
      /// This represents the method type for the set part method.
      /// </summary>
      private readonly MethodType type;
      /// <summary>
      /// This method is used to set the value during deserialization.
      /// </summary>
      private readonly Method method;
      /// <summary>
      /// This represents the name of this set part instance.
      /// </summary>
      private readonly String name;
      /// <summary>
      /// Constructor for the <c>SetPart</c> object. This is
      /// used to create a method part that will provide a means for
      /// the deserialization process to set a value to a object.
      /// </summary>
      /// <param name="method">
      /// the method that is used to set the value
      /// </param>
      /// <param name="label">
      /// this describes how to deserialize the value
      /// </param>
      public SetPart(MethodName method, Annotation label) {
         this.method = method.Method;
         this.name = method.Name;
         this.type = method.Type;
         this.label = label;
      }
      /// <summary>
      /// This provides the name of the method part as acquired from the
      /// method name. The name represents the Java Bean property name
      /// of the method and is used to pair getter and setter methods.
      /// </summary>
      /// <returns>
      /// this returns the Java Bean name of the method part
      /// </returns>
      public String Name {
         get {
            return name;
         }
      }
      //public String GetName() {
      //   return name;
      //}
      /// This is used to acquire the type for this method part. This
      /// is used by the serializer to determine the schema class that
      /// is used to match the XML elements to the object details.
      /// </summary>
      /// <returns>
      /// this returns the schema class for this method
      /// </returns>
      public Class Type {
         get {
            return method.getParameterTypes()[0];
         }
      }
      //public Class GetType() {
      //   return method.getParameterTypes()[0];
      //}
      /// This is used to acquire the dependent class for the method
      /// part. The dependent type is the type that represents the
      /// generic type of the type. This is used when collections are
      /// annotated as it allows a default entry class to be taken
      /// from the generic information provided.
      /// </summary>
      /// <returns>
      /// this returns the generic dependent for the type
      /// </returns>
      public Class Dependent {
         get {
            return Reflector.getParameterDependent(method, 0);
         }
      }
      //public Class GetDependent() {
      //   return Reflector.getParameterDependent(method, 0);
      //}
      /// This is used to acquire the dependent classes for the method
      /// part. The dependent types are the types that represents the
      /// generic types of the type. This is used when collections are
      /// annotated as it allows a default entry classes to be taken
      /// from the generic information provided.
      /// </summary>
      /// <returns>
      /// this returns the generic dependents for the type
      /// </returns>
      public Class[] Dependents {
         get {
            return Reflector.getParameterDependents(method, 0);
         }
      }
      //public Class[] GetDependents() {
      //   return Reflector.getParameterDependents(method, 0);
      //}
      /// This is used to acquire the annotation that was used to label
      /// the method this represents. This acts as a means to match the
      /// set method with the get method using an annotation comparison.
      /// </summary>
      /// <returns>
      /// this returns the annotation used to mark the method
      /// </returns>
      public Annotation Annotation {
         get {
            return label;
         }
      }
      //public Annotation GetAnnotation() {
      //   return label;
      //}
      /// This is the annotation associated with the point of contact.
      /// This will be an XML annotation that describes how the contact
      /// should be serialized and deserialized from the object.
      /// </summary>
      /// <param name="type">
      /// this is the type of the annotation to acquire
      /// </param>
      /// <returns>
      /// this provides the annotation associated with this
      /// </returns>
      public <T : Annotation> T getAnnotation(Class<T> type) {
         return method.getAnnotation(type);
      }
      /// <summary>
      /// This is the method type for the method part. This is used in
      /// the scanning process to determine which type of method a
      /// instance represents, this allows set and get methods to be
      /// paired.
      /// </summary>
      /// <returns>
      /// the method type that this part represents
      /// </returns>
      public MethodType MethodType {
         get {
            return type;
         }
      }
      //public MethodType GetMethodType() {
      //   return type;
      //}
      /// This is used to acquire the method that can be used to invoke
      /// the Java Bean method on the object. If the method represented
      /// by this is inaccessible then this will set it as accessible.
      /// </summary>
      /// <returns>
      /// returns the method used to interface with the object
      /// </returns>
      public Method Method {
         get {
            if(!method.isAccessible()) {
               method.setAccessible(true);
            }
            return method;
         }
      }
      //public Method GetMethod() {
      //   if(!method.isAccessible()) {
      //      method.setAccessible(true);
      //   }
      //   return method;
      //}
      /// This is used to describe the method as it exists within the
      /// owning class. This is used to provide error messages that can
      /// be used to debug issues that occur when processing a method.
      /// This returns the method as a generic string representation.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the method
      /// </returns>
      public String ToString() {
         return method.toGenericString();
      }
   }
}
