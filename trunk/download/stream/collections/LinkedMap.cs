using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class LinkedMap<K, V> : Map<K, V> {

      private Map<K, Entry> map;
      private EntryList list;
      private bool cache;

      public LinkedMap() : this(false) {
      }

      public LinkedMap(bool cache) {
         this.map = new HashMap<K, Entry>();
         this.list = new EntryList(map);
         this.cache = cache;
      }

      public override int Count {
         get {
            return map.Count;
         }
      }

      public override bool Empty {
         get {
            return map.Empty;
         }
      }

      public override K[] Keys {
         get {
            return list.Keys();
         }
      }

      public V[] Values {
         get {
            return list.Values();
         }
      }

      public override V this[K key] {
         get {
            return Get(key);
         }
         set {
            Put(key, value);
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
            K[] list = new K[map.Count];

            if(map.Count > 0 ) {
               head.Keys(list);
            }
            return list;
         }

         public V[] Values() {
            V[] list = new V[map.Count];

            if(map.Count > 0) {
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

      private sealed class Entry {
         private EntryList list;
         private Entry head;
         private Entry next;
         private Entry previous;
         private K key;
         private V value;

         public Entry(EntryList list) {
            this.head = list.Head;
            this.list = list;
         }
         public Entry(EntryList list, K key, V value) {
            this.head = list.Head;
            this.list = list;
            this.key = key;
            this.value = value;
         }
         public K Key {
            get {
               return key;
            }
         }
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
