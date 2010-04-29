using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class LinkedMapTest : TestCase {
      public void TestOrder() {
         LinkedMap<String, String> map = new LinkedMap<String, String>();
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
         String[] keys = map.Keys;
         AssertEquals(keys.Length, 3);
         AssertEquals(keys[0], "A");
         AssertEquals(keys[1], "C");
         AssertEquals(keys[2], "E");    
      }

      public void TestCache() {
         LinkedMap<String, String> map = new LinkedMap<String, String>(true);
         map.Put("A", "a");
         map.Put("B", "b");
         map.Put("C", "c");
         map.Put("D", "d");
         map.Put("E", "e");
         String[] keys = map.Keys;
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
         LinkedMap<String, String> map = new LinkedMap<String, String>(true);
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
         LinkedMap<String, String> map = new LinkedMap<String, String>(true);
         map.Put("A", "a");
         map.Put("B", "b");
         AssertEquals(map.Count, 2);
         AssertEquals(map.Keys[0], "A");
         AssertEquals(map.Keys[1], "B");
         map.Clear();
         AssertEquals(map.Count, 0);
         AssertEquals(map.Keys.Length, 0);
         map.Put("C", "c");
         map.Put("D", "d");
         AssertEquals(map.Get("C"), "c");
         AssertEquals(map.Get("D"), "d");
         AssertEquals(map["D"], "d");
         AssertEquals(map["C"], "c");
         AssertEquals(map.Keys[0], "D");
         AssertEquals(map.Keys[1], "C");
         map.Clear();
         AssertEquals(map.Count, 0);
         AssertEquals(map.Keys.Length, 0);
      }
   }
}
