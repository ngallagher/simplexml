#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class NamespaceAttributeTest : TestCase {
      private const String SOURCE =
      "<root xmlns='default' xmlns:a='A' xmlns:b='B'>" +
      "   <child a:attributeA='valueA' b:attributeB='valueB'>"+
      "      <leaf b:attributeC='c'/>"+
      "   </child>+" +
      "   <a:entry b:attributeD='valueD'/>"+
      "</root>";
      public void TestAttributes() {
         InputNode root = NodeBuilder.read(new StringReader(SOURCE));
         InputNode child = root.getNext();
         NodeMap<InputNode> map = child.getAttributes();
         AssertEquals(root.getReference(), "default");
         AssertEquals(child.getReference(), "default");
         AssertEquals(map.get("attributeA").getValue(), "valueA");
         AssertEquals(map.get("attributeA").getPrefix(), "a");
         AssertEquals(map.get("attributeA").getReference(), "A");
         AssertEquals(map.get("attributeB").getValue(), "valueB");
         AssertEquals(map.get("attributeB").getPrefix(), "b");
         AssertEquals(map.get("attributeB").getReference(), "B");
         InputNode leaf = child.getNext();
         AssertEquals(leaf.getReference(), "default");
         AssertEquals(leaf.getAttribute("attributeC").getValue(), "c");
         AssertEquals(leaf.getAttribute("attributeC").getPrefix(), "b");
         AssertEquals(leaf.getAttribute("attributeC").getReference(), "B");
         InputNode entry = root.getNext();
         AssertEquals(entry.getReference(), "A");
         AssertEquals(entry.getAttribute("attributeD").getValue(), "valueD");
         AssertEquals(entry.getAttribute("attributeD").getPrefix(), "b");
         AssertEquals(entry.getAttribute("attributeD").getReference(), "B");
      }
   }
}
