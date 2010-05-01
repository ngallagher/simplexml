﻿#region License
//
// Cache.cs July 2006
//
// Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//

#endregion
#region Using directives
using System;
#endregion

namespace SimpleFramework.Xml {

   /// <summary>
   /// The <c>Cache</c> interface is used to represent a cache
   /// that will store key value pairs. The cache exposes only several
   /// methods to ensure that implementations can focus on performance
   /// concerns rather than how to manage the cached values.
   /// </summary>
   public abstract class Cache<K, V> {

      /// <summary>
      /// This method is used to insert a key value mapping in to the
      /// cache. The value can later be retrieved or removed from the
      /// cache if desired. If the value associated with the key is
      /// null then nothing is stored within the cache.
      /// </summary>
      /// <param name="key">
      /// This is the key to cache the provided value to.
      /// </param>
      /// <param name="value">
      /// This is the value that is to be cached.
      /// </param>
      public abstract V Insert(K key, V value);

      /// <summary>
      /// This is used to exclusively take the value mapped to the
      /// specified key from the cache. Invoking this is effectively
      /// removing the value from the cache.
      /// </summary>
      /// <param name="key">
      /// This is the key to acquire the cache value with.
      /// </param>
      /// <returns>
      /// This returns the value mapped to the specified key.
      /// </returns>
      public abstract V Take(K key);

      /// <summary>
      /// This method is used to get the value from the cache that is
      /// mapped to the specified key. If there is no value mapped to
      /// the specified key then this method will return a null.
      /// </summary>
      /// <param name="key">
      /// This is the key to acquire the cache value with.
      /// </param>
      /// <returns>
      /// This returns the value mapped to the specified key.
      /// </returns>
      public abstract V Fetch(K key);

      /// <summary>
      /// This is used to determine whether the specified key exists
      /// with in the cache. Typically this can be done using the
      /// fetch method, which will acquire the object.
      /// </summary>
      /// <param name="key">
      /// This is the key to check within this segment.
      /// </param>
      /// <returns>
      /// True if the specified key is within the cache.
      /// </returns>
      public abstract bool Contains(K key);
   }
}
