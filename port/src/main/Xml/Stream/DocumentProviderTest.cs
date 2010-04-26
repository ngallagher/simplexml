#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class DocumentProviderTest : ValidationTestCase {
      private const String SOURCE =
         "<root name='top'>\n"+
         "    <!-- child node -->\n"+
         "    <child a='A' b='B'>\n"+
         "        <leaf>leaf node</leaf>\n"+
         "    </child>\n"+
         "</root>";
         public void TestReader() {
            Provider provider = new DocumentProvider();
            StringReader source = new StringReader(SOURCE);
            EventReader reader = provider.provide(source);
            AssertEquals(reader.peek().getName(), "root");
            AssertEquals(reader.next().getName(), "root");
            assertTrue(reader.peek().isText());
            assertTrue(reader.next().isText());
            while(reader.peek().isText()) {
               assertTrue(reader.next().isText()); // remove text from the document
            }
            AssertEquals(reader.peek().getName(), "child");
            AssertEquals(reader.next().getName(), "child");
            assertTrue(reader.peek().isText());
            assertTrue(reader.next().isText());
            while(reader.peek().isText()) {
               assertTrue(reader.next().isText()); // remove text from the document
            }
            AssertEquals(reader.peek().getName(), "leaf");
            AssertEquals(reader.next().getName(), "leaf");
            assertTrue(reader.peek().isText());
            AssertEquals(reader.peek().getValue(), "leaf node");
            AssertEquals(reader.next().getValue(), "leaf node");
            assertTrue(reader.next().isEnd());
            while(reader.peek().isText()) {
               assertTrue(reader.next().isText()); // remove text from the document
            }
            assertTrue(reader.next().isEnd());
            while(reader.peek().isText()) {
               assertTrue(reader.next().isText()); // remove text from the document
            }
         }
   }
}
