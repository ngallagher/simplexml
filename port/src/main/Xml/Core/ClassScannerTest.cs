#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ClassScannerTest : TestCase {
      [Root]
      [Order(Elements={"a", "b"}, Attributes={"A", "B"})]
      [Namespace(Prefix="prefix", Reference="http://domain/reference")]
      private static class Example {
         [Commit]
         public void Commit() {
            return;
         }
         [Validate]
         public void Validate() {
            return;
         }
      }
      public void TestClassScanner() {
         ClassScanner scanner = new ClassScanner(Example.class);
         assertNotNull(scanner.getRoot());
         assertNotNull(scanner.getOrder());
      }
   }
}
