#region License
//
// Signature.cs February 2005
//
// Copyright (C) 2005, Niall Gallagher <niallg@users.sf.net>
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
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Signature</c> object is used to determine the details
   /// to use for an annotated field or method using both the field an
   /// annotation details. This allows defaults to be picked up from the
   /// method or field type if that have not been explicitly overridden
   /// in the annotation.
   /// </summary>
   class Signature {
      /// <summary>
      /// This is the actual annotation from the specified contact.
      /// </summary>
      private Annotation marker;
      /// <summary>
      /// This is the field or method contact that has been annotated.
      /// </summary>
      private Contact contact;
      /// <summary>
      /// This is the label used to expose the annotation details.
      /// </summary>
      private Label label;
      /// <summary>
      /// Constructor for the <c>Signature</c> object. This is
      /// used to create an object that will use information available
      /// within the field and annotation to determine exactly what
      /// the name of the XML element is to be and the type to use.
      /// </summary>
      /// <param name="contact">
      /// this is the method or field contact used
      /// </param>
      /// <param name="label">
      /// this is the annotation on the contact object
      /// </param>
      public Signature(Contact contact, Label label) {
         this.marker = contact.Annotation;
         this.contact = contact;
         this.label = label;
      }
      /// <summary>
      /// This is used to acquire the <c>Contact</c> for this. The
      /// contact is the actual method or field that has been annotated
      /// and is used to set or get information from the object instance.
      /// </summary>
      /// <returns>
      /// the method or field that this signature represents
      /// </returns>
      public Contact Contact {
         get {
            return contact;
         }
      }
      //public Contact GetContact() {
      //   return contact;
      //}
      /// This returns the dependent type for the annotation. This type
      /// is the type other than the annotated field or method type that
      /// the label depends on. For the <c>ElementList</c> this
      /// can be the generic parameter to an annotated collection type.
      /// </summary>
      /// <returns>
      /// this is the type that the annotation depends on
      /// </returns>
      public Type Dependent {
         get {
            return label.Dependent;
         }
      }
      //public Type GetDependent() {
      //   return label.Dependent;
      //}
      /// This method is used to get the entry name of a label using
      /// the type of the label. This ensures that if there is no
      /// entry XML element name declared by the annotation that a
      /// suitable name can be calculated from the annotated type.
      /// </summary>
      /// <returns>
      /// this returns a suitable XML entry element name
      /// </returns>
      public String Entry {
         get {
            Type depend = Dependent;
            Class type = depend.Type;
            if(type.isArray()) {
               type = type.getComponentType();
            }
            String name = GetName(type);
            if(name == null) {
               return null;
            }
            return name.intern();
         }
      }
      //public String GetEntry() {
      //   Type depend = Dependent;
      //   Class type = depend.Type;
      //   if(type.isArray()) {
      //      type = type.getComponentType();
      //   }
      //   String name = GetName(type);
      //   if(name == null) {
      //      return null;
      //   }
      //   return name.intern();
      //}
      /// This is used to acquire the name of the specified type using
      /// the <c>Root</c> annotation for the class. This will
      /// use either the name explicitly provided by the annotation or
      /// it will use the name of the class that the annotation was
      /// placed on if there is no explicit name for the root.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the root name for
      /// </param>
      /// <returns>
      /// this returns the name of the type from the root
      /// </returns>
      public String GetName(Class type) {
         String name = GetRoot(type);
         if(name != null) {
            return name;
         } else {
            name = type.getSimpleName();
         }
         return Reflector.GetName(name);
      }
      /// <summary>
      /// This will acquire the name of the <c>Root</c> annotation
      /// for the specified class. This will traverse the inheritance
      /// hierarchy looking for the root annotation, when it is found it
      /// is used to acquire a name for the XML element it represents.
      /// </summary>
      /// <param name="type">
      /// this is the type to acquire the root name with
      /// </param>
      /// <returns>
      /// the root name for the specified type if it exists
      /// </returns>
      public String GetRoot(Class type) {
         Class real = type;
         while(type != null) {
            String name = GetRoot(real, type);
            if(name != null) {
              return name;
            }
            type = type.getSuperclass();
         }
         return null;
      }
      /// <summary>
      /// This will acquire the name of the <c>Root</c> annotation
      /// for the specified class. This will traverse the inheritance
      /// hierarchy looking for the root annotation, when it is found it
      /// is used to acquire a name for the XML element it represents.
      /// </summary>
      /// <param name="real">
      /// the actual type of the object being searched
      /// </param>
      /// <param name="type">
      /// this is the type to acquire the root name with
      /// </param>
      /// <returns>
      /// the root name for the specified type if it exists
      /// </returns>
      public String GetRoot(Class<?> real, Class<?> type) {
         String name = type.getSimpleName();
         if(type.isAnnotationPresent(Root.class)) {
            Root root = type.getAnnotation(Root.class);
            String text = root.name();
            if(!IsEmpty(text)) {
               return text;
            }
            return Reflector.GetName(name);
         }
         return null;
      }
      /// <summary>
      /// This is used to determine the name of the XML element that the
      /// annotated field or method represents. This will determine based
      /// on the annotation attributes and the dependent type required
      /// what the name of the XML element this represents is.
      /// </summary>
      /// <returns>
      /// this returns the name of the XML element expected
      /// </returns>
      public String Name {
         get {
            String entry = label.Entry;
            if(!label.IsInline()) {
               entry = Default;
            }
            return entry.intern();
         }
      }
      //public String GetName() {
      //   String entry = label.Entry;
      //   if(!label.IsInline()) {
      //      entry = Default;
      //   }
      //   return entry.intern();
      //}
      /// This is used to acquire the name for an element by firstly
      /// checking for an override in the annotation. If one exists
      /// then this is returned if not then the name of the field
      /// or method contact is returned.
      /// </summary>
      /// <returns>
      /// this returns the XML element name to be used
      /// </returns>
      public String Default {
         get {
            String name = label.Override;
            if(!IsEmpty(name)) {
               return name;
            }
            return contact.GetName();
         }
      }
      //public String GetDefault() {
      //   String name = label.Override;
      //   if(!IsEmpty(name)) {
      //      return name;
      //   }
      //   return contact.GetName();
      //}
      /// This method is used to determine if a root annotation value is
      /// an empty value. Rather than determining if a string is empty
      /// be comparing it to an empty string this method allows for the
      /// value an empty string represents to be changed in future.
      /// </summary>
      /// <param name="value">
      /// this is the value to determine if it is empty
      /// </param>
      /// <returns>
      /// true if the string value specified is an empty value
      /// </returns>
      public bool IsEmpty(String value) {
         if(value != null) {
            return value.length() == 0;
         }
         return true;
      }
      /// <summary>
      /// This method is used to construct a string that describes the
      /// signature of an XML annotated field or method. This will use
      /// the <c>Contact</c> object and the annotation used for
      /// that contact to construct a string that has sufficient
      /// information such that it can be used in error reporting.
      /// </summary>
      /// <returns>
      /// returns a string used to represent this signature
      /// </returns>
      public String ToString() {
         return String.format("%s on %s", marker, contact);
      }
   }
}
