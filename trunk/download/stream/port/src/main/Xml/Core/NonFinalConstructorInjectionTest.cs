#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NonFinalConstructorInjectionTest : ValidationTestCase {
      [Root]
      private static class NonFinalExample {
         [Element]
         private String name;
         [Element]
         private String value;
         public NonFinalExample(@Element(name="name") String name, @Element(name="value") String value) {
            this.name = name;
            this.value = value;
         }
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
            return value;
         }
      }
      public void TestNonFinal() {
         Persister persister = new Persister();
         NonFinalExample example = new NonFinalExample("A", "a");
         validate(example, persister);
      }
   }
}
