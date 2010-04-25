#region License
//
// Parameter.cs July 2009
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
   /// The <c>Parameter</c> is used to represent a constructor
   /// parameter. It contains the XML annotation used on the parameter
   /// as well as the name of the parameter and its position index.
   /// A parameter is used to validate against the annotated methods
   /// and fields and also to determine the deserialized values that
   /// should be injected in to the constructor to instantiate it.
   /// </summary>
   interface Parameter {
      /// <summary>
      /// This is used to acquire the name of the parameter that this
      /// represents. The name is determined using annotation and
      /// the name attribute of that annotation, if one is provided.
      /// </summary>
      /// <returns>
      /// this returns the name of the annotated parameter
      /// </returns>
      public String Name {
         get;
      }
      //public String GetName();
      /// This is used to acquire the annotation that is used for the
      /// parameter. The annotation provided will be an XML annotation
      /// such as the <c>Element</c> or <c>Attribute</c>
      /// annotation.
      /// </summary>
      /// <returns>
      /// this returns the annotation used on the parameter
      /// </returns>
      public Annotation Annotation {
         get;
      }
      //public Annotation GetAnnotation();
      /// This is used to acquire the annotated type class. The class
      /// is the type that is to be deserialized from the XML. This
      /// is used to validate against annotated fields and methods.
      /// </summary>
      /// <returns>
      /// this returns the type used for the parameter
      /// </returns>
      public Class Type {
         get;
      }
      //public Class GetType();
      /// This returns the index position of the parameter in the
      /// constructor. This is used to determine the order of values
      /// that are to be injected in to the constructor.
      /// </summary>
      /// <returns>
      /// this returns the index for the parameter
      /// </returns>
      public int Index {
         get;
      }
      //public int GetIndex();
}
