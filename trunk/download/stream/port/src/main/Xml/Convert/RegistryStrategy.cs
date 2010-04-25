#region License
//
// RegistryStrategy.cs January 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>RegistryStrategy</c> object is used to intercept
   /// the serialization process and delegate to custom converters. The
   /// custom converters are resolved from a <c>Registry</c>
   /// object, which is provided to the constructor. If there is no
   /// binding for a particular object then serialization is delegated
   /// to an internal strategy. All converters resolved by this are
   /// instantiated once and cached internally for performance.
   /// <p>
   /// By default the <c>TreeStrategy</c> is used to perform the
   /// normal serialization process should there be no class binding
   /// specifying a converter to use. However, any implementation can
   /// be used, including the <c>CycleStrategy</c>, which handles
   /// cycles in the object graph. To specify the internal strategy to
   /// use it can be provided in the constructor.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Util.Registry
   /// </seealso>
   public class RegistryStrategy : Strategy {
      /// <summary>
      /// This is the registry that is used to resolve bindings.
      /// </summary>
      private readonly Registry registry;
      /// <summary>
      /// This is the strategy used if there is no bindings.
      /// </summary>
      private readonly Strategy strategy;
      /// <summary>
      /// Constructor for the <c>RegistryStrategy</c> object. This
      /// is used to create a strategy that will intercept the normal
      /// serialization process by searching for bindings within the
      /// provided <c>Registry</c> instance.
      /// </summary>
      /// <param name="registry">
      /// this is the registry instance with bindings
      /// </param>
      public RegistryStrategy(Registry registry) {
         this(registry, new TreeStrategy());
      }
      /// <summary>
      /// Constructor for the <c>RegistryStrategy</c> object. This
      /// is used to create a strategy that will intercept the normal
      /// serialization process by searching for bindings within the
      /// provided <c>Registry</c> instance.
      /// </summary>
      /// <param name="registry">
      /// this is the registry instance with bindings
      /// </param>
      /// <param name="strategy">
      /// this is the strategy to delegate to
      /// </param>
      public RegistryStrategy(Registry registry, Strategy strategy){
         this.registry = registry;
         this.strategy = strategy;
      }
      /// <summary>
      /// This is used to read the <c>Value</c> which will be used
      /// to represent the deserialized object. If there is an binding
      /// present then the value will contain an object instance. If it
      /// does not then it is up to the internal strategy to determine
      /// what the returned value contains.
      /// </summary>
      /// <param name="type">
      /// this is the type that represents a method or field
      /// </param>
      /// <param name="node">
      /// this is the node representing the XML element
      /// </param>
      /// <param name="map">
      /// this is the session map that contain variables
      /// </param>
      /// <returns>
      /// the value representing the deserialized value
      /// </returns>
      public Value Read(Type type, NodeMap<InputNode> node, Dictionary map) {
         Value value = strategy.Read(type, node, map);
         if(IsReference(value)) {
            return value;
         }
         return Read(type, node, value);
      }
      /// <summary>
      /// This is used to read the <c>Value</c> which will be used
      /// to represent the deserialized object. If there is an binding
      /// present then the value will contain an object instance. If it
      /// does not then it is up to the internal strategy to determine
      /// what the returned value contains.
      /// </summary>
      /// <param name="type">
      /// this is the type that represents a method or field
      /// </param>
      /// <param name="node">
      /// this is the node representing the XML element
      /// </param>
      /// <param name="value">
      /// this is the value from the internal strategy
      /// </param>
      /// <returns>
      /// the value representing the deserialized value
      /// </returns>
      public Value Read(Type type, NodeMap<InputNode> node, Value value) {
         Converter converter = Lookup(type, value);
         InputNode source = node.getNode();
         if(converter != null) {
            Object data = converter.Read(source);
            if(value != null) {
               value.setValue(data);
            }
            return new Reference(value, data);
         }
         return value;
      }
      /// <summary>
      /// This is used to serialize a representation of the object value
      /// provided. If there is a <c>Registry</c> binding present
      /// for the provided type then this will use the converter specified
      /// to serialize a representation of the object. If however there
      /// is no binding present then this will delegate to the internal
      /// strategy. This returns true if the serialization has completed.
      /// </summary>
      /// <param name="type">
      /// this is the type that represents the field or method
      /// </param>
      /// <param name="value">
      /// this is the object instance to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element to be serialized to
      /// </param>
      /// <param name="map">
      /// this is the session map used by the serializer
      /// </param>
      /// <returns>
      /// this returns true if it was serialized, false otherwise
      /// </returns>
      public bool Write(Type type, Object value, NodeMap<OutputNode> node, Dictionary map) {
         bool reference = strategy.Write(type, value, node, map);
         if(!reference) {
            return Write(type, value, node);
         }
         return reference;
      }
      /// <summary>
      /// This is used to serialize a representation of the object value
      /// provided. If there is a <c>Registry</c> binding present
      /// for the provided type then this will use the converter specified
      /// to serialize a representation of the object. If however there
      /// is no binding present then this will delegate to the internal
      /// strategy. This returns true if the serialization has completed.
      /// </summary>
      /// <param name="type">
      /// this is the type that represents the field or method
      /// </param>
      /// <param name="value">
      /// this is the object instance to be serialized
      /// </param>
      /// <param name="node">
      /// this is the XML element to be serialized to
      /// </param>
      /// <returns>
      /// this returns true if it was serialized, false otherwise
      /// </returns>
      public bool Write(Type type, Object value, NodeMap<OutputNode> node) {
         Converter converter = Lookup(type, value);
         OutputNode source = node.getNode();
         if(converter != null) {
            converter.Write(source, value);
            return true;
         }
         return false;
      }
      /// <summary>
      /// This is used to acquire a <c>Converter</c> instance for
      /// the provided value object. The value object is used to resolve
      /// the converter to use for the serialization process.
      /// </summary>
      /// <param name="type">
      /// this is the type representing the field or method
      /// </param>
      /// <param name="value">
      /// this is the value that is to be serialized
      /// </param>
      /// <returns>
      /// this returns the converter instance that is matched
      /// </returns>
      public Converter Lookup(Type type, Value value) {
         Class real = type.getType();
         if(value != null) {
            real = value.getType();
         }
         return registry.Lookup(real);
      }
      /// <summary>
      /// This is used to acquire a <c>Converter</c> instance for
      /// the provided object instance. The instance class is used to
      /// resolve the converter to use for the serialization process.
      /// </summary>
      /// <param name="type">
      /// this is the type representing the field or method
      /// </param>
      /// <param name="value">
      /// this is the value that is to be serialized
      /// </param>
      /// <returns>
      /// this returns the converter instance that is matched
      /// </returns>
      public Converter Lookup(Type type, Object value) {
         Class real = type.getType();
         if(value != null) {
            real = value.getClass();
         }
         return registry.Lookup(real);
      }
      /// <summary>
      /// This is used to determine if the <c>Value</c> provided
      /// represents a reference. If it does represent a reference then
      /// this will return true, if it does not then this returns false.
      /// </summary>
      /// <param name="value">
      /// this is the value instance to be evaluated
      /// </param>
      /// <returns>
      /// this returns true if the value represents a reference
      /// </returns>
      public bool IsReference(Value value) {
         return value != null && value.IsReference();
      }
   }
}
