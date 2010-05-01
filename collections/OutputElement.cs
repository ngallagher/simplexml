#region License
//
// OutputElement.cs July 2006
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
   /// The <c>OutputElement</c> object represents an XML element.
   /// Attributes can be added to this before ant child element has been
   /// acquired from it. Once a child element has been acquired the
   /// attributes will be written an can no longer be manipulated, the
   /// same applies to any text value set for the element.
   /// </summary>
   class OutputElement : OutputNode {
      /// <summary>
      /// Represents the attributes that have been set for the element.
      /// </summary>
      private OutputNodeMap table;
      /// <summary>
      /// This is the namespace map that contains the namespaces.
      /// </summary>
      private NamespaceMap scope;
      /// <summary>
      /// Used to write the start tag and attributes for the document.
      /// </summary>
      private NodeWriter writer;
      /// <summary>
      /// This is the parent XML element to this output node.
      /// </summary>
      private OutputNode parent;
      /// <summary>
      /// This is the namespace reference URI associated with this.
      /// </summary>
      private String reference;
      /// <summary>
      /// This is the comment that has been set on this element.
      /// </summary>
      private String comment;
      /// <summary>
      /// Represents the value that has been set for the element.
      /// </summary>
      private String value;
      /// <summary>
      /// Represents the name of the element for this output node.
      /// </summary>
      private String name;
      /// <summary>
      /// This is the output mode that this element object is using.
      /// </summary>
      private Mode mode;
      /// <summary>
      /// Constructor for the <c>OutputElement</c> object. This is
      /// used to create an output element that can create elements for
      /// an XML document. This requires the writer that is used to
      /// generate the actual document and the name of this node.
      /// </summary>
      /// <param name="parent">
      /// this is the parent node to this output node
      /// </param>
      /// <param name="writer">
      /// this is the writer used to generate the file
      /// </param>
      /// <param name="name">
      /// this is the name of the element this represents
      /// </param>
      public OutputElement(OutputNode parent, NodeWriter writer, String name) {
         this.scope = new PrefixResolver(parent);
         this.table = new OutputNodeMap(this);
         this.mode = Mode.Inherit;
         this.writer = writer;
         this.parent = parent;
         this.name = name;
      }
      /// <summary>
      /// This is used to acquire the prefix for this output node. This
      /// will search its parent nodes until the prefix that is currently
      /// in scope is found.  If a prefix is not found in the parent
      /// nodes then a prefix in the current nodes namespace mappings is
      /// searched, failing that the prefix returned is null.
      /// </summary>
      /// <returns>
      /// this returns the prefix associated with this node
      /// </returns>
      public String GetPrefix() {
         return GetPrefix(true);
      }
      //public String GetPrefix() {
      //   return GetPrefix(true);
      //}
      /// This is used to acquire the prefix for this output node. If
      /// the output node is an element then this will search its parent
      /// nodes until the prefix that is currently in scope is found.
      /// If however this node is an attribute then the hierarchy of
      /// nodes is not searched as attributes to not inherit namespaces.
      /// </summary>
      /// <param name="inherit">
      /// if there is no explicit prefix then inherit
      /// </param>
      /// <returns>
      /// this returns the prefix associated with this node
      /// </returns>
      public String GetPrefix(bool inherit) {
         String prefix = scope.Get(reference);
         if(inherit) {
            if(prefix == null) {
               return parent.GetPrefix();
            }
         }
         return prefix;
      }
      /// <summary>
      /// This is used to acquire the namespace URI reference associated
      /// with this node. Although it is recommended that the namespace
      /// reference is a URI it does not have to be, it can be any unique
      /// identifier that can be used to distinguish the qualified names.
      /// </summary>
      /// <returns>
      /// this returns the namespace URI reference for this
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
      /// This is used to set the reference for the node. Setting the
      /// reference implies that the node is a qualified node within the
      /// XML document. Both elements and attributes can be qualified.
      /// Depending on the prefix set on this node or, failing that, any
      /// parent node for the reference, the element will appear in the
      /// XML document with that string prefixed to the node name.
      /// </summary>
      /// <param name="reference">
      /// this is used to set the reference for the node
      /// </param>
      //public void SetReference(String reference) {
      //   this.reference = reference;
      //}
      /// This returns the <c>NamespaceMap</c> for this node. Only
      /// an element can have namespaces, so if this node represents an
      /// attribute the elements namespaces will be provided when this is
      /// requested. By adding a namespace it becomes in scope for the
      /// current element all all child elements of that element.
      /// </summary>
      /// <returns>
      /// this returns the namespaces associated with the node
      /// </returns>
      public NamespaceMap Namespaces {
         get {
            return scope;
         }
      }
      //public NamespaceMap GetNamespaces() {
      //   return scope;
      //}
      /// This is used to acquire the <c>Node</c> that is the
      /// parent of this node. This will return the node that is
      /// the direct parent of this node and allows for siblings to
      /// make use of nodes with their parents if required.
      /// </summary>
      /// <returns>
      /// this returns the parent node for this node
      /// </returns>
      public OutputNode Parent {
         get {
            return parent;
         }
      }
      //public OutputNode GetParent() {
      //   return parent;
      //}
      /// Returns the name of the node that this represents. This is
      /// an immutable property and cannot be changed. This will be
      /// written as the tag name when this has been committed.
      /// </summary>
      /// <returns>
      /// returns the name of the node that this represents
      /// </returns>
      public String Name {
         get {
            return name;
         }
      }
      //public String GetName() {
      //   return name;
      //}
      /// Returns the value for the node that this represents. This
      /// is a modifiable property for the node and can be changed,
      /// however once committed any change will be irrelevant.
      /// </summary>
      /// <returns>
      /// the name of the value for this node instance
      /// </returns>
      public String Value {
         get {
            return value;
         }
         set {
            this.value = value;
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
      /// in the document and has no sibling nodes. This is false
      /// if the node has a parent node or a sibling node.
      /// </summary>
      /// <returns>
      /// true if this is the root node within the document
      /// </returns>
      public bool IsRoot() {
         return writer.IsRoot(this);
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
      /// This returns a <c>NodeMap</c> which can be used to add
      /// nodes to the element before that element has been committed.
      /// Nodes can be removed or added to the map and will appear as
      /// attributes on the written element when it is committed.
      /// </summary>
      /// <returns>
      /// returns the node map used to manipulate attributes
      /// </returns>
      public NodeMap<OutputNode> Attributes {
         get {
            return table;
         }
      }
      //public OutputNodeMap GetAttributes() {
      //   return table;
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
      /// This is used to set a text value to the element. This should
      /// be added to the element if the element contains no child
      /// elements. If the value cannot be added an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// this is the text value to add to this element
      /// </param>
      //public void SetValue(String value) {
      //   this.value = value;
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
               mode = Mode.Data;
            } else {
               mode = Mode.Escape;
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
      /// This method is used for convinience to add an attribute node
      /// to the attribute <c>NodeMap</c>. The attribute added
      /// can be removed from the element by useing the node map.
      /// </summary>
      /// <param name="name">
      /// this is the name of the attribute to be added
      /// </param>
      /// <param name="value">
      /// this is the value of the node to be added
      /// </param>
      /// <returns>
      /// this will return the attribute that was just set
      /// </returns>
      public OutputNode SetAttribute(String name, String value) {
         return table.Put(name, value);
      }
      /// <summary>
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
         writer.Remove(this);
      }
      /// <summary>
      /// This will commit this element and any uncommitted elements
      /// elements that are decendents of this node. For instance if
      /// any child or grand child remains open under this element
      /// then those elements will be closed before this is closed.
      /// </summary>
      public void Commit() {
         writer.Commit(this);
      }
      /// <summary>
      /// This is used to determine whether this node has been committed.
      /// If the node is committed then no further child elements can
      /// be created from this node instance. A node is considered to
      /// be committed if a parent creates another child element or if
      /// the <c>commit</c> method is invoked.
      /// </summary>
      /// <returns>
      /// true if the node has been committed
      /// </returns>
      public bool IsCommitted() {
         return writer.IsCommitted(this);
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
         return String.Format("element %s", name);
      }
   }
}
