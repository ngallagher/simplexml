#region License
//
// CycleStrategy.cs April 2007
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
   import static SimpleFramework.Xml.Strategy.Name.LABEL;
   import static SimpleFramework.Xml.Strategy.Name.LENGTH;
   import static SimpleFramework.Xml.Strategy.Name.MARK;
   import static SimpleFramework.Xml.Strategy.Name.REFER;
   /// <summary>
   /// The <c>CycleStrategy</c> represents a strategy that is used
   /// to augment the deserialization and serialization process such that
   /// cycles in an object graph can be supported. This adds additional
   /// attributes to the serialized XML elements so that during the
   /// deserialization process an objects cycles can be created. Without
   /// the use of a strategy such as this, cycles could cause an infinite
   /// loop during the serialization process while traversing the graph.
   /// </code>
   ///    &lt;root id="1"&gt;
   ///       &lt;object id="2"&gt;
   ///          &lt;object id="3" name="name"&gt;Example&lt;/item&gt;
   ///          &lt;object reference="2"/&gt;
   ///       &lt;/object&gt;
   ///    &lt;/root&gt;
   /// </code>
   /// In the above serialized XML there is a circular reference, where
   /// the XML element with id "2" contains a reference to itself. In
   /// most data binding frameworks this will cause an infinite loop,
   /// or in some cases will just fail to represent the references well.
   /// With this strategy you can ensure that cycles in complex object
   /// graphs will be maintained and can be serialized safely.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Core.Persister
   /// </seealso>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.Strategy
   /// </seealso>
   public class CycleStrategy : Strategy {
      /// <summary>
      /// This is used to maintain session state for writing the graph.
      /// </summary>
      private readonly WriteState write;
      /// <summary>
      /// This is used to maintain session state for reading the graph.
      /// </summary>
      private readonly ReadState read;
      /// <summary>
      /// This is used to provide the names of the attributes to use.
      /// </summary>
      private readonly Contract contract;
      /// <summary>
      /// Constructor for the <c>CycleStrategy</c> object. This is
      /// used to create a strategy with default values. By default the
      /// values used are "id" and "reference". These values will be
      /// added to XML elements during the serialization process. And
      /// will be used to deserialize the object cycles fully.
      /// </summary>
      public CycleStrategy() {
         this(MARK, REFER);
      }
      /// <summary>
      /// Constructor for the <c>CycleStrategy</c> object. This is
      /// used to create a strategy with the specified attributes, which
      /// will be added to serialized XML elements. These attributes
      /// are used to serialize the objects in such a way the cycles in
      /// the object graph can be deserialized and used fully.
      /// </summary>
      /// <param name="mark">
      /// this is used to mark the identity of an object
      /// </param>
      /// <param name="refer">
      /// this is used to refer to an existing object
      /// </param>
      public CycleStrategy(String mark, String refer) {
         this(mark, refer, LABEL);
      }
      /// <summary>
      /// Constructor for the <c>CycleStrategy</c> object. This is
      /// used to create a strategy with the specified attributes, which
      /// will be added to serialized XML elements. These attributes
      /// are used to serialize the objects in such a way the cycles in
      /// the object graph can be deserialized and used fully.
      /// </summary>
      /// <param name="mark">
      /// this is used to mark the identity of an object
      /// </param>
      /// <param name="refer">
      /// this is used to refer to an existing object
      /// </param>
      /// <param name="label">
      /// this is used to specify the class for the field
      /// </param>
      public CycleStrategy(String mark, String refer, String label){
         this(mark, refer, label, LENGTH);
      }
      /// <summary>
      /// Constructor for the <c>CycleStrategy</c> object. This is
      /// used to create a strategy with the specified attributes, which
      /// will be added to serialized XML elements. These attributes
      /// are used to serialize the objects in such a way the cycles in
      /// the object graph can be deserialized and used fully.
      /// </summary>
      /// <param name="mark">
      /// this is used to mark the identity of an object
      /// </param>
      /// <param name="refer">
      /// this is used to refer to an existing object
      /// </param>
      /// <param name="label">
      /// this is used to specify the class for the field
      /// </param>
      /// <param name="length">
      /// this is the length attribute used for arrays
      /// </param>
      public CycleStrategy(String mark, String refer, String label, String length){
         this.contract = new Contract(mark, refer, label, length);
         this.write = new WriteState(contract);
         this.read = new ReadState(contract);
      }
      /// <summary>
      /// This method is used to read an object from the specified node.
      /// In order to get the root type the field and node map are
      /// specified. The field represents the annotated method or field
      /// within the deserialized object. The node map is used to get
      /// the attributes used to describe the objects identity, or in
      /// the case of an existing object it contains an object reference.
      /// </summary>
      /// <param name="type">
      /// the method or field in the deserialized object
      /// </param>
      /// <param name="node">
      /// this is the XML element attributes to read
      /// </param>
      /// <param name="map">
      /// this is the session map used for deserialization
      /// </param>
      /// <returns>
      /// this returns an instance to insert into the object
      /// </returns>
      public Value Read(Type type, NodeMap node, Dictionary map) {
         ReadGraph graph = read.Find(map);
         if(graph != null) {
            return graph.Read(type, node);
         }
         return null;
      }
      /// <summary>
      /// This is used to write the reference in to the XML element that
      /// is to be written. This will either insert an object identity if
      /// the object has not previously been written, or, if the object
      /// has already been written in a previous element, this will write
      /// the reference to that object. This allows all cycles within the
      /// graph to be serialized so that they can be fully deserialized.
      /// </summary>
      /// <param name="type">
      /// the type of the field or method in the object
      /// </param>
      /// <param name="value">
      /// this is the actual object that is to be written
      /// </param>
      /// <param name="node">
      /// this is the XML element attribute map to use
      /// </param>
      /// <param name="map">
      /// this is the session map used for the serialization
      /// </param>
      /// <returns>
      /// returns true if the object has been fully serialized
      /// </returns>
      public bool Write(Type type, Object value, NodeMap node, Dictionary map) {
         WriteGraph graph = write.Find(map);
         if(graph != null) {
            return graph.Write(type, value, node);
         }
         return false;
      }
   }
}
