#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class ListCycleTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<root id='main'>\n"+
      "   <one length='5' id='numbers'>\n\r"+
      "      <text value='entry one'/>  \n\r"+
      "      <text value='entry two'/>  \n\r"+
      "      <text value='entry three'/>  \n\r"+
      "      <text value='entry four'/>  \n\r"+
      "      <text value='entry five'/>  \n\r"+
      "   </one>\n\r"+
      "   <two ref='numbers'/>\n"+
      "   <three length='3'>\n" +
      "      <text value='tom'/>  \n\r"+
      "      <text value='dick'/>  \n\r"+
      "      <text value='harry'/>  \n\r"+
      "   </three>\n"+
      "   <example ref='main'/>\n"+
      "</root>";
      private const String NESTED =
      "<?xml version=\"1.0\"?>\n"+
      "<root id='main'>\n"+
      "   <list id='list'>\n" +
      "      <value>  \n\r"+
      "         <list ref='list'/>\n"+
      "      </value>\r\n"+
      "   </list>\n"+
      "</root>";
      private const String INLINE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<inlineListExample id='main'>\n"+
      "   <text value='entry one'/>  \n\r"+
      "   <text value='entry two'/>  \n\r"+
      "   <text value='entry three'/>  \n\r"+
      "   <text value='entry four'/>  \n\r"+
      "   <text value='entry five'/>  \n\r"+
      "   <example ref='main'/>\n"+
      "</inlineListExample>";
      private const String INLINE_PRIMITIVE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<inlinePrimitiveListExample id='main'>\n"+
      "   <string>entry one</string>  \n\r"+
      "   <string>entry two</string>  \n\r"+
      "   <string>entry three</string>  \n\r"+
      "   <string>entry four</string>  \n\r"+
      "   <string>entry five</string>  \n\r"+
      "   <example ref='main'/>\n"+
      "</inlinePrimitiveListExample>";
      [Root(Name="root")]
      private static class ListExample {
         [ElementList(Name="one", Type=Entry.class)]
         public List<Entry> one;
         [ElementList(Name="two", Type=Entry.class)]
         public List<Entry> two;
         [ElementList(Name="three", Type=Entry.class)]
         public List<Entry> three;
         [Element(Name="example")]
         public ListExample example;
      }
      [Root]
      private static class InlineListExample {
         [ElementList(Inline=true)]
         public List<Entry> list;
         [Element]
         public InlineListExample example;
      }
      [Root]
      private static class InlinePrimitiveListExample {
         [ElementList(Inline=true, Data=true)]
         public List<String> list;
         [Element]
         public InlinePrimitiveListExample example;
      }
      [Root(Name="text")]
      private static class Entry {
         [Attribute(Name="value")]
         public String value;
         public Entry() {
            super();
         }
         public Entry(String value) {
            this.value = value;
         }
      }
      [Root(Name="root")]
      private static class NestedListExample {
         [ElementList(Name="list", Type=Value.class)]
         public List<Value> list;
      }
      [Root(Name="value")]
      private static class Value {
         [ElementList(Name="list", Type=Value.class, Required=false)]
         private List<Value> list;
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister(new CycleStrategy("id", "ref"));
      }
      public void TestCycle() {
         ListExample example = persister.read(ListExample.class, SOURCE);
         assertEquals(example.one.size(), 5);
         assertEquals(example.one.get(0).value, "entry one");
         assertEquals(example.one.get(1).value, "entry two");
         assertEquals(example.one.get(2).value, "entry three");
         assertEquals(example.one.get(3).value, "entry four");
         assertEquals(example.one.get(4).value, "entry five");
         assertEquals(example.two.size(), 5);
         assertEquals(example.two.get(0).value, "entry one");
         assertEquals(example.two.get(1).value, "entry two");
         assertEquals(example.two.get(2).value, "entry three");
         assertEquals(example.two.get(3).value, "entry four");
         assertEquals(example.two.get(4).value, "entry five");
         assertEquals(example.three.size(), 3);
         assertEquals(example.three.get(0).value, "tom");
         assertEquals(example.three.get(1).value, "dick");
         assertEquals(example.three.get(2).value, "harry");
         assertTrue(example.one == example.two);
         assertTrue(example == example.example);
         StringWriter out = new StringWriter();
         persister.write(example, out);
         example = persister.read(ListExample.class, SOURCE);
         assertEquals(example.one.size(), 5);
         assertEquals(example.one.get(0).value, "entry one");
         assertEquals(example.one.get(1).value, "entry two");
         assertEquals(example.one.get(2).value, "entry three");
         assertEquals(example.one.get(3).value, "entry four");
         assertEquals(example.one.get(4).value, "entry five");
         assertEquals(example.two.size(), 5);
         assertEquals(example.two.get(0).value, "entry one");
         assertEquals(example.two.get(1).value, "entry two");
         assertEquals(example.two.get(2).value, "entry three");
         assertEquals(example.two.get(3).value, "entry four");
         assertEquals(example.two.get(4).value, "entry five");
         assertEquals(example.three.size(), 3);
         assertEquals(example.three.get(0).value, "tom");
         assertEquals(example.three.get(1).value, "dick");
         assertEquals(example.three.get(2).value, "harry");
         assertTrue(example.one == example.two);
         assertTrue(example == example.example);
         validate(example, persister);
      }
      public void TestInlineList() {
         InlineListExample example = persister.read(InlineListExample.class, INLINE_LIST);
         assertEquals(example.list.size(), 5);
         assertEquals(example.list.get(0).value, "entry one");
         assertEquals(example.list.get(1).value, "entry two");
         assertEquals(example.list.get(2).value, "entry three");
         assertEquals(example.list.get(3).value, "entry four");
         assertEquals(example.list.get(4).value, "entry five");
         assertTrue(example == example.example);
         StringWriter out = new StringWriter();
         persister.write(example, out);
         example = persister.read(InlineListExample.class, INLINE_LIST);
         assertEquals(example.list.size(), 5);
         assertEquals(example.list.get(0).value, "entry one");
         assertEquals(example.list.get(1).value, "entry two");
         assertEquals(example.list.get(2).value, "entry three");
         assertEquals(example.list.get(3).value, "entry four");
         assertEquals(example.list.get(4).value, "entry five");
         assertTrue(example == example.example);
         validate(example, persister);
      }
      public void TestInlinePrimitiveList() {
         InlinePrimitiveListExample example = persister.read(InlinePrimitiveListExample.class, INLINE_PRIMITIVE_LIST);
         assertEquals(example.list.size(), 5);
         assertEquals(example.list.get(0), "entry one");
         assertEquals(example.list.get(1), "entry two");
         assertEquals(example.list.get(2), "entry three");
         assertEquals(example.list.get(3), "entry four");
         assertEquals(example.list.get(4), "entry five");
         assertTrue(example == example.example);
         StringWriter out = new StringWriter();
         persister.write(example, out);
         example = persister.read(InlinePrimitiveListExample.class, INLINE_PRIMITIVE_LIST);
         assertEquals(example.list.size(), 5);
         assertEquals(example.list.get(0), "entry one");
         assertEquals(example.list.get(1), "entry two");
         assertEquals(example.list.get(2), "entry three");
         assertEquals(example.list.get(3), "entry four");
         assertEquals(example.list.get(4), "entry five");
         assertTrue(example == example.example);
         validate(example, persister);
      }
      public void TestNestedExample() {
         NestedListExample root = persister.read(NestedListExample.class, NESTED);
         assertEquals(root.list.size(), 1);
         assertTrue(root.list.get(0).list == root.list);
         validate(root, persister);
      }
   }
}
