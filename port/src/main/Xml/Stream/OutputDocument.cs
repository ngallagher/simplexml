#region License
//
// OutputDocument.cs July 2006
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
   /// The <c>OutputDocument</c> object is used to represent the
   /// root of an XML document. This does not actually represent anything
   /// that will be written to the generated document. It is used as a
   /// way to create the root document element. Once the root element has
   /// been created it can be committed by using this object.
   /// </summary>
   class OutputDocument : OutputNode {
      /// <summary>
      /// Represents a dummy output node map for the attributes.
      /// </summary>
      private OutputNodeMap table;
      /// <summary>
      /// Represents the writer that is used to create the element.
      /// </summary>
      private NodeWriter writer;
      /// <summary>
      /// This is the output stack used by the node writer object.
      /// </summary>
      private OutputStack stack;
      /// <summary>
      /// This represents the namespace reference used by this.
      /// </summary>
      private String reference;
      /// <summary>
      /// This is the comment that is to be written for the node.
      /// </summary>
      private String comment;
      /// <summary>
      /// Represents the value that has been set on this document.
      /// </summary>
      private String value;
      /// <summary>
      /// This is the output mode of this output document object.
      /// </summary>
      private Mode mode;
      /// <summary>
      /// Constructor for the <c>OutputDocument</c> object. This
      /// is used to create an empty output node object that can be
      /// used to create a root element for the generated document.
      /// </summary>
      /// <param name="writer">
      /// this is the node writer to write the node to
      /// </param>
      /// <param name="stack">
      /// this is the stack that contains the open nodes
      /// </param>
      public OutputDocument(NodeWriter writer, OutputStack stack) {
         this.table = new OutputNodeMap(this);
         this.mode = Mode.INHERIT;
         this.writer = writer;
         this.stack = stack;
      }
      /// <summary>
      /// The default for the <c>OutputDocument</c> is null as it
      /// does not require a namespace. A null prefix is always used by
      /// the document as it represents a virtual node that does not
      /// exist and will not form any part of the resulting XML.
      /// </summary>
      /// <returns>
      /// this returns a null prefix for the output document
      /// </returns>
      public String Prefix {
         get {
            return null;
         }
      }
      //public String GetPrefix() {
      //   return null;
      //}
      /// The default for the <c>OutputDocument</c> is null as it
      /// does not require a namespace. A null prefix is always used by
      /// the document as it represents a virtual node that does not
      /// exist and will not form any part of the resulting XML.
      /// </summary>
      /// <param name="inherit">
      /// if there is no explicit prefix then inherit
      /// </param>
      /// <returns>
      /// this returns a null prefix for the output document
      /// </returns>
      public String GetPrefix(bool inherit) {
         return null;
      }
      /// <summary>
      /// This is used to acquire the reference that has been set on
      /// this output node. Typically this should be null as this node
      /// does not represent anything that actually exists. However
      /// if a namespace reference is set it can be acquired.
      /// </summary>
      /// <returns>
      /// this returns the namespace reference for this node
      /// </returns>
      public String Reference {
         get {
            return reference;
         }
         set {
            this.reference = value;
         }
      }
      //public String GetReference() {
      //   return reference;
      //}
      /// This is used to set the namespace reference for the document.
      /// Setting a reference for the document node has no real effect
      /// as the document node is virtual and is not written to the
      /// resulting XML document that is generated.
      /// </summary>
      /// <param name="reference">
      /// this is the namespace reference added
      /// </param>
      //public void SetReference(String reference) {
      //   this.reference = reference;
      //}
      /// This returns the <c>NamespaceMap</c> for the document.
      /// The namespace map for the document must be null as this will
      /// signify the end of the resolution process for a prefix if
      /// given a namespace reference.
      /// </summary>
      /// <returns>
      /// this will return a null namespace map object
      /// </returns>
      public NamespaceMap Namespaces {
         get {
            return null;
         }
      }
      //public NamespaceMap GetNamespaces() {
      //   return null;
      //}
      /// This is used to acquire the <c>Node</c> that is the
      /// parent of this node. This will return the node that is
      /// the direct parent of this node and allows for siblings to
      /// make use of nodes with their parents if required.
      /// </summary>
      /// <returns>
      /// this will always return null for this output
      /// </returns>
      public OutputNode Parent {
         get {
            return null;
         }
      }
      //public OutputNode GetParent() {
      //   return null;
      //}
      /// To signify that this is the document element this method will
      /// return null. Any object with a handle on an output node that
      /// has been created can check the name to determine its type.
      /// </summary>
      /// <returns>
      /// this returns null for the name of the node
      /// </returns>
      public String Name {
         get {
            return null;
         }
      }
      //public String GetName() {
      //   return null;
      //}
      /// This returns the value that has been set for this document.
      /// The value returned is essentially a dummy value as this node
      /// is never written to the resulting XML document.
      /// </summary>
      /// <returns>
      /// the value that has been set with this document
      /// </returns>
      public String Value {
         get {
            return value;
         }
         set {
            this.value = _value;
         }
      }
      //public String GetValue() {
      //   return value;
      //}
      /// This is used to get the text comment for the element. This can
      /// be null if no comment has been set. If no comment is set on
      /// the node then no comment will be written to the resulting XML.
      /// </summary>
      /// <returns>
      /// this is the comment associated with this element
      /// </returns>
      public String Comment {
         get {
            return comment;
         }
         set {
            this.comment = value;
         }
      }
      //public String GetComment() {
      //   return comment;
      //}
      /// This method is used to determine if this node is the root
      /// node for the XML document. The root node is the first node
      /// in the document and has no sibling nodes. This will return
      /// true although the document node is not strictly the root.
      /// </summary>
      /// <returns>
      /// returns true although this is not really a root
      /// </returns>
      public bool IsRoot() {
         return true;
      }
      /// <summary>
      /// The <c>Mode</c> is used to indicate the output mode
      /// of this node. Three modes are possible, each determines
      /// how a value, if specified, is written to the resulting XML
      /// document. This is determined by the <c>setData</c>
      /// method which will set the output to be CDATA or escaped,
      /// if neither is specified the mode is inherited.
      /// </summary>
      /// <returns>
      /// this returns the mode of this output node object
      /// </returns>
      public Mode Mode {
         get {
            return mode;
         }
         set {
            this.mode = value;
         }
      }
      //public Mode GetMode() {
      //   return mode;
      //}
      /// This is used to set the output mode of this node to either
      /// be CDATA, escaped, or inherited. If the mode is set to data
      /// then any value specified will be written in a CDATA block,
      /// if this is set to escaped values are escaped. If however
      /// this method is set to inherited then the mode is inherited
      /// from the parent node.
      /// </summary>
      /// <param name="mode">
      /// this is the output mode to set the node to
      /// </param>
      //public void SetMode(Mode mode) {
      //   this.mode = mode;
      //}
      /// This method is used for convenience to add an attribute node
      /// to the attribute <c>NodeMap</c>. The attribute added
      /// can be removed from the element by using the node map.
      /// </summary>
      /// <param name="name">
      /// this is the name of the attribute to be added
      /// </param>
      /// <param name="value">
      /// this is the value of the node to be added
      /// </param>
      /// <returns>
      /// this returns the node that has just been added
      /// </returns>
      public OutputNode SetAttribute(String name, String value) {
         return table.Put(name, value);
      }
      /// <summary>
      /// This returns a <c>NodeMap</c> which can be used to add
      /// nodes to this node. The node map returned by this is a dummy
      /// map, as this output node is never written to the XML document.
      /// </summary>
      /// <returns>
      /// returns the node map used to manipulate attributes
      /// </returns>
      public NodeMap<OutputNode> Attributes {
         get {
            return table;
         }
      }
      //public NodeMap<OutputNode> GetAttributes() {
      //   return table;
      //}
      /// This is used to set a text value to the element. This effect
      /// of adding this to the document node will not change what
      /// is actually written to the generated XML document.
      /// </summary>
      /// <param name="value">
      /// this is the text value to add to this element
      /// </param>
      //public void SetValue(String value) {
      //   this.value = value;
      //}
      /// This is used to set a text comment to the element. This will
      /// be written just before the actual element is written. Only a
      /// single comment can be set for each output node written.
      /// </summary>
      /// <param name="comment">
      /// this is the comment to set on the node
      /// </param>
      //public void SetComment(String comment) {
      //   this.comment = comment;
      //}
      /// This is used to set the output mode of this node to either
      /// be CDATA or escaped. If this is set to true the any value
      /// specified will be written in a CDATA block, if this is set
      /// to false the values is escaped. If however this method is
      /// never invoked then the mode is inherited from the parent.
      /// </summary>
      /// <param name="data">
      /// if true the value is written as a CDATA block
      /// </param>
      public bool Data {
         set {
            if(value) {
               mode = Mode.DATA;
            } else {
               mode = Mode.ESCAPE;
            }
         }
      }
      //public void SetData(bool data) {
      //   if(data) {
      //      mode = Mode.DATA;
      //   } else {
      //      mode = Mode.ESCAPE;
      //   }
      //}
      /// This is used to create a child element within the element that
      /// this object represents. When a new child is created with this
      /// method then the previous child is committed to the document.
      /// The created <c>OutputNode</c> object can be used to add
      /// attributes to the child element as well as other elements.
      /// </summary>
      /// <param name="name">
      /// this is the name of the child element to create
      /// </param>
      public OutputNode GetChild(String name) {
         return writer.WriteElement(this, name);
      }
      /// <summary>
      /// This is used to remove any uncommitted changes. Removal of an
      /// output node can only be done if it has no siblings and has
      /// not yet been committed. If the node is committed then this
      /// will throw an exception to indicate that it cannot be removed.
      /// </summary>
      public void Remove() {
         if(stack.isEmpty()) {
            throw new NodeException("No root node");
         }
         stack.Bottom().Remove();
      }
      /// <summary>
      /// This will commit this element and any uncommitted elements
      /// elements that are decendents of this node. For instance if
      /// any child or grand child remains open under this element
      /// then those elements will be closed before this is closed.
      /// </summary>
      /// or if a root element has not yet been created
      public void Commit() {
         if(stack.isEmpty()) {
            throw new NodeException("No root node");
         }
         stack.Bottom().Commit();
      }
      /// <summary>
      /// This is used to determine whether this node has been committed.
      /// This will return true if no root element has been created or
      /// if the root element for the document has already been commited.
      /// </summary>
      /// <returns>
      /// true if the node is committed or has not been created
      /// </returns>
      public bool IsCommitted() {
         return stack.isEmpty();
      }
   }
}
