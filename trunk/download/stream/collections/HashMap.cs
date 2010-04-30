using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class HashMap<K, V> : Map<K, V> {
      private readonly Dictionary<K, V> table;
      public HashMap() {
         this.table = new Dictionary<K, V>();
      }
      public override int Count {
         get {
            return table.Count;
         }
      }
      public override bool Empty {
         get {
            return Count == 0;
         }
      }
      public override K[] Keys {
         get {
            K[] list = new K[Count];

            if(list.Length > 0) {
               table.Keys.CopyTo(list, 0);
            }
            return list;
         }
      }
      public override V[] Values {
         get {
            V[] list = new V[Count];

            if(list.Length > 0) {
               table.Values.CopyTo(list, 0);
            }
            return list;
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

         if(exists == true) {
            return table[key];
         }
         return default(V);
      }
      public override V Put(K key, V value) {
         V entry = Get(key);

         if(table != null) {
            table.Add(key, value);
         }
         return entry;
      }
      public override V Remove(K key) {
         V value = Get(key);

         if(value != null) {
            table.Remove(key);
         }
         return value;
      }
      public override bool Contains(K key) {
         return table.ContainsKey(key);
      }
      public override void Clear() {
         table.Clear();
      }
   }
}
