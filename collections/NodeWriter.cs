#region License
//
// NodeWriter.cs July 2006
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
using System.Collections;
using System.IO;
using System;
#endregion

namespace SimpleFramework.Xml.Stream {

   /// <summary>
   /// The <c>NodeWriter</c> object is used to create a writer that
   /// will write well formed indented XML for a given output node. This
   /// is used in the serialization process to convert an object into an
   /// XML document.
   /// <p>
   /// This keeps a stack of all the active output nodes so that if an
   /// output node has been committed it cannot write any further data to
   /// the XML document. This allows all output nodes to be independent
   /// of each other as the node write organizes the write access.
   /// </summary>
   class NodeWriter {

      /// <summary>
      /// Represents the stack of output nodes that are not yet ended.
      /// </summary>
      private readonly OutputStack stack;

      /// <summary>
      /// Formatter used to indent the XML elements and escape text.
      /// </summary>
      private readonly Formatter writer;

      /// <summary>
      /// Contains the set of as yet uncommitted elements blocks.
      /// </summary>
      private readonly ArrayList active;

      /// <summary>
      /// This determines if we expand the namespace prefixes.
      /// </summary>
      private readonly bool verbose;

      /// <summary>
      /// Constructor for the <c>NodeWriter</c> object. This will
      /// create the object that is used to control an output elements
      /// access to the generated XML document. This keeps a stack of
      /// active and uncommitted elements.
      /// </summary>
      /// <param name="result">
      /// This is the output for the resulting document.
      /// </param>
      /// <param name="format">
      /// This is used to format the generated document.
      /// </param>
      public NodeWriter(TextWriter result, Format format) : this(result, format, false) {
      }

      /// <summary>
      /// Constructor for the <c>NodeWriter</c> object. This will
      /// create the object that is used to control an output elements
      /// access to the generated XML document. This keeps a stack of
      /// active and uncommitted elements.
      /// </summary>
      /// <param name="result">
      /// This is the output for the resulting document.
      /// </param>
      /// <param name="format">
      /// This is used to format the generated document.
      /// </param>
      /// <param name="verbose">
      /// This determines if we expand the namespaces.
      /// </param>
      private NodeWriter(TextWriter result, Format format, bool verbose) {
         this.writer = new Formatter(result, format);
         this.active = new ArrayList();
         this.stack = new OutputStack(active);
         this.verbose = verbose;
      }

      /// <summary>
      /// This is used to acquire the root output node for the document.
      /// This will create an empty node that can be used to generate
      /// the root document element as a child to the document.
      /// <p>
      /// Depending on whether or not an encoding has been specified
      /// this method will write a prolog to the generated XML document.
      /// Each prolog written uses an XML version of "1.0".
      /// </summary>
      /// <returns>
      /// This returns an output element for the document.
      /// </returns>
      public OutputNode WriteRoot() {
         OutputDocument root = new OutputDocument(this, stack);

         if(stack.IsEmpty()) {
            writer.WriteProlog();
         }
         return root;
      }

      /// <summary>
      /// This method is used to determine if the node is the root
      /// node for the XML document. The root node is the first node
      /// in the document and has no sibling nodes. This is false
      /// if the node has a parent node or a sibling node.
      /// </summary>
      /// <param name="node">
      /// This is the node that is check as the root.
      /// </param>
      /// <returns>
      /// True if the node is the root node for the document.
      /// </returns>
      public bool IsRoot(OutputNode node) {
         return stack.Bottom() == node;
      }

      /// <summary>
      /// This is used to determine if the specified node has been
      /// committed. If this returns tre then the node is committed
      /// and cannot be used to add further child elements.
      /// </summary>
      /// <param name="node">
      /// This is the node to check for commit status.
      /// </param>
      /// <returns>
      /// This returns true if the node has been committed.
      /// </returns>
      public bool IsCommitted(OutputNode node) {
         return !active.Contains(node);
      }

      /// <summary>
      /// This method is used to commit all nodes on the stack up to and
      /// including the specified node. This will effectively create end
      /// tags for any nodes that are currently open up to the specified
      /// element. Once committed the output node can no longer be used
      /// to create child elements, nor can any of its child elements.
      /// </summary>
      /// <param name="parent">
      /// This is the node that is to be committed.
      /// </param>
      public void Commit(OutputNode parent) {
         if(stack.Contains(parent)) {
            OutputNode top = stack.Top();

            if(!IsCommitted(top)) {
               WriteStart(top);
            }
            while(stack.Top() != parent) {
               WriteEnd(stack.Pop());
            }
            WriteEnd(parent);
            stack.Pop();
         }
      }

      /// <summary>
      /// This method is used to remove the output node from the output
      /// buffer if that node has not yet been committed. This allows a
      /// node that has been created to be deleted, ensuring that it
      /// will not affect the resulting XML document structure.
      /// </summary>
      /// <param name="node">
      /// This is the output node that is to be removed.
      /// </param>
      public void Remove(OutputNode node) {
         if(stack.Top() != node) {
            throw new NodeException("Cannot remove node");
         }
         stack.Pop();
      }

      /// <summary>
      /// This is used to create a new element under the specified node.
      /// This will effectively commit all nodes that are open until this
      /// node is encountered. Once the specified node is encountered on
      /// the stack a new element is created with the specified name.
      /// </summary>
      /// <param name="parent">
      /// This is the node that is to be committed.
      /// </param>
      /// <param name="name">
      /// This is the name of the start element to create.
      /// </param>
      /// <returns>
      /// This will return a child node for the given parent.
      /// </returns>
      public OutputNode WriteElement(OutputNode parent, String name) {
         if(stack.IsEmpty()) {
            return WriteStart(parent, name);
         }
         if(stack.Contains(parent)) {
            OutputNode top = stack.Top();

            if(!IsCommitted(top)) {
               WriteStart(top);
            }
            while(stack.Top() != parent) {
               WriteEnd(stack.Pop());
            }
            return WriteStart(parent, name);
         }
         return null;
      }

      /// <summary>
      /// This is used to begin writing on a new XML element. This is
      /// typically done by writing any comments required. This will
      /// create an output node of the specified name before writing
      /// the comment, if any exists. Once the comment has been written
      /// the node is pushed on to the head of the output node stack.
      /// </summary>
      /// <param name="parent">
      /// This is the parent node to the next output node.
      /// </param>
      /// <param name="name">
      /// This is the name of the node that is to be created.
      /// </param>
      /// <returns>
      /// This returns an output node used for writing content.
      /// </returns>
      public OutputNode WriteStart(OutputNode parent, String name) {
         OutputNode node = new OutputElement(parent, this, name);

         if(name == null) {
            throw new NodeException("Can not have a null name");
         }
         return stack.Push(node);
      }

      /// <summary>
      /// This is used to write the XML element to the underlying buffer.
      /// The element is written in the order of element prefix and name
      /// followed by the attributes an finally the namespaces for the
      /// element. Once this is finished the element is committed to
      /// </summary>
      /// <param name="node">
      /// This is the node that is to be fully written.
      /// </param>
      public void WriteStart(OutputNode node) {
         WriteComment(node);
         WriteName(node);
         WriteAttributes(node);
         WriteNamespaces(node);
      }

      /// <summary>
      /// This is used to write a comment to the document. Comments
      /// appear just before the element name, this allows an logical
      /// association between the comment and the node to be made.
      /// </summary>
      /// <param name="node">
      /// This is the node that is to have its name written.
      /// </param>
      public void WriteComment(OutputNode node) {
         String comment = node.Comment;

         if(comment != null) {
            writer.WriteComment(comment);
         }
      }
      /// <summary>
      /// This is used to write a new start element to the resulting XML
      /// document. This will create an output node of the specified
      /// name before writing the start tag. Once the tag is written
      /// the node is pushed on to the head of the output node stack.
      /// </summary>
      /// <param name="node">
      /// This is the node that is to have its name written.
      /// </param>
      public void WriteName(OutputNode node) {
         String prefix = node.GetPrefix(verbose);
         String name = node.Name;

         if(name != null) {
            writer.WriteStart(name, prefix);
         }
      }
      /// <summary>
      /// This is used to write a new end element to the resulting XML
      /// document. This will acquire the name and value of the given
      /// node, if the node has a value that is written. Finally a new
      /// end tag is written to the document and the output is flushed.
      /// </summary>
      /// <param name="node">
      /// This is the node that is to have an end tag.
      /// </param>
      public void WriteEnd(OutputNode node) {
         Mode mode = node.Mode;

         foreach(OutputNode next in stack) {
            if(mode != Mode.Inherit) {
               break;
            }
            mode = next.Mode;
         }
         WriteEnd(node, mode);
      }

      /// <summary>
      /// This is used to write a new end element to the resulting XML
      /// document. This will acquire the name and value of the given
      /// node, if the node has a value that is written. Finally a new
      /// end tag is written to the document and the output is flushed.
      /// </summary>
      /// <param name="node">
      /// This is the node that is to have an end tag.
      /// </param>
      public void WriteEnd(OutputNode node, Mode mode) {
         String value = node.Value;

         if(value != null) {
            writer.WriteText(value, mode);
         }
         String prefix = node.GetPrefix(verbose);
         String name = node.Name;

         if(name != null) {
            writer.WriteEnd(name, prefix);
            writer.Flush();
         }
      }
      /// <summary>
      /// This is used to write the attributes of the specified node to
      /// the output. This will iterate over each node entered on to
      /// the node. Once written the node is considered inactive.
      /// </summary>
      /// <param name="node">
      /// This is the node to have is attributes written.
      /// </param>
      public void WriteAttributes(OutputNode node) {
         NodeMap<OutputNode> map = node.Attributes;

         foreach(String name in map.Names) {
            OutputNode entry = map.Get(name);
            String prefix = entry.GetPrefix(verbose);
            String value = entry.Value;

            writer.WriteAttribute(name, value, prefix);
         }
         active.Remove(node);
      }
      /// <summary>
      /// This is used to write the namespaces of the specified node to
      /// the output. This will iterate over each namespace entered on
      /// to the node. Once written the node is considered qualified.
      /// </summary>
      /// <param name="node">
      /// This is the node to have is attributes written.
      /// </param>
      public void WriteNamespaces(OutputNode node) {
         NamespaceMap map = node.Namespaces;

         foreach(String name in map.References) {
            String prefix = map.Get(name);
            writer.WriteNamespace(name, prefix);
         }
      }
   }
}
