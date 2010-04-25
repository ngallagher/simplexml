#region License
//
// ElementParameter.cs July 2009
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
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>ElementParameter</c> represents a constructor
   /// parameter. It contains the XML annotation used on the parameter
   /// as well as the name of the parameter and its position index.
   /// A parameter is used to validate against the annotated methods
   /// and fields and also to determine the deserialized values that
   /// should be injected in to the constructor to instantiate it.
   /// </summary>
   class ElementParameter : Parameter {
      /// <summary>
      /// This is the constructor the parameter was declared in.
      /// </summary>
      private readonly Constructor factory;
      /// <summary>
      /// This is the contact used to determine the parameter name.
      /// </summary>
      private readonly Contact contact;
      /// <summary>
      /// This is the label that will create the parameter name.
      /// </summary>
      private readonly Label label;
      /// <summary>
      /// This is the actual name that has been determined.
      /// </summary>
      private readonly String name;
      /// <summary>
      /// This is the index that the parameter was declared at.
      /// </summary>
      private readonly int index;
      /// <summary>
      /// Constructor for the <c>ElementParameter</c> object.
      /// This is used to create a parameter that can be used to
      /// determine a consistent name using the provided XML annotation.
      /// </summary>
      /// <param name="factory">
      /// this is the constructor the parameter is in
      /// </param>
      /// <param name="value">
      /// this is the annotation used for the parameter
      /// </param>
      /// <param name="index">
      /// this is the index the parameter appears at
      /// </param>
      public ElementParameter(Constructor factory, Element value, int index) {
         this.contact = new Contact(value, factory, index);
         this.label = new ElementLabel(contact, value);
         this.name = label.GetName();
         this.factory = factory;
         this.index = index;
      }
      /// <summary>
      /// This is used to acquire the name of the parameter that this
      /// represents. The name is determined using annotation and
      /// the name attribute of that annotation, if one is provided.
      /// </summary>
      /// <returns>
      /// this returns the name of the annotated parameter
      /// </returns>
      public String Name {
         get {
            return name;
         }
      }
      //public String GetName() {
      //   return name;
      //}
      /// This is used to acquire the annotated type class. The class
      /// is the type that is to be deserialized from the XML. This
      /// is used to validate against annotated fields and methods.
      /// </summary>
      /// <returns>
      /// this returns the type used for the parameter
      /// </returns>
      public Class Type {
         get {
            return factory.getParameterTypes()[index];
         }
      }
      //public Class GetType() {
      //   return factory.getParameterTypes()[index];
      //}
      /// This is used to acquire the annotation that is used for the
      /// parameter. The annotation provided will be an XML annotation
      /// such as the <c>Element</c> or <c>Attribute</c>
      /// annotation.
      /// </summary>
      /// <returns>
      /// this returns the annotation used on the parameter
      /// </returns>
      public Annotation Annotation {
         get {
            return contact.Annotation;
         }
      }
      //public Annotation GetAnnotation() {
      //   return contact.Annotation;
      //}
      /// This returns the index position of the parameter in the
      /// constructor. This is used to determine the order of values
      /// that are to be injected in to the constructor.
      /// </summary>
      /// <returns>
      /// this returns the index for the parameter
      /// </returns>
      public int Index {
         get {
            return index;
         }
      }
      //public int GetIndex() {
      //   return index;
      //}
      /// The <c>Contact</c> represents a contact object that is
      /// to be used for a label in order to extract a parameter name.
      /// The parameter name is taken from the XML annotation.
      /// </summary>
      private static class Contact : ParameterContact<Element>  {
         /// <summary>
         /// Constructor for the <c>Contact</c> object. This is
         /// used to create an object that acts like an adapter so that
         /// the label can create a consistent name for the parameter.
         /// </summary>
         /// <param name="label">
         /// this is the annotation for the parameter
         /// </param>
         /// <param name="factory">
         /// this is the constructor the parameter is in
         /// </param>
         /// <param name="index">
         /// this is the index for the parameter
         /// </param>
         public Contact(Element label, Constructor factory, int index) {
            super(label, factory, index);
         }
         /// <summary>
         /// This returns the name of the parameter as taken from the XML
         /// annotation. The name provided here is taken by the label
         /// and used to compose a name consistent with how fields and
         /// methods are named by the system.
         /// </summary>
         /// <returns>
         /// this returns the name of the annotated parameter
         /// </returns>
         public String Name {
            get {
               return label.name();
            }
         }
         //public String GetName() {
         //   return label.name();
         //}
   }
}
