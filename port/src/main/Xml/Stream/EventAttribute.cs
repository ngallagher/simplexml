#region License
//
// EventAttribute.cs January 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion
#region Using directives
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>EventAttribute</c> object represents an attribute
   /// that is associated with an event node. An attribute is required
   /// to provide the name and value for the attribute, and optionally
   /// the namespace reference and prefix. For debugging purposes the
   /// source object from the internal XML provider can be acquired.
   /// </summary>
   internal abstract class EventAttribute : Attribute {
      /// <summary>
      /// This is used to acquire the namespace prefix associated with
      /// this attribute. A prefix is used to qualify the attribute
      /// within a namespace. So, if this has a prefix then it should
      /// have a reference associated with it.
      /// </summary>
      /// <returns>
      /// this returns the namespace prefix for the attribute
      /// </returns>
      public String Prefix {
         get {
            return null;
         }
      }
      //public String GetPrefix() {
      //   return null;
      //}
      /// This is used to acquire the namespace reference that this
      /// attribute is in. A namespace is normally associated with an
      /// attribute if that attribute is prefixed with a known token.
      /// If there is no prefix then this will return null.
      /// </summary>
      /// <returns>
      /// this provides the associated namespace reference
      /// </returns>
      public String Reference {
         get {
            return null;
         }
      }
      //public String GetReference() {
      //   return null;
      //}
      /// This is used to return the source of the attribute. Depending
      /// on which provider was selected to parse the XML document an
      /// object for the internal parsers representation of this will
      /// be returned. This is useful for debugging purposes.
      /// </summary>
      /// <returns>
      /// this will return the source object for this event
      /// </returns>
      public Object Source {
         get {
            return null;
         }
      }
      //public Object GetSource() {
      //   return null;
      //}
      /// This returns true if the attribute is reserved. An attribute
      /// is considered reserved if it begins with "xml" according to
      /// the namespaces in XML 1.0 specification. Such attributes are
      /// used for namespaces and other such details.
      /// </summary>
      /// <returns>
      /// this returns true if the attribute is reserved
      /// </returns>
      public bool IsReserved() {
         return false;
      }
   }
}
