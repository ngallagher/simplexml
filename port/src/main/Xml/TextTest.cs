#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class TextTest : ValidationTestCase {
      private const String TEXT_LIST =
      "<test>\n"+
      "   <array length='3'>\n"+
      "     <entry name='a' version='ONE'>Example 1</entry>\r\n"+
      "     <entry name='b' version='TWO'>Example 2</entry>\r\n"+
      "     <entry name='c' version='THREE'>Example 3</entry>\r\n"+
      "   </array>\n\r"+
      "</test>";
      private const String DATA_TEXT =
      "<text name='a' version='ONE'>\r\n"+
      "   <![CDATA[ \n"+
      "      <element> \n"+
      "         <data>This is hidden</data>\n"+
      "      </element> \n"+
      "   ]]>\n"+
      "</text>\r\n";
      private const String DUPLICATE_TEXT =
      "<text name='a' version='ONE'>Example 1</text>\r\n";
      private const String ILLEGAL_ELEMENT =
      "<text name='a' version='ONE'>\r\n"+
      "  Example 1\n\r"+
      "  <illegal>Not allowed</illegal>\r\n"+
      "</text>\r\n";
      private const String EMPTY_TEXT =
      "<text name='a' version='ONE'/>";
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
      [Root(Name="text")]
      private static class OptionalTextEntry {
         [Attribute(Name="name")]
         private String name;
         [Attribute(Name="version")]
         private Version version;
         [Text(Required=false)]
         private String text;
      }
      private static class DuplicateTextEntry : TextEntry {
         [Text]
         private String duplicate;
      }
      private static class IllegalElementTextEntry : TextEntry {
         [Element(Name="illegal")]
         private String name;
      }
      private static class NonPrimitiveTextEntry {
         [Attribute(Name="name")]
         private String name;
         [Attribute(Name="version")]
         private Version version;
         [Text]
         private List list;
      }
      private enum Version {
         ONE,
         TWO,
         THREE
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestText() {
         TextList list = persister.read(TextList.class, TEXT_LIST);
         assertEquals(list.array[0].version, Version.ONE);
         assertEquals(list.array[0].name, "a");
         assertEquals(list.array[0].text, "Example 1");
         assertEquals(list.array[1].version, Version.TWO);
         assertEquals(list.array[1].name, "b");
         assertEquals(list.array[1].text, "Example 2");
         assertEquals(list.array[2].version, Version.THREE);
         assertEquals(list.array[2].name, "c");
         assertEquals(list.array[2].text, "Example 3");
         StringWriter buffer = new StringWriter();
         persister.write(list, buffer);
         validate(list, persister);
         list = persister.read(TextList.class, buffer.toString());
         assertEquals(list.array[0].version, Version.ONE);
         assertEquals(list.array[0].name, "a");
         assertEquals(list.array[0].text, "Example 1");
         assertEquals(list.array[1].version, Version.TWO);
         assertEquals(list.array[1].name, "b");
         assertEquals(list.array[1].text, "Example 2");
         assertEquals(list.array[2].version, Version.THREE);
         assertEquals(list.array[2].name, "c");
         assertEquals(list.array[2].text, "Example 3");
         validate(list, persister);
      }
      public void TestData() {
         TextEntry entry = persister.read(TextEntry.class, DATA_TEXT);
         assertEquals(entry.version, Version.ONE);
         assertEquals(entry.name, "a");
         assertTrue(entry.text != null);
         StringWriter buffer = new StringWriter();
         persister.write(entry, buffer);
         validate(entry, persister);
         entry = persister.read(TextEntry.class, buffer.toString());
         assertEquals(entry.version, Version.ONE);
         assertEquals(entry.name, "a");
         assertTrue(entry.text != null);
         validate(entry, persister);
      }
      public void TestDuplicate() {
         bool success = false;
         try {
            persister.read(DuplicateTextEntry.class, DUPLICATE_TEXT);
         } catch(TextException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestIllegalElement() {
         bool success = false;
         try {
            persister.read(IllegalElementTextEntry.class, ILLEGAL_ELEMENT);
         } catch(TextException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestEmpty() {
         bool success = false;
         try {
            persister.read(TextEntry.class, EMPTY_TEXT);
         } catch(ValueRequiredException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestOptional() {
         OptionalTextEntry entry = persister.read(OptionalTextEntry.class, EMPTY_TEXT);
         assertEquals(entry.version, Version.ONE);
         assertEquals(entry.name, "a");
         assertTrue(entry.text == null);
         StringWriter buffer = new StringWriter();
         persister.write(entry, buffer);
         validate(entry, persister);
         entry = persister.read(OptionalTextEntry.class, buffer.toString());
         assertEquals(entry.version, Version.ONE);
         assertEquals(entry.name, "a");
         assertTrue(entry.text == null);
         validate(entry, persister);
      }
      public void TestNonPrimitive() {
         bool success = false;
         try {
            persister.read(NonPrimitiveTextEntry.class, DATA_TEXT);
         } catch(TextException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
   }
}
