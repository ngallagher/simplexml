#region License
//
// WriteGraph.cs April 2007
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
   /// The <c>WriteGraph</c> object is used to build the graph that
   /// is used to represent the serialized object and its references. The
   /// graph is stored in an <c>IdentityHashMap</c> which will
   /// store the objects in such a way that this graph object can tell if
   /// it has already been written to the XML document. If an object has
   /// already been written to the XML document an reference attribute
   /// is added to the element representing the object and serialization
   /// of that object is complete, that is, no more elements are written.
   /// <p>
   /// The attribute values written by this are unique strings, which
   /// allows the deserialization process to identify object references
   /// easily. By default these references are incrementing integers
   /// however for deserialization they can be any unique string value.
   /// </summary>
   class WriteGraph : IdentityHashMap<Object, String> {
      /// <summary>
      /// This is used to specify the length of array instances.
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
      /// Constructor for the <c>WriteGraph</c> object. This is
      /// used to build the graph used for writing objects to the XML
      /// document. The specified strategy is used to acquire the names
      /// of the special attributes used during the serialization.
      /// </summary>
      /// <param name="contract">
      /// this is the name scheme used by the strategy
      /// </param>
      public WriteGraph(Contract contract) {
         this.refer = contract.getReference();
         this.mark = contract.getIdentity();
         this.length = contract.getLength();
         this.label = contract.getLabel();
      }
      /// <summary>
      /// This is used to write the XML element attributes representing
      /// the serialized object instance. If the object has already been
      /// serialized to the XML document then a reference attribute is
      /// inserted and this returns true, if not, then this will write
      /// a unique identity marker attribute and return false.
      /// </summary>
      /// <param name="type">
      /// this is the type of the object to be serialized
      /// </param>
      /// <param name="value">
      /// this is the instance that is to be serialized
      /// </param>
      /// <param name="node">
      /// this is the node that contains the attributes
      /// </param>
      /// <returns>
      /// returns true if the element has been fully written
      /// </returns>
      public bool Write(Type type, Object value, NodeMap node) {
         Class actual = value.getClass();
         Class expect = type.getType();
         Class real = actual;
         if(actual.isArray()) {
            real = WriteArray(actual, value, node);
         }
         if(actual != expect) {
            node.put(label, real.getName());
         }
         return WriteReference(value, node);
      }
      /// <summary>
      /// This is used to write the XML element attributes representing
      /// the serialized object instance. If the object has already been
      /// serialized to the XML document then a reference attribute is
      /// inserted and this returns true, if not, then this will write
      /// a unique identity marker attribute and return false.
      /// </summary>
      /// <param name="value">
      /// this is the instance that is to be serialized
      /// </param>
      /// <param name="node">
      /// this is the node that contains the attributes
      /// </param>
      /// <returns>
      /// returns true if the element has been fully written
      /// </returns>
      public bool WriteReference(Object value, NodeMap node) {
         String name = get(value);
         int size = size();
         if(name != null) {
            node.put(refer, name);
            return true;
         }
         String unique = String.valueOf(size);
         node.put(mark, unique);
         put(value, unique);
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
         if(!containsKey(value)) {
            node.put(length, String.valueOf(size));
         }
         return field.getComponentType();
      }
   }
}
