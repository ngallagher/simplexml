using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public abstract class Map<K, V> {
      public abstract int Count {
         get;
      }
      public abstract bool Empty {
         get;
      }
      public abstract K[] Keys {
         get;
      }
      public abstract V this[K key] {
         get;
         set;
      }
      public abstract V Get(K key);
      public abstract V Remove(K key);
      public abstract V Put(K key, V value);
      public abstract bool Contains(K key);
      public abstract void Clear();
   }
}
