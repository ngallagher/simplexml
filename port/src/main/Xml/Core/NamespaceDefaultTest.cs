#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NamespaceDefaultTest : ValidationTestCase {
      private const String SOURCE =
      "<a xmlns:x='http://domain/x' xmlns='http://domain/z'>\n"+
      "    <y:b xmlns:y='http://domain/y'>\n"+
      "        <c xmlns='http://domain/c'>\n"+
      "            <d xmlns='http://domain/z'>d</d>\n"+
      "        </c>\n"+
      "    </y:b>\n"+
      "</a>\n";
      @Root
      @NamespaceList({
      [Namespace(Prefix="x", Reference="http://domain/x")]
      [Namespace(Prefix="z", Reference="http://domain/z")})]
      [Namespace(Reference="http://domain/z")]
      private static class A {
         @Element
         [Namespace(Prefix="y", Reference="http://domain/y")]
         private B b;
      }
      @Root
      private static class B {
         @Element
         [Namespace(Reference="http://domain/c")]
         private C c;
      }
      @Root
      private static class C{
         @Element
         [Namespace(Reference="http://domain/z")]
         private String d;
      }
      public void TestScope() {
         Persister persister = new Persister();
         StringWriter writer = new StringWriter();
         A example = persister.read(A.class, SOURCE);
         assertEquals(example.b.c.d, "d");
         assertElementHasNamespace(SOURCE, "/a", "http://domain/z");
         assertElementHasNamespace(SOURCE, "/a/b", "http://domain/y");
         assertElementHasNamespace(SOURCE, "/a/b/c", "http://domain/c");
         assertElementHasNamespace(SOURCE, "/a/b/c/d", "http://domain/z");
         persister.write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementHasNamespace(text, "/a", "http://domain/z");
         assertElementHasNamespace(text, "/a/b", "http://domain/y");
         assertElementHasNamespace(text, "/a/b/c", "http://domain/c");
         assertElementHasNamespace(text, "/a/b/c/d", "http://domain/z");
         assertElementHasAttribute(text, "/a", "xmlns", "http://domain/z");
         assertElementDoesNotHaveAttribute(text, "/a", "xmlns:z", "http://domain/z");
         assertElementHasAttribute(text, "/a", "xmlns:x", "http://domain/x");
         assertElementHasAttribute(text, "/a/b", "xmlns:y", "http://domain/y");
         assertElementHasAttribute(text, "/a/b/c", "xmlns", "http://domain/c");
         assertElementHasAttribute(text, "/a/b/c/d", "xmlns", "http://domain/z");
      }
   }
}
