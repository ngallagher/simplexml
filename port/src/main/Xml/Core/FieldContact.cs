#region License
//
// FieldContact.cs April 2007
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
   /// The <c>FieldContact</c> object is used to act as a contact
   /// for a field within an object. This allows a value to be set on an
   /// object field during deserialization and acquired from the same
   /// field during serialization.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.FieldScanner
   /// </seealso>
   class FieldContact : Contact {
      /// <summary>
      /// This is the label that marks the field within the object.
      /// </summary>
      private Annotation label;
      /// <summary>
      /// This represents the field within the schema class object.
      /// </summary>
      private Field field;
      /// <summary>
      /// This is the name for this contact as taken from the field.
      /// </summary>
      private String name;
      /// <summary>
      /// This is the modifiers for the field that this represents.
      /// </summary>
      private int modifier;
      /// <summary>
      /// Constructor for the <c>FieldContact</c> object. This is
      /// used as a point of contact for a field within a schema class.
      /// Values can be read and written directly to the field with this.
      /// </summary>
      /// <param name="field">
      /// this is the field that is the point of contact
      /// </param>
      /// <param name="label">
      /// this is the annotation that is used by the field
      /// </param>
      public FieldContact(Field field, Annotation label) {
         this.modifier = field.getModifiers();
         this.label = label;
         this.field = field;
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
         return !IsStatic() && IsFinal();
      }
      /// <summary>
      /// This is used to determine if the annotated contact is for a
      /// static field or method. A static field or method is one that
      /// contains the "static" keyword. Any const fields will
      /// be read only and does not require any matching annotation.
      /// </summary>
      /// <returns>
      /// this returns true if the contact is a static one
      /// </returns>
      public bool IsStatic() {
         return Modifier.IsStatic(modifier);
      }
      /// <summary>
      /// This is used to identify annotated methods are fields that
      /// can not be modified. Such field will require that there is
      /// a constructor that can have the value injected in to it.
      /// </summary>
      /// <returns>
      /// this returns true if the field or method is readonly
      /// </returns>
      public bool IsFinal() {
         return Modifier.IsFinal(modifier);
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
            return field.Type;
         }
      }
      //public Class GetType() {
      //   return field.Type;
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
            return Reflector.getDependent(field);
         }
      }
      //public Class GetDependent() {
      //   return Reflector.getDependent(field);
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
            return Reflector.getDependents(field);
         }
      }
      //public Class[] GetDependents() {
      //   return Reflector.getDependents(field);
      //}
      /// This is used to acquire the name of the field. This will return
      /// the name of the field which can then be used to determine the
      /// XML attribute or element the contact represents. This ensures
      /// that the name provided string is internalized for performance.
      /// </summary>
      /// <returns>
      /// this returns the name of the field represented
      /// </returns>
      public String Name {
         get {
            if(name == null) {
               name = GetName(field);
            }
            return name;
         }
      }
      //public String GetName() {
      //   if(name == null) {
      //      name = GetName(field);
      //   }
      //   return name;
      //}
      /// This is used to acquire the name of the field such that it is
      /// an internalized string. Internalization of the contact name
      /// ensures that comparisons can be made to annotation names with
      /// a simple reference comparison rather than a string comparison.
      /// </summary>
      /// <param name="field">
      /// the field to acquire the internalized name from
      /// </param>
      /// <returns>
      /// this returns the name of the string, internalized
      /// </returns>
      public String GetName(Field field) {
         String name = field.GetName();
         if(name != null) {
            name = name.intern();
         }
         return name;
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
         if(type == label.annotationType()) {
            return (T) label;
         }
         return field.getAnnotation(type);
      }
      /// <summary>
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
         if(!IsFinal()) {
            field.Set(source, value);
         }
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
         return field.Get(source);
      }
      /// <summary>
      /// This is used to describe the contact as it exists within the
      /// owning class. It is used to provide error messages that can
      /// be used to debug issues that occur when processing a contact.
      /// The string provided is the generic field string.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the contact
      /// </returns>
      public String ToString() {
         return String.format("field '%s' %s", GetName(), field.ToString());
      }
   }
}
