#region License
//
// HashMap.cs May 2010
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
using System.Collections.Generic;
#endregion
namespace SimpleFramework.Xml {
   public class HashMap<K, V> : Map<K, V> {
      private readonly Dictionary<K, V> table;
      public HashMap() {
         this.table = new Dictionary<K, V>();
      }
      public override int Size {
         get {
            return table.Count;
         }
      }
      public override K[] Keys {
         get {
            K[] list = new K[Size];

            if(list.Length > 0) {
               table.Keys.CopyTo(list, 0);
            }
            return list;
         }
      }
      public override V[] Values {
         get {
            V[] list = new V[Size];

            if(list.Length > 0) {
               table.Values.CopyTo(list, 0);
            }
            return list;
         }
      }
      public override V Get(K key) {
         bool exists = Contains(key);

         if(exists == true) {
            return table[key];
         }
         return default(V);
      }
      public override V Put(K key, V value) {
         V entry = Get(key);

         if(table != null) {
            table.Add(key, value);
         }
         return entry;
      }
      public override V Remove(K key) {
         V value = Get(key);

         if(value != null) {
            table.Remove(key);
         }
         return value;
      }
      public override bool Contains(K key) {
         return table.ContainsKey(key);
      }
      public override void Clear() {
         table.Clear();
      }
   }
}
