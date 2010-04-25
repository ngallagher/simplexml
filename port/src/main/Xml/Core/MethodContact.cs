#region License
//
// MethodContact.cs April 2007
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
   /// The <c>MethodContact</c> object is acts as a contact that
   /// can set and get data to and from an object using methods. This
   /// requires a get method and a set method that share the same class
   /// type for the return and parameter respectively.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.MethodScanner
   /// </seealso>
   class MethodContact : Contact {
      /// <summary>
      /// This is the label that marks both the set and get methods.
      /// </summary>
      private Annotation label;
      /// <summary>
      /// This is the set method which is used to set the value.
      /// </summary>
      private MethodPart set;
      /// <summary>
      /// This is the dependent types as taken from the get method.
      /// </summary>
      private Class[] items;
      /// <summary>
      /// This is the dependent type as taken from the get method.
      /// </summary>
      private Class item;
      /// <summary>
      /// This is the type associated with this point of contact.
      /// </summary>
      private Class type;
      /// <summary>
      /// This is the get method which is used to get the value.
      /// </summary>
      private Method get;
      /// <summary>
      /// This represents the name of the method for this contact.
      /// </summary>
      private String name;
      /// <summary>
      /// Constructor for the <c>MethodContact</c> object. This is
      /// used to compose a point of contact that makes use of a get and
      /// set method on a class. The specified methods will be invoked
      /// during the serialization process to get and set values.
      /// </summary>
      /// <param name="get">
      /// this forms the get method for the object
      /// </param>
      public MethodContact(MethodPart get) {
         this(get, null);
      }
      /// <summary>
      /// Constructor for the <c>MethodContact</c> object. This is
      /// used to compose a point of contact that makes use of a get and
      /// set method on a class. The specified methods will be invoked
      /// during the serialization process to get and set values.
      /// </summary>
      /// <param name="get">
      /// this forms the get method for the object
      /// </param>
      /// <param name="set">
      /// this forms the get method for the object
      /// </param>
      public MethodContact(MethodPart get, MethodPart set) {
         this.label = get.Annotation;
         this.items = get.Dependents;
         this.item = get.Dependent;
         this.get = get.Method;
         this.type = get.Type;
         this.name = get.Name;
         this.set = set;
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
         return set == null;
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
         T result = get.getAnnotation(type);
         if(type == label.annotationType()) {
            return (T) label;
         }
         if(result == null && set != null) {
            return set.getAnnotation(type);
         }
         return result;
      }
      /// <summary>
      /// This will provide the contact type. The contact type is the
      /// class that is to be set and get on the object. This represents
      /// the return type for the get and the parameter for the set.
      /// </summary>
      /// <returns>
      /// this returns the type that this contact represents
      /// </returns>
      public Class Type {
         get {
            return type;
         }
      }
      //public Class GetType() {
      //   return type;
      //}
      /// This provides the dependent class for the contact. This will
      /// actually represent a generic type for the actual type. For
      /// contacts that use a <c>Collection</c> type this will
      /// be the generic type parameter for that collection.
      /// </summary>
      /// <returns>
      /// this returns the dependent type for the contact
      /// </returns>
      public Class Dependent {
         get {
            return item;
         }
      }
      //public Class GetDependent() {
      //   return item;
      //}
      /// This provides the dependent classes for the contact. This will
      /// typically represent a generic types for the actual type. For
      /// contacts that use a <c>Map</c> type this will be the
      /// generic type parameter for that map type declaration.
      /// </summary>
      /// <returns>
      /// this returns the dependent type for the contact
      /// </returns>
      public Class[] Dependents {
         get {
            return items;
         }
      }
      //public Class[] GetDependents() {
      //   return items;
      //}
      /// This is used to acquire the name of the method. This returns
      /// the name of the method without the get, set or is prefix that
      /// represents the Java Bean method type. Also this decapitalizes
      /// the resulting name. The result is used to represent the XML
      /// attribute of element within the class schema represented.
      /// </summary>
      /// <returns>
      /// this returns the name of the method represented
      /// </returns>
      public String Name {
         get {
            return name;
         }
      }
      //public String GetName() {
      //   return name;
      //}
      /// This is used to set the specified value on the provided object.
      /// The value provided must be an instance of the contact class so
      /// that it can be set without a runtime class compatibility error.
      /// </summary>
      /// <param name="source">
      /// this is the object to set the value on
      /// </param>
      /// <param name="value">
      /// this is the value that is to be set on the object
      /// </param>
      public void Set(Object source, Object value) {
         Class type = Type;
         if(set == null) {
            throw new MethodException("Method '%s' of '%s' is read only", name, type);
         }
         set.Method.invoke(source, value);
      }
      /// <summary>
      /// This is used to get the specified value on the provided object.
      /// The value returned from this method will be an instance of the
      /// contact class type. If the returned object is of a different
      /// type then the serialization process will fail.
      /// </summary>
      /// <param name="source">
      /// this is the object to acquire the value from
      /// </param>
      /// <returns>
      /// this is the value that is acquired from the object
      /// </returns>
      public Object Get(Object source) {
         return get.invoke(source);
      }
      /// <summary>
      /// This is used to describe the contact as it exists within the
      /// owning class. It is used to provide error messages that can
      /// be used to debug issues that occur when processing a contact.
      /// The string provided contains both the set and get methods.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the contact
      /// </returns>
      public String ToString() {
         return String.format("method '%s'", name);
      }
   }
}
