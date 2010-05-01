#region License
//
// OutputNode.cs July 2006
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
   /// The <c>OutputNode</c> object is used to represent a cursor
   /// which can be used to write XML elements and attributes. Each of
   /// the output node objects represents a element, and can be used
   /// to add attributes to that element as well as child elements.
   /// </summary>
   public interface OutputNode : Node {

      /// <summary>
      /// This is used to acquire the namespace URI reference associated
      /// with this node. Although it is recommended that the namespace
      /// reference is a URI it does not have to be, it can be any unique
      /// identifier that can be used to distinguish the qualified names.
      /// </summary>
      /// <returns>
      /// This returns the namespace URI reference for this.
      /// </returns>
      String Reference {
         get;
         set;
      }

      /// The <c>Mode</c> is used to indicate the output mode
      /// of this node. Three modes are possible, each determines
      /// how a value, if specified, is written to the resulting XML
      /// document. This is determined by the <c>setData</c>
      /// method which will set the output to be CDATA or escaped,
      /// if neither is specified the mode is inherited.
      /// </summary>
      /// <returns>
      /// This returns the mode of this output node object.
      /// </returns>
      Mode Mode {
         get;
         set;
      }

      /// This is used to set the output mode of this node to either
      /// be CDATA or escaped. If this is set to true the any value
      /// specified will be written in a CDATA block, if this is set
      /// to false the values is escaped. If however this method is
      /// never invoked then the mode is inherited from the parent.
      /// </summary>
      /// <param name="data">
      /// If true the value is written as a CDATA block.
      /// </param>
      bool Data {
         set;
      }

      /// This is used to get the text comment for the element. This can
      /// be null if no comment has been set. If no comment is set on
      /// the node then no comment will be written to the resulting XML.
      /// </summary>
      /// <returns>
      /// This is the comment associated with this element.
      /// </returns>
      String Comment {
         get;
         set;
      }

      /// This is used to set a text value to the element. This should
      /// be added to the element if the element contains no child
      /// elements. If the value cannot be added an exception is thrown.
      /// </summary>
      /// <param name="value">
      /// This is the text value to add to this element.
      /// </param>
      String Value {
         get;
         set;
      }

      /// <summary>
      /// This is used to acquire the <c>Node</c> that is the
      /// parent of this node. This will return the node that is
      /// the direct parent of this node and allows for siblings to
      /// make use of nodes with their parents if required.
      /// </summary>
      /// <returns>
      /// This returns the parent node for this node.
      /// </returns>
      OutputNode Parent {
         get;
      }

      /// This returns the <c>NamespaceMap</c> for this node. Only
      /// an element can have namespaces, so if this node represents an
      /// attribute the elements namespaces will be provided when this is
      /// requested. By adding a namespace it becomes in scope for the
      /// current element all all child elements of that element.
      /// </summary>
      /// <returns>
      /// This returns the namespaces associated with the node.
      /// </returns>
      NamespaceMap Namespaces {
         get;
      }

      /// <summary>
      /// This returns a <c>NodeMap</c> which can be used to add
      /// nodes to the element before that element has been committed.
      /// Nodes can be removed or added to the map and will appear as
      /// attributes on the written element when it is committed.
      /// </summary>
      /// <returns>
      /// Returns the node map used to manipulate attributes.
      /// </returns>
      NodeMap<OutputNode> Attributes {
         get;
      }

      /// This is used to acquire the prefix for this output node. If
      /// the output node is an element then this will search its parent
      /// nodes until the prefix that is currently in scope is found.
      /// If however this node is an attribute then the hierarchy of
      /// nodes is not searched as attributes to not inherit namespaces.
      /// </summary>
      /// <returns>
      /// This returns the prefix associated with this node.
      /// </returns>
      String GetPrefix();

      /// This is used to acquire the prefix for this output node. If
      /// the output node is an element then this will search its parent
      /// nodes until the prefix that is currently in scope is found.
      /// If however this node is an attribute then the hierarchy of
      /// nodes is not searched as attributes to not inherit namespaces.
      /// </summary>
      /// <param name="inherit">
      /// If there is no explicit prefix then inherit.
      /// </param>
      /// <returns>
      /// This returns the prefix associated with this node.
      /// </returns>
      String GetPrefix(bool inherit);

      /// This method is used for convenience to add an attribute node
      /// to the attribute <c>NodeMap</c>. The attribute added
      /// can be removed from the element by using the node map.
      /// </summary>
      /// <param name="name">
      /// This is the name of the attribute to be added.
      /// </param>
      /// <param name="value">
      /// This is the value of the node to be added.
      /// </param>
      /// <returns>
      /// This returns the node that has just been added.
      /// </returns>
      OutputNode SetAttribute(String name, String value);

      /// This is used to create a child element within the element that
      /// this object represents. When a new child is created with this
      /// method then the previous child is committed to the document.
      /// The created <c>OutputNode</c> object can be used to add
      /// attributes to the child element as well as other elements.
      /// </summary>
      /// <param name="name">
      /// This is the name of the child element to create.
      /// </param>
      OutputNode GetChild(String name);

      /// <summary>
      /// This is used to remove any uncommitted changes. Removal of an
      /// output node can only be done if it has no siblings and has
      /// not yet been committed. If the node is committed then this
      /// will throw an exception to indicate that it cannot be removed.
      /// </summary>
      void Remove();

      /// <summary>
      /// The <c>commit</c> method is used flush and commit any
      /// child nodes that have been created by this node. This allows
      /// the output to be completed when building of the XML document
      /// has been completed. If output fails an exception is thrown.
      /// </summary>
      void Commit();

      /// <summary>
      /// This is used to determine whether the node has been committed.
      /// If the node has been committed, then this will return true.
      /// When committed the node can no longer produce chile nodes.
      /// </summary>
      /// <returns>
      /// True if this node has already been committed.
      /// </returns>
      bool IsCommitted();

      /// <summary>
      /// This method is used to determine if this node is the root
      /// node for the XML document. The root node is the first node
      /// in the document and has no sibling nodes. This is false
      /// if the node has a parent node or a sibling node.
      /// </summary>
      /// <returns>
      /// True if this is the root node within the document.
      /// </returns>
      bool IsRoot();
   }
}
