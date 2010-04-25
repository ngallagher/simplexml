#region License
//
// CompositeList.cs July 2006
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
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>CompositeList</c> object is used to convert an element
   /// list to a collection of element entries. This in effect performs a
   /// root serialization and deserialization of entry elements for the
   /// collection object. On serialization each objects type must be
   /// checked against the XML annotation entry so that it is serialized
   /// in a form that can be deserialized.
   /// </code>
   ///    &lt;list&gt;
   ///       &lt;entry attribute="value"&gt;
   ///          &lt;text&gt;example text value&lt;/text&gt;
   ///       &lt;/entry&gt;
   ///       &lt;entry attribute="demo"&gt;
   ///          &lt;text&gt;some other example&lt;/text&gt;
   ///       &lt;/entry&gt;
   ///    &lt;/list&gt;
   /// </code>
   /// For the above XML element list the element <c>entry</c> is
   /// contained within the list. Each entry element is thus deserialized
   /// as a root element and then inserted into the list. This enables
   /// lists to be composed from XML documents. For serialization the
   /// reverse is done, each element taken from the collection is written
   /// as a root element to the owning element to create the list.
   /// Entry objects do not need to be of the same type.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Traverser
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.ElementList
   /// </seealso>
   class CompositeList : Converter {
      /// <summary>
      /// This factory is used to create a suitable collection list.
      /// </summary>
      private readonly CollectionFactory factory;
      /// <summary>
      /// This performs the traversal used for object serialization.
      /// </summary>
      private readonly Traverser root;
      /// <summary>
      /// This represents the name of the entry elements to write.
      /// </summary>
      private readonly String name;
      /// <summary>
      /// This is the entry type for elements within the list.
      /// </summary>
      private readonly Type entry;
      /// <summary>
      /// Constructor for the <c>CompositeList</c> object. This is
      /// given the list type and entry type to be used. The list type is
      /// the <c>Collection</c> implementation that deserialized
      /// entry objects are inserted into.
      /// </summary>
      /// <param name="context">
      /// this is the context object used for serialization
      /// </param>
      /// <param name="type">
      /// this is the collection type for the list used
      /// </param>
      /// <param name="entry">
      /// the entry type to be stored within the list
      /// </param>
      public CompositeList(Context context, Type type, Type entry, String name) {
         this.factory = new CollectionFactory(context, type);
         this.root = new Traverser(context);
         this.entry = entry;
         this.name = name;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This will each entry type is deserialized as a root type, that
      /// is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node) {
         Instance type = factory.GetInstance(node);
         Object list = type.Instance;
         if(!type.isReference()) {
            return Populate(node, list);
         }
         return list;
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
      /// This <c>populate</c> method wll read the XML element list
      /// from the provided node and deserialize its children as entry types.
      /// This will each entry type is deserialized as a root type, that
      /// is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="result">
      /// this is the collection that is to be populated
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Populate(InputNode node, Object result) {
         Collection list = (Collection) result;
         while(true) {
            InputNode next = node.getNext();
            Class expect = entry.Type;
            if(next == null) {
               return list;
            }
            list.add(root.Read(next, expect));
         }
      }
      /// <summary>
      /// This <c>validate</c> method will validate the XML element
      /// list from the provided node and deserialize its children as entry
      /// types. This takes each entry type and validates it as a root type,
      /// that is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be validated
      /// </param>
      /// <returns>
      /// true if the element matches the XML schema class given
      /// </returns>
      public bool Validate(InputNode node) {
         Instance value = factory.GetInstance(node);
         if(!value.isReference()) {
            Object result = value.setInstance(null);
            Class type = value.Type;
            return Validate(node, type);
         }
         return true;
      }
      /// <summary>
      /// This <c>validate</c> method will validate the XML element
      /// list from the provided node and deserialize its children as entry
      /// types. This takes each entry type and validates it as a root type,
      /// that is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be validated
      /// </param>
      /// <param name="type">
      /// this is the type to validate against the input node
      /// </param>
      /// <returns>
      /// true if the element matches the XML schema class given
      /// </returns>
      public bool Validate(InputNode node, Class type) {
         while(true) {
            InputNode next = node.getNext();
            Class expect = entry.Type;
            if(next == null) {
               return true;
            }
            root.Validate(next, expect);
         }
      }
      /// <summary>
      /// This <c>write</c> method will write the specified object
      /// to the given XML element as as list entries. Each entry within
      /// the given collection must be assignable from the annotated
      /// type specified within the <c>ElementList</c> annotation.
      /// Each entry is serialized as a root element, that is, its
      /// <c>Root</c> annotation is used to extract the name.
      /// </summary>
      /// <param name="source">
      /// this is the source collection to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element container to be populated
      /// </param>
      public void Write(OutputNode node, Object source) {
         Collection list = (Collection) source;
         for(Object item : list) {
            if(item != null) {
               Class expect = entry.Type;
               Class type = item.getClass();
               if(!expect.isAssignableFrom(type)) {
                  throw new PersistenceException("Entry %s does not match %s", type, entry);
               }
               root.Write(node, item, expect, name);
            }
         }
      }
   }
}
