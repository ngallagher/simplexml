#region License
//
// WeakCache.cs July 2006
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
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   /// <summary>
   /// The <c>WeakCache</c> object is an implementation of a cache
   /// that holds on to cached items only if the key remains in memory.
   /// This is effectively like a concurrent hash map with weak keys, it
   /// ensures that multiple threads can concurrently access weak hash
   /// maps in a way that lowers contention for the locks used.
   /// </summary>
   public class WeakCache<K, V> : Cache<K, V> {
      /// <summary>
      /// This is used to store a list of segments for the cache.
      /// </summary>
      private SegmentList list;
      /// <summary>
      /// Constructor for the <c>WeakCache</c> object. This is
      /// used to create a cache that stores values in such a way that
      /// when the key is garbage collected the value is removed from
      /// the map. This is similar to the concurrent hash map.
      /// </summary>
      public WeakCache() {
         this(10);
      }
      /// <summary>
      /// Constructor for the <c>WeakCache</c> object. This is
      /// used to create a cache that stores values in such a way that
      /// when the key is garbage collected the value is removed from
      /// the map. This is similar to the concurrent hash map.
      /// </summary>
      /// <param name="size">
      /// this is the number of segments within the cache
      /// </param>
      public WeakCache(int size) {
         this.list = new SegmentList(size);
      }
      /// <summary>
      /// This method is used to insert a key value mapping in to the
      /// cache. The value can later be retrieved or removed from the
      /// cache if desired. If the value associated with the key is
      /// null then nothing is stored within the cache.
      /// </summary>
      /// <param name="key">
      /// this is the key to cache the provided value to
      /// </param>
      /// <param name="value">
      /// this is the value that is to be cached
      /// </param>
      public void Cache(K key, V value) {
         Map(key).Cache(key, value);
      }
      /// <summary>
      /// This is used to exclusively take the value mapped to the
      /// specified key from the cache. Invoking this is effectively
      /// removing the value from the cache.
      /// </summary>
      /// <param name="key">
      /// this is the key to acquire the cache value with
      /// </param>
      /// <returns>
      /// this returns the value mapped to the specified key
      /// </returns>
      public V Take(K key) {
         return Map(key).Take(key);
      }
      /// <summary>
      /// This method is used to get the value from the cache that is
      /// mapped to the specified key. If there is no value mapped to
      /// the specified key then this method will return a null.
      /// </summary>
      /// <param name="key">
      /// this is the key to acquire the cache value with
      /// </param>
      /// <returns>
      /// this returns the value mapped to the specified key
      /// </returns>
      public V Fetch(K key) {
         return Map(key).Fetch(key);
      }
      /// <summary>
      /// This is used to determine whether the specified key exists
      /// with in the cache. Typically this can be done using the
      /// fetch method, which will acquire the object.
      /// </summary>
      /// <param name="key">
      /// this is the key to check within this segment
      /// </param>
      /// <returns>
      /// true if the specified key is within the cache
      /// </returns>
      public bool Contains(K key) {
         return Map(key).Contains(key);
      }
      /// <summary>
      /// This method is used to acquire a <c>Segment</c> using
      /// the keys has code. This method effectively uses the hash to
      /// find a specific segment within the fixed list of segments.
      /// </summary>
      /// <param name="key">
      /// this is the key used to acquire the segment
      /// </param>
      /// <returns>
      /// this returns the segment used to get acquire value
      /// </returns>
      public Segment Map(K key) {
         return list.Get(key);
      }
      /// <summary>
      /// This is used to maintain a list of segments. All segments that
      /// are stored by this object can be acquired using a given key.
      /// The keys hash is used to select the segment, this ensures that
      /// all read and write operations with the same key result in the
      /// same segment object within this list.
      /// </summary>
      private class SegmentList {
         /// <summary>
         /// The list of segment objects maintained by this object.
         /// </summary>
         private List<Segment> list;
         /// <summary>
         /// Represents the number of segments this object maintains.
         /// </summary>
         private int size;
         /// <summary>
         /// Constructor for the <c>SegmentList</c> object. This
         /// is used to create a list of weak hash maps that can be
         /// acquired using the hash code of a given key.
         /// </summary>
         /// <param name="size">
         /// this is the number of hash maps to maintain
         /// </param>
         public SegmentList(int size) {
            this.list = new ArrayList<Segment>();
            this.size = size;
            this.Create(size);
         }
         /// <summary>
         /// This is used to acquire the segment using the given key.
         /// The keys hash is used to determine the index within the
         /// list to acquire the segment, which is a synchronized weak
         /// hash map storing the key value pairs for a given hash.
         /// </summary>
         /// <param name="key">
         /// this is the key used to determine the segment
         /// </param>
         /// <returns>
         /// the segment that is stored at the resolved hash
         /// </returns>
         public Segment Get(K key) {
            int segment = Segment(key);
            if(segment < size) {
               return list.Get(segment);
            }
            return null;
         }
         /// <summary>
         /// Upon initialization the segment list is populated in such
         /// a way that synchronization is not needed. Each segment is
         /// created and stored in an increasing index within the list.
         /// </summary>
         /// <param name="size">
         /// this is the number of segments to be used
         /// </param>
         public void Create(int size) {
            int count = size;
            while(count-- > 0) {
               list.add(new Segment());
            }
         }
         /// <summary>
         /// This method performs the translation of the key hash code
         /// to the segment index within the list. Translation is done
         /// by acquiring the modulus of the hash and the list size.
         /// </summary>
         /// <param name="key">
         /// this is the key used to resolve the index
         /// </param>
         /// <returns>
         /// the index of the segment within the list
         /// </returns>
         public int Segment(K key) {
            return Math.abs(key.hashCode() % size);
         }
      }
      /// <summary>
      /// The segment is effectively a synchronized weak hash map. If is
      /// used to store the key value pairs in such a way that they are
      /// kept only as long as the garbage collector does not collect
      /// the key. This ensures the cache does not cause memory issues.
      /// </summary>
      private class Segment : WeakHashMap<K, V> {
         /// <summary>
         /// This method is used to insert a key value mapping in to the
         /// cache. The value can later be retrieved or removed from the
         /// cache if desired. If the value associated with the key is
         /// null then nothing is stored within the cache.
         /// </summary>
         /// <param name="key">
         /// this is the key to cache the provided value to
         /// </param>
         /// <param name="value">
         /// this is the value that is to be cached
         /// </param>
         public synchronized void Cache(K key, V value) {
            put(key, value);
         }
         /// <summary>
         /// This method is used to get the value from the cache that is
         /// mapped to the specified key. If there is no value mapped to
         /// the specified key then this method will return a null.
         /// </summary>
         /// <param name="key">
         /// this is the key to acquire the cache value with
         /// </param>
         /// <returns>
         /// this returns the value mapped to the specified key
         /// </returns>
         public synchronized V Fetch(K key) {
            return Get(key);
         }
         /// <summary>
         /// This is used to exclusively take the value mapped to the
         /// specified key from the cache. Invoking this is effectively
         /// removing the value from the cache.
         /// </summary>
         /// <param name="key">
         /// this is the key to acquire the cache value with
         /// </param>
         /// <returns>
         /// this returns the value mapped to the specified key
         /// </returns>
         public synchronized V Take(K key) {
            return remove(key);
         }
         /// <summary>
         /// This is used to determine whether the specified key exists
         /// with in the cache. Typically this can be done using the
         /// fetch method, which will acquire the object.
         /// </summary>
         /// <param name="key">
         /// this is the key to check within this segment
         /// </param>
         /// <returns>
         /// true if the specified key is within the cache
         /// </returns>
         public synchronized bool Contains(K key) {
            return containsKey(key);
         }
      }
   }
}
