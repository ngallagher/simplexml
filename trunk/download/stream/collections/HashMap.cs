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
      public virtual int Size {
         get {
            return table.Count;
         }
      }

      /// <summary>
      /// This is used to determine if the map is empty or not. This is 
      /// used to indicate whether there are any mappings within the map.
      /// To empty the map the <c>Clear</c> method can be used. 
      /// </summary>
      /// <returns>
      /// True if there are no mappings within the map, false otherwise.
      /// </returns>
      public virtual bool Empty {
         get {
            return Size == 0;
         }
      }

      /// <summary>
      /// This is used to return a list of keys. Depending on the map
      /// implementation keys may be returned in a random order or in an
      /// ordered manner. Keys are returned as a list so that can be
      /// used in a <c>foreach</c> loop. 
      /// </summary>
      /// <returns>
      /// An list of keys representing the keys for each mapping.
      /// </returns>
      public virtual List<K> Keys {
         get {
            List<K> list = new List<K>(Size);

            foreach(K key in table.Keys) {
               list.Add(key);
            }
            return list;
         }
      }

      /// <summary>
      /// This is used to return a list of values. Depending on the map
      /// implementation values may be returned in a random order This is 
      /// provided for convenient use in a <c>foreach</c> loop.
      /// </summary>
      /// <returns>
      /// A list of values that have been mapped within this instance.
      /// </returns>
      public virtual List<V> Values {
         get {
            List<V> list = new List<V>(Size);

            foreach(V value in table.Values) {
               list.Add(value);
            }
            return list;
         }
      }

      /// <summary>
      /// Provides an indexer used to get and set values for this map. 
      /// This indexer offers a more convenient way to get and set mappings
      /// than the <c>Get</c> and <c>Put</c> methods. If no mappin exists
      /// for the specified key then this will return a null value.
      /// </summary>
      /// <param name="key">
      /// The key is used to identify the mapping within this instance.
      /// </param>
      /// <returns>
      /// An array of values that have been mapped within this instance.
      /// </returns>
      public virtual V this[K key] {
         get {
            return Get(key);
         }
         set {
            Put(key, value);
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
      public virtual V Get(K key) {
         bool exists = ContainsKey(key);

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
      public virtual V Put(K key, V value) {
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
      public virtual V Remove(K key) {
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
      public virtual bool ContainsKey(K key) {
         return table.ContainsKey(key);
      }

      /// <summary>
      /// This is used to determine if a value exists within the map. If
      /// this returns true then the <c>Values</c> property will return the
      /// specified value within the array of keys. The map may contain 
      /// may equal values within the map.
      /// </summary>
      /// <param name="value">
      /// This is the value to be searched for within this map instance.
      /// </param>
      /// <returns>
      /// This will return true if the value exists within the instance.
      /// </returns>
      public virtual bool ContainsValue(V value) {
         return table.ContainsValue(value);
      }

      /// <summary>
      /// This is used to clear the <c>Map</c> instance. Clearing the map
      /// instance ensures that there are no mappings maintained. Further
      /// attempts to get via the <c>Get</c> method will return null.
      /// </summary>
      public virtual void Clear() {
         table.Clear();
      }
   }
}
