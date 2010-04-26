#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class WeakCacheTest : TestCase {
      private const int LOAD_COUNT = 100000;
      public void TestCache() {
         WeakCache cache = new WeakCache();
         Dictionary map = new HashMap();
         for(int i = 0; i < LOAD_COUNT; i++) {
            String key = String.valueOf(i);
            cache.cache(key, key);
            map.put(key, key);
         }
         for(int i = 0; i < LOAD_COUNT; i++) {
            String key = String.valueOf(i);
            AssertEquals(cache.fetch(key), key);
            AssertEquals(map.get(key), cache.fetch(key));
         }
      }
   }
}
