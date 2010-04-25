#region License
//
// OutputAttribute.cs July 2006
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
   /// The <c>OutputAttribute</c> object is used to represent a
   /// node added to the output node map. It represents a simple name
   /// value pair that is used as an attribute by an output element.
   /// This shares its namespaces with the parent element so that any
   /// namespaces added to the attribute are actually added to the
   /// parent element, which ensures correct scoping.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Stream.Node
   /// </seealso>
   class OutputAttribute : OutputNode {
      /// <summary>
      /// This contains the namespaces for the parent element.
      /// </summary>
      private NamespaceMap scope;
      /// <summary>
      /// Represents the output node that this node requires.
      /// </summary>
      private OutputNode source;
      /// <summary>
      /// Represents the namespace reference for this node.
      /// </summary>
      private String reference;
      /// <summary>
      /// Represents the name of this node object instance.
      /// </summary>
      private String name;
      /// <summary>
      /// Represents the value of this node object instance.
      /// </summary>
      private String value;
      /// <summary>
      /// Constructor for the <c>OutputAttribute</c> object. This
      /// is used to create a simple name value pair attribute holder.
      /// </summary>
      /// <param name="name">
      /// this is the name that is used for the node
      /// </param>
      /// <param name="value">
      /// this is the value used for the node
      /// </param>
      public OutputAttribute(OutputNode source, String name, String value) {
         this.scope = source.Namespaces;
         this.source = source;
         this.value = value;
         this.name = name;
      }
      /// <summary>
      /// Returns the value for the node that this represents. This
      /// is a modifiable property for the node and can be changed.
      /// When set this forms the value the attribute contains in
      /// the parent XML element, which is written in quotations.
      /// </summary>
      /// <returns>
      /// the name of the value for this node instance
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
      /// This is used to set a text value to the attribute. This should
      /// be added to the attribute if the attribute is to be written
      /// to the parent element. Without a value this is invalid.
      /// </summary>
      /// <param name="value">
      /// this is the text value to add to this attribute
      /// </param>
      //public void SetValue(String value) {
      //   this.value = value;
      //}
      /// Returns the name of the node that this represents. This is
      /// an immutable property and should not change for any node.
      /// If this is null then the attribute will not be added to
      /// the node map, all attributes must have a valid key.
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
           return source;
         }
      }
      //public OutputNode GetParent() {
      //  return source;
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
            return new OutputNodeMap(this);
         }
      }
      //public NodeMap<OutputNode> GetAttributes() {
      //   return new OutputNodeMap(this);
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
         return null;
      }
      /// <summary>
      /// This is used to get the text comment for the element. This can
      /// be null if no comment has been set. If no comment is set on
      /// the node then no comment will be written to the resulting XML.
      /// </summary>
      /// <returns>
      /// this is the comment associated with this element
      /// </returns>
      public String Comment {
         get {
            return null;
         }
         set {
            return;
         }
      }
      //public String GetComment() {
      //   return null;
      //}
      /// This is used to set a text comment to the element. This will
      /// be written just before the actual element is written. Only a
      /// single comment can be set for each output node written.
      /// </summary>
      /// <param name="comment">
      /// this is the comment to set on the node
      /// </param>
      //public void SetComment(String comment) {
      //   return;
      //}
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
            return Mode.INHERIT;
         }
         set {
            return;
         }
      }
      //public Mode GetMode() {
      //   return Mode.INHERIT;
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
      //   return;
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
            return;
         }
      }
      //public void SetData(bool data) {
      //   return;
      //}
      /// This is used to acquire the prefix for this output node. If
      /// the output node is an element then this will search its parent
      /// nodes until the prefix that is currently in scope is found.
      /// If however this node is an attribute then the hierarchy of
      /// nodes is not searched as attributes to not inherit namespaces.
      /// </summary>
      /// <returns>
      /// this returns the prefix associated with this node
      /// </returns>
      public String Prefix {
         get {
            return scope.Get(reference);
         }
      }
      //public String GetPrefix() {
      //   return scope.Get(reference);
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
         return scope.Get(reference);
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
      /// this returns the node that has just been added
      /// </returns>
      public OutputNode SetAttribute(String name, String value) {
         return null;
      }
      /// <summary>
      /// This is used to remove any uncommitted changes. Removal of an
      /// output node can only be done if it has no siblings and has
      /// not yet been committed. If the node is committed then this
      /// will throw an exception to indicate that it cannot be removed.
      /// </summary>
      public void Remove() {
         return;
      }
      /// <summary>
      /// The <c>commit</c> method is used flush and commit any
      /// child nodes that have been created by this node. This allows
      /// the output to be completed when building of the XML document
      /// has been completed. If output fails an exception is thrown.
      /// </summary>
      public void Commit() {
         return;
      }
      /// <summary>
      /// This method is used to determine if this node is the root
      /// node for the XML document. The root node is the first node
      /// in the document and has no sibling nodes. This is false
      /// if the node has a parent node or a sibling node.
      /// </summary>
      /// <returns>
      /// true if this is the root node within the document
      /// </returns>
      public bool IsRoot() {
         return false;
      }
      /// <summary>
      /// This is used to determine whether the node has been committed.
      /// If the node has been committed, then this will return true.
      /// When committed the node can no longer produce child nodes.
      /// </summary>
      /// <returns>
      /// true if this node has already been committed
      /// </returns>
      public bool IsCommitted() {
         return true;
      }
      /// <summary>
      /// This is used to acquire the name and value of the attribute.
      /// Implementing this method ensures that debugging the output
      /// node is simplified as it is possible to get the actual value.
      /// </summary>
      /// <returns>
      /// this returns the details of this output node
      /// </returns>
      public String ToString() {
          return String.format("attribute %s='%s'", name, value);
      }
   }
}
