using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SimpleFramework.Xml.Filter;
using SimpleFramework.Xml.Stream
;
namespace SimpleFramework.Xml {
   public class TestSuite {
      public Type[] Suite() {
         return new Type[] {
            typeof(StackFilterTest),
            typeof(StyleTest)
         };
      }
   }
}
