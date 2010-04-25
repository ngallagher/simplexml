#region License
//
// AnnotationStrategy.cs January 2010
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
   /// The <c>AnnotationStrategy</c> object is used to intercept
   /// the serialization process and delegate to custom converters. This
   /// strategy uses the <c>Convert</c> annotation to specify the
   /// converter to use for serialization and deserialization. If there
   /// is no annotation present on the field or method representing the
   /// object instance to be serialized then this acts as a transparent
   /// proxy to an internal strategy.
   /// <p>
   /// By default the <c>TreeStrategy</c> is used to perform the
   /// normal serialization process should there be no annotation
   /// specifying a converter to use. However, any implementation can
   /// be used, including the <c>CycleStrategy</c>, which handles
   /// cycles in the object graph. To specify the internal strategy to
   /// use it can be provided in the constructor.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.TreeStrategy
   /// </seealso>
   public class AnnotationStrategy : Strategy {
      /// <summary>
      /// This is used to scan for an annotation and create a converter.
      /// </summary>
      private readonly ConverterScanner scanner;
      /// <summary>
      /// This is the strategy that is delegated to for serialization.
      /// </summary>
      private readonly Strategy strategy;
      /// <summary>
      /// Constructor for the <c>AnnotationStrategy</c> object.
      /// This creates a strategy that intercepts serialization on any
      /// annotated method or field. If no annotation exists then this
      /// delegates to an internal <c>TreeStrategy</c> object.
      /// </summary>
      public AnnotationStrategy() {
         this(new TreeStrategy());
      }
      /// <summary>
      /// Constructor for the <c>AnnotationStrategy</c> object.
      /// This creates a strategy that intercepts serialization on any
      /// annotated method or field. If no annotation exists then this
      /// will delegate to the <c>Strategy</c> provided.
      /// </summary>
      /// <param name="strategy">
      /// the internal strategy to delegate to
      /// </param>
      public AnnotationStrategy(Strategy strategy) {
         this.scanner = new ConverterScanner();
         this.strategy = strategy;
      }
      /// <summary>
      /// This is used to read the <c>Value</c> which will be used
      /// to represent the deserialized object. If there is an annotation
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
      /// to represent the deserialized object. If there is an annotation
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
         Converter converter = scanner.GetConverter(type, value);
         InputNode parent = node.getNode();
         if(converter != null) {
            Object data = converter.Read(parent);
            if(value != null) {
               value.setValue(data);
            }
            return new Reference(value, data);
         }
         return value;
      }
      /// <summary>
      /// This is used to serialize a representation of the object value
      /// provided. If there is a <c>Convert</c> annotation present
      /// on the provided type then this will use the converter specified
      /// to serialize a representation of the object. If however there
      /// is no annotation then this will delegate to the internal
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
      /// provided. If there is a <c>Convert</c> annotation present
      /// on the provided type then this will use the converter specified
      /// to serialize a representation of the object. If however there
      /// is no annotation then this will delegate to the internal
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
         Converter converter = scanner.GetConverter(type, value);
         OutputNode parent = node.getNode();
         if(converter != null) {
            converter.Write(parent, value);
            return true;
         }
         return false;
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
