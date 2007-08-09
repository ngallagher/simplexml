package org.simpleframework.xml.util;

public interface Cache<K, V> {
   
   public void cache(K key, V value);
   
   public V take(K key);
   
   public V fetch(K key);   
}
