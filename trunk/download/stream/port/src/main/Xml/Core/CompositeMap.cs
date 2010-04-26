#region License
//
// CompositeMap.cs July 2007
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
   class CompositeMap : Converter {
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
      public CompositeMap(Context context, Entry entry, Type type) {
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
         Instance type = factory.GetInstance(node);
         Object map = type.Instance;
         if(!type.isReference()) {
            return Populate(node, map);
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
      /// <param name="result">
      /// this is the map object that is to be populated
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node, Object result) {
         Instance type = factory.GetInstance(node);
         if(type.isReference()) {
            return type.Instance;
         }
         type.setInstance(result);
         if(result != null) {
            return Populate(node, result);
         }
         return result;
      }
      /// <summary>
      /// This <c>populate</c> method will read the XML element map
      /// from the provided node and deserialize its children as entry types.
      /// Each entry type must contain a key and value so that the entry
      /// can be inserted in to the map as a pair. If either the key or
      /// value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="result">
      /// this is the map object that is to be populated
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Populate(InputNode node, Object result) {
         Dictionary map = (Map) result;
         while(true) {
            InputNode next = node.getNext();
            if(next == null) {
               return map;
            }
            Object index = key.Read(next);
            Object item = value.Read(next);
            map.put(index, item);
         }
      }
      /// <summary>
      /// This <c>validate</c> method will validate the XML element
      /// map from the provided node and validate its children as entry
      /// types. Each entry type must contain a key and value so that the
      /// entry can be inserted in to the map as a pair. If either the key
      /// or value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be validate
      /// </param>
      /// <returns>
      /// true if the element matches the XML schema class given
      /// </returns>
      public bool Validate(InputNode node) {
         Instance value = factory.GetInstance(node);
         if(!value.isReference()) {
            Object result = value.setInstance(null);
            Class type = value.getType();
            return Validate(node, type);
         }
         return true;
      }
      /// <summary>
      /// This <c>validate</c> method will validate the XML element
      /// map from the provided node and validate its children as entry
      /// types. Each entry type must contain a key and value so that the
      /// entry can be inserted in to the map as a pair. If either the key
      /// or value is composite it is read as a root object, which means its
      /// <c>Root</c> annotation must be present and the name of the
      /// object element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be validate
      /// </param>
      /// <param name="type">
      /// this is the type to validate the input node against
      /// </param>
      /// <returns>
      /// true if the element matches the XML schema class given
      /// </returns>
      public bool Validate(InputNode node, Class type) {
         while(true) {
            InputNode next = node.getNext();
            if(next == null) {
               return true;
            }
            if(!key.Validate(next)) {
               return false;
            }
            if(!value.Validate(next)) {
               return false;
            }
         }
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
         Dictionary map = (Map) source;
         for(Object index : map.keySet()) {
            String root = entry.Entry;
            String name = style.GetElement(root);
            OutputNode next = node.getChild(name);
            Object item = map.get(index);
            key.Write(next, index);
            value.Write(next, item);
         }
      }
   }
}
