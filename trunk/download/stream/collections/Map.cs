#region License
//
// Map.cs May 2010
//
// Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
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
   /// The <c>Map</c> class represents a data structure that maintains 
   /// mappings between key value pairs. Unlike the <c>Dictionary</c> that
   /// is provided with the standard .NET collections this is not use fail
   /// fast lookups. If a mapping is requested that does not exist a null
   /// is returned. This is similar to the Java <c>Map</c> collection but
   /// is stripped down to the most basic operations.
   /// <p>
   /// In addition to the standard Java collection methods this map has an
   /// indexer that accepts a key, which makes getting and setting mappings
   /// much easier. A <c>Map</c> instance must be qualified with key and
   /// value generic parameters. Implementations include <c>HashMap</c> 
   /// and <c>LinkedMap</c>, which provides a means to implement both an
   /// ordered map and a least recently used cache if required.
   /// </summary>
   public abstract class Map<K, V> {
      /// <summary>
      /// This is used to determine the number of mappings that have been
      /// inserted in to the map. If count is zero <c>Empty</c> will be
      /// true. This can be used to iterate over the key and value arrays
      /// that are returned from the <c>Keys</c> and <c>Values</c> methods.
      /// </summary>
      /// <returns>
      /// The name number of mappings that have been added to the map.
      /// </returns>
      public abstract int Count {
         get;
      }
      /// <summary>
      /// This is used to determine if the map is empty or not. This is 
      /// used to indicate whether there are any mappings within the map.
      /// To empty the map the <c>Clear</c> method can be used. 
      /// </summary>
      /// <returns>
      /// The name number of mappings that have been added to the map.
      /// </returns>
      public abstract bool Empty {
         get;
      }
      /// <summary>
      /// This is used to return an array of keys. 
      /// </summary>
      /// <returns>
      /// The name number of mappings that have been added to the map.
      /// </returns>
      public abstract K[] Keys {
         get;
      }
      public abstract V[] Values {
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
