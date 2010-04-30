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
   public class LinkedHashMap<K, V> : Map<K, V> {

      private readonly Map<K, Entry> map;
      private readonly EntryList list;
      private readonly bool cache;

      public LinkedHashMap() : this(false) {
      }

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

      protected virtual bool RemoveEldest(K key, V value) {
         return false;
      }

      private sealed class EntryList {
         private Map<K, Entry> map;
         private Entry head;
         private Entry tail;

         public EntryList(Map<K, Entry> map) {
            this.head = new Entry(this);
            this.map = map;
         }


         public Entry Head {
            get {
               return head;
            }
            set {
               head = value;
            }
         }

         public Entry Tail {
            get {
               return tail;
            }
            set {
               tail = value;
            }
         }

         public K[] Keys() {
            K[] list = new K[map.Size];

            if(map.Size > 0 ) {
               head.Keys(list);
            }
            return list;
         }

         public V[] Values() {
            V[] list = new V[map.Size];

            if(map.Size > 0) {
               head.Values(list);
            }
            return list;
         }

         public Entry Insert(K key, V value) {
            Entry entry = new Entry(this, key, value);

            if(head != null) {
               head.Next(entry);
            }
            return entry;
         }

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
         /// <return>
         /// This is used to return the key associated with this entry.
         /// </return>
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
         /// <return>
         /// This is used to return the value associated with this entry.
         /// </return>
         public V Value {
            get {
               return value;
            }
         }


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

         public void Values(V[] array) {
            Entry entry = list.Tail;

            for(int i = 0; i < array.Length; i++) {
               if(entry != null) {
                  array[i] = entry.value;
                  entry = entry.previous;
               }
            }
         }

         public void Keys(K[] array) {
            Entry entry = list.Tail;

            for(int i = 0; i < array.Length; i++) {
               if(entry != null) {
                  array[i] = entry.key;
                  entry = entry.previous;
               }
            }
         }

         public override String ToString() {
            return String.Format("{0}.{1}", key, value);
         }
      }
   }
}
