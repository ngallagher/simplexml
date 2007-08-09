package org.simpleframework.xml.util;

import java.util.WeakHashMap;
import java.util.ArrayList;
import java.util.List;

public class WeakCache<K, V> implements Cache<K, V> {
   
   private SegmentList list;
   
   public WeakCache() {
      this(10);
   }
   
   public WeakCache(int size) {
      this.list = new SegmentList(size);
   }
   
   public void cache(K key, V value) {
      map(key).put(key, value);
   }
   
   public V take(K key) {
      return map(key).remove(key);
   }
   
   public V fetch(K key) {
      return map(key).get(key);
   }
   
   private Segment map(K key) {
      return list.get(key);
   }   
   
   private class SegmentList {
      
      private List<Segment> list;
      
      private int size;
      
      public SegmentList(int size) {
         this.list = new ArrayList<Segment>();
         this.size = size;
         this.create(size);         
      }
      
      public Segment get(K key) {
         int segment = segment(key);
         
         if(segment <= size) {
            return list.get(segment);
         }
         return null;
      }
      
      
      private void create(int size) {
         int count = size;
         
         while(count-- > 0) {
            list.add(new Segment());
         }
      }
      
      private int segment(K key) {
         return key.hashCode() % size;
      }
   }
   
   private class Segment extends WeakHashMap<K, V> {
      
      public Segment() {
         super();
      }      
   }
}
