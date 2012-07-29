package org.simpleframework.xml.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LinkedCache<T> extends LinkedHashMap<Object, T> implements Cache<T> {
   
   private final int capacity;
   
   public LinkedCache() {
      this(50000);
   }
   
   public LinkedCache(int capacity) {
      this.capacity = capacity;
   }

   public void cache(Object key, T value) {
      put(key, value);
   }

   public T take(Object key) {
      return remove(key);
   }

   public T fetch(Object key) {
      return get(key);
   }

   public boolean contains(Object key) {
      return containsKey(key);
   }
   
   protected boolean removeEldestEntry(Entry<Object, T> entry) {
      return size() > capacity;
   }
}
