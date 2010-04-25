#region License
//
// LabelFactory.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>LabelFactory</c> object is used to create instances of
   /// the <c>Label</c> object that can be used to convert an XML
   /// node into a Java object. Each label created requires the contact it
   /// represents and the XML annotation it is marked with.
   /// <p>
   /// The <c>Label</c> objects created by this factory a selected
   /// using the XML annotation type. If the annotation type is not known
   /// the factory will throw an exception, otherwise a label instance
   /// is created that will expose the properties of the annotation.
   /// </summary>
   sealed class LabelFactory {
      /// <summary>
      /// Creates a <c>Label</c> using the provided contact and XML
      /// annotation. The label produced contains all information related
      /// to an object member. It knows the name of the XML entity, as
      /// well as whether it is required. Once created the converter can
      /// transform an XML node into Java object and vice versa.
      /// </summary>
      /// <param name="contact">
      /// this is contact that the label is produced for
      /// </param>
      /// <param name="label">
      /// represents the XML annotation for the contact
      /// </param>
      /// <returns>
      /// returns the label instantiated for the field
      /// </returns>
      public Label GetInstance(Contact contact, Annotation label) {
         Label value = GetLabel(contact, label);
         if(value == null) {
            return value;
         }
         return new CacheLabel(value);
      }
      /// <summary>
      /// Creates a <c>Label</c> using the provided contact and XML
      /// annotation. The label produced contains all information related
      /// to an object member. It knows the name of the XML entity, as
      /// well as whether it is required. Once created the converter can
      /// transform an XML node into Java object and vice versa.
      /// </summary>
      /// <param name="contact">
      /// this is contact that the label is produced for
      /// </param>
      /// <param name="label">
      /// represents the XML annotation for the contact
      /// </param>
      /// <returns>
      /// returns the label instantiated for the field
      /// </returns>
      public Label GetLabel(Contact contact, Annotation label) {
         Constructor factory = GetConstructor(label);
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         return (Label)factory.newInstance(contact, label);
      }
       /// <summary>
       /// Creates a constructor that can be used to instantiate the label
       /// used to represent the specified annotation. The constructor
       /// created by this method takes two arguments, a contact object
       /// and an <c>Annotation</c> of the type specified.
       /// </summary>
       /// <param name="label">
       /// the XML annotation representing the label
       /// </param>
       /// <returns>
       /// returns a constructor for instantiating the label
       /// </returns>
       public Constructor GetConstructor(Annotation label) {
          return GetEntry(label).Constructor;
       }
       /// <summary>
       /// Creates an entry that is used to select the constructor for the
       /// label. Each label must implement a constructor that takes a
       /// contact and the specific XML annotation for that field. If the
       /// annotation is not know this method throws an exception.
       /// </summary>
       /// <param name="label">
       /// the XML annotation used to create the label
       /// </param>
       /// <returns>
       /// this returns the entry used to create a suitable
       /// </returns>
       ///         constructor for the label
       public Entry GetEntry(Annotation label) {
          if(label instanceof Element) {
             return new Entry(ElementLabel.class, Element.class);
          }
          if(label instanceof ElementList) {
             return new Entry(ElementListLabel.class, ElementList.class);
          }
          if(label instanceof ElementArray) {
             return new Entry(ElementArrayLabel.class, ElementArray.class);
          }
          if(label instanceof ElementMap) {
             return new Entry(ElementMapLabel.class, ElementMap.class);
          }
          if(label instanceof Attribute) {
             return new Entry(AttributeLabel.class, Attribute.class);
          }
          if(label instanceof Version) {
             return new Entry(VersionLabel.class, Version.class);
          }
          if(label instanceof Text) {
             return new Entry(TextLabel.class, Text.class);
          }
          throw new PersistenceException("Annotation %s not supported", label);
       }
       /// <summary>
       /// The <c>Entry<c> object is used to create a constructor
       /// that can be used to instantiate the correct label for the XML
       /// annotation specified. The constructor requires two arguments
       /// a <c>Contact</c> and the specified XML annotation.
       /// </summary>
       /// <seealso>
       /// java.lang.reflect.Constructor
       /// </seealso>
       private static class Entry {
          /// <summary>
          /// This is the XML annotation type within the constructor.
          /// </summary>
          public Class argument;
          /// <summary>
          /// This is the label type that is to be instantiated.
          /// </summary>
          public Class label;
          /// <summary>
          /// Constructor for the <c>Entry</c> object. This pairs
          /// the label type with the XML annotation argument used within
          /// the constructor. This allows constructor to be selected.
          /// </summary>
          /// <param name="label">
          /// this is the label type to be instantiated
          /// </param>
          /// <param name="argument">
          /// type that is used within the constructor
          /// </param>
          public Entry(Class label, Class argument) {
             this.argument = argument;
             this.label = label;
          }
          /// <summary>
          /// Creates the constructor used to instantiate the label for
          /// the XML annotation. The constructor returned will take two
          /// arguments, a contact and the XML annotation type.
          /// </summary>
          /// <returns>
          /// returns the constructor for the label object
          /// </returns>
          public Constructor Constructor {
             get {
                return GetConstructor(Contact.class);
             }
          }
          //public Constructor GetConstructor() {
          //   return GetConstructor(Contact.class);
          //}
          /// Creates the constructor used to instantiate the label for
          /// the XML annotation. The constructor returned will take two
          /// arguments, a contact and the XML annotation type.
          /// </summary>
          /// <param name="type">
          /// this is the XML annotation argument type used
          /// </param>
          /// <returns>
          /// returns the constructor for the label object
          /// </returns>
          public Constructor GetConstructor(Class type) {
             return label.GetConstructor(type, argument);
          }
       }
   }
}
