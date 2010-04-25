#region License
//
// ParameterContact.cs April 2007
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
   /// The <c>ParameterContact</c> object is used to represent
   /// a contact that is provided so that a <c>Label</c> can be
   /// used to determine a consistent name for the parameter. Unlike
   /// field and method contacts this is essentially an adapter that
   /// is used so that the parameter name can be determined in a
   /// similar way to a method or field.
   /// </summary>
   abstract class ParameterContact<T : Annotation> : Contact {
      /// <summary>
      /// This is the constructor the parameter was declared within.
      /// </summary>
      protected readonly Constructor factory;
      /// <summary>
      /// This is the index of the parameter within the constructor.
      /// </summary>
      protected readonly int index;
      /// <summary>
      /// This is the annotation used to label the parameter.
      /// </summary>
      protected readonly T label;
      /// <summary>
      /// Constructor for the <c>ParameterContact</c> object. This
      /// is used to create a contact that can be used to determine a
      /// consistent name for the parameter. It requires the annotation,
      /// the constructor, and the parameter declaration index.
      /// </summary>
      /// <param name="label">
      /// this is the annotation used for the parameter
      /// </param>
      /// <param name="factory">
      /// this is the constructor that is represented
      /// </param>
      /// <param name="index">
      /// this is the index for the parameter
      /// </param>
      public ParameterContact(T label, Constructor factory, int index) {
         this.factory = factory;
         this.index = index;
         this.label = label;
      }
      /// <summary>
      /// This is the annotation associated with the point of contact.
      /// This will be an XML annotation that describes how the contact
      /// should be serialized and deserialized from the object.
      /// </summary>
      /// <returns>
      /// this provides the annotation associated with this
      /// </returns>
      public Annotation Annotation {
         get {
            return label;
         }
      }
      //public Annotation GetAnnotation() {
      //   return label;
      //}
      /// This will provide the contact type. The contact type is the
      /// class that is to be set and get on the object. Typically the
      /// type will be a serializable object or a primitive type.
      /// </summary>
      /// <returns>
      /// this returns the type that this contact represents
      /// </returns>
      public Class Type {
         get {
            return factory.getParameterTypes()[index];
         }
      }
      //public Class GetType() {
      //   return factory.getParameterTypes()[index];
      //}
      /// This provides the dependent class for the contact. This will
      /// typically represent a generic type for the actual type. For
      /// contacts that use a <c>Collection</c> type this will
      /// be the generic type parameter for that collection.
      /// </summary>
      /// <returns>
      /// this returns the dependent type for the contact
      /// </returns>
      public Class Dependent {
         get {
            return Reflector.getParameterDependent(factory, index);
         }
      }
      //public Class GetDependent() {
      //   return Reflector.getParameterDependent(factory, index);
      //}
      /// This provides the dependent classes for the contact. This will
      /// typically represent a generic types for the actual type. For
      /// contacts that use a <c>Map</c> type this will be the
      /// generic type parameter for that map type declaration.
      /// </summary>
      /// <returns>
      /// this returns the dependent types for the contact
      /// </returns>
      public Class[] Dependents {
         get {
            return Reflector.getParameterDependents(factory, index);
         }
      }
      //public Class[] GetDependents() {
      //   return Reflector.getParameterDependents(factory, index);
      //}
      /// This is used to get the value from the specified object using
      /// the point of contact. Typically the value is retrieved from
      /// the specified object by invoking a get method of by acquiring
      /// the value from a field within the specified object.
      /// </summary>
      /// <param name="source">
      /// this is the object to acquire the value from
      /// </param>
      /// <returns>
      /// this is the value acquired from the point of contact
      /// </returns>
      public Object Get(Object source) {
         return null;
      }
      /// <summary>
      /// This is used to set the value on the specified object through
      /// this contact. Depending on the type of contact this will set
      /// the value given, typically this will be done by invoking a
      /// method or setting the value on the object field.
      /// </summary>
      /// <param name="source">
      /// this is the object to set the value on
      /// </param>
      /// <param name="value">
      /// this is the value to be set through the contact
      /// </param>
      public void Set(Object source, Object value) {
         return;
      }
      /// <summary>
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
      public <A : Annotation> A getAnnotation(Class<A> type) {
         return null;
      }
      /// <summary>
      /// This is used to determine if the annotated contact is for a
      /// read only variable. A read only variable is a field that
      /// can be set from within the constructor such as a blank readonly
      /// variable. It can also be a method with no set counterpart.
      /// </summary>
      /// <returns>
      /// this returns true if the contact is a constant one
      /// </returns>
      public bool IsReadOnly() {
         return false;
      }
      /// <summary>
      /// This represents the name of the parameter. Because the name
      /// of the parameter does not exist at runtime the name must
      /// be taken directly from the annotation and the parameter type.
      /// Each XML annotation must provide their own unique way of
      /// providing a name for the parameter contact.
      /// </summary>
      /// <returns>
      /// this returns the name of the contact represented
      /// </returns>
      public abstract String GetName();
   }
}
