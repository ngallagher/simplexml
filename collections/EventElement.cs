#region License
//
// EventElement.cs January 2010
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
   /// The <c>EventElement</c> object is used to represent an event
   /// that has been extracted from the XML document. Events provide a
   /// framework neutral way to represent a token from the source XML.
   /// It provides the name and value of the event, if applicable, and
   /// also provides namespace information. Some nodes will have
   /// associated <c>Attribute</c> objects, typically these will
   /// be the XML element events. Also, if available, the event will
   /// provide the line number the event was encountered in the XML.
   /// </summary>
   class EventElement : EventNode {

      /// <summary>
      /// This is true when the node represents a new element. This is
      /// the core event type as it contains the element name and any
      /// associated attributes. The core reader uses this to compose
      /// the input nodes that are produced.
      /// </summary>
      /// <returns>
      /// This returns true if the event represents an element.
      /// </returns>
      public override bool IsStart() {
         return true;
      }
   }
}
