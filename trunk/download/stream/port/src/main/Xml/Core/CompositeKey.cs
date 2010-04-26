#region License
//
// CompositeKey.cs July 2007
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
   /// The <c>CompositeKey</c> object is used to convert an object
   /// to an from an XML element. This accepts only composite objects and
   /// will throw an exception if the <c>ElementMap</c> annotation
   /// is configured to have an attribute key. If a key name is given for
   /// the annotation then this will act as a parent element to the
   /// resulting XML element for the composite object.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.ElementMap
   /// </seealso>
   class CompositeKey : Converter {
      /// <summary>
      /// This is the context used to support the serialization process.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// This is the traverser used to read and write the composite key.
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
      /// This represents the type of object the key is written as.
      /// </summary>
      private readonly Type type;
      /// <summary>
      /// Constructor for the <c>CompositeKey</c> object. This will
      /// create an object capable of reading an writing composite keys
      /// from an XML element. This also allows a parent element to be
      /// created to wrap the key object if desired.
      /// </summary>
      /// <param name="context">
      /// this is the root context for the serialization
      /// </param>
      /// <param name="entry">
      /// this is the entry object used for configuration
      /// </param>
      /// <param name="type">
      /// this is the type of object the key represents
      /// </param>
      public CompositeKey(Context context, Entry entry, Type type) {
         this.root = new Traverser(context);
         this.style = context.Style;
         this.context = context;
         this.entry = entry;
         this.type = type;
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then null is assumed and returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object Read(InputNode node) {
         Position line = node.getPosition();
         Class expect = type.Type;
         String name = entry.Key;
         if(name == null) {
            name = context.GetName(expect);
         }
         if(entry.IsAttribute()) {
            throw new ElementException("Can not have %s as an attribute at %s", expect, line);
         }
         return Read(node, name);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then null is assumed and returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
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
            throw new PersistenceException("Can not read key of %s", expect);
         }
         return Read(node);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then null is assumed and returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <param name="key">
      /// this is the name of the key wrapper XML element
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object Read(InputNode node, String key) {
         String name = style.GetElement(key);
         Class expect = type.Type;
         if(name != null) {
            node = node.getNext(name);
         }
         if(node == null) {
            return null;
         }
         if(node.IsEmpty()) {
            return null;
         }
         return root.Read(node, expect);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then null is assumed and the node is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public bool Validate(InputNode node) {
         Position line = node.getPosition();
         Class expect = type.Type;
         String name = entry.Key;
         if(name == null) {
            name = context.GetName(expect);
         }
         if(entry.IsAttribute()) {
            throw new ElementException("Can not have %s as an attribute at %s", expect, line);
         }
         return Validate(node, name);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then null is assumed and the node is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <param name="key">
      /// this is the name of the key wrapper XML element
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
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
      /// the element map annotation is configured to have a key attribute
      /// then this method will throw an exception.
      /// </summary>
      /// <param name="node">
      /// this is the node that the value is written to
      /// </param>
      /// <param name="item">
      /// this is the item that is to be written
      /// </param>
      public void Write(OutputNode node, Object item) {
         Class expect = type.Type;
         String key = entry.Key;
         if(entry.IsAttribute()) {
            throw new ElementException("Can not have %s as an attribute", expect);
         }
         if(key == null) {
            key = context.GetName(expect);
         }
         String name = style.GetElement(key);
         root.Write(node, item, expect, name);
      }
   }
}
