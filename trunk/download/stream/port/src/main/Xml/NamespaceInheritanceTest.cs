#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NamespaceInheritanceTest : ValidationTestCase {
      [Namespace(Reference="namespace1")]
      private static class Aaa {
         [Element(Name="bbb", Required=false)]
         public Bbb bbb;
      }
      [Namespace(Reference="namespace2")]
      private static class Bbb {
         [Element(Name="aaa", Required=false)]
         public Aaa aaa;
      }
      [Namespace(Prefix="aaa", Reference="namespace1")]
      private static class AaaWithPrefix {
         [Element(Name="bbb", Required=false)]
         public BbbWithPrefix bbb;
      }
      [Namespace(Prefix="bbb", Reference="namespace2")]
      private static class BbbWithPrefix {
         [Element(Name="aaa", Required=false)]
         public AaaWithPrefix aaa;
      }
      public void TestNamespace() {
         Aaa parent = new Aaa();
         Bbb child = new Bbb();
         parent.bbb = child;
         Aaa grandchild = new Aaa();
         child.aaa = grandchild;
         grandchild.bbb = new Bbb();
         ByteArrayOutputStream tmp = new ByteArrayOutputStream();
         Serializer serializer = new Persister();
         serializer.write(parent, tmp);
         String result = new String(tmp.toByteArray());
         System.out.println(result);
         assertElementHasAttribute(result, "/aaa", "xmlns", "namespace1");
         assertElementHasAttribute(result, "/aaa/bbb", "xmlns", "namespace2");
         assertElementHasAttribute(result, "/aaa/bbb/aaa", "xmlns", "namespace1");
         assertElementHasAttribute(result, "/aaa/bbb/aaa/bbb", "xmlns", "namespace2");
         assertElementHasNamespace(result, "/aaa", "namespace1");
         assertElementHasNamespace(result, "/aaa/bbb", "namespace2");
         assertElementHasNamespace(result, "/aaa/bbb/aaa", "namespace1");
         assertElementHasNamespace(result, "/aaa/bbb/aaa/bbb", "namespace2");
      }
      public void TestNamespacePrefix() {
         AaaWithPrefix parent = new AaaWithPrefix();
         BbbWithPrefix child = new BbbWithPrefix();
         parent.bbb = child;
         AaaWithPrefix grandchild = new AaaWithPrefix();
         child.aaa = grandchild;
         grandchild.bbb = new BbbWithPrefix();
         ByteArrayOutputStream tmp = new ByteArrayOutputStream();
         Serializer serializer = new Persister();
         serializer.write(parent, tmp);
         String result = new String(tmp.toByteArray());
         System.out.println(result);
         assertElementHasAttribute(result, "/aaaWithPrefix", "xmlns:aaa", "namespace1");
         assertElementHasAttribute(result, "/aaaWithPrefix/bbb", "xmlns:bbb", "namespace2");
         assertElementDoesNotHaveAttribute(result, "/aaaWithPrefix/bbb/aaa", "xmlns:aaa", "namespace1");
         assertElementDoesNotHaveAttribute(result, "/aaaWithPrefix/bbb/aaa/bbb", "xmlns:bbb", "namespace2");
         assertElementHasNamespace(result, "/aaaWithPrefix", "namespace1");
         assertElementHasNamespace(result, "/aaaWithPrefix/bbb", "namespace2");
         assertElementHasNamespace(result, "/aaaWithPrefix/bbb/aaa", "namespace1");
         assertElementHasNamespace(result, "/aaaWithPrefix/bbb/aaa/bbb", "namespace2");
      }
   }
}
