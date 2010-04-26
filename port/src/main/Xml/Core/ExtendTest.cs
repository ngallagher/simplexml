#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ExtendTest : TestCase {
      private const String FIRST =
      "<?xml version=\"1.0\"?>\n"+
      "<root id='12'>\n"+
      "   <text>entry text</text>  \n\r"+
      "</root>";
      private const String SECOND =
      "<?xml version=\"1.0\"?>\n"+
      "<root id='12'>\n"+
      "   <text>entry text</text>  \n\r"+
      "   <name>some name</name> \n\r"+
      "</root>";
      private const String THIRD =
      "<?xml version=\"1.0\"?>\n"+
      "<override id='12' flag='true'>\n"+
      "   <text>entry text</text>  \n\r"+
      "   <name>some name</name> \n"+
      "   <third>added to schema</third>\n"+
      "</override>";
      [Root(Name="root")]
      private static class First {
         [Attribute(Name="id")]
         public int id;
         [Element(Name="text")]
         public String text;
      }
      private static class Second : First {
         [Element(Name="name")]
         public String name;
      }
      [Root(Name="override")]
      private static class Third : Second {
         [Attribute(Name="flag")]
         public bool bool;
         [Element(Name="third")]
         public String third;
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestFirst() {
         First first = serializer.Read(First.class, new StringReader(FIRST));
         AssertEquals(first.id, 12);
         AssertEquals(first.text, "entry text");
      }
      public void TestSecond() {
         Second second = serializer.Read(Second.class, new StringReader(SECOND));
         AssertEquals(second.id, 12);
         AssertEquals(second.text, "entry text");
         AssertEquals(second.name, "some name");
      }
      public void TestThird() {
         Third third = serializer.Read(Third.class, new StringReader(THIRD));
         AssertEquals(third.id, 12);
         AssertEquals(third.text, "entry text");
         AssertEquals(third.name, "some name");
         AssertEquals(third.third, "added to schema");
         assertTrue(third.bool);
      }
      public void TestFailure() {
         bool fail = false;
         try {
            Third third = serializer.Read(Third.class, new StringReader(SECOND));
         }catch(Exception e) {
            fail = true;
         }
         assertTrue(fail);
      }
   }
}
