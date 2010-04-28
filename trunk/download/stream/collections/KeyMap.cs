using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class KeyMap<T> : Map<Key<T>, T> {
      protected readonly Dictionary<Object, T> table;
      protected readonly KeyList<T> order;
      public KeyMap() {
         this.table = new Dictionary<Object, T>();
         this.order = new KeyList<T>();
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

      public override Key<T>[] Keys {
         get {
            return order.ToArray();
         }
      }

      public override T this[Key<T> key] {
         get {
            return Get(key);
         }
         set {
            Put(key, value);
         }
      }

      public override T Get(Key<T> key) {
         bool exists = Contains(key);

         if(exists) {
            return table[key.Reference];
         }
         return default(T);
      }

      public virtual T Put(Key<T> key, T value) {
         T entry = Get(key);

         if(table != null) {
            table.Add(key.Reference, value);
         }
         return entry;
      }

      public override T Remove(Key<T> key) {
         T value = Get(key);

         if(value != null) {
            table.Remove(key.Reference);
         }
         return value;
      }

      public override bool Contains(Key<T> key) {
         return table.ContainsKey(key.Reference);
      }

      public override void Clear() {
         table.Clear();
      }
      
      protected internal class KeyList<T> : List<Key<T>> {
         public KeyList() {
         }
      }  
   }

   public abstract class Key<T> {
      private readonly Object key;
      protected Key() {
         this.key = new Object();
      }
      public Object Reference {
         get {
            return key;
         }
      }
   }
}

