#region License
//
// ParameterFactory.cs July 2006
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
   /// The <c>ParameterFactory</c> object is used to create instances
   /// of the <c>Parameter</c> object. Each parameter created can be
   /// used to validate against the annotated fields and methods to ensure
   /// that the annotations are compatible.
   /// <p>
   /// The <c>Parameter</c> objects created by this are selected
   /// using the XML annotation type. If the annotation type is not known
   /// the factory will throw an exception, otherwise a parameter instance
   /// is created that will expose the properties of the annotation.
   /// </summary>
   sealed class ParameterFactory {
      /// <summary>
      /// Creates a <c>Parameter</c> using the provided constructor
      /// and the XML annotation. The parameter produced contains all
      /// information related to the constructor parameter. It knows the
      /// name of the XML entity, as well as the type.
      /// </summary>
      /// <param name="method">
      /// this is the constructor the parameter exists in
      /// </param>
      /// <param name="label">
      /// represents the XML annotation for the contact
      /// </param>
      /// <returns>
      /// returns the parameter instantiated for the field
      /// </returns>
      public Parameter GetInstance(Constructor method, Annotation label, int index) {
         Constructor factory = GetConstructor(label);
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         return (Parameter)factory.newInstance(method, label, index);
      }
       /// <summary>
       /// Creates a constructor that is used to instantiate the parameter
       /// used to represent the specified annotation. The constructor
       /// created by this method takes three arguments, a constructor,
       /// an annotation, and the parameter index.
       /// </summary>
       /// <param name="label">
       /// the XML annotation representing the label
       /// </param>
       /// <returns>
       /// returns a constructor for instantiating the parameter
       /// </returns>
       public Constructor GetConstructor(Annotation label) {
          return GetEntry(label).Constructor;
       }
       /// <summary>
       /// Creates an entry that is used to select the constructor for the
       /// parameter. Each parameter must implement a constructor that takes
       /// a constructor, and annotation, and the index of the parameter. If
       /// the annotation is not know this method throws an exception.
       /// </summary>
       /// <param name="label">
       /// the XML annotation used to create the parameter
       /// </param>
       /// <returns>
       /// this returns the entry used to create a suitable
       /// </returns>
       ///         constructor for the parameter
       public Entry GetEntry(Annotation label) {
          if(label instanceof Element) {
             return new Entry(ElementParameter.class, Element.class);
          }
          if(label instanceof ElementList) {
             return new Entry(ElementListParameter.class, ElementList.class);
          }
          if(label instanceof ElementArray) {
             return new Entry(ElementArrayParameter.class, ElementArray.class);
          }
          if(label instanceof ElementMap) {
             return new Entry(ElementMapParameter.class, ElementMap.class);
          }
          if(label instanceof Attribute) {
             return new Entry(AttributeParameter.class, Attribute.class);
          }
          throw new PersistenceException("Annotation %s not supported", label);
       }
       /// <summary>
       /// The <c>Entry<c> object is used to create a constructor
       /// that can be used to instantiate the correct parameter for the
       /// XML annotation specified. The constructor requires three
       /// arguments, the constructor, the annotation, and the index.
       /// </summary>
       /// <seealso>
       /// java.lang.reflect.Constructor
       /// </seealso>
       private static class Entry {
          /// <summary>
          /// This is the parameter type that is to be instantiated.
          /// </summary>
          public Class create;
          /// <summary>
          /// This is the XML annotation type within the constructor.
          /// </summary>
          public Class type;
          /// <summary>
          /// Constructor for the <c>Entry</c> object. This pairs
          /// the parameter type with the annotation argument used within
          /// the constructor. This allows constructor to be selected.
          /// </summary>
          /// <param name="create">
          /// this is the label type to be instantiated
          /// </param>
          /// <param name="type">
          /// the type that is used within the constructor
          /// </param>
          public Entry(Class create, Class type) {
             this.create = create;
             this.type = type;
          }
          /// <summary>
          /// Creates the constructor used to instantiate the parameter
          /// for the XML annotation. The constructor returned will take
          /// two arguments, a contact and the XML annotation type.
          /// </summary>
          /// <returns>
          /// returns the constructor for the parameter object
          /// </returns>
          public Constructor Constructor {
             get {
                return GetConstructor(Constructor.class, type, int.class);
             }
          }
          //public Constructor GetConstructor() {
          //   return GetConstructor(Constructor.class, type, int.class);
          //}
          /// Creates the constructor used to instantiate the parameter
          /// for the XML annotation. The constructor returned will take
          /// three arguments, a constructor, an annotation and a type.
          /// </summary>
          /// <param name="types">
          /// these are the arguments for the constructor
          /// </param>
          /// <returns>
          /// returns the constructor for the parameter object
          /// </returns>
          public Constructor GetConstructor(Class... types) {
             return create.GetConstructor(types);
          }
       }
   }
}
