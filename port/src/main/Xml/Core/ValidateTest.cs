#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ValidateTest : ValidationTestCase {
      private const String VERSION_MISSING =
      "<test>\n"+
      "   <array length='3'>\n"+
      "     <entry name='a' version='ONE'>Example 1</entry>\r\n"+
      "     <entry name='b' version='TWO'>Example 2</entry>\r\n"+
      "     <entry name='c'>Example 3</entry>\r\n"+
      "   </array>\n\r"+
      "</test>";
      private const String NAME_MISSING =
      "<test>\n"+
      "   <array length='3'>\n"+
      "     <entry version='ONE'>Example 1</entry>\r\n"+
      "     <entry name='b' version='TWO'>Example 2</entry>\r\n"+
      "     <entry name='c' version='THREE'>Example 3</entry>\r\n"+
      "   </array>\n\r"+
      "</test>";
      private const String TEXT_MISSING =
      "<test>\n"+
      "   <array length='3'>\n"+
      "     <entry name='a' version='ONE'>Example 1</entry>\r\n"+
      "     <entry name='b' version='TWO'>Example 2</entry>\r\n"+
      "     <entry name='c' version='THREE'/>\r\n"+
      "   </array>\n\r"+
      "</test>";
      private const String EXTRA_ELEMENT =
      "<test>\n"+
      "   <array length='3'>\n"+
      "     <entry name='a' version='ONE'>Example 1</entry>\r\n"+
      "     <entry name='b' version='TWO'>Example 2</entry>\r\n"+
      "     <entry name='c' version='THREE'>Example 3</entry>\r\n"+
      "   </array>\n\r"+
      "   <array length='4'>\n"+
      "     <entry name='f' version='ONE'>Example 4</entry>\r\n"+
      "   </array>\n"+
      "</test>";
      [Root(Name="test")]
      private static class TextList {
         [ElementArray(Name="array", Entry="entry")]
         private TextEntry[] array;
      }
      [Root(Name="text")]
      private static class TextEntry {
         [Attribute(Name="name")]
         private String name;
         [Attribute(Name="version")]
         private Version version;
         [Text(Data=true)]
         private String text;
      }
      private enum Version {
         ONE,
         TWO,
         THREE
      }
      public void TestVersionMissing() {
         Serializer persister = new Persister();
         bool success = false;
         try {
            success = persister.validate(TextList.class, VERSION_MISSING);
         } catch(Exception e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestNameMissing() {
         Serializer persister = new Persister();
         bool success = false;
         try {
            success = persister.validate(TextList.class, NAME_MISSING);
         } catch(Exception e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestTextMissing() {
         Serializer persister = new Persister();
         bool success = false;
         try {
            success = persister.validate(TextList.class, TEXT_MISSING);
         } catch(Exception e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestExtraElement() {
         Serializer persister = new Persister();
         bool success = false;
         try {
            success = persister.validate(TextList.class, EXTRA_ELEMENT);
         } catch(Exception e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
   }
}
