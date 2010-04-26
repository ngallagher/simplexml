#region License
//
// InputNode.cs July 2006
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
   /// The <c>InputNode</c> object represents an iterator for the
   /// elements within an element. This allows the input node object to
   /// become a self contained iterator for an element and its children.
   /// Each child taken from the input node object, is itself an input
   /// node, and can be used to explore its sub elements without having
   /// any affect on its outer elements.
   /// </summary>
   public interface InputNode : Node {
      /// <summary>
      /// This method is used to determine if this node is the root
      /// node for the XML document. The root node is the first node
      /// in the document and has no sibling nodes. This is false
      /// if the node has a parent node or a sibling node.
      /// </summary>
      /// <returns>
      /// true if this is the root node within the document
      /// </returns>
      bool IsRoot();
      /// <summary>
      /// This is used to determine if this node is an element. This
      /// allows users of the framework to make a distinction between
      /// nodes that represent attributes and nodes that represent
      /// elements. This is particularly useful given that attribute
      /// nodes do not maintain a node map of attributes.
      /// </summary>
      /// <returns>
      /// this returns true if the node is an element node
      /// </returns>
      bool IsElement();
      /// <summary>
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
         get;
      }
      //public String GetPrefix();
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
         get;
      }
      //public String GetReference();
      /// This provides the position of this node within the document.
      /// This allows the user of this node to report problems with
      /// the location within the document, allowing the XML to be
      /// debugged if it does not match the class schema.
      /// </summary>
      /// <returns>
      /// this returns the position of the XML read cursor
      /// </returns>
      public Position Position {
         get;
      }
      //public Position GetPosition();
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
      InputNode GetAttribute(String name);
      /// <summary>
      /// This returns a map of the attributes contained within the
      /// element. If no elements exist within the element then this
      /// returns an empty map.
      /// </summary>
      /// <returns>
      /// this returns a map of attributes for the element
      /// </returns>
      public NodeMap<InputNode> Attributes {
         get;
      }
      //public NodeMap<InputNode> GetAttributes();
      /// This is used to acquire the <c>Node</c> that is the
      /// parent of this node. This will return the node that is
      /// the direct parent of this node and allows for siblings to
      /// make use of nodes with their parents if required.
      /// </summary>
      /// <returns>
      /// this returns the parent node for this node
      /// </returns>
      public InputNode Parent {
         get;
      }
      //public InputNode GetParent();
      /// This is used to return the source object for this node. This
      /// is used primarily as a means to determine which XML provider
      /// is parsing the source document and producing the nodes. It
      /// is useful to be able to determine the XML provider like this.
      /// </summary>
      /// <returns>
      /// this returns the source of this input node
      /// </returns>
      public Object Source {
         get;
      }
      //public Object GetSource();
      /// This returns the next child element within this element if
      /// one exists. If all children have been read, or if there are
      /// no child elements for this element then this returns null.
      /// </summary>
      /// <returns>
      /// this returns an input node for the next child
      /// </returns>
      public InputNode Next {
         get;
      }
      //public InputNode GetNext();
      /// This returns the next child in this element if that child
      /// has the name provided. If the next child element in this
      /// node does not have the name given then null is returned.
      /// </summary>
      /// <param name="name">
      /// this is the name of the next child element
      /// </param>
      /// <returns>
      /// the next element if it has the name specified
      /// </returns>
      InputNode GetNext(String name);
      /// <summary>
      /// This method is used to skip all child elements from this
      /// element. This allows elements to be effectively skipped such
      /// that when parsing a document if an element is not required
      /// then that element can be completely removed from the XML.
      /// </summary>
      void Skip();
      /// <summary>
      /// This is used to determine if this input node is empty. An
      /// empty node is one with no attributes or children. This can
      /// be used to determine if a given node represents an empty
      /// entity, with which no extra data can be extracted.
      /// </summary>
      /// <returns>
      /// this returns true if the node is an empty element
      /// </returns>
      bool IsEmpty();
   }
}
