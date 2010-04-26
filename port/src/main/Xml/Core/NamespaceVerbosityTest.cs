#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NamespaceVerbosityTest : ValidationTestCase {
      private const String SOURCE =
      "<a xmlns:x='http://domain/x' xmlns:y='http://domain/y'>\n"+
      "    <x:b>b</x:b>\n"+
      "    <y:c>c</y:c>\n"+
      "    <x:d>\n"+
      "        <e>e</e>\n"+
      "    </x:d>\n"+
      "</a>\n";
      [Root]
      [NamespaceList]
      [Namespace(Prefix="x", Reference="http://domain/x")]
      [Namespace(Prefix="y", Reference="http://domain/y")})]
      private static class A {
         [Element]
         [Namespace(Prefix="i", Reference="http://domain/x")]
         private String b;
         [Element]
         [Namespace(Prefix="j", Reference="http://domain/y")]
         private String c;
         [Element]
         [Namespace(Prefix="k", Reference="http://domain/x")]
         private D d;
      }
      [Root]
      private static class D {
         [Element]
         private String e;
      }
      public void TestScope() {
         Persister persister = new Persister();
         StringWriter writer = new StringWriter();
         A example = persister.read(A.class, SOURCE);
         AssertEquals(example.b, "b");
         AssertEquals(example.c, "c");
         AssertEquals(example.d.e, "e");
         assertElementHasNamespace(SOURCE, "/a", null);
         assertElementHasNamespace(SOURCE, "/a/b", "http://domain/x");
         assertElementHasNamespace(SOURCE, "/a/c", "http://domain/y");
         assertElementHasNamespace(SOURCE, "/a/d", "http://domain/x");
         assertElementHasNamespace(SOURCE, "/a/d/e", null);
         persister.write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementHasNamespace(text, "/a", null);
         assertElementHasNamespace(text, "/a/b", "http://domain/x");
         assertElementHasNamespace(text, "/a/c", "http://domain/y");
         assertElementHasNamespace(text, "/a/d", "http://domain/x");
         assertElementHasNamespace(text, "/a/d/e", null);
         assertElementHasAttribute(text, "/a", "xmlns:x", "http://domain/x");
         assertElementHasAttribute(text, "/a", "xmlns:y", "http://domain/y");
         assertElementDoesNotHaveAttribute(text, "/a/b", "xmlns:i", "http://domain/x");
         assertElementDoesNotHaveAttribute(text, "/a/c", "xmlns:j", "http://domain/y");
         assertElementDoesNotHaveAttribute(text, "/a/d", "xmlns:k", "http://domain/x");
      }
   }
}
