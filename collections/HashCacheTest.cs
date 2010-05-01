#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml {
   public class HashCacheTest : TestCase {
      private const int LOAD_COUNT = 100000;
      public void TestCache() {
         Cache<String, String> cache = new HashCache<String, String>();
         Map<String, String> map = new HashMap<String, String>();
         for(int i = 0; i < LOAD_COUNT; i++) {
            String key = i.ToString();
            cache.Insert(key, key);
            map.Put(key, key);
         }
         for(int i = 0; i < LOAD_COUNT; i++) {
            String key = i.ToString();
            AssertEquals(cache.Fetch(key), key);
            AssertEquals(map.Get(key), cache.Fetch(key));
         }
      }
   }
}
