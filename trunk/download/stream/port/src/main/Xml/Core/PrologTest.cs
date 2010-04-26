#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class PrologTest : ValidationTestCase {
      private const String SOURCE =
      "<prologExample id='12' flag='true'>\n"+
      "   <text>entry text</text>  \n\r"+
      "   <name>some name</name> \n"+
      "</prologExample>";
      [Root]
      private static class PrologExample {
         [Attribute]
         public int id;
         [Element]
         public String name;
         [Element]
         public String text;
         [Attribute]
         public bool flag;
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister(new Format(4, "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"));
   	}
      public void TestProlog() {
         PrologExample example = serializer.Read(PrologExample.class, SOURCE);
         AssertEquals(example.id, 12);
         AssertEquals(example.text, "entry text");
         AssertEquals(example.name, "some name");
         assertTrue(example.flag);
         StringWriter buffer = new StringWriter();
         serializer.Write(example, buffer);
         String text = buffer.toString();
         assertTrue(text.startsWith("<?xml"));
         Validate(example, serializer);
      }
   }
}
