#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   import org.w3c.dom.Document;
   public class NodeBuilderTest : TestCase {
       private const String SOURCE =
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
       public void TestNodeAdapter() {
          Reader reader = new StringReader(SOURCE);
          InputNode event = NodeBuilder.read(reader);
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
   }
}
