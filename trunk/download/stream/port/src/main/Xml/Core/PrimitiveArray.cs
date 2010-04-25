#region License
//
// PrimitiveArray.cs July 2006
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
   /// The <c>PrimitiveArray</c> object is used to convert a list of
   /// elements to an array of object entries. This in effect performs a
   /// serialization and deserialization of primitive elements for the
   /// array object. On serialization each primitive type must be checked
   /// against the array component type so that it is serialized in a form
   /// that can be deserialized dynamically.
   /// </code>
   ///    &lt;array&gt;
   ///       &lt;entry&gt;example text one&lt;/entry&gt;
   ///       &lt;entry&gt;example text two&lt;/entry&gt;
   ///       &lt;entry&gt;example text three&lt;/entry&gt;
   ///    &lt;/array&gt;
   /// </code>
   /// For the above XML element list the element <c>entry</c> is
   /// contained within the array. Each entry element is deserialized as
   /// a from a parent XML element, which is specified in the annotation.
   /// For serialization the reverse is done, each element taken from the
   /// array is written into an element created from the parent element.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Primitive
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.ElementArray
   /// </seealso>
   class PrimitiveArray : Converter {
      /// <summary>
      /// This factory is used to create an array for the contact.
      /// </summary>
      private readonly ArrayFactory factory;
      /// <summary>
      /// This performs the serialization of the primitive element.
      /// </summary>
      private readonly Primitive root;
      /// <summary>
      /// This is the name that each array element is wrapped with.
      /// </summary>
      private readonly String parent;
      /// <summary>
      /// This is the type of object that will be held in the list.
      /// </summary>
      private readonly Type entry;
      /// <summary>
      /// Constructor for the <c>PrimitiveArray</c> object. This is
      /// given the array type for the contact that is to be converted. An
      /// array of the specified type is used to hold the deserialized
      /// elements and will be the same length as the number of elements.
      /// </summary>
      /// <param name="context">
      /// this is the context object used for serialization
      /// </param>
      /// <param name="type">
      /// this is the actual field type from the schema
      /// </param>
      /// <param name="entry">
      /// the entry type to be stored within the array
      /// </param>
      /// <param name="parent">
      /// this is the name to wrap the array element with
      /// </param>
      public PrimitiveArray(Context context, Type type, Type entry, String parent) {
         this.factory = new ArrayFactory(context, type);
         this.root = new Primitive(context, entry);
         this.parent = parent;
         this.entry = entry;
      }
      /// <summary>
      /// This <c>read</c> method will read the XML element list from
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
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
      /// </summary>
      /// <param name="node">
      /// this is the XML element that is to be deserialized
      /// </param>
      /// <param name="list">
      /// this is the array to read the array values in to
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
            Array.set(list, pos, root.Read(next));
         }
      }
      /// <summary>
      /// This <c>validate</c> method will validate the XML element list
      /// from the provided node and validate its children as entry types.
      /// This will validate each entry type as a primitive value. In order
      /// to do this the parent string provided forms the element.
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
            Class expect = value.Type;
            return Validate(node, expect);
         }
         return true;
      }
      /// <summary>
      /// This <c>validate</c> method wll validate the XML element list
      /// from the provided node and validate its children as entry types.
      /// This will validate each entry type as a primitive value. In order
      /// to do this the parent string provided forms the element.
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
            root.Validate(next);
         }
      }
      /// <summary>
      /// This <c>write</c> method will write the specified object
      /// to the given XML element as as array entries. Each entry within
      /// the given array must be assignable to the array component type.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
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
            OutputNode child = node.getChild(parent);
            if(child == null) {
               break;
            }
            Write(child, source, i);
         }
      }
      /// <summary>
      /// This <c>write</c> method will write the specified object
      /// to the given XML element as as array entries. Each entry within
      /// the given array must be assignable to the array component type.
      /// This will deserialize each entry type as a primitive value. In
      /// order to do this the parent string provided forms the element.
      /// </summary>
      /// <param name="source">
      /// this is the source object array to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element container to be populated
      /// </param>
      /// <param name="index">
      /// this is the position in the array to set the item
      /// </param>
      public void Write(OutputNode node, Object source, int index) {
         Object item = Array.get(source, index);
         if(item != null) {
            if(!IsOverridden(node, item)) {
               root.Write(node, item);
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
