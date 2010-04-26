#region License
//
// CompositeInlineMap.cs July 2007
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>CompositeMap</c> is used to serialize and deserialize
   /// maps to and from a source XML document. The structure of the map in
   /// the XML format is determined by the annotation. Keys can be either
   /// attributes or elements, and values can be inline. This can perform
   /// serialization and deserialization of the key and value objects
   /// whether the object types are primitive or composite.
   /// </code>
   ///    &lt;map&gt;
   ///       &lt;entry key='1'&gt;
   ///          &lt;value&gt;one&lt;/value&gt;
   ///       &lt;/entry&gt;
   ///       &lt;entry key='2'&gt;
   ///          &lt;value&gt;two&lt;/value&gt;
   ///       &lt;/entry&gt;
   ///    &lt;/map&gt;
   /// </code>
   /// For the above XML element map the element <c>entry</c> is
   /// used to wrap the key and value such that they can be grouped. This
   /// element does not represent any real object. The names of each of
   /// the XML elements serialized and deserialized can be configured.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Entry
   /// </seealso>
   class CompositeInlineMap : Repeater {
      /// <summary>
      /// The factory used to create suitable map object instances.
      /// </summary>
      private readonly MapFactory factory;
      /// <summary>
      /// This is the type that the value objects are instances of.
      /// </summary>
      private readonly Converter value;
      /// <summary>
      /// This is the name of the entry wrapping the key and value.
      /// </summary>
      private readonly Converter key;
      /// <summary>
      /// This is the style used to style the names used for the XML.
      /// </summary>
      private readonly Style style;
      /// <summary>
      /// The entry object contains the details on how to write the map.
      /// </summary>
      private readonly Entry entry;
      /// <summary>
      /// Constructor for the <c>CompositeMap</c> object. This will
      /// create a converter that is capable of writing map objects to
      /// and from XML. The resulting XML is configured by an annotation
      /// such that key values can attributes and values can be inline.
      /// </summary>
      /// <param name="context">
      /// this is the root context for the serialization
      /// </param>
      /// <param name="entry">
      /// this provides configuration for the resulting XML
      /// </param>
      /// <param name="type">
      /// this is the map type that is to be converted
      /// </param>
      public CompositeInlineMap(Context context, Entry entry, Type type) {
         this.factory = new MapFactory(context, type);
         this.value = entry.GetValue(context);
         this.key = entry.GetKey(context);
         this.style = context.getStyle();
         this.entry = entry;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element map from
      /// the provided node and deserialize its children as entry types.
      /// Each entry type must contain a key and value so that the entry
      /// can be inserted in to the map as a pair. If either the key or
      /// value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node) {
         Object value = factory.Instance;
         Dictionary table = (Map) value;
         if(table != null) {
            return Read(node, table);
         }
         return null;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element map from
      /// the provided node and deserialize its children as entry types.
      /// Each entry type must contain a key and value so that the entry
      /// can be inserted in to the map as a pair. If either the key or
      /// value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node, Object value) {
         Dictionary map = (Map) value;
         if(map != null) {
            return Read(node, map);
         }
         return Read(node);
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element map from
      /// the provided node and deserialize its children as entry types.
      /// Each entry type must contain a key and value so that the entry
      /// can be inserted in to the map as a pair. If either the key or
      /// value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="map">
      /// this is the map object that is to be populated
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node, Dictionary map) {
         InputNode from = node.getParent();
         String name = node.getName();
         while(node != null) {
            Object index = key.Read(node);
            Object item = value.Read(node);
            if(map != null) {
               map.put(index, item);
            }
            node = from.getNext(name);
         }
         return map;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element map from
      /// the provided node and deserialize its children as entry types.
      /// Each entry type must contain a key and value so that the entry
      /// can be inserted in to the map as a pair. If either the key or
      /// value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public bool Validate(InputNode node) {
         InputNode from = node.getParent();
         String name = node.getName();
         while(node != null) {
            if(!key.Validate(node)) {
               return false;
            }
            if(!value.Validate(node)) {
               return false;
            }
            node = from.getNext(name);
         }
         return true;
      }
      /// <summary>
      /// This <c>write</c> method will write the key value pairs
      /// within the provided map to the specified XML node. This will
      /// write each entry type must contain a key and value so that
      /// the entry can be deserialized in to the map as a pair. If the
      /// key or value object is composite it is read as a root object
      /// so its <c>Root</c> annotation must be present.
      /// </summary>
      /// <param name="node">
      /// this is the node the map is to be written to
      /// </param>
      /// <param name="source">
      /// this is the source map that is to be written
      /// </param>
      public void Write(OutputNode node, Object source) {
         OutputNode parent = node.getParent();
         Mode mode = node.getMode();
         Dictionary map = (Map) source;
         if(!node.isCommitted()) {
            node.remove();
         }
         Write(parent, map, mode);
      }
      /// <summary>
      /// This <c>write</c> method will write the key value pairs
      /// within the provided map to the specified XML node. This will
      /// write each entry type must contain a key and value so that
      /// the entry can be deserialized in to the map as a pair. If the
      /// key or value object is composite it is read as a root object
      /// so its <c>Root</c> annotation must be present.
      /// </summary>
      /// <param name="node">
      /// this is the node the map is to be written to
      /// </param>
      /// <param name="map">
      /// this is the source map that is to be written
      /// </param>
      /// <param name="mode">
      /// this is the mode that has been inherited
      /// </param>
      public void Write(OutputNode node, Dictionary map, Mode mode) {
         String root = entry.Entry;
         String name = style.GetElement(root);
         for(Object index : map.keySet()) {
            OutputNode next = node.getChild(name);
            Object item = map.get(index);
            next.setMode(mode);
            key.Write(next, index);
            value.Write(next, item);
         }
      }
   }
}
