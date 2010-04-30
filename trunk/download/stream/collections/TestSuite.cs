using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class TestSuite {
      public Type[] Suite() {
         return new Type[] {
            typeof(HashMapTest),
            typeof(LinkedHashMapTest)

         };
      }
   }
}
