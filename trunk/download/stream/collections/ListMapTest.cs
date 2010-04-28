using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class ListMapTest : TestCase {
      public void TestMap() {
         Map<String, String> map = new ListMap<String, String>();
         map["A"] = "a";
         map["B"] = "b";
         map["C"] = "c";
         AssertFalse(map.Empty);
         AssertEquals(map.Count, 3);
         AssertEquals(map["A"], "a");
         AssertEquals(map["B"], "b");
         AssertEquals(map["C"], "c");
         List<String> order = new List<String>();
         foreach(String key in map.Keys) {
            order.Add(key);
         }
         AssertEquals(map.Keys[0], order[0]);
         AssertEquals(map.Keys[1], order[1]);
         AssertEquals(map.Keys[2], order[2]);
         map.Clear();
         AssertTrue(map.Empty);
         AssertEquals(map.Count, 0);
         AssertEquals(map.Keys.Length, 0);
      }
   }
}
