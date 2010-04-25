#region License
//
// InputElement.cs July 2006
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
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   /// <summary>
   /// The <c>InputElement</c> represents a self contained element
   /// that will allow access to its child elements. If the next element
   /// read from the <c>NodeReader</c> is not a child then this
   /// will return null. The input element node also allows the attribute
   /// values associated with the node to be accessed.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.NodeReader
   /// </seealso>
   class InputElement : InputNode {
      /// <summary>
      /// This contains all the attributes associated with the element.
      /// </summary>
      private readonly InputNodeMap map;
      /// <summary>
      /// This is the node reader that reads from the XML document.
      /// </summary>
      private readonly NodeReader reader;
      /// <summary>
      /// This is the parent node for this XML input element node.
      /// </summary>
      private readonly InputNode parent;
      /// <summary>
      /// This is the XML element that this node provides access to.
      /// </summary>
      private readonly EventNode node;
      /// <summary>
      /// Constructor for the <c>InputElement</c> object. This
      /// is used to create an input node that will provide access to
      /// an XML element. All attributes associated with the element
      /// given are extracted and exposed via the attribute node map.
      /// </summary>
      /// <param name="parent">
      /// this is the parent XML element for this
      /// </param>
      /// <param name="reader">
      /// this is the reader used to read XML elements
      /// </param>
      /// <param name="node">
      /// this is the XML element wrapped by this node
      /// </param>
      public InputElement(InputNode parent, NodeReader reader, EventNode node) {
         this.map = new InputNodeMap(this, node);
         this.reader = reader;
         this.parent = parent;
         this.node = node;
      }
      /// <summary>
      /// This is used to return the source object for this node. This
      /// is used primarily as a means to determine which XML provider
      /// is parsing the source document and producing the nodes. It
      /// is useful to be able to determine the XML provider like this.
      /// </summary>
      /// <returns>
      /// this returns the source of this input node
      /// </returns>
      public Object Source {
         get {
            return node.Source;
         }
      }
      //public Object GetSource() {
      //   return node.Source;
      //}
      /// This is used to acquire the <c>Node</c> that is the
      /// parent of this node. This will return the node that is
      /// the direct parent of this node and allows for siblings to
      /// make use of nodes with their parents if required.
      /// </summary>
      /// <returns>
      /// this returns the parent node for this node
      /// </returns>
      public InputNode Parent {
         get {
            return parent;
         }
      }
      //public InputNode GetParent() {
      //   return parent;
      //}
      /// This provides the position of this node within the document.
      /// This allows the user of this node to report problems with
      /// the location within the document, allowing the XML to be
      /// debugged if it does not match the class schema.
      /// </summary>
      /// <returns>
      /// this returns the position of the XML read cursor
      /// </returns>
      public Position Position {
         get {
            return new InputPosition(node);
         }
      }
      //public Position GetPosition() {
      //   return new InputPosition(node);
      //}
      /// Returns the name of the node that this represents. This is
      /// an immutable property and should not change for any node.
      /// This provides the name without the name space part.
      /// </summary>
      /// <returns>
      /// returns the name of the node that this represents
      /// </returns>
      public String Name {
         get {
            return node.Name;
         }
      }
      //public String GetName() {
      //   return node.Name;
      //}
      /// This is used to acquire the namespace prefix for the node.
      /// If there is no namespace prefix for the node then this will
      /// return null. Acquiring the prefix enables the qualification
      /// of the node to be determined. It also allows nodes to be
      /// grouped by its prefix and allows group operations.
      /// </summary>
      /// <returns>
      /// this returns the prefix associated with this node
      /// </returns>
      public String Prefix {
         get {
            return node.Prefix;
         }
      }
      //public String GetPrefix() {
      //   return node.Prefix;
      //}
      /// This allows the namespace reference URI to be determined.
      /// A reference is a globally unique string that allows the
      /// node to be identified. Typically the reference will be a URI
      /// but it can be any unique string used to identify the node.
      /// This allows the node to be identified within the namespace.
      /// </summary>
      /// <returns>
      /// this returns the associated namespace reference URI
      /// </returns>
      public String Reference {
         get {
            return node.Reference;
         }
      }
      //public String GetReference() {
      //   return node.Reference;
      //}
      /// This method is used to determine if this node is the root
      /// node for the XML document. The root node is the first node
      /// in the document and has no sibling nodes. This is false
      /// if the node has a parent node or a sibling node.
      /// </summary>
      /// <returns>
      /// true if this is the root node within the document
      /// </returns>
      public bool IsRoot() {
         return reader.IsRoot(this);
      }
      /// <summary>
      /// This is used to determine if this node is an element. This
      /// allows users of the framework to make a distinction between
      /// nodes that represent attributes and nodes that represent
      /// elements. This is particularly useful given that attribute
      /// nodes do not maintain a node map of attributes.
      /// </summary>
      /// <returns>
      /// this returns true as this instance is an element
      /// </returns>
      public bool IsElement() {
         return true;
      }
      /// <summary>
      /// Provides an attribute from the element represented. If an
      /// attribute for the specified name does not exist within the
      /// element represented then this method will return null.
      /// </summary>
      /// <param name="name">
      /// this is the name of the attribute to retrieve
      /// </param>
      /// <returns>
      /// this returns the value for the named attribute
      /// </returns>
      public InputNode GetAttribute(String name) {
         return map.Get(name);
      }
      /// <summary>
      /// This returns a map of the attributes contained within the
      /// element. If no elements exist within the element then this
      /// returns an empty map.
      /// </summary>
      /// <returns>
      /// this returns a map of attributes for the element
      /// </returns>
      public NodeMap<InputNode> Attributes {
         get {
            return map;
         }
      }
      //public NodeMap<InputNode> GetAttributes() {
      //   return map;
      //}
      /// Returns the value for the node that this represents. This
      /// is an immutable value for the node and cannot be changed.
      /// If there is a problem reading an exception is thrown.
      /// </summary>
      /// <returns>
      /// the name of the value for this node instance
      /// </returns>
      public String Value {
         get {
            return reader.ReadValue(this);
         }
      }
      //public String GetValue() {
      //   return reader.ReadValue(this);
      //}
      /// The method is used to acquire the next child attribute of this
      /// element. If the next element from the <c>NodeReader</c>
      /// is not a child node to the element that this represents then
      /// this will return null, which ensures each element represents
      /// a self contained collection of child nodes.
      /// </summary>
      /// <returns>
      /// this returns the next child element of this node
      /// </returns>
      public InputNode Next {
         get {
            return reader.ReadElement(this);
         }
      }
      //public InputNode GetNext() {
      //   return reader.ReadElement(this);
      //}
      /// The method is used to acquire the next child attribute of this
      /// element. If the next element from the <c>NodeReader</c>
      /// is not a child node to the element that this represents then
      /// this will return null, also if the next element does not match
      /// the specified name then this will return null.
      /// </summary>
      /// <param name="name">
      /// this is the name expected fromt he next element
      /// </param>
      /// <returns>
      /// this returns the next child element of this node
      /// </returns>
      public InputNode GetNext(String name) {
         return reader.ReadElement(this, name);
      }
      /// <summary>
      /// This method is used to skip all child elements from this
      /// element. This allows elements to be effectively skipped such
      /// that when parsing a document if an element is not required
      /// then that element can be completely removed from the XML.
      /// </summary>
      public void Skip() {
         reader.SkipElement(this);
      }
      /// <summary>
      /// This is used to determine if this input node is empty. An
      /// empty node is one with no attributes or children. This can
      /// be used to determine if a given node represents an empty
      /// entity, with which no extra data can be extracted.
      /// </summary>
      /// <returns>
      /// this returns true if the node is an empty element
      /// </returns>
      public bool IsEmpty() {
         if(!map.IsEmpty()) {
            return false;
         }
         return reader.IsEmpty(this);
      }
      /// <summary>
      /// This is the string representation of the element. It is
      /// used for debugging purposes. When evaluating the element
      /// the to string can be used to print out the element name.
      /// </summary>
      /// <returns>
      /// this returns a text description of the element
      /// </returns>
      public String ToString() {
         return String.format("element %s", Name);
      }
   }
}
