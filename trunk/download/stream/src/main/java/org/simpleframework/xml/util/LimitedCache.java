/*
 * LinkedCache.java July 2012
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * The <code>LimitedCache</code> interface is used to represent a 
 * cache that will store key value pairs. This implementation is
 * backed by a <code>LinkedHashMap</code> so that only a specific
 * number of elements can be stored in the cache at one time.
 * 
 * @author Niall Gallagher
 */   
public class LimitedCache<T> extends LinkedHashMap<Object, T> implements Cache<T> {
   
   /**
    * This represents the capacity of this cache instance.
    */
   private final int capacity;
   
   /**
    * Constructor of the <code>LimitedCache</code> object. This is
    * used to create a cache with a fixed size. The strategy for
    * this cache is least recently used. Any insert or fetch from
    * the cache is considered to be a use.
    */
   public LimitedCache() {
      this(50000);
   }
   
   /**
    * Constructor of the <code>LimitedCache</code> object. This is
    * used to create a cache with a fixed size. The strategy for
    * this cache is least recently used. Any insert or fetch from
    * the cache is considered to be a use.
    *
    * @param capacity this is the capacity of the cache object
    */
   public LimitedCache(int capacity) {
      this.capacity = capacity;
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
   public void cache(Object key, T value) {
      put(key, value);
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
   public T take(Object key) {
      return remove(key);
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
   public T fetch(Object key) {
      return get(key);
   }

   /**
    * This is used to determine whether the specified key exists
    * with in the cache. Typically this can be done using the 
    * fetch method, which will acquire the object. 
    * 
    * @param key this is the key to check within this segment
    * 
    * @return true if the specified key is within the cache
    */
   public boolean contains(Object key) {
      return containsKey(key);
   }
   
   /**
    * This is used to remove the eldest entry from the cache.
    * The eldest entry is removed from the cache if the size of
    * the map grows larger than the maximum entries permitted.
    *
    * @param entry this is the eldest entry that can be removed
    *
    * @return this returns true if the entry should be removed
    */ 
   protected boolean removeEldestEntry(Entry<Object, T> entry) {
      return size() > capacity;
   }
}
