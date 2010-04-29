using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class LinkedMap<K, V> : Map<K, V> {

      private Map<K, Entry<K, V>> map;
      private EntryList<K, V> list;
      private bool cache;

      public LinkedMap() : this(false) {
      }

      public LinkedMap(bool cache) {
         this.map = new HashMap<K, Entry<K, V>>();
         this.list = new EntryList<K, V>(this);
         this.cache = cache;
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

      public override V Get(K key) {
         Entry<K, V> entry = map.Get(key);

         if(entry == null) {
            return default(V);
         }
         if(cache == true) {
            entry.First();
         }
         return entry.Value;
      }

      public override V Put(K key, V value) {
         Entry<K, V> entry = list.Insert(key, value);
         Entry<K, V> item = map.Put(key, entry);
         Entry<K, V> tail = list.Tail;

         if(item == null) {
            bool trim = RemoveEldest(tail);

            if(trim == true) {
               tail.Delete();
            }
            return default(V);
         }
         return item.Delete();
      }

      public override V Remove(K key) {
         Entry<K, V> entry = map.Remove(key);

         if(entry != null) {
            return entry.Delete();
         }
         return default(V);
      }

      private bool RemoveEldest(Entry<K, V> entry) {
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

      private sealed class EntryList<K, V> {
         private Entry<K, V> head;
         private Entry<K, V> tail;
         private Map<K, V> map;

         public EntryList(Map<K, V> map) {
            this.head = new Entry<K, V>(this);
            this.map = map;
         }


         public Entry<K, V> Head {
            get {
               return head;
            }
            set {
               head = value;
            }
         }

         public Entry<K, V> Tail {
            get {
               return tail;
            }
            set {
               tail = value;
            }
         }

         public virtual K[] Keys() {
            K[] list = new K[map.Count];

            if(map.Count > 0 ) {
               head.Keys(list);
            }
            return list;
         }

         public virtual V[] Values() {
            V[] list = new V[map.Count];

            if(map.Count > 0) {
               head.Values(list);
            }
            return list;
         }

         public virtual Entry<K, V> Insert(K key, V value) {
            Entry<K, V> entry = new Entry<K, V>(this, key, value);

            if(head != null) {
               head.Next(entry);
            }
            return entry;
         }
      }

      private sealed class Entry<K, V> {
         private EntryList<K, V> list;
         private Entry<K, V> head;
         private Entry<K, V> next;
         private Entry<K, V> previous;
         private K key;
         private V value;

         public Entry(EntryList<K, V> list) {
            this.head = list.Head;
            this.list = list;
         }
         public Entry(EntryList<K, V> list, K key, V value) {
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
         public void Next(Entry<K, V> entry) {
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
         public void Values(V[] list) {
            for(int i = 0; i < list.Length; i++) {
               list[i] = next.Value;
            }
         }
         public void Keys(K[] list) {
            for(int i = 0; i < list.Length; i++) {
               list[i] = next.Key;
            }
         }
      }
   }
}
