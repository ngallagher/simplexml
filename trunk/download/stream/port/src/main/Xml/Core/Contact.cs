#region License
//
// Contact.cs April 2007
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
using SimpleFramework.Xml.Strategy;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Contact</c> interface is used to provide a point of
   /// contact with an object. Typically this will be used to get and
   /// set to an from a field or a pair of matching bean methods. Each
   /// contact must be labeled with an annotation.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Label
   /// </seealso>
   interface Contact : Type {
      /// <summary>
      /// This represents the name of the object contact. If the contact
      /// is a field then the name of the field is provided. If however
      /// the contact is a method then the Java Bean name of the method
      /// is provided, which will be the decapitalized name of the
      /// method without the get, set, or is prefix to the method.
      /// </summary>
      /// <returns>
      /// this returns the name of the contact represented
      /// </returns>
      public String Name {
         get;
      }
      //public String GetName();
      /// This provides the dependent class for the contact. This will
      /// typically represent a generic type for the actual type. For
      /// contacts that use a <c>Collection</c> type this will
      /// be the generic type parameter for that collection.
      /// </summary>
      /// <returns>
      /// this returns the dependent type for the contact
      /// </returns>
      public Class Dependent {
         get;
      }
      //public Class GetDependent();
      /// This provides the dependent classes for the contact. This will
      /// typically represent a generic types for the actual type. For
      /// contacts that use a <c>Map</c> type this will be the
      /// generic type parameter for that map type declaration.
      /// </summary>
      /// <returns>
      /// this returns the dependent types for the contact
      /// </returns>
      public Class[] Dependents {
         get;
      }
      //public Class[] GetDependents();
      /// This is the annotation associated with the point of contact.
      /// This will be an XML annotation that describes how the contact
      /// should be serialized and deserialized from the object.
      /// </summary>
      /// <returns>
      /// this provides the annotation associated with this
      /// </returns>
      public Annotation Annotation {
         get;
      }
      //public Annotation GetAnnotation();
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
      public void Set(Object source, Object value);
      /// <summary>
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
      public Object Get(Object source);
      /// <summary>
      /// This is used to determine if the annotated contact is for a
      /// read only variable. A read only variable is a field that
      /// can be set from within the constructor such as a blank readonly
      /// variable. It can also be a method with no set counterpart.
      /// </summary>
      /// <returns>
      /// this returns true if the contact is a constant one
      /// </returns>
      public bool IsReadOnly();
      /// <summary>
      /// This is used to describe the contact as it exists within the
      /// owning class. This is used to provide error messages that can
      /// be used to debug issues that occur when processing a contact.
      /// </summary>
      /// <returns>
      /// this returns a string representation of the contact
      /// </returns>
      public String ToString();
   }
}
