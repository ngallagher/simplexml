package org.simpleframework.xml.util;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCache<T> extends ConcurrentHashMap<Object, T> implements Cache<T> {
   
   public ConcurrentCache() {
      super();
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
}
