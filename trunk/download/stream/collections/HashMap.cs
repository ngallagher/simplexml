#region License
//
// HashMap.cs May 2010
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
using System;
using System.Collections.Generic;
#endregion

namespace SimpleFramework.Xml {

   /// <summary>
   /// The <c>HashMap</c> class represents a data structure that maintains 
   /// mappings between key value pairs. Unlike the <c>Dictionary</c> that
   /// is provided with the standard .NET collections this is not use fail
   /// fast lookups. If a mapping is requested that does not exist a null
   /// is returned. This is similar to the Java <c>Map</c> collection but
   /// is stripped down to the most basic operations.
   /// <p>
   /// In addition to the standard Java collection methods this map has an
   /// indexer that accepts a key, which makes getting and setting mappings
   /// much easier. A <c>HashMap</c> instance must be qualified with key 
   /// and value generic parameters. This is backed using the standard .NET
   /// dictionary and acts as an adapter to facilitate ease of porting.
   /// </summary>
   public class HashMap<K, V> : Map<K, V> {

      /// <summary>
      /// This is the dictionary that backs this hash table instance.
      /// </summary>
      private readonly Dictionary<K, V> table;

      /// <summary>
      /// Constructor for the <c>HashMap</c> object. This is used to
      /// create a map using the .NET dictionary implementation. All of
      /// the operations performed on this map are delegated to the 
      /// underlying dictionary instance. This is an unsynchronized map.
      /// </summary>
      public HashMap() {
         this.table = new Dictionary<K, V>();
      }

      /// <summary>
      /// This is used to determine the number of mappings that have been
      /// inserted in to the map. If size is zero <c>Empty</c> will be
      /// true. This can be used to iterate over the key and value arrays.
      /// </summary>
      /// <returns>
      /// The name number of mappings that have been added to the map.
      /// </returns>
      public override int Size {
         get {
            return table.Count;
         }
      }

      /// <summary>
      /// This is used to return an array of keys. Depending on the map
      /// implementation keys may be returned in a random order or in an
      /// ordered manner. Keys are returned as an array so that can be
      /// used in a <c>foreach</c> loop. 
      /// </summary>
      /// <returns>
      /// An array of keys representing the keys for each mapping.
      /// </returns>
      public override K[] Keys {
         get {
            K[] list = new K[Size];

            if(list.Length > 0) {
               table.Keys.CopyTo(list, 0);
            }
            return list;
         }
      }

      /// <summary>
      /// This is used to return an array of values. Depending on the map
      /// implementation values may be returned in a random order This is 
      /// provided for convenient use in a <c>foreach</c> loop.
      /// </summary>
      /// <returns>
      /// An array of values that have been mapped within this instance.
      /// </returns>
      public override V[] Values {
         get {
            V[] list = new V[Size];

            if(list.Length > 0) {
               table.Values.CopyTo(list, 0);
            }
            return list;
         }
      }

      /// <summary>
      /// This is used to acquire the value associated with the specified
      /// key. If there is no mapping within the <c>Map</c> this returns 
      /// null. Given that mappings can be made to a null value this can
      /// not be used to determine if a mapping exists within the instance.
      /// </summary>
      /// <param name="key">
      /// This is the key that is used to acquire the mapped value.
      /// </param>
      /// <returns>
      /// This is used to acquire the value associated with the key.
      /// </returns>
      public override V Get(K key) {
         bool exists = Contains(key);

         if(exists == true) {
            return table[key];
         }
         return default(V);
      }

      /// <summary>
      /// This is used to establish a mapping within the map. If a mapping
      /// already exists with the specified key then this will return the
      /// value that was previously associated with the key. Once mapped
      /// the <c>Get</c> method or indexer can be used to get the value.
      /// </summary>
      /// <param name="key">
      /// This is the key that is used to establish the mapping.
      /// </param>
      /// <param name="value">
      /// This is the value that will be associated with the given key.
      /// </param>
      /// <returns>
      /// Returns the value previously associated with the key, or null.
      /// </returns>
      public override V Put(K key, V value) {
         V entry = Remove(key);

         if(table != null) {
            table.Add(key, value);
         }
         return entry;
      }

      /// <summary>
      /// This is used to remove a mapping from the <c>Map</c>. This acts
      /// much like a take method would for a cache, in that it will 
      /// the value of the mapping that has just been removed. If the
      /// mapping did not exist then this method will return null.
      /// </summary>
      /// <param name="key">
      /// This is the key to be searched for within this map instance.
      /// </param>
      /// <returns>
      /// This returns the value associated with the mapping if it exists.
      /// </returns>
      public override V Remove(K key) {
         V value = Get(key);

         if(value != null) {
            table.Remove(key);
         }
         return value;
      }

      /// <summary>
      /// This is used to determine if a mapping exists within the map. If
      /// this returns true then the <c>Keys</c> property will return the
      /// specified key within the array of keys. However, if the key is 
      /// mapped to null then the <c>Get</c> method will return null.
      /// </summary>
      /// <param name="key">
      /// This is the key to be searched for within this map instance.
      /// </param>
      /// <returns>
      /// This will return true if the mapping exists within the instance.
      /// </returns>
      public override bool Contains(K key) {
         return table.ContainsKey(key);
      }

      /// <summary>
      /// This is used to clear the <c>Map</c> instance. Clearing the map
      /// instance ensures that there are no mappings maintained. Further
      /// attempts to get via the <c>Get</c> method will return null.
      /// </summary>
      public override void Clear() {
         table.Clear();
      }
   }
}
