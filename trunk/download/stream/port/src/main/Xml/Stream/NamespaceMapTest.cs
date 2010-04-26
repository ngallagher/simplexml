#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class NamespaceMapTest : ValidationTestCase {
      private const String SOURCE =
      "<root a:name='value' xmlns:a='http://www.domain.com/a'>\n" +
      "   <a:child>this is the child</a:child>\n" +
      "</root>";
      public void TestInputNode() {
         StringReader reader = new StringReader(SOURCE);
         InputNode node = NodeBuilder.read(reader);
         NodeMap<InputNode> map = node.getAttributes();
         InputNode attr = map.get("name");
         AssertEquals("value", attr.getValue());
         AssertEquals("a", attr.getPrefix());
         AssertEquals("http://www.domain.com/a", attr.getReference());
         InputNode child = node.getNext();
         AssertEquals("this is the child", child.getValue());
         AssertEquals("a", child.getPrefix());
         AssertEquals("http://www.domain.com/a", child.getReference());
      }
      public void TestOutputNode() {
         StringWriter out = new StringWriter();
         OutputNode top = NodeBuilder.write(out);
         OutputNode root = top.getChild("root");
         NamespaceMap map = root.getNamespaces();
         root.setReference("http://www.sun.com/jsp");
         map.put("http://www.w3c.com/xhtml", "xhtml");
         map.put("http://www.sun.com/jsp", "jsp");
         OutputNode child = root.getChild("child");
         child.setAttribute("name.1", "1");
         child.setAttribute("name.2", "2");
         OutputNode attribute = child.getAttributes().get("name.1");
         attribute.setReference("http://www.w3c.com/xhtml");
         OutputNode otherChild = root.getChild("otherChild");
         otherChild.setAttribute("name.a", "a");
         otherChild.setAttribute("name.b", "b");
         map = otherChild.getNamespaces();
         map.put("http://www.w3c.com/xhtml", "ignore");
         OutputNode yetAnotherChild = otherChild.getChild("yetAnotherChild");
         yetAnotherChild.setReference("http://www.w3c.com/xhtml");
         yetAnotherChild.setValue("example text for yet another namespace");
         OutputNode readonlyChild = otherChild.getChild("readonlyChild");
         map = readonlyChild.getNamespaces();
         map.put("http://www.w3c.com/anonymous");
         readonlyChild.setReference("http://www.w3c.com/anonymous");
         OutputNode veryLastChild = readonlyChild.getChild("veryLastChild");
         map = veryLastChild.getNamespaces();
         map.put("");
         OutputNode veryVeryLastChild = veryLastChild.getChild("veryVeryLastChild");
         map = veryVeryLastChild.getNamespaces();
         map.put("");
         veryVeryLastChild.setReference("");
         veryVeryLastChild.setValue("very very last child");
         OutputNode otherVeryVeryLastChild = veryLastChild.getChild("otherVeryVeryLastChild");
         // Problem here with anonymous namespace
         otherVeryVeryLastChild.setReference("http://www.w3c.com/anonymous");
         otherVeryVeryLastChild.setValue("other very very last child");
         OutputNode yetAnotherVeryVeryLastChild = veryLastChild.getChild("yetAnotherVeryVeryLastChild");
         yetAnotherVeryVeryLastChild.setReference("http://www.w3c.com/xhtml");
         yetAnotherVeryVeryLastChild.setValue("yet another very very last child");
         root.commit();
         validate(out.toString());
      }
   }
}
