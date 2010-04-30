#region License
//
// LinkedHashMap.cs May 2010
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
   /// The <c>LinkedHashMap</c> object represents an ordered map that can
   /// be used as conventional map or a least recently used cache. Using
   /// this implementation offers advantages such as the ability to order
   /// the keys by insertion. Order is reflected in the <c>Keys</c> method
   /// and can be used within a <c>foreach</c> loop to traverse the key
   /// value pairs in a predictable manner. 
   /// <p>
   /// This offers a substitute for the <c>LinkedHashMap</c> available in
   /// the Java collections framework. It is used to aid the porting of
   /// Java projects to .NET where the linked hash map is heavily used.
   /// Operations on this map are constant time regardless of size.
   /// </summary>
   public class LinkedHashMap<K, V> : Map<K, V> {

      /// <summary>
      /// This is the map that maintains the mappings for this instance.
      /// </summary>
      private readonly Map<K, Entry> map;

      /// <summary>
      /// This is the linked list that is used to bind the entries.
      /// </summary>
      private readonly EntryList list;

      /// <summary>
      /// This determines if this is a least recently used cache map.
      /// </summary>
      private readonly bool cache;

      /// <summary>
      /// Constructor for the <c>LinkedHashMap</c> object.
      /// </summary>
      public LinkedHashMap() : this(false) {
      }

      /// <summary>
      /// Constructor for the <c>LinkedHashMap</c> object.
      /// </summary>
      /// <param name="cache">
      /// If this is true then this map is to be used as a cache map
      /// </param>
      public LinkedHashMap(bool cache) {
         this.map = new HashMap<K, Entry>();
         this.list = new EntryList(map);
         this.cache = cache;
      }

      public override int Size {
         get {
            return map.Size;
         }
      }

      public override K[] Keys {
         get {
            return list.Keys();
         }
      }

      public override V[] Values {
         get {
            return list.Values();
         }
      }

      public override V Get(K key) {
         Entry entry = map.Get(key);

         if(entry == null) {
            return default(V);
         }
         if(cache == true) {
            entry.First();
         }
         return entry.Value;
      }

      public override V Put(K key, V value) {
         Entry entry = list.Insert(key, value);
         Entry item = map.Put(key, entry);
         Entry tail = list.Tail;

         if(item == null) {
            bool trim = RemoveEldest(tail);

            if(trim == true) {
               Remove(tail.Key);
            }
            return default(V);
         }
         return item.Delete();
      }

      public override V Remove(K key) {
         Entry entry = map.Remove(key);

         if(entry != null) {
            return entry.Delete();
         }
         return default(V);
      }

      public override bool Contains(K key) {
         return map.Contains(key);
      }
      public override void Clear() {
         list.Clear();
         map.Clear();
      }

      private bool RemoveEldest(Entry entry) {
         V value = entry.Value;
         K key = entry.Key;

         if(cache == true) {
            return RemoveEldest(key, value);
         }
         return false;
      }

      /// <summary>
      /// This is used to determine if the eldest entry is to be removed
      /// from the list. To ensure this can be used like a least recently
      /// used cache the <c>LinkedHashMap</c> should be extended such 
      /// that this will return true if the map reaches a certain size.
      /// </summary>
      /// <param name="key">
      /// Represents the key for the eldest entry within this map.
      /// </param>
      /// <param name="value">
      /// Represents the value for the eldest entry within this map.
      /// </param>
      /// <returns>
      /// If true then the eldest entry will be removed from this map.
      /// </returns>
      protected virtual bool RemoveEldest(K key, V value) {
         return false;
      }

      /// <summary>
      /// The <c>EntryList</c> object represents a linked list that is used
      /// to bind the hash map together. Maintaining a linked list is done
      /// so that acquiring the eldest entry is a constant time operation.
      /// Also, moving the entry to the top of the list and removing entries
      /// is constant time. Finally, building the key and value arrays can
      /// be done in an expected order according to addition to the map.
      /// </summary>
      private sealed class EntryList {

         /// <summary>
         /// Represents the map that is used to maintain the entries.
         /// </summary>
         private Map<K, Entry> map;

         /// <summary>
         /// Represents a pointer to the first entry within the linked list.
         /// </summary>
         private Entry head;

         /// <summary>
         /// Represents the last entry within this linked list instance.
         /// </summary>
         private Entry tail;

         /// <summary>
         /// Constructor for the <c>EntryList</c> object. This creates a
         /// linked list that binds the entries that are maintained within
         /// the provided map. The map is required so that access to the
         /// number of entries is maintained in a single place.
         /// </summary>
         /// <param name="map">
         /// Represents the map that this list is binding entries for.
         /// </param>
         public EntryList(Map<K, Entry> map) {
            this.head = new Entry(this);
            this.map = map;
         }

         /// <summary>
         /// The head represents a pointer to the first entry within this
         /// list. Acquiring the head is used when the list needs to be
         /// traversed. The head is a fixed pointer that can not be set.
         /// </summary>
         public Entry Head {
            get {
               return head;
            }
         }

         /// <summary>
         /// The tail represents a pointer to the last entry within this
         /// list. Acquiring the tail is used when the list needs to be
         /// traversed. The tail can be set an also nulled for deletion.
         /// </summary>
         public Entry Tail {
            get {
               return tail;
            }
            set {
               tail = value;
            }
         }

         /// <summary>
         /// This is used to acquire the keys for each mapping within the
         /// list. Acquiring the key mappings in this way ensures that the
         /// array of values will be in the order they have been added to
         /// the linked list.
         /// </summary>
         /// <returns>
         /// Provides an ordered array of keys from the mappings created.
         /// </returns>
         public K[] Keys() {
            K[] list = new K[map.Size];

            if(map.Size > 0 ) {
               head.Keys(list);
            }
            return list;
         }

         /// <summary>
         /// This is used to acquire the values for each mapping within the
         /// list. Acquiring the value mappings in this way ensures that the
         /// array of values will be in the order they have been added to
         /// the linked list.
         /// </summary>
         /// <returns>
         /// Provides an ordered array of values from the mappings created.
         /// </returns>
         public V[] Values() {
            V[] list = new V[map.Size];

            if(map.Size > 0) {
               head.Values(list);
            }
            return list;
         }

         /// <summary>
         /// This is used to insert the provided key value pair as an entry
         /// to the linked list. The created <c>Entry</c> is positioned at
         /// the top of the list. Positioning in this way ensures that key
         /// value pairs can be acquired in the order they were inserted.
         /// </summary>
         /// <param name="key">
         /// Represents the key that is used to establish the mapping.
         /// </param>
         /// <param name="value">
         /// Represents the value that is to be mapped to the provided key.
         /// </param>
         /// <returns>
         /// Returns the entry that is created for this key value pair.
         /// </returns>
         public Entry Insert(K key, V value) {
            Entry entry = new Entry(this, key, value);

            if(head != null) {
               head.Next(entry);
            }
            return entry;
         }

         /// <summary>
         /// This is used to clear the linked list. Clearing the list is
         /// done by cutting the list at the head and nulling the tail of
         /// of the list. This ensures it is eligible for collection by 
         /// the garbage collector and that traversal can not be done.
         /// </summary>
         public void Clear() {
            if(head != null) {
               head.Next(null);
            }
            tail = null;
         }
      }

      /// <summary>
      /// The <c>Entry</c> represents a link within the linked list that is
      /// used to bind this hash map together. Each entry contains the key
      /// and value associated with the mapping. As well as maintain the
      /// key value pair, it also contains links for a bi-directional linked
      /// list. Maintaining a doubly linked list allows entries to be added
      /// and replaced with a constant time operation regardless of the 
      /// number of links within the list. 
      /// </summary>
      private sealed class Entry {

         /// <summary>
         /// This is the linked list that is used to manage the entries.
         /// </summary>
         private EntryList list;

         /// <summary>
         /// This represents a pointer to the first entry of the list.
         /// </summary>
         private Entry head;

         /// <summary>
         /// This represents the next entry in the list relative to this.
         /// </summary>
         private Entry next;

         /// <summary>
         /// This represents the previous entry relative to this one.
         /// </summary>
         private Entry previous;

         /// <summary>
         /// This is the key associated with this key value pair.
         /// </summary>
         private K key;

         /// <summary>
         /// This represents the value that has been mapped to the key.
         /// </summary>
         private V value;

         /// <summary>
         /// Constructor for the <c>Entry</c> object. This is a constructor
         /// for the list entry that is used to create an entry without a 
         /// key value pair. Typically this is used to create the head of
         /// the linked list.
         /// </summary>
         /// <param name="list">
         /// This represents the source object for the linked list entry.
         /// </param>
         public Entry(EntryList list) {
            this.head = list.Head;
            this.list = list;
         }

         /// <summary>
         /// Constructor for the <c>Entry</c> object. This is a constructor 
         /// for the list entry that is used to create an entry that has
         /// a key value pair. A typical entry within this linked list is
         /// created using this constructor.
         /// </summary>
         /// <param name="list">
         /// This represents the source object for the linked list entry.
         /// </param>
         /// <param name="key">
         /// This is the key that identifies this entry within the map.
         /// </param>
         /// <param name="value">
         /// This is the value that is linked to the associated entry key.
         /// </param>
         public Entry(EntryList list, K key, V value) {
            this.head = list.Head;
            this.list = list;
            this.key = key;
            this.value = value;
         }

         /// <summary>
         /// Provides the key associated with this <c>Entry</c> in the list.
         /// If this is the head of the list this returns null, otherwise
         /// this will return the key associated with this list entry.
         /// </summary>
         public K Key {
            get {
               return key;
            }
         }

         /// <summary>
         /// Provides the value associated with this <c>Entry</c> in the 
         /// list. If this is the head of the list this returns null. This
         /// can also return null if the entry is a mapping for a null.
         /// </summary>
         public V Value {
            get {
               return value;
            }
         }

         /// <summary>
         /// This is used to move this <c>Entry</c> to the top of the list.
         /// Moving the entry to the top of the list is used when the map
         /// is acting as a least recently used cache. It ensures that 
         /// when an entry is accessed frequently it will not be removed.
         /// </summary>
         public void First() {
            if(next != null) {
               next.previous = previous;
            }
            if(previous != null) {
               previous.next = next;
            }
            if(next == null) {
               list.Tail = previous;
            }
            head.Next(this);
         }

         /// <summary>
         /// This is used to position the provided <c>Entry</c> to the
         /// entry after this node. Positioning a node in this manner is
         /// used to add new nodes to the head of the linked list. If 
         /// the provided entry is null this cuts the list at this node.
         /// </summary>
         /// <param name="entry">
         /// The node that is to be positioned after this node.
         /// </param>
         public void Next(Entry entry) {
            if(next != null) {
               next.previous = entry;
            }
            if(entry != null) {
               entry.next = next;
               entry.previous = this;
            }
            if(next == null) {
               list.Tail = entry;
            }
            next = entry;
         }

         /// <summary>
         /// This is used to delete the node from the list. When deleting 
         /// the entry from the list this will repair the list and join
         /// it at this point. Removing the node is required if there is
         /// a removal from the source map.
         /// </summary>
         /// <returns>
         /// Returns the value of the entry that is to be deleted.
         /// </returns>
         public V Delete() {
            if(next != null) {
               next.previous = previous;
            }
            if(previous != null) {
               previous.next = next;
            }
            if(next == null) {
               list.Tail = previous;
            }
            return value;
         }

         /// <summary>
         /// This is used to acquire the values for each mapping within the
         /// list. Acquiring the value mappings in this way ensures that the
         /// array of values will be in the order they have been added to
         /// the linked list.
         /// </summary>
         /// <param name="array">
         /// Provides an ordered array of values from the mappings created.
         /// </param>
         public void Values(V[] array) {
            Entry entry = list.Tail;

            for(int i = 0; i < array.Length; i++) {
               if(entry != null) {
                  array[i] = entry.value;
                  entry = entry.previous;
               }
            }
         }

         /// <summary>
         /// This is used to acquire the keys for each mapping within the
         /// list. Acquiring the key mappings in this way ensures that the
         /// array of values will be in the order they have been added to
         /// the linked list.
         /// </summary>
         /// <param name="array">
         /// Provides an ordered array of keys from the mappings created.
         /// </param>
         public void Keys(K[] array) {
            Entry entry = list.Tail;

            for(int i = 0; i < array.Length; i++) {
               if(entry != null) {
                  array[i] = entry.key;
                  entry = entry.previous;
               }
            }
         }
      }
   }
}
