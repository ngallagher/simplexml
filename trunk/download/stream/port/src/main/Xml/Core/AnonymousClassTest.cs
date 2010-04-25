#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class AnonymousClassTest : ValidationTestCase {
      [Root(Name="anonymous")]
      private static class Anonymous {
         @Element
         [Namespace(Prefix="prefix", Reference="http://www.domain.com/reference")]
         private static Object anonymous = new Object() {
            [Attribute(Name="attribute")]
            private const String attribute = "example attribute";
            [Element(Name="element")]
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
