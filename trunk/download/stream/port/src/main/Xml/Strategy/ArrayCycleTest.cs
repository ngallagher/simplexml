#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class ArrayCycleTest : ValidationTestCase {
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
      "   <array id='array' length='2'>\n" + // NestedExample.array : Value[]
      "      <entry>  \n\r"+
      "         <list ref='array'/>\n"+  // Value.list : Value[] -> NestedArray.array : Value[]
      "      </entry>\r\n"+
      "      <entry id='text'>\n"+
      "         <list length='3' id='foo'>\n"+  // Value.list : Value[]
      "            <entry name='blah' class='SimpleFramework.Xml.Strategy.ArrayCycleTest$TextValue'>\n"+ // Value.list[0] : Value
      "              <text>Some text</text>\n"+  // TextExample.text : String
      "              <list ref='foo'/>\n"+ // TextExample.list : Value[]
      "            </entry>\n"+
      "            <entry ref='text'/>\n"+ // Value.list[1] : Value
      "            <entry class='SimpleFramework.Xml.Strategy.ArrayCycleTest$ElementValue'>\n"+ // Value.list[2] : Value
      "               <element><![CDATA[Example element text]]></element>\n"+ // ElementExample.element : String
      "            </entry>\n"+
      "         </list>\n"+
      "      </entry> \n\t\n"+
      "   </array>\n"+
      "</root>";
      private const String PROMOTE =
      "<value>\n"+
      "   <list length='1' class='SimpleFramework.Xml.Strategy.ArrayCycleTest$ElementValue'>\n"+
      "      <entry class='SimpleFramework.Xml.Strategy.ArrayCycleTest$ElementValue'>\n"+
      "         <element>Example text</element>\n"+
      "      </entry>\n"+
      "   </list>\n"+
      "</value>\n";
      [Root(Name="root")]
      private static class ArrayCycleExample {
         [ElementArray(Name="one", Entry="entry")]
         public Entry[] one;
         [ElementArray(Name="two", Entry="entry")]
         public Entry[] two;
         [ElementArray(Name="three", Entry="entry")]
         public Entry[] three;
         [Element(Name="example")]
         public ArrayCycleExample example;
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
      private static class NestedArrayCycleExample {
         [ElementArray(Name="array", Entry="entry")]
         public Value[] array;
      }
      [Root(Name="value")]
      private static class Value {
         [ElementArray(Name="list", Entry="entry", Required=false)]
         private Value[] list;
      }
      private static class TextValue : Value {
         [Attribute(Name="name")]
         private String name;
         [Element(Name="text")]
         private String text;
      }
      private static class ElementValue : Value {
         [Element(Name="element")]
         private String element;
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister(new CycleStrategy("id", "ref"));
      }
      public void TestCycle() {
         ArrayCycleExample example = persister.Read(ArrayCycleExample.class, SOURCE);
         AssertEquals(example.one.length, 5);
         AssertEquals(example.one[0].value, "entry one");
         AssertEquals(example.one[1].value, "entry two");
         AssertEquals(example.one[2].value, "entry three");
         AssertEquals(example.one[3].value, "entry four");
         AssertEquals(example.one[4].value, "entry five");
         AssertEquals(example.two.length, 5);
         AssertEquals(example.two[0].value, "entry one");
         AssertEquals(example.two[1].value, "entry two");
         AssertEquals(example.two[2].value, "entry three");
         AssertEquals(example.two[3].value, "entry four");
         AssertEquals(example.two[4].value, "entry five");
         AssertEquals(example.three.length, 3);
         AssertEquals(example.three[0].value, "tom");
         AssertEquals(example.three[1].value, "dick");
         AssertEquals(example.three[2].value, "harry");
         assertTrue(example.one == example.two);
         assertTrue(example == example.example);
         StringWriter out = new StringWriter();
         persister.Write(example, out);
         example = persister.Read(ArrayCycleExample.class, SOURCE);
         AssertEquals(example.one.length, 5);
         AssertEquals(example.one[0].value, "entry one");
         AssertEquals(example.one[1].value, "entry two");
         AssertEquals(example.one[2].value, "entry three");
         AssertEquals(example.one[3].value, "entry four");
         AssertEquals(example.one[4].value, "entry five");
         AssertEquals(example.two.length, 5);
         AssertEquals(example.two[0].value, "entry one");
         AssertEquals(example.two[1].value, "entry two");
         AssertEquals(example.two[2].value, "entry three");
         AssertEquals(example.two[3].value, "entry four");
         AssertEquals(example.two[4].value, "entry five");
         AssertEquals(example.three.length, 3);
         AssertEquals(example.three[0].value, "tom");
         AssertEquals(example.three[1].value, "dick");
         AssertEquals(example.three[2].value, "harry");
         assertTrue(example.one == example.two);
         assertTrue(example == example.example);
         Validate(example, persister);
      }
      public void TestNestedExample() {
         NestedArrayCycleExample root = persister.Read(NestedArrayCycleExample.class, NESTED);
         AssertEquals(root.array.length, 2);
         assertTrue(root.array[0].list == root.array);
         assertTrue(root.array[1].list[0] instanceof TextValue);
         assertTrue(root.array[1].list == root.array[1].list[0].list);
         assertTrue(root.array[1] == root.array[1].list[1]);
         assertTrue(root.array[1].list == root.array[1].list[0].list);
         assertTrue(root.array[1].list[2] instanceof ElementValue);
         ElementValue element = (ElementValue) root.array[1].list[2];
         TextValue text = (TextValue) root.array[1].list[0];
         AssertEquals(element.element, "Example element text");
         AssertEquals(text.name, "blah");
         AssertEquals(text.text, "Some text");
         Validate(root, persister);
         StringWriter out = new StringWriter();
         persister.Write(root, out);
         // Ensure references survive serialization
         root = persister.Read(NestedArrayCycleExample.class, out.toString());
         AssertEquals(root.array.length, 2);
         assertTrue(root.array[0].list == root.array);
         assertTrue(root.array[1].list[0] instanceof TextValue);
         assertTrue(root.array[1].list == root.array[1].list[0].list);
         assertTrue(root.array[1] == root.array[1].list[1]);
         assertTrue(root.array[1].list == root.array[1].list[0].list);
         assertTrue(root.array[1].list[2] instanceof ElementValue);
         element = (ElementValue) root.array[1].list[2];
         text = (TextValue) root.array[1].list[0];
         AssertEquals(element.element, "Example element text");
         AssertEquals(text.name, "blah");
         AssertEquals(text.text, "Some text");
      }
      public void TestPromotion() {
         Value example = persister.Read(Value.class, PROMOTE);
         AssertEquals(example.list.length, 1);
         assertTrue(example.list instanceof ElementValue[]);
         ElementValue value = (ElementValue) example.list[0];
         AssertEquals(value.element, "Example text");
         Validate(example, persister);
      }
   }
}
