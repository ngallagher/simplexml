#region License
//
// PrimitiveKey.cs July 2007
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
   /// The <c>PrimitiveKey</c> is used to serialize a primitive key
   /// to and from a node. If a key name is provided in the annotation
   /// then this will serialize and deserialize that key with the given
   /// name, if the key is an attribute, then it is written using the
   /// provided name.
   /// </code>
   ///    &lt;entry key="one"&gt;example one&lt;/entry&gt;
   ///    &lt;entry key="two"&gt;example two&lt;/entry&gt;
   ///    &lt;entry key="three"&gt;example three&lt;/entry&gt;
   /// </code>
   /// Allowing the key to be written as either an XML attribute or an
   /// element enables a more flexible means for representing the key.
   /// Composite elements can not be used as attribute values as they do
   /// not serialize to a string. Primitive keys as elements can be
   /// maintained as references using the cycle strategy.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.CompositeMap
   /// </seealso>
   class PrimitiveKey : Converter {
      /// <summary>
      /// The primitive factory used to resolve the primitive to a string.
      /// </summary>
      private readonly PrimitiveFactory factory;
      /// <summary>
      /// This is the context used to support the serialization process.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// The primitive converter used to read the key from the node.
      /// </summary>
      private readonly Primitive root;
      /// <summary>
      /// This is the style used to style the XML elements for the key.
      /// </summary>
      private readonly Style style;
      /// <summary>
      /// The entry object contains the details on how to write the key.
      /// </summary>
      private readonly Entry entry;
      /// <summary>
      /// Represents the primitive type the key is serialized to and from.
      /// </summary>
      private readonly Type type;
      /// <summary>
      /// Constructor for the <c>PrimitiveKey</c> object. This is
      /// used to create the key object which converts the map key to an
      /// instance of the key type. This can also resolve references.
      /// </summary>
      /// <param name="context">
      /// this is the context object used for serialization
      /// </param>
      /// <param name="entry">
      /// this is the entry object that describes entries
      /// </param>
      /// <param name="type">
      /// this is the type that this converter deals with
      /// </param>
      public PrimitiveKey(Context context, Entry entry, Type type) {
         this.factory = new PrimitiveFactory(context, type);
         this.root = new Primitive(context, type);
         this.style = context.Style;
         this.context = context;
         this.entry = entry;
         this.type = type;
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then an exception is thrown.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object Read(InputNode node) {
         Class expect = type.Type;
         String name = entry.Key;
         if(name == null) {
            name = context.GetName(expect);
         }
         if(!entry.IsAttribute()) {
            return ReadElement(node, name);
         }
         return ReadAttribute(node, name);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then an exception is thrown.
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
      /// attributes then an null is assumed and returned.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <param name="key">
      /// this is the name of the attribute used by the key
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object ReadAttribute(InputNode node, String key) {
         String name = style.GetAttribute(key);
         InputNode child = node.GetAttribute(name);
         if(child == null) {
            return null;
         }
         return root.Read(child);
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
      /// this is the name of the element used by the key
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public Object ReadElement(InputNode node, String key) {
         String name = style.GetElement(key);
         InputNode child = node.getNext(name);
         if(child == null) {
            return null;
         }
         return root.Read(child);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then the node is considered as null and is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public bool Validate(InputNode node) {
         Class expect = type.Type;
         String name = entry.Key;
         if(name == null) {
            name = context.GetName(expect);
         }
         if(!entry.IsAttribute()) {
            return ValidateElement(node, name);
         }
         return ValidateAttribute(node, name);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then the node is considered as null and is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <param name="key">
      /// this is the name of the attribute used by the key
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public bool ValidateAttribute(InputNode node, String key) {
         String name = style.GetElement(key);
         InputNode child = node.GetAttribute(name);
         if(child == null) {
            return true;
         }
         return root.Validate(child);
      }
      /// <summary>
      /// This method is used to read the key value from the node. The
      /// value read from the node is resolved using the template filter.
      /// If the key value can not be found according to the annotation
      /// attributes then the node is considered as null and is valid.
      /// </summary>
      /// <param name="node">
      /// this is the node to read the key value from
      /// </param>
      /// <param name="key">
      /// this is the name of the element used by the key
      /// </param>
      /// <returns>
      /// this returns the value deserialized from the node
      /// </returns>
      public bool ValidateElement(InputNode node, String key) {
         String name = style.GetElement(key);
         InputNode child = node.getNext(name);
         if(child == null) {
            return true;
         }
         return root.Validate(child);
      }
      /// <summary>
      /// This method is used to write the value to the specified node.
      /// The value written to the node can be an attribute or an element
      /// depending on the annotation attribute values. This method will
      /// maintain references for serialized elements.
      /// </summary>
      /// <param name="node">
      /// this is the node that the value is written to
      /// </param>
      /// <param name="item">
      /// this is the item that is to be written
      /// </param>
      public void Write(OutputNode node, Object item) {
         if(!entry.IsAttribute()) {
            WriteElement(node, item);
         } else if(item != null) {
            WriteAttribute(node, item);
         }
      }
      /// <summary>
      /// This method is used to write the value to the specified node.
      /// This will write the item as an element to the provided node,
      /// also this enables references to be used during serialization.
      /// </summary>
      /// <param name="node">
      /// this is the node that the value is written to
      /// </param>
      /// <param name="item">
      /// this is the item that is to be written
      /// </param>
      public void WriteElement(OutputNode node, Object item) {
         Class expect = type.Type;
         String key = entry.Key;
         if(key == null) {
            key = context.GetName(expect);
         }
         String name = style.GetElement(key);
         OutputNode child = node.getChild(name);
         if(item != null) {
            if(!IsOverridden(child, item)) {
               root.Write(child, item);
            }
         }
      }
      /// <summary>
      /// This method is used to write the value to the specified node.
      /// This will write the item as an attribute to the provided node,
      /// the name of the attribute is taken from the annotation.
      /// </summary>
      /// <param name="node">
      /// this is the node that the value is written to
      /// </param>
      /// <param name="item">
      /// this is the item that is to be written
      /// </param>
      public void WriteAttribute(OutputNode node, Object item) {
         Class expect = type.Type;
         String text = factory.GetText(item);
         String key = entry.Key;
         if(key == null) {
            key = context.GetName(expect);
         }
         String name = style.GetAttribute(key);
         if(text != null) {
            node.setAttribute(name, text);
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
         return factory.SetOverride(type, value, node);
      }
   }
}
