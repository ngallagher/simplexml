/*
 * Cache.java July 2006
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

/**
 * The <code>Cache</code> interface is used to represent a cache
 * that will store key value pairs. The cache exposes only several
 * methods to ensure that implementations can focus on performance
 * concerns rather than how to manage the cached values.
 * 
 * @author Niall Gallagher
 */
public interface Cache<K, V> {
   
   /**
    * This method is used to insert a key value mapping in to the
    * cache. The value can later be retrieved or removed from the
    * cache if desired. If the value associated with the key is 
    * null then nothing is stored within the cache.
    * 
    * @param key this is the key to cache the provided value to
    * @param value this is the value that is to be cached
    */
   public void cache(K key, V value);
 
   /**
    * This is used to exclusively take the value mapped to the 
    * specified key from the cache. Invoking this is effectively
    * removing the value from the cache.
    * 
    * @param key this is the key to acquire the cache value with
    * 
    * @return this returns the value mapped to the specified key 
    */
   public V take(K key);
   
   /**
    * This method is used to get the value from the cache that is
    * mapped to the specified key. If there is no value mapped to
    * the specified key then this method will return a null.
    * 
    * @param key this is the key to acquire the cache value with
    * 
    * @return this returns the value mapped to the specified key 
    */
   public V fetch(K key);   
   
   /**
    * This is used to determine whether the specified key exists
    * with in the cache. Typically this can be done using the 
    * fetch method, which will acquire the object. 
    * 
    * @param key this is the key to check within this segment
    * 
    * @return true if the specified key is within the cache
    */
   public boolean contains(K key);
}