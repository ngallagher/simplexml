#region Using directives
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class ConverterFactoryTest : TestCase {
      private static class A : Converter {
         public Object Read(InputNode node) {
            return null;
         }
         public void Write(OutputNode node, Object value) {
            return;
         }
      }
      private static class B : A {}
      private static class C : A {}
      public void TestFactory() {
         ConverterFactory factory = new ConverterFactory();
         Converter a1 = factory.getInstance(A.class);
         Converter b1 = factory.getInstance(B.class);
         Converter c1 = factory.getInstance(C.class);
         Converter a2 = factory.getInstance(A.class);
         Converter b2 = factory.getInstance(B.class);
         Converter c2 = factory.getInstance(C.class);
         assertTrue(a1 == a2);
         assertTrue(b1 == b2);
         assertTrue(c1 == c2);
         AssertEquals(a1.getClass(), A.class);
         AssertEquals(b1.getClass(), B.class);
         AssertEquals(c1.getClass(), C.class);
      }
   }
}
