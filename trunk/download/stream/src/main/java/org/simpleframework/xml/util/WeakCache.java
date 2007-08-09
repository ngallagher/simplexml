/*
 * WeakCache.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package org.simpleframework.xml.util;

import java.util.WeakHashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>WeakCache</code> object is an implementation of a cache
 * that holds on to cached items only if the key remains in memory.
 * This is effectively like a concurrent hash map with weak keys, it
 * ensures that multiple threads can concurrently access weak hash
 * maps in a way that lowers contention for the locks used. 
 * 
 * @author Niall Gallagher
 */
public class WeakCache<K, V> implements Cache<K, V> {
   
   /**
    * This is used to store a list of segments for the cache.
    */
   private SegmentList list;
   
   /**
    * Constructor for the <code>WeakCache</code> object. This is
    * used to create a cache that stores values in such a way that
    * when the key is garbage collected the value is removed from
    * the map. This is similar to the concurrent hash map.
    */
   public WeakCache() {
      this(10);
   }
   
   /**
    * Constructor for the <code>WeakCache</code> object. This is
    * used to create a cache that stores values in such a way that
    * when the key is garbage collected the value is removed from
    * the map. This is similar to the concurrent hash map.
    * 
    * @param size this is the number of segments within the cache
    */
   public WeakCache(int size) {      
      this.list = new SegmentList(size);
   }
   
   /**
    * This method is used to insert a key value mapping in to the
    * cache. The value can later be retrieved or removed from the
    * cache if desired. If the value associated with the key is 
    * null then nothing is stored within the cache.
    * 
    * @param key this is the key to cache the provided value to
    * @param value this is the value that is to be cached
    */
   public void cache(K key, V value) {
      map(key).cache(key, value);
   }
   
   /**
    * This is used to exclusively take the value mapped to the 
    * specified key from the cache. Invoking this is effectively
    * removing the value from the cache.
    * 
    * @param key this is the key to acquire the cache value with
    * 
    * @return this returns the value mapped to the specified key 
    */
   public V take(K key) {
      return map(key).take(key);
   }
   
   /**
    * This method is used to get the value from the cache that is
    * mapped to the specified key. If there is no value mapped to
    * the specified key then this method will return a null.
    * 
    * @param key this is the key to acquire the cache value with
    * 
    * @return this returns the value mapped to the specified key 
    */
   public V fetch(K key) {
      return map(key).fetch(key);
   }
   
   /**
    * This method is used to acquire a <code>Segment</code> using
    * the keys has code. This method effectively uses the hash to
    * find a specific segment within the fixed list of segments.
    * 
    * @param key this is the key used to acquire the segment
    * 
    * @return this returns the segment used to get acquire value
    */
   private Segment map(K key) {
      return list.get(key);
   }   
   
   /**
    * This is used to maintain a list of segments. All segments that
    * are stored by this object can be acquired using a given key.
    * The keys hash is used to select the segment, this ensures that
    * all read and write operations with the same key result in the
    * same segment object within this list.
    * 
    * @author Niall Gallagher
    */
   private class SegmentList {
      
      /**
       * The list of segment objects maintained by this object.
       */
      private List<Segment> list;
      
      /**
       * Represents the number of segments this object maintains.
       */
      private int size;
      
      /**
       * Constructor for the <code>SegmentList</code> object. This
       * is used to create a list of weak hash maps that can be
       * acquired using the hash code of a given key. 
       * 
       * @param size this is the number of hash maps to maintain
       */
      public SegmentList(int size) {
         this.list = new ArrayList<Segment>();
         this.size = size;
         this.create(size);         
      }
      
      /**
       * This is used to acquire the segment using the given key.
       * The keys hash is used to determine the index within the 
       * list to acquire the segment, which is a synchronized weak        
       * hash map storing the key value pairs for a given hash.
       * 
       * @param key this is the key used to determine the segment
       * 
       * @return the segment that is stored at the resolved hash 
       */
      public Segment get(K key) {
         int segment = segment(key);
         
         if(segment < size) {
            return list.get(segment);
         }
         return null;
      }
      
      /**
       * Upon initialization the segment list is populated in such
       * a way that synchronization is not needed. Each segment is
       * created and stored in an increasing index within the list.
       * 
       * @param size this is the number of segments to be used
       */
      private void create(int size) {
         int count = size;
         
         while(count-- > 0) {
            list.add(new Segment());
         }
      }
      
      /**
       * This method performs the translation of the key hash code
       * to the segment index within the list. Translation is done
       * by acquiring the modulus of the hash and the list size.
       * 
       * @param key this is the key used to resolve the index
       * 
       * @return the index of the segment within the list 
       */
      private int segment(K key) {
         return key.hashCode() % size;
      }
   }
   
   /**
    * The segment is effectively a synchronized weak hash map. If is
    * used to store the key value pairs in such a way that they are
    * kept only as long as the garbage collector does not collect 
    * the key. This ensures the cache does not cause memory issues. 
    * 
    * @author Niall Gallagher
    */
   private class Segment extends WeakHashMap<K, V> {      
      
      /**
       * This method is used to insert a key value mapping in to the
       * cache. The value can later be retrieved or removed from the
       * cache if desired. If the value associated with the key is 
       * null then nothing is stored within the cache.
       * 
       * @param key this is the key to cache the provided value to
       * @param value this is the value that is to be cached
       */
      public synchronized void cache(K key, V value) {
         put(key, value);
      }
      
      /**
       * This method is used to get the value from the cache that is
       * mapped to the specified key. If there is no value mapped to
       * the specified key then this method will return a null.
       * 
       * @param key this is the key to acquire the cache value with
       * 
       * @return this returns the value mapped to the specified key 
       */
      public synchronized V fetch(K key) {
         return get(key);
      }
      
      /**
       * This is used to exclusively take the value mapped to the 
       * specified key from the cache. Invoking this is effectively
       * removing the value from the cache.
       * 
       * @param key this is the key to acquire the cache value with
       * 
       * @return this returns the value mapped to the specified key 
       */
      public synchronized V take(K key) {
         return remove(key);
      }
   }
}
