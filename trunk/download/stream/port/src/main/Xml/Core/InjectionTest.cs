#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class InjectionTest : TestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<injectionExample id='12' flag='true'>\n"+
      "   <text>entry text</text>  \n\r"+
      "   <date>01/10/1916</date> \n"+
      "   <message trim='true'>\r\n"+
      "        This is an example message.\r\n"+
      "   </message>\r\n"+
      "</injectionExample>";
      [Root]
      private static class InjectionExample {
         [Attribute]
         private bool flag;
         [Attribute]
         private int id;
         [Element]
         private String text;
         [Element]
         private String date;
         [Element]
         private ExampleMessage message;
         private String name;
         public InjectionExample(String name) {
            this.name = name;
         }
      }
      private static class ExampleMessage {
         [Attribute]
         private bool trim;
         [Text]
         private String text;
         [Commit]
         public void Prepare() {
            if(trim) {
               text = text.trim();
            }
         }
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestFirst() {
         InjectionExample example = new InjectionExample("name");
         AssertEquals(example.flag, false);
         AssertEquals(example.id, 0);
         AssertEquals(example.text, null);
         AssertEquals(example.date, null);
         AssertEquals(example.name, "name");
         AssertEquals(example.message, null);
         InjectionExample result = serializer.Read(example, SOURCE);
         AssertEquals(example, result);
         AssertEquals(example.flag, true);
         AssertEquals(example.id, 12);
         AssertEquals(example.text, "entry text");
         AssertEquals(example.date, "01/10/1916");
         AssertEquals(example.name, "name");
         AssertEquals(example.message.trim, true);
         AssertEquals(example.message.text, "This is an example message.");
      }
   }
}
