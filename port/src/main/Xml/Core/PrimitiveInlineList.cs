#region License
//
// PrimitiveInlineList.cs July 2006
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
   /// The <c>PrimitiveInlineList</c> object is used to convert a
   /// group of elements in to a collection of element entries. This is
   /// used when a containing element for a list is not required. It
   /// extracts the elements by matching elements to name of the type
   /// that the annotated field or method requires. This enables these
   /// element entries to exist as siblings to other objects within the
   /// object.  One restriction is that the <c>Root</c> annotation
   /// for each of the types within the list must be the same.
   /// </code>
   ///    &lt;entry&gt;example one&lt;/entry&gt;
   ///    &lt;entry&gt;example two&lt;/entry&gt;
   ///    &lt;entry&gt;example three&lt;/entry&gt;
   ///    &lt;entry&gt;example four&lt;/entry&gt;
   /// </code>
   /// For the above XML element list the element <c>entry</c> is
   /// used to wrap the primitive string value. This wrapping XML element
   /// is configurable and defaults to the lower case string for the name
   /// of the class it represents. So, for example, if the primitive type
   /// is an <c>int</c> the enclosing element will be called int.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Primitive
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.ElementList
   /// </seealso>
   class PrimitiveInlineList : Repeater {
      /// <summary>
      /// This factory is used to create a suitable collection list.
      /// </summary>
      private readonly CollectionFactory factory;
      /// <summary>
      /// This performs the traversal used for object serialization.
      /// </summary>
      private readonly Primitive root;
      /// <summary>
      /// This is the name that each list element is wrapped with.
      /// </summary>
      private readonly String parent;
      /// <summary>
      /// This is the type of object that will be held in the list.
      /// </summary>
      private readonly Type entry;
      /// <summary>
      /// Constructor for the <c>PrimitiveInlineList</c> object.
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
      /// <param name="parent">
      /// this is the name to wrap the list element with
      /// </param>
      public PrimitiveInlineList(Context context, Type type, Type entry, String parent) {
         this.factory = new CollectionFactory(context, type);
         this.root = new Primitive(context, entry);
         this.parent = parent;
         this.entry = entry;
      }
      /// <summary>
      /// This <c>read</c> method wll read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
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
      /// This <c>read</c> method wll read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
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
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
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
         String name = node.getName();
         while(node != null) {
            Object item = root.Read(node);
            if(item != null) {
               list.add(item);
            }
            node = from.getNext(name);
         }
         return list;
      }
      /// <summary>
      /// This <c>read</c> method wll read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
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
            bool valid = root.Validate(node);
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
      /// the given list must be assignable to the given primitive type.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
      /// </summary>
      /// <param name="source">
      /// this is the source collection to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element container to be populated
      /// </param>
      public void Write(OutputNode node, Object source) {
         OutputNode parent = node.getParent();
         Mode mode = node.getMode();
         if(!node.isCommitted()) {
            node.remove();
         }
         Write(parent, source, mode);
      }
      /// <summary>
      /// This <c>write</c> method will write the specified object
      /// to the given XML element as as list entries. Each entry within
      /// the given list must be assignable to the given primitive type.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
      /// </summary>
      /// <param name="node">
      /// this is the parent output node to write values to
      /// </param>
      /// <param name="source">
      /// this is the source collection to be serialized
      /// </param>
      /// <param name="mode">
      /// this is used to determine whether to output CDATA
      /// </param>
      public void Write(OutputNode node, Object source, Mode mode) {
         Collection list = (Collection) source;
         for(Object item : list) {
            if(item != null) {
               OutputNode child = node.getChild(parent);
               if(!IsOverridden(child, item)) {
                  child.setMode(mode);
                  root.Write(child, item);
               }
            }
         }
      }
      /// <summary>
      /// This is used to determine whether the specified value has been
      /// overridden by the strategy. If the item has been overridden
      /// then no more serialization is require for that value, this is
      /// effectively telling the serialization process to stop writing.
      /// </summary>
      /// <param name="node">
      /// the node that a potential override is written to
      /// </param>
      /// <param name="value">
      /// this is the object instance to be serialized
      /// </param>
      /// <returns>
      /// returns true if the strategy overrides the object
      /// </returns>
      public bool IsOverridden(OutputNode node, Object value) {
         return factory.setOverride(entry, value, node);
      }
   }
}
