#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class InlineTest : ValidationTestCase {
      private const String INLINE_LIST =
      "<test version='ONE'>\n"+
      "   <message>Some example message</message>\r\n"+
      "   <text name='a' version='ONE'>Example 1</text>\r\n"+
      "   <text name='b' version='TWO'>Example 2</text>\r\n"+
      "   <text name='c' version='THREE'>Example 3</text>\r\n"+
      "</test>";
      private const String INLINE_PRIMITIVE_LIST =
      "<test version='ONE'>\n"+
      "   <message>Some example message</message>\r\n"+
      "   <string>Example 1</string>\r\n"+
      "   <string>Example 2</string>\r\n"+
      "   <string>Example 3</string>\r\n"+
      "</test>";
      [Root(Name="test")]
      private static class InlineTextList {
         [Element]
         private String message;
         [ElementList(Inline=true)]
         private List<TextEntry> list;
         [Attribute]
         private Version version;
         public TextEntry Get(int index) {
            return list.Get(index);
         }
      }
      [Root(Name="test")]
      private static class InlinePrimitiveList {
         [Element]
         private String message;
         [ElementList(Inline=true)]
         private List<String> list;
         [Attribute]
         private Version version;
         public String Get(int index) {
            return list.Get(index);
         }
      }
      [Root(Name="text")]
      private static class TextEntry {
         [Attribute]
         private String name;
         [Attribute]
         private Version version;
         [Text]
         private String text;
      }
      [Root]
      private static class SimpleInlineList {
         [ElementList(Inline=true)]
         private ArrayList<SimpleEntry> list = new ArrayList<SimpleEntry>();
      }
      [Root]
      private static class SimpleEntry {
         [Attribute]
         private String content;
      }
      [Root]
      private static class SimplePrimitiveInlineList {
         [ElementList(Inline=true)]
         private ArrayList<String> list = new ArrayList<String>();
      }
      [Root]
      private static class SimpleNameInlineList {
         [ElementList(Inline=true, Entry="item")]
         private ArrayList<SimpleEntry> list = new ArrayList<SimpleEntry>();
      }
      private static enum Version {
         ONE,
         TWO,
         THREE
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestList() {
         InlineTextList list = persister.Read(InlineTextList.class, INLINE_LIST);
         AssertEquals(list.version, Version.ONE);
         AssertEquals(list.message, "Some example message");
         AssertEquals(list.Get(0).version, Version.ONE);
         AssertEquals(list.Get(0).name, "a");
         AssertEquals(list.Get(0).text, "Example 1");
         AssertEquals(list.Get(1).version, Version.TWO);
         AssertEquals(list.Get(1).name, "b");
         AssertEquals(list.Get(1).text, "Example 2");
         AssertEquals(list.Get(2).version, Version.THREE);
         AssertEquals(list.Get(2).name, "c");
         AssertEquals(list.Get(2).text, "Example 3");
         StringWriter buffer = new StringWriter();
         persister.Write(list, buffer);
         Validate(list, persister);
         list = persister.Read(InlineTextList.class, buffer.toString());
         AssertEquals(list.version, Version.ONE);
         AssertEquals(list.message, "Some example message");
         AssertEquals(list.Get(0).version, Version.ONE);
         AssertEquals(list.Get(0).name, "a");
         AssertEquals(list.Get(0).text, "Example 1");
         AssertEquals(list.Get(1).version, Version.TWO);
         AssertEquals(list.Get(1).name, "b");
         AssertEquals(list.Get(1).text, "Example 2");
         AssertEquals(list.Get(2).version, Version.THREE);
         AssertEquals(list.Get(2).name, "c");
         AssertEquals(list.Get(2).text, "Example 3");
         Validate(list, persister);
      }
      public void TestPrimitiveList() {
         InlinePrimitiveList list = persister.Read(InlinePrimitiveList.class, INLINE_PRIMITIVE_LIST);
         AssertEquals(list.version, Version.ONE);
         AssertEquals(list.message, "Some example message");
         AssertEquals(list.Get(0), "Example 1");
         AssertEquals(list.Get(1), "Example 2");
         AssertEquals(list.Get(2), "Example 3");
         StringWriter buffer = new StringWriter();
         persister.Write(list, buffer);
         Validate(list, persister);
         list = persister.Read(InlinePrimitiveList.class, buffer.toString());
         AssertEquals(list.Get(0), "Example 1");
         AssertEquals(list.Get(1), "Example 2");
         AssertEquals(list.Get(2), "Example 3");
         Validate(list, persister);
      }
      public void TestSimpleList() {
         SimpleInlineList list = new SimpleInlineList();
         for(int i = 0; i < 10; i++) {
            SimpleEntry entry = new SimpleEntry();
            entry.content = String.format("test %s", i);
            list.list.add(entry);
         }
         Validate(list, persister);
      }
      public void TestSimpleNameList() {
         SimpleNameInlineList list = new SimpleNameInlineList();
         for(int i = 0; i < 10; i++) {
            SimpleEntry entry = new SimpleEntry();
            entry.content = String.format("test %s", i);
            list.list.add(entry);
         }
         Validate(list, persister);
      }
      public void TestSimplePrimitiveList() {
         SimplePrimitiveInlineList list = new SimplePrimitiveInlineList();
         list.list.add("test");
         Validate(list, persister);
      }
   }
}
