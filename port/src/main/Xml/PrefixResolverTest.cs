#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class PrefixResolverTest : ValidationTestCase {
      public void TestPrefixResolver() {
         StringWriter writer = new StringWriter();
         OutputNode node = NodeBuilder.write(writer);
         // <root xmlns="ns1">
         OutputNode root = node.getChild("root");
         root.setReference("ns1");
         root.getNamespaces().put("ns1", "n");
         // <child xmlns="ns2">
         OutputNode child = root.getChild("child");
         child.setReference("ns2");
         child.getNamespaces().put("ns2", "n");
         // <grandchild xmlns="ns1">
         OutputNode grandchild = child.getChild("grandchild");
         grandchild.setReference("ns1");
         grandchild.getNamespaces().put("ns1", "n");
         root.commit();
         String text = writer.toString();
         System.out.println(text);
      }
   }
}
