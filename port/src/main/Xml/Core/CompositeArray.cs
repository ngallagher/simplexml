#region License
//
// CompositeArray.cs July 2006
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
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>CompositeArray</c> object is used to convert a list of
   /// elements to an array of object entries. This in effect performs a
   /// root serialization and deserialization of entry elements for the
   /// array object. On serialization each objects type must be checked
   /// against the array component type so that it is serialized in a form
   /// that can be deserialized dynamically.
   /// </code>
   ///    &lt;array length="2"&gt;
   ///       &lt;entry&gt;
   ///          &lt;text&gt;example text value&lt;/text&gt;
   ///       &lt;/entry&gt;
   ///       &lt;entry&gt;
   ///          &lt;text&gt;some other example&lt;/text&gt;
   ///       &lt;/entry&gt;
   ///    &lt;/array&gt;
   /// </code>
   /// For the above XML element list the element <c>entry</c> is
   /// contained within the array. Each entry element is deserialized as
   /// a root element and then inserted into the array. For serialization
   /// the reverse is done, each element taken from the array is written
   /// as a root element to the parent element to create the list. Entry
   /// objects do not need to be of the same type.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Traverser
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.ElementArray
   /// </seealso>
   class CompositeArray : Converter {
      /// <summary>
      /// This factory is used to create an array for the contact.
      /// </summary>
      private readonly ArrayFactory factory;
      /// <summary>
      /// This performs the traversal used for object serialization.
      /// </summary>
      private readonly Traverser root;
      /// <summary>
      /// This is the name to wrap each entry that is represented.
      /// </summary>
      private readonly String parent;
      /// <summary>
      /// This is the entry type for elements within the array.
      /// </summary>
      private readonly Type entry;
      /// <summary>
      /// Constructor for the <c>CompositeArray</c> object. This is
      /// given the array type for the contact that is to be converted. An
      /// array of the specified type is used to hold the deserialized
      /// elements and will be the same length as the number of elements.
      /// </summary>
      /// <param name="context">
      /// this is the context object used for serialization
      /// </param>
      /// <param name="type">
      /// this is the field type for the array being used
      /// </param>
      /// <param name="entry">
      /// this is the entry type for the array elements
      /// </param>
      /// <param name="parent">
      /// this is the name to wrap the array element with
      /// </param>
      public CompositeArray(Context context, Type type, Type entry, String parent) {
         this.factory = new ArrayFactory(context, type);
         this.root = new Traverser(context);
         this.parent = parent;
         this.entry = entry;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This ensures each entry type is deserialized as a root type, that
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
            return Read(node, list);
         }
         return list;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element list from
      /// the provided node and deserialize its children as entry types.
      /// This ensures each entry type is deserialized as a root type, that
      /// is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="list">
      /// this is the array that is to be deserialized
      /// </param>
      /// <returns>
      /// this returns the item to attach to the object contact
      /// </returns>
      public Object Read(InputNode node, Object list) {
         int length = Array.getLength(list);
         for(int pos = 0; true; pos++) {
            Position line = node.getPosition();
            InputNode next = node.getNext();
            if(next == null) {
               return list;
            }
            if(pos >= length){
                throw new ElementException("Array length missing or incorrect at %s", line);
            }
            Read(next, list, pos);
         }
      }
      /// <summary>
      /// This is used to read the specified node from in to the list. If
      /// the node is null then this represents a null element value in
      /// the array. The node can be null only if there is a parent and
      /// that parent contains no child XML elements.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the array value from
      /// </param>
      /// <param name="list">
      /// this is the list to add the array value in to
      /// </param>
      /// <param name="index">
      /// this is the offset to set the value in the array
      /// </param>
      public void Read(InputNode node, Object list, int index) {
         Class type = entry.Type;
         Object value = null;
         if(!node.isEmpty()) {
            value = root.Read(node, type);
         }
         Array.set(list, index, value);
      }
      /// <summary>
      /// This <c>validate</c> method will validate the XML element
      /// list against the provided node and validate its children as entry
      /// types. This ensures each entry type is validated as a root type,
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
      /// This <c>validate</c> method wll validate the XML element
      /// list against the provided node and validate its children as entry
      /// types. This ensures each entry type is validated as a root type,
      /// that is, its <c>Root</c> annotation must be present and the
      /// name of the entry element must match that root element name.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be validated
      /// </param>
      /// <param name="type">
      /// this is the array type used to create the array
      /// </param>
      /// <returns>
      /// true if the element matches the XML schema class given
      /// </returns>
      public bool Validate(InputNode node, Class type) {
         for(int i = 0; true; i++) {
            InputNode next = node.getNext();
            if(next == null) {
               return true;
            }
            if(!next.isEmpty()) {
               root.Validate(next, type);
            }
         }
      }
      /// <summary>
      /// This <c>write</c> method will write the specified object
      /// to the given XML element as as array entries. Each entry within
      /// the given array must be assignable to the array component type.
      /// Each array entry is serialized as a root element, that is, its
      /// <c>Root</c> annotation is used to extract the name.
      /// </summary>
      /// <param name="source">
      /// this is the source object array to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element container to be populated
      /// </param>
      public void Write(OutputNode node, Object source) {
         int size = Array.getLength(source);
         for(int i = 0; i < size; i++) {
            Object item = Array.get(source, i);
            Class type = entry.Type;
            root.Write(node, item, type, parent);
         }
         node.commit();
      }
   }
}
