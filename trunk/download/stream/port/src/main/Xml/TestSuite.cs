using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SimpleFramework.Xml.Util;

namespace SimpleFramework.Xml {
   public class TestSuite {
      public Type[] Suite() {
         return new Type[] {
            typeof(ResolverTest)
         };
      }
   }
}
