#region License
//
// EventNode.cs January 2010
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
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion

#region Using directives
using System.Collections.Generic;
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>EventNode</c> object is used to represent an event
   /// that has been extracted from the XML document. Events provide a
   /// framework neutral way to represent a token from the source XML.
   /// It provides the name and value of the event, if applicable, and
   /// also provides namespace information. Some nodes will have
   /// associated <c>Attribute</c> objects, typically these will
   /// be the XML element events. Also, if available, the event will
   /// provide the line number the event was encountered in the XML.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.EventReader
   /// </seealso>
   class EventNode : List<Attribute> {

      /// <summary>
      /// This is used to provide the line number the XML event was
      /// encountered at within the XML document. If there is no line
      /// number available for the node then this will return a -1.
      /// </summary>
      /// <returns>
      /// This returns the line number if it is available.
      /// </returns>
      public virtual int Line {
         get {
            return -1;
         }
      }

      /// This provides the name of the event. Typically this will be
      /// the name of an XML element if the event represents an element.
      /// If however the event represents a text token or an element
      /// close token then this method may return null for the name.
      /// </summary>
      /// <returns>
      /// This returns the name of this event or null.
      /// </returns>
      public virtual String Name {
         get {
            return null;
         }
      }

      /// This returns the value of the event. Typically this will be
      /// the text value that the token contains. If the event does
      /// not contain a value then this returns null. Only text events
      /// are required to produce a value via this methods.
      /// </summary>
      /// <returns>
      /// This returns the value represented by this event.
      /// </returns>
      public virtual String Value {
         get {
            return null;
         }
      }

      /// This is used to acquire the namespace reference that this
      /// node is in. A namespace is normally associated with an XML
      /// element or attribute, so text events and element close events
      /// are not required to contain any namespace references.
      /// </summary>
      /// <returns>
      /// This will provide the associated namespace reference.
      /// </returns>
      public virtual String Reference {
         get {
            return null;
         }
      }

      /// This is used to acquire the namespace prefix associated with
      /// this node. A prefix is used to qualify an XML element or
      /// attribute within a namespace. So, if this represents a text
      /// event then a namespace prefix is not required.
      /// </summary>
      /// <returns>
      /// This returns the namespace prefix for this event.
      /// </returns>
      public virtual String Prefix {
         get {
            return null;
         }
      }

      /// This is used to return the source of the event. Depending on
      /// which provider was selected to parse the XML document an
      /// object for the internal parsers representation of the event
      /// will be returned. This is useful for debugging purposes.
      /// </summary>
      /// <returns>
      /// This will return the source object for this event.
      /// </returns>
      public virtual Object Source {
         get {
            return null;
         }
      }

      /// This is true when the node represents an element close. Such
      /// events are required by the core reader to determine if a
      /// node is still in context. This helps to determine if there
      /// are any more children to be read from a specific node.
      /// </summary>
      /// <returns>
      /// This returns true if the event is an element close.
      /// </returns>
      public virtual bool IsEnd() {
         return false;
      }

      /// <summary>
      /// This is true when the node represents a new element. This is
      /// the core event type as it contains the element name and any
      /// associated attributes. The core reader uses this to compose
      /// the input nodes that are produced.
      /// </summary>
      /// <returns>
      /// This returns true if the event represents an element.
      /// </returns>
      public virtual bool IsStart() {
         return false;
      }

      /// <summary>
      /// This is true when the node represents a text token. Text
      /// tokens are required to provide a value only. So namespace
      /// details and the node name will typically return null.
      /// </summary>
      /// <returns>
      /// This returns true if this represents text.
      /// </returns>
      public virtual bool IsText() {
         return false;
      }
   }
}
