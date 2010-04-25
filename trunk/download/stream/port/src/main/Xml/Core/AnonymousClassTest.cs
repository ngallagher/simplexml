#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class AnonymousClassTest : ValidationTestCase {
      @Root(name="anonymous")
      private static class Anonymous {
         @Element
         @Namespace(prefix="prefix", reference="http://www.domain.com/reference")
         private static Object anonymous = new Object() {
            @Attribute(name="attribute")
            private const String attribute = "example attribute";
            @Element(name="element")
            private const String element = "example element";
         };
      }
      /*
       TODO fix this test
      public void TestAnonymousClass() {
         Persister persister = new Persister();
         Anonymous anonymous = new Anonymous();
         validate(persister, anonymous);
      }
      */
      public void TestA() {
   }
}
