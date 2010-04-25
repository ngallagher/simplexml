#region License
//
// TreeStrategy.cs July 2006
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
using SimpleFramework.Xml.Stream;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   import static SimpleFramework.Xml.Strategy.Name.LABEL;
   import static SimpleFramework.Xml.Strategy.Name.LENGTH;
   /// <summary>
   /// The <c>TreeStrategy</c> object is used to provide a simple
   /// strategy for handling object graphs in a tree structure. This does
   /// not resolve cycles in the object graph. This will make use of the
   /// specified class attribute to resolve the class to use for a given
   /// element during the deserialization process. For the serialization
   /// process the "class" attribute will be added to the element specified.
   /// If there is a need to use an attribute name other than "class" then
   /// the name of the attribute to use can be specified.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.CycleStrategy
   /// </seealso>
   public class TreeStrategy : Strategy {
      /// <summary>
      /// This is the loader that is used to load the specified class.
      /// </summary>
      private readonly Loader loader;
      /// <summary>
      /// This is the attribute that is used to determine an array size.
      /// </summary>
      private readonly String length;
      /// <summary>
      /// This is the attribute that is used to determine the real type.
      /// </summary>
      private readonly String label;
      /// <summary>
      /// Constructor for the <c>TreeStrategy</c> object. This
      /// is used to create a strategy that can resolve and load class
      /// objects for deserialization using a "class" attribute. Also
      /// for serialization this will add the appropriate "class" value.
      /// </summary>
      public TreeStrategy() {
         this(LABEL, LENGTH);
      }
      /// <summary>
      /// Constructor for the <c>TreeStrategy</c> object. This
      /// is used to create a strategy that can resolve and load class
      /// objects for deserialization using the specified attribute.
      /// The attribute value can be any legal XML attribute name.
      /// </summary>
      /// <param name="label">
      /// this is the name of the attribute to use
      /// </param>
      /// <param name="length">
      /// this is used to determine the array length
      /// </param>
      public TreeStrategy(String label, String length) {
         this.loader = new Loader();
         this.length = length;
         this.label = label;
      }
      /// <summary>
      /// This is used to resolve and load a class for the given element.
      /// Resolution of the class to used is done by inspecting the
      /// XML element provided. If there is a "class" attribute on the
      /// element then its value is used to resolve the class to use.
      /// If no such attribute exists on the element this returns null.
      /// </summary>
      /// <param name="type">
      /// this is the type of the XML element expected
      /// </param>
      /// <param name="node">
      /// this is the element used to resolve an override
      /// </param>
      /// <param name="map">
      /// this is used to maintain contextual information
      /// </param>
      /// <returns>
      /// returns the class that should be used for the object
      /// </returns>
      public Value Read(Type type, NodeMap node, Dictionary map) {
         Class actual = ReadValue(type, node);
         Class expect = type.getType();
         if(expect.isArray()) {
            return ReadArray(actual, node);
         }
         if(expect != actual) {
            return new ObjectValue(actual);
         }
         return null;
      }
      /// <summary>
      /// This is used to resolve and load a class for the given element.
      /// Resolution of the class to used is done by inspecting the
      /// XML element provided. If there is a "class" attribute on the
      /// element then its value is used to resolve the class to use.
      /// This also expects a "length" attribute for the array length.
      /// </summary>
      /// <param name="type">
      /// this is the type of the XML element expected
      /// </param>
      /// <param name="node">
      /// this is the element used to resolve an override
      /// </param>
      /// <returns>
      /// returns the class that should be used for the object
      /// </returns>
      public Value ReadArray(Class type, NodeMap node) {
         Node entry = node.remove(length);
         int size = 0;
         if(entry != null) {
            String value = entry.getValue();
            size = Integer.parseInt(value);
         }
         return new ArrayValue(type, size);
      }
      /// <summary>
      /// This is used to resolve and load a class for the given element.
      /// Resolution of the class to used is done by inspecting the
      /// XML element provided. If there is a "class" attribute on the
      /// element then its value is used to resolve the class to use.
      /// If no such attribute exists the specified field is returned,
      /// or if the field type is an array then the component type.
      /// </summary>
      /// <param name="type">
      /// this is the type of the XML element expected
      /// </param>
      /// <param name="node">
      /// this is the element used to resolve an override
      /// </param>
      /// <returns>
      /// returns the class that should be used for the object
      /// </returns>
      public Class ReadValue(Type type, NodeMap node) {
         Node entry = node.remove(label);
         Class expect = type.getType();
         if(expect.isArray()) {
            expect = expect.getComponentType();
         }
         if(entry != null) {
            String name = entry.getValue();
            expect = loader.Load(name);
         }
         return expect;
      }
      /// <summary>
      /// This is used to attach a attribute to the provided element
      /// that is used to identify the class. The attribute name is
      /// "class" and has the value of the fully qualified class
      /// name for the object provided. This will only be invoked
      /// if the object class is different from the field class.
      /// </summary>
      /// <param name="type">
      /// this is the declared class for the field used
      /// </param>
      /// <param name="value">
      /// this is the instance variable being serialized
      /// </param>
      /// <param name="node">
      /// this is the element used to represent the value
      /// </param>
      /// <param name="map">
      /// this is used to maintain contextual information
      /// </param>
      /// <returns>
      /// this returns true if serialization is complete
      /// </returns>
      public bool Write(Type type, Object value, NodeMap node, Dictionary map) {
         Class actual = value.getClass();
         Class expect = type.getType();
         Class real = actual;
         if(actual.isArray()) {
            real = WriteArray(expect, value, node);
         }
         if(actual != expect) {
            node.put(label, real.getName());
         }
         return false;
      }
      /// <summary>
      /// This is used to add a length attribute to the element due to
      /// the fact that the serialized value is an array. The length
      /// of the array is acquired and inserted in to the attributes.
      /// </summary>
      /// <param name="field">
      /// this is the field type for the array to set
      /// </param>
      /// <param name="value">
      /// this is the actual value for the array to set
      /// </param>
      /// <param name="node">
      /// this is the map of attributes for the element
      /// </param>
      /// <returns>
      /// returns the array component type that is set
      /// </returns>
      public Class WriteArray(Class field, Object value, NodeMap node) {
         int size = Array.getLength(value);
         if(length != null) {
            node.put(length, String.valueOf(size));
         }
         return field.getComponentType();
      }
   }
}
