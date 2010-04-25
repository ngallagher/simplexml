#region License
//
// CompositeInlineList.cs July 2006
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
   /// The <c>CompositeInlineList</c> object is used to convert an
   /// group of elements in to a collection of element entries. This is
   /// used when a containing element for a list is not required. It
   /// extracts the elements by matching elements to name of the type
   /// that the annotated field or method requires. This enables these
   /// element entries to exist as siblings to other objects within the
   /// object.  One restriction is that the <c>Root</c> annotation
   /// for each of the types within the list must be the same.
   /// </code>
   ///    &lt;entry attribute="one"&gt;
   ///       &lt;text&gt;example text value&lt;/text&gt;
   ///    &lt;/entry&gt;
   ///    &lt;entry attribute="two"&gt;
   ///       &lt;text&gt;some other example&lt;/text&gt;
   ///    &lt;/entry&gt;
   ///    &lt;entry attribute="three"&gt;
   ///       &lt;text&gt;yet another example&lt;/text&gt;
   ///    &lt;/entry&gt;
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
   class CompositeInlineList : Repeater {
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
      /// Constructor for the <c>CompositeInlineList</c> object.
      /// This is given the list type and entry type to be used. The list
      /// type is the <c>Collection</c> implementation that is used
      /// to collect the deserialized entry objects from the XML source.
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
      /// <param name="name">
      /// this is the name of the entries used for this list
      /// </param>
      public CompositeInlineList(Context context, Type type, Type entry, String name) {
         this.factory = new CollectionFactory(context, type);
         this.root = new Traverser(context);
         this.entry = entry;
         this.name = name;
      }
      /// <summary>
      /// This <c>read</c> method wll read the XML element list from
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
         Object value = factory.Instance;
         Collection list = (Collection) value;
         if(list != null) {
            return Read(node, list);
         }
         return null;
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
      public Object Read(InputNode node, Object value) {
         Collection list = (Collection) value;
         if(list != null) {
            return Read(node, list);
         }
         return Read(node);
      }
      /// <summary>
      /// This <c>read</c> method wll read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This will each entry type is deserialized as a root type, that
      /// is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="list">
      /// this is the collection that is to be populated
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node, Collection list) {
         InputNode from = node.getParent();
         String name = node.GetName();
         while(node != null) {
            Class type = entry.Type;
            Object item = Read(node, type);
            if(item != null) {
               list.add(item);
            }
            node = from.getNext(name);
         }
         return list;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element from the
      /// provided node. This checks to ensure that the deserialized type
      /// is the same as the entry type provided. If the types are not
      /// the same then an exception is thrown. This is done to ensure
      /// each node in the collection contain the same root annotation.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="expect">
      /// this is the type expected of the deserialized type
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node, Class expect) {
         Object item = root.Read(node, expect);
         Class result = item.getClass();
         Class type = entry.Type;
         if(!type.isAssignableFrom(result)) {
            throw new PersistenceException("Entry %s does not match %s", result, entry);
         }
         return item;
      }
      /// <summary>
      /// This <c>read</c> method wll read the XML element list from
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
      public bool Validate(InputNode node) {
         InputNode from = node.getParent();
         Class type = entry.Type;
         String name = node.GetName();
         while(node != null) {
            bool valid = root.Validate(node, type);
            if(valid == false) {
               return false;
            }
            node = from.getNext(name);
         }
         return true;
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
         OutputNode parent = node.getParent();
         if(!node.isCommitted()) {
            node.remove();
         }
         Write(parent, list);
      }
      /// <summary>
      /// This <c>write</c> method will write the specified object
      /// to the given XML element as as list entries. Each entry within
      /// the given collection must be assignable from the annotated
      /// type specified within the <c>ElementList</c> annotation.
      /// Each entry is serialized as a root element, that is, its
      /// <c>Root</c> annotation is used to extract the name.
      /// </summary>
      /// <param name="list">
      /// this is the source collection to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element container to be populated
      /// </param>
      public void Write(OutputNode node, Collection list) {
         for(Object item : list) {
            if(item != null) {
               Class type = entry.Type;
               Class result = item.getClass();
               if(!type.isAssignableFrom(result)) {
                  throw new PersistenceException("Entry %s does not match %s", result, type);
               }
               root.Write(node, item, type, name);
            }
         }
      }
   }
}
