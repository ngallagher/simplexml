#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class DefaultTest : ValidationTestCase {
      private const String SOURCE =
      "<defaultTextList version='ONE'>\n"+
      "   <list>\r\n" +
      "      <textEntry name='a' version='ONE'>Example 1</textEntry>\r\n"+
      "      <textEntry name='b' version='TWO'>Example 2</textEntry>\r\n"+
      "      <textEntry name='c' version='THREE'>Example 3</textEntry>\r\n"+
      "   </list>\r\n"+
      "</defaultTextList>";
      @Root
      private static class DefaultTextList {
         @ElementList
         private List<TextEntry> list;
         @Attribute
         private Version version;
         public TextEntry Get(int index) {
            return list.Get(index);
         }
      }
      @Root
      private static class TextEntry {
         @Attribute
         private String name;
         @Attribute
         private Version version;
         @Text
         private String text;
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
      public void TestList() {
         DefaultTextList list = persister.read(DefaultTextList.class, SOURCE);
         assertEquals(list.version, Version.ONE);
         assertEquals(list.Get(0).version, Version.ONE);
         assertEquals(list.Get(0).name, "a");
         assertEquals(list.Get(0).text, "Example 1");
         assertEquals(list.Get(1).version, Version.TWO);
         assertEquals(list.Get(1).name, "b");
         assertEquals(list.Get(1).text, "Example 2");
         assertEquals(list.Get(2).version, Version.THREE);
         assertEquals(list.Get(2).name, "c");
         assertEquals(list.Get(2).text, "Example 3");
         StringWriter buffer = new StringWriter();
         persister.write(list, buffer);
         String text = buffer.toString();
         assertXpathExists("/defaultTextList/list[@class='java.util.ArrayList']", text);
         assertXpathExists("/defaultTextList/list/textEntry[@name='a']", text);
         assertXpathExists("/defaultTextList/list/textEntry[@name='b']", text);
         assertXpathExists("/defaultTextList/list/textEntry[@name='c']", text);
         assertXpathEvaluatesTo("Example 1", "/defaultTextList/list/textEntry[1]", text);
         assertXpathEvaluatesTo("Example 2", "/defaultTextList/list/textEntry[2]", text);
         assertXpathEvaluatesTo("Example 3", "/defaultTextList/list/textEntry[3]", text);
         validate(list, persister);
         list = persister.read(DefaultTextList.class, text);
         assertEquals(list.version, Version.ONE);
         assertEquals(list.Get(0).version, Version.ONE);
         assertEquals(list.Get(0).name, "a");
         assertEquals(list.Get(0).text, "Example 1");
         assertEquals(list.Get(1).version, Version.TWO);
         assertEquals(list.Get(1).name, "b");
         assertEquals(list.Get(1).text, "Example 2");
         assertEquals(list.Get(2).version, Version.THREE);
         assertEquals(list.Get(2).name, "c");
         assertEquals(list.Get(2).text, "Example 3");
         buffer = new StringWriter();
         persister.write(list, buffer);
         String copy = buffer.toString();
         assertXpathExists("/defaultTextList/list[@class='java.util.ArrayList']", copy);
         assertXpathExists("/defaultTextList/list/textEntry[@name='a']", copy);
         assertXpathExists("/defaultTextList/list/textEntry[@name='b']", copy);
         assertXpathExists("/defaultTextList/list/textEntry[@name='c']", copy);
         assertXpathEvaluatesTo("Example 1", "/defaultTextList/list/textEntry[1]", copy);
         assertXpathEvaluatesTo("Example 2", "/defaultTextList/list/textEntry[2]", copy);
         assertXpathEvaluatesTo("Example 3", "/defaultTextList/list/textEntry[3]", copy);
         assertXMLEqual(text, copy);
         validate(list, persister);
      }
   }
}
