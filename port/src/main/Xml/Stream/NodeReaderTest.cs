#region Using directives
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class NodeReaderTest : TestCase {
      private const String SMALL_SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<override id='12' flag='true'>\n"+
      "   <text>entry text</text>  \n\r"+
      "   <name>some name</name> \n"+
      "   <third>added to schema</third>\n"+
      "</override>";
      private const String LARGE_SOURCE =
       "<?xml version='1.0'?>\n" +
       "<root version='2.1' id='234'>\n" +
       "   <list type='sorted'>\n" +
       "      <entry name='1'>\n" +
       "         <value>value 1</value>\n" +
       "      </entry>\n" +
       "      <entry name='2'>\n" +
       "         <value>value 2</value>\n" +
       "      </entry>\n" +
       "      <entry name='3'>\n" +
       "         <value>value 3</value>\n" +
       "      </entry>\n" +
       "   </list>\n" +
       "   <object name='name'>\n" +
       "      <integer>123</integer>\n" +
       "      <object name='key'>\n" +
       "         <integer>12345</integer>\n" +
       "      </object>\n" +
       "   </object>\n" +
       "</root>";
      public const String EMPTY_SOURCE =
      "<root>\r\n" +
      "   <empty/>\r\n" +
      "   <notEmpty name='foo'/>\r\n" +
      "   <empty></empty>\r\n" +
      "</root>";
      public void TestEmptySource() {
         InputNode event = NodeBuilder.read(new StringReader(EMPTY_SOURCE));
         assertTrue(event.isRoot());
         assertFalse(event.isEmpty());
         AssertEquals("root", event.getName());
         InputNode child  = event.getNext();
         assertTrue(child.isEmpty());
         AssertEquals("empty", child.getName());
         child = event.getNext();
         assertFalse(child.isEmpty());
         AssertEquals("notEmpty", child.getName());
         AssertEquals("foo", child.getAttribute("name").getValue());
         child = event.getNext();
         assertTrue(child.isEmpty());
         AssertEquals("empty", child.getName());
      }
      public void TestSmallSource() {
         InputNode event = NodeBuilder.read(new StringReader(SMALL_SOURCE));
         assertTrue(event.isRoot());
         AssertEquals("override", event.getName());
         AssertEquals("12", event.getAttribute("id").getValue());
         AssertEquals("true", event.getAttribute("flag").getValue());
         NodeMap list = event.getAttributes();
         AssertEquals("12", list.get("id").getValue());
         AssertEquals("true", list.get("flag").getValue());
         InputNode text = event.getNext();
         assertFalse(text.isRoot());
         assertTrue(event.isRoot());
         AssertEquals("text", text.getName());
         AssertEquals("entry text", text.getValue());
         AssertEquals(null, text.getNext());
         InputNode name = event.getNext();
         assertFalse(name.isRoot());
         AssertEquals("name", name.getName());
         AssertEquals("some name", name.getValue());
         AssertEquals(null, name.getNext());
         AssertEquals(null, text.getNext());
         InputNode third = event.getNext();
         assertTrue(event.isRoot());
         assertFalse(third.isRoot());
         AssertEquals("third", third.getName());
         AssertEquals("text", text.getName());
         AssertEquals(null, text.getNext());
         AssertEquals("added to schema", third.getValue());
         AssertEquals(null, event.getNext());
      }
      public void TestLargeSource() {
         InputNode event = NodeBuilder.read(new StringReader(LARGE_SOURCE));
         assertTrue(event.isRoot());
         AssertEquals("root", event.getName());
         AssertEquals("2.1", event.getAttribute("version").getValue());
         AssertEquals("234", event.getAttribute("id").getValue());
         NodeMap attrList = event.getAttributes();
         AssertEquals("2.1", attrList.get("version").getValue());
         AssertEquals("234", attrList.get("id").getValue());
         InputNode list = event.getNext();
         assertFalse(list.isRoot());
         AssertEquals("list", list.getName());
         AssertEquals("sorted", list.getAttribute("type").getValue());
         InputNode entry = list.getNext();
         InputNode value = list.getNext(); // same as entry.getNext()
         AssertEquals("entry", entry.getName());
         AssertEquals("1", entry.getAttribute("name").getValue());
         AssertEquals("value", value.getName());
         AssertEquals("value 1", value.getValue());
         AssertEquals(null, value.getAttribute("name"));
         AssertEquals(null, entry.getNext());
         AssertEquals(null, value.getNext());
         entry = list.getNext();
         value = entry.getNext(); // same as list.getNext()
         AssertEquals("entry", entry.getName());
         AssertEquals("2", entry.getAttribute("name").getValue());
         AssertEquals("value", value.getName());
         AssertEquals("value 2", value.getValue());
         AssertEquals(null, value.getAttribute("name"));
         AssertEquals(null, entry.getNext());
         entry = list.getNext();
         value = entry.getNext(); // same as list.getNext()
         AssertEquals("entry", entry.getName());
         AssertEquals("3", entry.getAttribute("name").getValue());
         AssertEquals("value", value.getName());
         AssertEquals("value 3", value.getValue());
         AssertEquals(null, value.getAttribute("name"));
         AssertEquals(null, entry.getNext());
         AssertEquals(null, list.getNext());
         InputNode object = event.getNext();
         InputNode integer = event.getNext(); // same as object.getNext()
         AssertEquals("object", object.getName());
         AssertEquals("name", object.getAttribute("name").getValue());
         AssertEquals("integer", integer.getName());
         AssertEquals("123", integer.getValue());
         object = object.getNext(); // same as event.getNext()
         integer = object.getNext();
         AssertEquals("object", object.getName());
         AssertEquals("key", object.getAttribute("name").getValue());
         AssertEquals("integer", integer.getName());
         AssertEquals("12345", integer.getValue());
      }
      public void TestSkip() {
         InputNode event = NodeBuilder.read(new StringReader(LARGE_SOURCE));
         assertTrue(event.isRoot());
         AssertEquals("root", event.getName());
         AssertEquals("2.1", event.getAttribute("version").getValue());
         AssertEquals("234", event.getAttribute("id").getValue());
         NodeMap attrList = event.getAttributes();
         AssertEquals("2.1", attrList.get("version").getValue());
         AssertEquals("234", attrList.get("id").getValue());
         InputNode list = event.getNext();
         assertFalse(list.isRoot());
         AssertEquals("list", list.getName());
         AssertEquals("sorted", list.getAttribute("type").getValue());
         InputNode entry = list.getNext();
         InputNode value = list.getNext(); // same as entry.getNext()
         AssertEquals("entry", entry.getName());
         AssertEquals("1", entry.getAttribute("name").getValue());
         AssertEquals("value", value.getName());
         AssertEquals("value 1", value.getValue());
         AssertEquals(null, value.getAttribute("name"));
         AssertEquals(null, entry.getNext());
         AssertEquals(null, value.getNext());
         entry = list.getNext();
         entry.skip();
         AssertEquals(entry.getNext(), null);
         entry = list.getNext();
         value = entry.getNext(); // same as list.getNext()
         AssertEquals("entry", entry.getName());
         AssertEquals("3", entry.getAttribute("name").getValue());
         AssertEquals("value", value.getName());
         AssertEquals("value 3", value.getValue());
         AssertEquals(null, value.getAttribute("name"));
         AssertEquals(null, entry.getNext());
         AssertEquals(null, list.getNext());
         InputNode object = event.getNext();
         InputNode integer = event.getNext(); // same as object.getNext()
         AssertEquals("object", object.getName());
         AssertEquals("name", object.getAttribute("name").getValue());
         AssertEquals("integer", integer.getName());
         AssertEquals("123", integer.getValue());
         object = object.getNext(); // same as event.getNext()
         integer = object.getNext();
         AssertEquals("object", object.getName());
         AssertEquals("key", object.getAttribute("name").getValue());
         AssertEquals("integer", integer.getName());
         AssertEquals("12345", integer.getValue());
      }
   }
}
