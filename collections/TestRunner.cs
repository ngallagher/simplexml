using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SimpleFramework.Xml {
   public class TestRunner {
      public static void Main(String[] list) {
         TestSuite suite = new TestSuite();
         Type[] types = suite.Suite();
         foreach (Type type in types) {
            Object value = Activator.CreateInstance(type);
            TestCase test = value as TestCase;
            if (test != null) {
               test.Run();
            }
         }
      }
   }
}
