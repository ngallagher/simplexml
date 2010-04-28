using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class WeakMap<T> : KeyMap<T> {
      private readonly List<WeakReference> order;
      public WeakMap() {
      }
      public override Key<T>[] Keys {
         get {
            return Available();
         }
      }
      public override T Put(Key<T> key, T value) {
         T entry = Get(key);

         if(table != null) {
            table.Add(key.Reference, value);
         }
         return entry;
      }
      private Key<T>[] Available() {
         KeyList<T> list = new KeyList<T>();
         for(int i = order.Count; i > 0; i--) {
            Key<T> key = Target(order[i - 1]);
            if(key != null) {
               list.Add(key);
            }
         }
         return list.ToArray();
      }
      private Key<T> Target(WeakReference entry) {
         Key<T> key = entry.Target as Key<T>;
         if(key != null) {
            order.Remove(entry);
         }
         return key;
      }
      public void Free(Object key) {
         table.Remove(key);
      }
   }
   public abstract class WeakKey<T> : Key<T> {
      private WeakMap<T> map;
      public WeakKey(WeakMap<T> map) {
         this.map = map;
      }
      public void Finalize() {
         map.Free(Reference);
      }
   }
}
