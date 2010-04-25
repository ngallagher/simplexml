#region License
//
// NodeExtractor.cs January 2010
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
   import static org.w3c.dom.Node.COMMENT_NODE;
   import org.w3c.dom.Document;
   import org.w3c.dom.Node;
   import org.w3c.dom.NodeList;
   /// <summary>
   /// The <c>NodeExtractor</c> object is used to extract nodes
   /// from a provided DOM document. This is used so that the nodes of
   /// a given document can be read with queue like semantics, such
   /// that the first node encountered is the first node taken from
   /// the queue. Queue semantics help transform DOM documents to an
   /// event stream much like the StAX framework.
   /// </summary>
   class NodeExtractor : LinkedList<Node> {
      /// <summary>
      /// Constructor for the <c>NodeExtractor</c> object. This
      /// is used to instantiate an object that flattens a document
      /// in to a queue so that the nodes can be used for streaming.
      /// </summary>
      /// <param name="source">
      /// this is the source document to be flattened
      /// </param>
      public NodeExtractor(Document source) {
         this.Extract(source);
      }
      /// <summary>
      /// This is used to extract the nodes of the document in such a
      /// way that it can be navigated as a queue. In order to do this
      /// each node encountered is pushed in to the queue so that
      /// when finished the nodes can be dealt with as a stream.
      /// </summary>
      /// <param name="source">
      /// this is the source document to be flattened
      /// </param>
      public void Extract(Document source) {
         Node node = source.getDocumentElement();
         if(node != null) {
            offer(node);
            Extract(node);
         }
      }
      /// <summary>
      /// This is used to extract the nodes of the element in such a
      /// way that it can be navigated as a queue. In order to do this
      /// each node encountered is pushed in to the queue so that
      /// when finished the nodes can be dealt with as a stream.
      /// </summary>
      /// <param name="source">
      /// this is the source element to be flattened
      /// </param>
      public void Extract(Node source) {
         NodeList list = source.getChildNodes();
         int length = list.getLength();
         for(int i = 0; i < length; i++) {
            Node node = list.item(i);
            short type = node.getNodeType();
            if(type != COMMENT_NODE) {
               offer(node);
               Extract(node);
            }
         }
      }
   }
}
