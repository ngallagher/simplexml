#region License
//
// ReadGraph.cs April 2007
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
using SimpleFramework.Xml.Stream;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>ReadGraph</c> object is used to build a graph of the
   /// objects that have been deserialized from the XML document. This is
   /// required so that cycles in the object graph can be recreated such
   /// that the deserialized object is an exact duplicate of the object
   /// that was serialized. Objects are stored in the graph using unique
   /// keys, which for this implementation are unique strings.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.WriteGraph
   /// </seealso>
   class ReadGraph : Dictionary {
      /// <summary>
      /// This is the class loader that is used to load the types used.
      /// </summary>
      private readonly Loader loader;
      /// <summary>
      /// This is used to represent the length of array object values.
      /// </summary>
      private readonly String length;
      /// <summary>
      /// This is the label used to mark the type of an object.
      /// </summary>
      private readonly String label;
      /// <summary>
      /// This is the attribute used to mark the identity of an object.
      /// </summary>
      private readonly String mark;
      /// <summary>
      /// This is the attribute used to refer to an existing instance.
      /// </summary>
      private readonly String refer;
      /// <summary>
      /// Constructor for the <c>ReadGraph</c> object. This is used
      /// to create graphs that are used for reading objects from the XML
      /// document. The specified strategy is used to acquire the names
      /// of the special attributes used during the serialization.
      /// </summary>
      /// <param name="contract">
      /// this is the name scheme used by the strategy
      /// </param>
      /// <param name="loader">
      /// this is the class loader to used for the graph
      /// </param>
      public ReadGraph(Contract contract, Loader loader) {
         this.refer = contract.getReference();
         this.mark = contract.getIdentity();
         this.length = contract.getLength();
         this.label = contract.getLabel();
         this.loader = loader;
      }
      /// <summary>
      /// This is used to recover the object references from the document
      /// using the special attributes specified. This allows the element
      /// specified by the <c>NodeMap</c> to be used to discover
      /// exactly which node in the object graph the element represents.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the instance
      /// </param>
      /// <param name="node">
      /// this is the XML element to be deserialized
      /// </param>
      /// <returns>
      /// this is used to return the type to acquire the value
      /// </returns>
      public Value Read(Type type, NodeMap node) {
         Node entry = node.remove(label);
         Class expect = type.getType();
         if(expect.isArray()) {
            expect = expect.getComponentType();
         }
         if(entry != null) {
            String name = entry.getValue();
            expect = loader.Load(name);
         }
         return ReadInstance(type, expect, node);
      }
      /// <summary>
      /// This is used to recover the object references from the document
      /// using the special attributes specified. This allows the element
      /// specified by the <c>NodeMap</c> to be used to discover
      /// exactly which node in the object graph the element represents.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the instance
      /// </param>
      /// <param name="real">
      /// this is the overridden type from the XML element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be deserialized
      /// </param>
      /// <returns>
      /// this is used to return the type to acquire the value
      /// </returns>
      public Value ReadInstance(Type type, Class real, NodeMap node) {
         Node entry = node.remove(mark);
         if(entry == null) {
            return ReadReference(type, real, node);
         }
         String key = entry.getValue();
         if(containsKey(key)) {
            throw new CycleException("Element '%s' already exists", key);
         }
         return ReadValue(type, real, node, key);
      }
      /// <summary>
      /// This is used to recover the object references from the document
      /// using the special attributes specified. This allows the element
      /// specified by the <c>NodeMap</c> to be used to discover
      /// exactly which node in the object graph the element represents.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the instance
      /// </param>
      /// <param name="real">
      /// this is the overridden type from the XML element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be deserialized
      /// </param>
      /// <returns>
      /// this is used to return the type to acquire the value
      /// </returns>
      public Value ReadReference(Type type, Class real, NodeMap node) {
         Node entry = node.remove(refer);
         if(entry == null) {
            return ReadValue(type, real, node);
         }
         String key = entry.getValue();
         Object value = get(key);
         if(!containsKey(key)) {
            throw new CycleException("Invalid reference '%s' found", key);
         }
         return new Reference(value, real);
      }
      /// <summary>
      /// This is used to acquire the <c>Value</c> which can be used
      /// to represent the deserialized value. The type create cab be
      /// added to the graph of created instances if the XML element has
      /// an identification attribute, this allows cycles to be completed.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the instance
      /// </param>
      /// <param name="real">
      /// this is the overridden type from the XML element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be deserialized
      /// </param>
      /// <returns>
      /// this is used to return the type to acquire the value
      /// </returns>
      public Value ReadValue(Type type, Class real, NodeMap node) {
         Class expect = type.getType();
         if(expect.isArray()) {
            return ReadArray(type, real, node);
         }
         return new ObjectValue(real);
      }
      /// <summary>
      /// This is used to acquire the <c>Value</c> which can be used
      /// to represent the deserialized value. The type create cab be
      /// added to the graph of created instances if the XML element has
      /// an identification attribute, this allows cycles to be completed.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the instance
      /// </param>
      /// <param name="real">
      /// this is the overridden type from the XML element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be deserialized
      /// </param>
      /// <param name="key">
      /// the key the instance is known as in the graph
      /// </param>
      /// <returns>
      /// this is used to return the type to acquire the value
      /// </returns>
      public Value ReadValue(Type type, Class real, NodeMap node, String key) {
         Value value = ReadValue(type, real, node);
         if(key != null) {
            return new Allocate(value, this, key);
         }
         return value;
      }
      /// <summary>
      /// This is used to acquire the <c>Value</c> which can be used
      /// to represent the deserialized value. The type create cab be
      /// added to the graph of created instances if the XML element has
      /// an identification attribute, this allows cycles to be completed.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the instance
      /// </param>
      /// <param name="real">
      /// this is the overridden type from the XML element
      /// </param>
      /// <param name="node">
      /// this is the XML element to be deserialized
      /// </param>
      /// <returns>
      /// this is used to return the type to acquire the value
      /// </returns>
      public Value ReadArray(Type type, Class real, NodeMap node) {
         Node entry = node.remove(length);
         int size = 0;
         if(entry != null) {
            String value = entry.getValue();
            size = Integer.parseInt(value);
         }
         return new ArrayValue(real, size);
      }
   }
}
