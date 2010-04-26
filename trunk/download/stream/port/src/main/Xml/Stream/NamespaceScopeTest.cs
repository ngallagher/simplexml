#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class NamespaceScopeTest : ValidationTestCase {
      private const String EMPTY_OVERRIDE =
      "<root xmlns='http://www.default.com/'>\n"+ // http://www.default.com/
      "<entry xmlns=''>\n"+
      "<p:book xmlns:p='http://www.example.com/book'>\n"+ // http://www.example.com/book
      "<author>saurabh</author>\n"+ // empty
      "<p:title>simple xml</p:title>\n"+ // http://www.example.com/book
      "<p:isbn>ISB-16728-10</p:isbn>\n"+ // http://www.example.com/book
      "</p:book>\n"+
      "</entry>\n"+
      "</root>";
      private const String DEFAULT_FIRST =
      "<root xmlns='http://www.default.com/'>\n"+ // http://www.default.com/
      "<p:book xmlns:p='http://www.example.com/book'>\n"+ // http://www.example.com/book
      "<author>saurabh</author>\n"+ // http://www.default.com/
      "<title>simple xml</title>\n"+ // http://www.default.com/
      "<isbn>ISB-16728-10</isbn>\n"+ // http://www.default.com/
      "</p:book>\n"+
      "</root>";
      public void TestEmptyOverride() {
         InputNode node = NodeBuilder.read(new StringReader(EMPTY_OVERRIDE));
         String reference = node.getReference();
         String prefix = node.getPrefix();
         assertTrue(IsEmpty(prefix));
         AssertEquals(reference, "http://www.default.com/");
         node = node.getNext("entry");
         reference = node.getReference();
         prefix = node.getPrefix();
         assertTrue(IsEmpty(prefix));
         assertTrue(IsEmpty(reference));
         node = node.getNext("book");
         reference = node.getReference();
         prefix = node.getPrefix();
         AssertEquals(prefix, "p");
         AssertEquals(reference, "http://www.example.com/book");
         InputNode author = node.getNext("author");
         reference = author.getReference();
         prefix = author.getPrefix();
         assertTrue(IsEmpty(prefix));
         assertTrue(IsEmpty(reference));
         InputNode title = node.getNext("title");
         reference = title.getReference();
         prefix = title.getPrefix();
         AssertEquals(prefix, "p");
         AssertEquals(reference, "http://www.example.com/book");
         InputNode isbn = node.getNext("isbn");
         reference = isbn.getReference();
         prefix = isbn.getPrefix();
         AssertEquals(prefix, "p");
         AssertEquals(reference, "http://www.example.com/book");
      }
      public void TestDefaultFirst() {
         InputNode node = NodeBuilder.read(new StringReader(DEFAULT_FIRST));
         String reference = node.getReference();
         String prefix = node.getPrefix();
         assertTrue(IsEmpty(prefix));
         AssertEquals(reference, "http://www.default.com/");
         node = node.getNext("book");
         reference = node.getReference();
         prefix = node.getPrefix();
         AssertEquals(prefix, "p");
         AssertEquals(reference, "http://www.example.com/book");
         InputNode author = node.getNext("author");
         reference = author.getReference();
         prefix = author.getPrefix();
         assertTrue(IsEmpty(prefix));
         AssertEquals(reference, "http://www.default.com/");
         InputNode title = node.getNext("title");
         reference = title.getReference();
         prefix = title.getPrefix();
         assertTrue(IsEmpty(prefix));
         AssertEquals(reference, "http://www.default.com/");
         InputNode isbn = node.getNext("isbn");
         reference = isbn.getReference();
         prefix = isbn.getPrefix();
         assertTrue(IsEmpty(prefix));
         AssertEquals(reference, "http://www.default.com/");
      }
      public bool IsEmpty(String name) {
         return name == null || name.equals("");
      }
   }
}
