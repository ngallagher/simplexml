#region License
//
// Contract.cs April 2007
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
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>Contract</c> object is used to expose the attribute
   /// names used by the cycle strategy. This ensures that reading and
   /// writing of the XML document is done in a consistent manner. Each
   /// attribute is used to mark special meta-data for the object graph.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.CycleStrategy
   /// </seealso>
   class Contract {
      /// <summary>
      /// This is used to specify the length of array instances.
      /// </summary>
      private String length;
      /// <summary>
      /// This is the label used to mark the type of an object.
      /// </summary>
      private String label;
      /// <summary>
      /// This is the attribute used to mark the identity of an object.
      /// </summary>
      private String mark;
      /// <summary>
      /// This is the attribute used to refer to an existing instance.
      /// </summary>
      private String refer;
      /// <summary>
      /// Constructor for the <c>Syntax</c> object. This is used
      /// to expose the attribute names used by the strategy. All the
      /// names can be acquired and shared by the read and write graph
      /// objects, which ensures consistency between the two objects.
      /// </summary>
      /// <param name="mark">
      /// this is used to mark the identity of an object
      /// </param>
      /// <param name="refer">
      /// this is used to refer to an existing object
      /// </param>
      /// <param name="label">
      /// this is used to specify the class for the field
      /// </param>
      /// <param name="length">
      /// this is the length attribute used for arrays
      /// </param>
      public Contract(String mark, String refer, String label, String length){
         this.length = length;
         this.label = label;
         this.refer = refer;
         this.mark = mark;
      }
      /// <summary>
      /// This is returns the attribute used to store information about
      /// the type to the XML document. This attribute name is used to
      /// add data to XML elements to enable the deserialization process
      /// to know the exact instance to use when creating a type.
      /// </summary>
      /// <returns>
      /// the name of the attribute used to store the type
      /// </returns>
      public String Label {
         get {
            return label;
         }
      }
      //public String GetLabel() {
      //   return label;
      //}
      /// This returns the attribute used to store references within the
      /// serialized XML document. The reference attribute is added to
      /// the serialized XML element so that cycles in the object graph
      /// can be recreated. This is an optional attribute.
      /// </summary>
      /// <returns>
      /// this returns the name of the reference attribute
      /// </returns>
      public String Reference {
         get {
            return refer;
         }
      }
      //public String GetReference() {
      //   return refer;
      //}
      /// This returns the attribute used to store the identities of all
      /// objects serialized to the XML document. The identity attribute
      /// stores a unique identifiers, which enables this strategy to
      /// determine an objects identity within the serialized XML.
      /// </summary>
      /// <returns>
      /// this returns the name of the identity attribute used
      /// </returns>
      public String Identity {
         get {
            return mark;
         }
      }
      //public String GetIdentity() {
      //   return mark;
      //}
      /// This returns the attribute used to store the array length in
      /// the serialized XML document. The array length is required so
      /// that the deserialization process knows how to construct the
      /// array before any of the array elements are deserialized.
      /// </summary>
      /// <returns>
      /// this returns the name of the array length attribute
      /// </returns>
      public String Length {
         get {
            return length;
         }
      }
      //public String GetLength() {
      //   return length;
      //}
}
