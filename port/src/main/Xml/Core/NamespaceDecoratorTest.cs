#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NamespaceDecoratorTest : ValidationTestCase {
      private static class MockNamespace : Namespace {
         private readonly String prefix;
         private readonly String reference;
         public MockNamespace(String prefix, String reference) {
            this.prefix = prefix;
            this.reference = reference;
         }
         public String Reference() {
            return reference;
         }
         public String Prefix() {
            return prefix;
         }
         public Class<? : Annotation> annotationType() {
            return Namespace.class;
         }
      }
      public void TestQualifier() {
         NamespaceDecorator global = new NamespaceDecorator();
         NamespaceDecorator qualifier = new NamespaceDecorator();
         NamespaceDecorator attribute = new NamespaceDecorator();
         global.add(new MockNamespace("global", "http://www.domain.com/global"));
         qualifier.add(new MockNamespace("a", "http://www.domain.com/a"));
         qualifier.add(new MockNamespace("b", "http://www.domain.com/b"));
         qualifier.add(new MockNamespace("c", "http://www.domain.com/c"));
         attribute.add(new MockNamespace("d", "http://www.domain.com/d"));
         global.set(new MockNamespace("first", "http://www.domain.com/ignore"));
         qualifier.set(new MockNamespace("a", "http://www.domain.com/a"));
         attribute.set(new MockNamespace("b", "http://www.domain.com/b"));
         StringWriter out = new StringWriter();
         OutputNode top = NodeBuilder.write(out);
         OutputNode root = top.getChild("root");
         root.setAttribute("version", "1.0");
         qualifier.decorate(root, global);
         OutputNode child = root.getChild("child");
         child.setAttribute("name", "John Doe");
         OutputNode name = child.getAttributes().get("name");
         attribute.decorate(name);
         OutputNode grandChild = child.getChild("grandChild");
         grandChild.setValue("this is the grand child");
         root.commit();
         validate(out.toString());
      }
   }
}
