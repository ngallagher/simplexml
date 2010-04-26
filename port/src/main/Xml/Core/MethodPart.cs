#region License
//
// MethodPart.cs April 2007
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
   /// The <c>MethodPart</c> interface is used to provide a point
   /// of contact with an object. Typically this will be used to get a
   /// method from an object which is contains an XML annotation. This
   /// provides the type the method is associated with, this type is
   /// either the method return type or the single value parameter.
   /// </summary>
   interface MethodPart {
      /// <summary>
      /// This provides the name of the method part as acquired from the
      /// method name. The name represents the Java Bean property name
      /// of the method and is used to pair getter and setter methods.
      /// </summary>
      /// <returns>
      /// this returns the Java Bean name of the method part
      /// </returns>
      public String Name {
         get;
      }
      //public String GetName();
      /// This is the annotation associated with the point of contact.
      /// This will be an XML annotation that describes how the contact
      /// should be serializaed and deserialized from the object.
      /// </summary>
      /// <returns>
      /// this provides the annotation associated with this
      /// </returns>
      public Annotation Annotation {
         get;
      }
      //public Annotation GetAnnotation();
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
      <T : Annotation> T getAnnotation(Class<T> type);
      /// <summary>
      /// This will provide the contact type. The contact type is the
      /// class that is either the method return type or the single
      /// value parameter type associated with the method.
      /// </summary>
      /// <returns>
      /// this returns the type that this contact represents
      /// </returns>
      public Class Type {
         get;
      }
      //public Class GetType();
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
         get;
      }
      //public Class GetDependent();
      /// This is used to acquire the dependent classes for the method
      /// part. The dependent types are the types that represent the
      /// generic types of the type. This is used when collections are
      /// annotated as it allows a default entry class to be taken
      /// from the generic information provided.
      /// </summary>
      /// <returns>
      /// this returns the generic dependent for the type
      /// </returns>
      public Class[] Dependents {
         get;
      }
      //public Class[] GetDependents();
      /// This is the method for this point of contact. This is what
      /// will be invoked by the serialization or deserialization
      /// process when an XML element or attribute is to be used.
      /// </summary>
      /// <returns>
      /// this returns the method associated with this
      /// </returns>
      public Method Method {
         get;
      }
      //public Method GetMethod();
      /// This is the method type for the method part. This is used in
      /// the scanning process to determine which type of method a
      /// instance represents, this allows set and get methods to be
      /// paired.
      /// </summary>
      /// <returns>
      /// the method type that this part represents
      /// </returns>
      public MethodType MethodType {
         get;
      }
      //public MethodType GetMethodType();
      /// This is used to describe the method as it exists within the
      /// owning class. This is used to provide error messages that can
      /// be used to debug issues that occur when processing a method.
      /// This should return the method as a generic representation.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the method
      /// </returns>
      String ToString();
   }
}
