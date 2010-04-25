#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class InjectTest : TestCase {
      private const String SOURCE =
      "<example>"+
      "   <name>Some Name</name>"+
      "   <value>Some Value</value>"+
      "</example>";
      @Root
      private static class InjectExample {
         @Element
         private String name;
         @Element
         private String value;
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
      public void TestInject() {
         Persister persister = new Persister();
         InjectExample example = new InjectExample();
         persister.read(example, SOURCE);
         assertEquals(example.Name, "Some Name");
         assertEquals(example.Value, "Some Value");
      }
   }
}
