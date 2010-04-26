#region License
//
// CompositeValue.cs July 2007
//
// Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
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
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>CompositeValue</c> object is used to convert an object
   /// to an from an XML element. This accepts only composite objects and
   /// will maintain all references within the object using the cycle
   /// strategy if required. This also ensures that should the value to
   /// be written to the XML element be null that nothing is written.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.ElementMap
   /// </seealso>
   class CompositeValue : Converter {
      /// <summary>
      /// This is the context used to support the serialization process.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This is the traverser used to read and write the value with.
      /// </summary>
      private readonly Traverser root;
      /// <summary>
      /// This is the style used to style the names used for the XML.
      /// </summary>
      private readonly Style style;
      /// <summary>
      /// This is the entry object used to provide configuration details.
      /// </summary>
      private readonly Entry entry;
      /// <summary>
      /// This represents the type of object the value is written as.
      /// </summary>
      private readonly Type type;
      /// <summary>
      /// Constructor for the <c>CompositeValue</c> object. This
      /// will create an object capable of reading an writing composite
      /// values from an XML element. This also allows a parent element
      /// to be created to wrap the key object if desired.
      /// </summary>
      /// <param name="context">
      /// this is the root context for the serialization
      /// </param>
      /// <param name="entry">
      /// this is the entry object used for configuration
      /// </param>
      /// <param name="type">
      /// this is the type of object the value represents
      /// </param>
      public CompositeValue(Context context, Entry entry, Type type) {
         this.root = new Traverser(context);
         this.style = context.Style;
         this.context = context;
         this.entry = entry;
         this.type = type;
      }
      /// <summary>
      /// This method is used to read the value object from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the value data can not be found according to the annotation
      /// attributes then null is assumed and returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the value object from
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object Read(InputNode node) {
         InputNode next = node.getNext();
         Class expect = type.Type;
         if(next == null) {
            return null;
         }
         if(next.IsEmpty()) {
            return null;
         }
         return root.Read(next, expect);
      }
      /// <summary>
      /// This method is used to read the value object from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the value data can not be found according to the annotation
      /// attributes then null is assumed and returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the value object from
      /// </param>
      /// <param name="value">
      /// this is the value to deserialize in to
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object Read(InputNode node, Object value) {
         Class expect = type.Type;
         if(value != null) {
            throw new PersistenceException("Can not read value of %s", expect);
         }
         return Read(node);
      }
      /// <summary>
      /// This method is used to read the value object from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the value data can not be found according to the annotation
      /// attributes then null is assumed and the node is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the value object from
      /// </param>
      /// <returns>
      /// this returns true if this represents a valid value
      /// </returns>
      public bool Validate(InputNode node) {
         Class expect = type.Type;
         String name = entry.GetValue();
         if(name == null) {
            name = context.GetName(expect);
         }
         return Validate(node, name);
      }
      /// <summary>
      /// This method is used to read the value object from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the value data can not be found according to the annotation
      /// attributes then null is assumed and the node is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the value object from
      /// </param>
      /// <param name="key">
      /// this is the name of the value element
      /// </param>
      /// <returns>
      /// this returns true if this represents a valid value
      /// </returns>
      public bool Validate(InputNode node, String key) {
         String name = style.GetElement(key);
         InputNode next = node.getNext(name);
         Class expect = type.Type;
         if(next == null) {
            return true;
         }
         if(next.IsEmpty()) {
            return true;
         }
         return root.Validate(next, expect);
      }
      /// <summary>
      /// This method is used to write the value to the specified node.
      /// The value written to the node must be a composite object and if
      /// the object provided to this is null then nothing is written.
      /// </summary>
      /// <param name="node">
      /// this is the node that the value is written to
      /// </param>
      /// <param name="item">
      /// this is the item that is to be written
      /// </param>
      public void Write(OutputNode node, Object item) {
         Class expect = type.Type;
         String key = entry.GetValue();
         if(key == null) {
            key = context.GetName(expect);
         }
         String name = style.GetElement(key);
         root.Write(node, item, expect, name);
      }
   }
}
