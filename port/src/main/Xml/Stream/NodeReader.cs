#region License
//
// NodeReader.cs July 2006
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
   /// The <c>NodeReader</c> object is used to read elements from
   /// the specified XML event reader. This reads input node objects
   /// that represent elements within the source XML document. This will
   /// allow details to be read using input node objects, as long as
   /// the end elements for those input nodes have not been ended.
   /// <p>
   /// For example, if an input node represented the root element of a
   /// document then that input node could read all elements within the
   /// document. However, if the input node represented a child element
   /// then it would only be able to read its children.
   /// </summary>
   class NodeReader {
      /// <summary>
      /// Represents the XML event reader used to read all elements.
      /// </summary>
      private readonly EventReader reader;
      /// <summary>
      /// This stack enables the reader to keep track of elements.
      /// </summary>
      private readonly InputStack stack;
      /// <summary>
      /// Constructor for the <c>NodeReader</c> object. This is used
      /// to read XML events a input node objects from the event reader.
      /// </summary>
      /// <param name="reader">
      /// this is the event reader for the XML document
      /// </param>
      public NodeReader(EventReader reader) {
         this.stack = new InputStack();
         this.reader = reader;
      }
      /// <summary>
      /// Returns the root input node for the document. This is returned
      /// only if no other elements have been read. Once the root element
      /// has been read from the event reader this will return null.
      /// </summary>
      /// <returns>
      /// this returns the root input node for the document
      /// </returns>
      public InputNode ReadRoot() {
         if(stack.IsEmpty()) {
            return ReadElement(null);
         }
         return null;
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
      public bool IsRoot(InputNode node) {
         return stack.bottom() == node;
      }
      /// <summary>
      /// Returns the next input node from the XML document, if it is a
      /// child element of the specified input node. This essentially
      /// determines whether the end tag has been read for the specified
      /// node, if so then null is returned. If however the specified
      /// node has not had its end tag read then this returns the next
      /// element, if that element is a child of the that node.
      /// </summary>
      /// <param name="from">
      /// this is the input node to read with
      /// </param>
      /// <returns>
      /// this returns the next input node from the document
      /// </returns>
      public InputNode ReadElement(InputNode from) {
         if(!stack.IsRelevant(from)) {
            return null;
         }
         EventNode event = reader.Next();
         while(event != null) {
            if(event.isEnd()) {
               if(stack.pop() == from) {
                  return null;
               }
            } else if(event.isStart()) {
               return ReadStart(from, event);
            }
            event = reader.Next();
         }
         return null;
      }
      /// <summary>
      /// Returns the next input node from the XML document, if it is a
      /// child element of the specified input node. This essentially
      /// the same as the <c>ReadElement(InputNode)</c> object
      /// except that this will not read the element if it does not have
      /// the name specified. This essentially acts as a peak function.
      /// </summary>
      /// <param name="from">
      /// this is the input node to read with
      /// </param>
      /// <param name="name">
      /// this is the name expected from the next element
      /// </param>
      /// <returns>
      /// this returns the next input node from the document
      /// </returns>
      public InputNode ReadElement(InputNode from, String name) {
         if(!stack.IsRelevant(from)) {
            return null;
        }
        EventNode event = reader.Peek();
        while(event != null) {
           if(event.isEnd()) {
              if(stack.top() == from) {
                 return null;
              } else {
                 stack.pop();
              }
           } else if(event.isStart()) {
              if(IsName(event, name)) {
                 return ReadElement(from);
              }
              break;
           }
           event = reader.Next();
           event = reader.Peek();
        }
        return null;
      }
      /// <summary>
      /// This is used to convert the start element to an input node.
      /// This will push the created input node on to the stack. The
      /// input node created contains a reference to this reader. so
      /// that it can be used to read child elements and values.
      /// </summary>
      /// <param name="from">
      /// this is the parent element for the start event
      /// </param>
      /// <param name="event">
      /// this is the start element to be wrapped
      /// </param>
      /// <returns>
      /// this returns an input node for the given element
      /// </returns>
      public InputNode ReadStart(InputNode from, EventNode event) {
         InputElement input = new InputElement(from, this, event);
         if(event.isStart()) {
            return stack.push(input);
         }
         return input;
      }
      /// <summary>
      /// This is used to determine the name of the node specified. The
      /// name of the node is determined to be the name of the element
      /// if that element is converts to a valid StAX start element.
      /// </summary>
      /// <param name="node">
      /// this is the StAX node to acquire the name from
      /// </param>
      /// <param name="name">
      /// this is the name of the node to check against
      /// </param>
      /// <returns>
      /// true if the specified node has the given local name
      /// </returns>
      public bool IsName(EventNode node, String name) {
         String local = node.getName();
         if(local == null) {
            return false;
         }
         return local.equals(name);
      }
      /// <summary>
      /// Read the contents of the characters between the specified XML
      /// element tags, if the read is currently at that element. This
      /// allows characters associated with the element to be used. If
      /// the specified node is not the current node, null is returned.
      /// </summary>
      /// <param name="from">
      /// this is the input node to read the value from
      /// </param>
      /// <returns>
      /// this returns the characters from the specified node
      /// </returns>
      public String ReadValue(InputNode from) {
         StringBuilder value = new StringBuilder();
         while(stack.top() == from) {
            EventNode event = reader.Peek();
            if(!event.isText()) {
               if(value.length() == 0) {
                  return null;
               }
               return value.toString();
            }
            String data = event.getValue();
            value.append(data);
            reader.Next();
         }
         return null;
      }
      /// <summary>
      /// This is used to determine if this input node is empty. An
      /// empty node is one with no attributes or children. This can
      /// be used to determine if a given node represents an empty
      /// entity, with which no extra data can be extracted.
      /// </summary>
      /// <param name="from">
      /// this is the input node to read the value from
      /// </param>
      /// <returns>
      /// this returns true if the node is an empty element
      /// </returns>
      public bool IsEmpty(InputNode from) {
         if(stack.top() == from) {
            EventNode event = reader.Peek();
            if(event.isEnd()) {
               return true;
            }
         }
         return false;
      }
      /// <summary>
      /// This method is used to skip an element within the XML document.
      /// This will simply read each element from the document until
      /// the specified element is at the top of the stack. When the
      /// specified element is at the top of the stack this returns.
      /// </summary>
      /// <param name="from">
      /// this is the element to skip from the XML document
      /// </param>
      public void SkipElement(InputNode from) {
         while(ReadElement(from) != null);
      }
   }
}
