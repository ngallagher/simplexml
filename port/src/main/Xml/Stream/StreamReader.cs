#region License
//
// StreamReader.cs January 2010
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
   /// The <c>StreamReader</c> object provides an implementation
   /// for reading XML events using StAX. This will pretty much wrap
   /// core StAX events as the framework is very closely related. The
   /// implementation is basically required to ensure StAX events can
   /// be digested by the core reader. For performance this will match
   /// the underlying implementation closely as all this basically
   /// does is act as a means to adapt the underlying framework events.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.StreamProvider
   /// </seealso>
   class StreamReader : EventReader {
      /// <summary>
      /// This is the reader that is used to parse the XML document.
      /// </summary>
      private XMLEventReader reader;
      /// <summary>
      /// This is used to keep track of any events that were peeked.
      /// </summary>
      private EventNode peek;
      /// <summary>
      /// Constructor for the <c>StreamReader</c> object. This
      /// creates a reader that extracts events from the provided object.
      /// All StAX events returned from the provided instance will be
      /// adapted so that they can be digested by the core reader.
      /// </summary>
      /// <param name="reader">
      /// this is the reader used to parse the XML source
      /// </param>
      public StreamReader(XMLEventReader reader) {
         this.reader = reader;
      }
      /// <summary>
      /// This is used to peek at the node from the document. This will
      /// scan through the document, ignoring any comments to find the
      /// next relevant XML event to acquire. Typically events will be
      /// the start and end of an element, as well as any text nodes.
      /// </summary>
      /// <returns>
      /// this returns the next event taken from the document
      /// </returns>
      public EventNode Peek() {
         if(peek == null) {
            peek = Next();
         }
         return peek;
      }
      /// <summary>
      /// This is used to take the next node from the document. This will
      /// scan through the document, ignoring any comments to find the
      /// next relevant XML event to acquire. Typically events will be
      /// the start and end of an element, as well as any text nodes.
      /// </summary>
      /// <returns>
      /// this returns the next event taken from the source XML
      /// </returns>
      public EventNode Next() {
         EventNode next = peek;
         if(next == null) {
            next = Read();
         } else {
            peek = null;
         }
         return next;
      }
      /// <summary>
      /// This is used to read the next node from the document. This will
      /// scan through the document, ignoring any comments to find the
      /// next relevant XML event to acquire. Typically events will be
      /// the start and end of an element, as well as any text nodes.
      /// </summary>
      /// <returns>
      /// this returns the next event taken from the document
      /// </returns>
      public EventNode Read() {
         XMLEvent event = reader.nextEvent();
         if(event.isStartElement()) {
            return Start(event);
         }
         if(event.isCharacters()) {
            return Text(event);
         }
         if(event.isEndElement()) {
            return End();
         }
         return Read();
      }
      /// <summary>
      /// This is used to convert the provided event to a start event. The
      /// conversion process ensures the node can be digested by the core
      /// reader and used to provide an <c>InputNode</c> that can
      /// be used to represent an XML elements within the source document.
      /// </summary>
      /// <param name="event">
      /// the event that is to be converted to a start event
      /// </param>
      /// <returns>
      /// this returns a start event created from the given event
      /// </returns>
      public Start Start(XMLEvent event) {
         Start node = new Start(event);
         if(node.isEmpty()) {
            return Build(node);
         }
         return node;
      }
      /// <summary>
      /// This is used to build the attributes that are to be used to
      /// populate the start event. Populating the start event with the
      /// attributes it contains is required so that each element will
      /// contain its associated attributes. Only attributes that are
      /// not reserved will be added to the start event.
      /// </summary>
      /// <param name="event">
      /// this is the start event that is to be populated
      /// </param>
      /// <returns>
      /// this returns a start event with its attributes
      /// </returns>
      public Start Build(Start event) {
         Iterator<Attribute> list = event.Attributes;
         while (list.hasNext()) {
            Attribute node = list.Next();
            Entry entry = Attribute(node);
            if(!entry.IsReserved()) {
               event.add(entry);
            }
         }
         return event;
      }
      /// <summary>
      /// This is used to convert the provided object to an attribute. The
      /// conversion process ensures the node can be digested by the core
      /// reader and used to provide an <c>InputNode</c> that can
      /// be used to represent an XML attribute within the source document.
      /// </summary>
      /// <param name="entry">
      /// the object that is to be converted to an attribute
      /// </param>
      /// <returns>
      /// this returns an attribute created from the given object
      /// </returns>
      public Entry Attribute(Attribute entry) {
         return new Entry(entry);
      }
      /// <summary>
      /// This is used to convert the provided event to a text event. The
      /// conversion process ensures the node can be digested by the core
      /// reader and used to provide an <c>InputNode</c> that can
      /// be used to represent an XML attribute within the source document.
      /// </summary>
      /// <param name="event">
      /// the event that is to be converted to a text event
      /// </param>
      /// <returns>
      /// this returns the text event created from the given event
      /// </returns>
      public Text Text(XMLEvent event) {
         return new Text(event);
      }
      /// <summary>
      /// This is used to create an event to signify that an element has
      /// just ended. End events are important as they allow the core
      /// reader to determine if a node is still in context. This provides
      /// a more convenient way to use <c>InputNode</c> objects as
      /// they should only ever be able to extract their children.
      /// </summary>
      /// <returns>
      /// this returns an end event to signify an element close
      /// </returns>
      public End End() {
         return new End();
      }
      /// <summary>
      /// The <c>Entry</c> object is used to represent an attribute
      /// within a start element. This holds the name and value of the
      /// attribute as well as the namespace prefix and reference. These
      /// details can be used to represent the attribute so that should
      /// the core reader require these details they can be acquired.
      /// </summary>
      private static class Entry : EventAttribute {
         /// <summary>
         /// This is the attribute object representing this attribute.
         /// </summary>
         private readonly Attribute entry;
         /// <summary>
         /// Constructor for the <c>Entry</c> object. This creates
         /// an attribute object that is used to extract the name, value
         /// namespace prefix, and namespace reference from the provided
         /// node. This is used to populate any start events created.
         /// </summary>
         /// <param name="entry">
         /// this is the node that represents the attribute
         /// </param>
         public Entry(Attribute entry) {
            this.entry = entry;
         }
         /// <summary>
         /// This provides the name of the attribute. This will be the
         /// name of the XML attribute without any namespace prefix. If
         /// the name begins with "xml" then this attribute is reserved.
         /// according to the namespaces for XML 1.0 specification.
         /// </summary>
         /// <returns>
         /// this returns the name of this attribute object
         /// </returns>
         public String Name {
            get {
               return entry.Name.getLocalPart();
            }
         }
         //public String GetName() {
         //   return entry.Name.getLocalPart();
         //}
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
               return entry.Name.Prefix;
            }
         }
         //public String GetPrefix() {
         //   return entry.Name.Prefix;
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
               return entry.Name.getNamespaceURI();
            }
         }
         //public String GetReference() {
         //   return entry.Name.getNamespaceURI();
         //}
         /// This returns the value of the event. This will be the value
         /// that the attribute contains. If the attribute does not have
         /// a value then this returns null or an empty string.
         /// </summary>
         /// <returns>
         /// this returns the value represented by this object
         /// </returns>
         public String Value {
            get {
               return entry.Value;
            }
         }
         //public String GetValue() {
         //   return entry.Value;
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
         /// <summary>
         /// This is used to return the node for the attribute. Because
         /// this represents a StAX attribute the StAX object is returned.
         /// Returning the node helps with certain debugging issues.
         /// </summary>
         /// <returns>
         /// this will return the source object for this
         /// </returns>
         public Object Source {
            get {
               return entry;
            }
         }
         //public Object GetSource() {
         //   return entry;
         //}
      /// <summary>
      /// The <c>Start</c> object is used to represent the start of
      /// an XML element. This will hold the attributes associated with
      /// the element and will provide the name, the namespace reference
      /// and the namespace prefix. For debugging purposes the source XML
      /// element is provided for this start event.
      /// </summary>
      private static class Start : EventElement {
         /// <summary>
         /// This is the start element to be used by this start event.
         /// </summary>
         private readonly StartElement element;
         /// <summary>
         /// This is the element location used to detmine line numbers.
         /// </summary>
         private readonly Location location;
         /// <summary>
         /// Constructor for the <c>Start</c> object. This will
         /// wrap the provided node and expose the required details such
         /// as the name, namespace prefix and namespace reference. The
         /// provided element node can be acquired for debugging purposes.
         /// </summary>
         /// <param name="event">
         /// this is the element being wrapped by this
         /// </param>
         public Start(XMLEvent event) {
            this.element = event.asStartElement();
            this.location = event.getLocation();
         }
         /// <summary>
         /// This is used to provide the line number the XML event was
         /// encountered at within the XML document. If there is no line
         /// number available for the node then this will return a -1.
         /// </summary>
         /// <returns>
         /// this returns the line number if it is available
         /// </returns>
         public int Line {
            get {
               return location.getLineNumber();
            }
         }
         //public int GetLine() {
         //   return location.getLineNumber();
         //}
         /// This provides the name of the event. This will be the name
         /// of an XML element the event represents. If there is a prefix
         /// associated with the element, this extracts that prefix.
         /// </summary>
         /// <returns>
         /// this returns the name without the namespace prefix
         /// </returns>
         public String Name {
            get {
               return element.Name.getLocalPart();
            }
         }
         //public String GetName() {
         //   return element.Name.getLocalPart();
         //}
         /// This is used to acquire the namespace prefix associated with
         /// this node. A prefix is used to qualify an XML element or
         /// attribute within a namespace. So, if this represents a text
         /// event then a namespace prefix is not required.
         /// </summary>
         /// <returns>
         /// this returns the namespace prefix for this event
         /// </returns>
         public String Prefix {
            get {
               return element.Name.Prefix;
            }
         }
         //public String GetPrefix() {
         //   return element.Name.Prefix;
         //}
         /// This is used to acquire the namespace reference that this
         /// node is in. A namespace is normally associated with an XML
         /// element or attribute, so text events and element close events
         /// are not required to contain any namespace references.
         /// </summary>
         /// <returns>
         /// this will provide the associated namespace reference
         /// </returns>
         public String Reference {
            get {
               return element.Name.getNamespaceURI();
            }
         }
         //public String GetReference() {
         //   return element.Name.getNamespaceURI();
         //}
         /// This is used to acquire the attributes associated with the
         /// element. Providing the attributes in this format allows
         /// the reader to build a list of attributes for the event.
         /// </summary>
         /// <returns>
         /// this returns the attributes associated with this
         /// </returns>
         public Iterator<Attribute> Attributes {
            get {
               return element.Attributes;
            }
         }
         //public Iterator<Attribute> GetAttributes() {
         //   return element.Attributes;
         //}
         /// This is used to return the node for the element. Because
         /// this represents a StAX event the StAX event is returned.
         /// Returning the node helps with certain debugging issues.
         /// </summary>
         /// <returns>
         /// this will return the source object for this
         /// </returns>
         public Object Source {
            get {
               return element;
            }
         }
         //public Object GetSource() {
         //   return element;
         //}
      /// <summary>
      /// The <c>Text</c> object is used to represent a text event.
      /// If wraps a node that holds text consumed from the document.
      /// These are used by <c>InputNode</c> objects to extract the
      /// text values for elements For debugging this exposes the node.
      /// </summary>
      private static class Text : EventToken {
         /// <summary>
         /// This is the event that is used to represent the text value.
         /// </summary>
         private readonly Characters text;
         /// <summary>
         /// Constructor for the <c>Text</c> object. This creates
         /// an event that provides text to the core reader. Text can be
         /// in the form of a CDATA section or a normal text entry.
         /// </summary>
         /// <param name="event">
         /// this is the node that represents the text value
         /// </param>
         public Text(XMLEvent event) {
            this.text = event.asCharacters();
         }
         /// <summary>
         /// This is true as this event represents a text token. Text
         /// tokens are required to provide a value only. So namespace
         /// details and the node name will always return null.
         /// </summary>
         /// <returns>
         /// this returns true as this event represents text
         /// </returns>
         public bool IsText() {
            return true;
         }
         /// <summary>
         /// This returns the value of the event. This will return the
         /// text value contained within the node. If there is no
         /// text within the node this should return an empty string.
         /// </summary>
         /// <returns>
         /// this returns the value represented by this event
         /// </returns>
         public String Value {
            get {
               return text.getData();
            }
         }
         //public String GetValue() {
         //   return text.getData();
         //}
         /// This is used to return the node for the text. Because
         /// this represents a StAX event the StAX event is returned.
         /// Returning the node helps with certain debugging issues.
         /// </summary>
         /// <returns>
         /// this will return the source object for this
         /// </returns>
         public Object Source {
            get {
               return text;
            }
         }
         //public Object GetSource() {
         //   return text;
         //}
      /// <summary>
      /// The <c>End</c> object is used to represent the end of an
      /// element. It is used by the core reader to determine which nodes
      /// are in context and which ones are out of context. This allows
      /// the input nodes to determine if it can read any more children.
      /// </summary>
      private static class End : EventToken {
         /// <summary>
         /// This is true as this event represents an element end. Such
         /// events are required by the core reader to determine if a
         /// node is still in context. This helps to determine if there
         /// are any more children to be read from a specific node.
         /// </summary>
         /// <returns>
         /// this returns true as this token represents an end
         /// </returns>
         public bool IsEnd() {
            return true;
         }
      }
   }
}
