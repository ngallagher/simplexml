#region License
//
// WriteState.cs April 2007
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
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   /// <summary>
   /// The <c>WriteState</c> object is used to store all graphs that
   /// are currently been written with a given cycle strategy. The goal of
   /// this object is to act as a temporary store for graphs such that
   /// when the persistence session has completed the write graph will be
   /// garbage collected. This ensures that there are no lingering object
   /// reference that could cause a memory leak. If a graph for the given
   /// session key does not exist then a new one is created.
   /// </summary>
   /// <seealso>
   /// SimpleFramework.Xml.Strategy.WriteGraph
   /// </seealso>
   class WriteState : WeakCache<Object, WriteGraph> {
      /// <summary>
      /// This is the contract that specifies the attributes to use.
      /// </summary>
      private Contract contract;
      /// <summary>
      /// Constructor for the <c>WriteState</c> object. This is
      /// used to create graphs that are used for writing objects to the
      /// the XML document. The specified strategy is used to acquire the
      /// names of the special attributes used during the serialization.
      /// </summary>
      /// <param name="contract">
      /// this is the name scheme used by the strategy
      /// </param>
      public WriteState(Contract contract) {
         this.contract = contract;
      }
      /// <summary>
      /// This will acquire the graph using the specified session map. If
      /// a graph does not already exist mapped to the given session then
      /// one will be created and stored with the key provided. Once the
      /// specified key is garbage collected then so is the graph object.
      /// </summary>
      /// <param name="map">
      /// this is typically the persistence session map used
      /// </param>
      /// <returns>
      /// returns a graph used for writing the XML document
      /// </returns>
      public WriteGraph Find(Object map) {
         WriteGraph write = fetch(map);
         if(write == null) {
            write = new WriteGraph(contract);
            cache(map, write);
         }
         return write;
      }
   }
}
