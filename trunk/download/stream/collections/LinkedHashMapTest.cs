﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class LinkedHashMapTest : TestCase {
      public void TestOrder() {
         LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
         map.Put("A", "a");
         map.Put("B", "b");
         map.Put("C", "c");
         map.Put("D", "d");
         map.Put("E", "e");
         AssertEquals(map.Get("A"), "a");
         AssertEquals(map.Get("B"), "b");
         AssertEquals(map.Get("C"), "c");
         AssertEquals(map.Get("D"), "d");
         AssertEquals(map.Get("E"), "e");
         AssertEquals(map.Remove("D"), "d");
         AssertEquals(map.Remove("B"), "b");
         List<String> keys = map.Keys;
         AssertEquals(keys.Count, 3);
         AssertEquals(keys[0], "A");
         AssertEquals(keys[1], "C");
         AssertEquals(keys[2], "E");    
      }

      public void TestCache() {
         LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(true);
         map.Put("A", "a");
         map.Put("B", "b");
         map.Put("C", "c");
         map.Put("D", "d");
         map.Put("E", "e");
         List<String> keys = map.Keys;
         AssertEquals(keys[0], "A");
         AssertEquals(keys[1], "B");
         AssertEquals(keys[2], "C");
         AssertEquals(keys[3], "D");
         AssertEquals(keys[4], "E");    
         AssertEquals(map.Get("E"), "e");
         AssertEquals(map.Get("D"), "d");
         AssertEquals(map.Get("C"), "c");
         AssertEquals(map.Get("B"), "b");
         AssertEquals(map.Get("A"), "a");
         keys = map.Keys;
         AssertEquals(keys[0], "E");
         AssertEquals(keys[1], "D");
         AssertEquals(keys[2], "C");
         AssertEquals(keys[3], "B");
         AssertEquals(keys[4], "A"); 
      }

      public void TestKeyOrder() {
         LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(true);
         map.Put("A", "a");
         map.Put("B", "b");
         map.Put("C", "c");
         map.Put("D", "d");
         AssertEquals(map.Keys[0], "A");
         AssertEquals(map.Keys[1], "B");
         AssertEquals(map.Get("C"), "c");
         map.Put("E", "e");
         map.Put("F", "f");
         AssertEquals(map.Keys[0], "A");
         AssertEquals(map.Keys[1], "B");
         AssertEquals(map.Keys[2], "D");
         AssertEquals(map.Keys[3], "C");
         AssertEquals(map.Keys[4], "E");
         AssertEquals(map.Keys[5], "F");
      }

      public void TestClear() {
         LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(true);
         map.Put("A", "a");
         map.Put("B", "b");
         AssertEquals(map.Size, 2);
         AssertEquals(map.Keys[0], "A");
         AssertEquals(map.Keys[1], "B");
         map.Clear();
         AssertEquals(map.Size, 0);
         AssertEquals(map.Keys.Count, 0);
         map.Put("C", "c");
         map.Put("D", "d");
         AssertEquals(map.Get("C"), "c");
         AssertEquals(map.Get("D"), "d");
         AssertEquals(map["D"], "d");
         AssertEquals(map["C"], "c");
         AssertEquals(map.Keys[0], "D");
         AssertEquals(map.Keys[1], "C");
         map.Clear();
         AssertEquals(map.Size, 0);
         AssertEquals(map.Keys.Count, 0);
      }

      public void TestRemoveEldest() {
         LinkedHashMap<String, String> map = new LeastRecentlyUsedMap<String, String>(2);
         map.Put("A", "a");
         map.Put("B", "b");
         map.Put("C", "c");
         map.Put("D", "d");
         AssertEquals(map.Size, 2);
         AssertEquals(map.Keys[0], "C");
         AssertEquals(map.Keys[1], "D");
         map.Put("E", "e");
         map.Put("F", "f");
         AssertEquals(map.Size, 2);
         AssertEquals(map["E"], "e");
         AssertEquals(map["F"], "f");
         AssertNull(map["C"]);
         AssertNull(map["D"]);
         AssertEquals(map.Keys[0], "E");
         AssertEquals(map.Keys[1], "F");
      }

      private class LeastRecentlyUsedMap<K, V> : LinkedHashMap<K, V> {
         private readonly int capacity;
         public LeastRecentlyUsedMap(int capacity) : base(true) {
            this.capacity = capacity;
         }

         protected override bool RemoveEldest(K key, V value) {
            return Size > capacity;
         }
      }
   }
}
