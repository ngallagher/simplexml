using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class ListMap<K, V> : Map<K, V> {
      private readonly Dictionary<K, V> table;
      private readonly List<K> order;

      public ListMap() {
         this.table = new Dictionary<K, V>();
         this.order = new List<K>();
      }

      public override int Count {
         get {
            return order.Count;
         }
      }

      public override bool Empty {
         get {
            return Count == 0;
         }
      }

      public virtual K[] Keys {
         get {
            return order.ToArray();
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
         bool exists = Contains(key);

         if(exists) {
            return table[key];
         }
         return default(V);
      }

      public override V Put(K key, V value) {
         V entry = Get(key);

         if(table != null) {
            table.Add(key, value);
            order.Add(key);
         }
         return entry;
      }

      public override V Remove(K key) {
         V value = Get(key);

         if(value != null) {
            table.Remove(key);
            order.Remove(key);
         }
         return value;
      }

      public override bool Contains(K key) {
         return table.ContainsKey(key);
      }

      public override void Clear() {
         table.Clear();
         order.Clear();
      }
   }
}
