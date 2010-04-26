#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class StringArrayTransformTest : ValidationTestCase {
   [Root]
   public static class StringArrayExample {
      [Attribute]
      private String[] attribute;
      [Element]
      private String[] element;
      [ElementList]
      private List<String[]> list;
      [ElementArray]
      private String[][] array;
      public StringArrayExample() {
         super();
      }
      public StringArrayExample(String[] list) {
         this.attribute = list;
         this.element = list;
         this.list = new ArrayList<String[]>();
         this.list.add(list);
         this.list.add(list);
         this.array = new String[1][];
         this.array[0] = list;
      }
   }
   public void TestRead() {
      StringArrayTransform transform = new StringArrayTransform();
      String[] list = transform.read("one,two,three,four");
      AssertEquals("one", list[0]);
      AssertEquals("two", list[1]);
      AssertEquals("three", list[2]);
      AssertEquals("four", list[3]);
      list = transform.read("  this is some string ,\t\n "+
                            "that has each\n\r," +
                            "string split,\t over\n,\n"+
                            "several lines\n\t");
      AssertEquals("this is some string", list[0]);
      AssertEquals("that has each", list[1]);
      AssertEquals("string split", list[2]);
      AssertEquals("over", list[3]);
      AssertEquals("several lines", list[4]);
   }
   public void TestWrite() {
      StringArrayTransform transform = new StringArrayTransform();
      String value = transform.write(new String[] { "one", "two", "three", "four"});
      AssertEquals(value, "one, two, three, four");
      value = transform.write(new String[] {"one", null, "three", "four"});
      AssertEquals(value, "one, three, four");
   }
   public void TestPersistence() {
      String[] list = new String[] { "one", "two", "three", "four"};
      Persister persister = new Persister();
      StringArrayExample example = new StringArrayExample(list);
      StringWriter out = new StringWriter();
      AssertEquals(example.attribute[0], "one");
      AssertEquals(example.attribute[1], "two");
      AssertEquals(example.attribute[2], "three");
      AssertEquals(example.attribute[3], "four");
      AssertEquals(example.element[0], "one");
      AssertEquals(example.element[1], "two");
      AssertEquals(example.element[2], "three");
      AssertEquals(example.element[3], "four");
      AssertEquals(example.list.get(0)[0], "one");
      AssertEquals(example.list.get(0)[1], "two");
      AssertEquals(example.list.get(0)[2], "three");
      AssertEquals(example.list.get(0)[3], "four");
      AssertEquals(example.array[0][0], "one");
      AssertEquals(example.array[0][1], "two");
      AssertEquals(example.array[0][2], "three");
      AssertEquals(example.array[0][3], "four");
      persister.write(example, out);
      String text = out.toString();
      System.err.println(text);
      example = persister.read(StringArrayExample.class, text);
      AssertEquals(example.attribute[0], "one");
      AssertEquals(example.attribute[1], "two");
      AssertEquals(example.attribute[2], "three");
      AssertEquals(example.attribute[3], "four");
      AssertEquals(example.element[0], "one");
      AssertEquals(example.element[1], "two");
      AssertEquals(example.element[2], "three");
      AssertEquals(example.element[3], "four");
      AssertEquals(example.list.get(0)[0], "one");
      AssertEquals(example.list.get(0)[1], "two");
      AssertEquals(example.list.get(0)[2], "three");
      AssertEquals(example.list.get(0)[3], "four");
      AssertEquals(example.array[0][0], "one");
      AssertEquals(example.array[0][1], "two");
      AssertEquals(example.array[0][2], "three");
      AssertEquals(example.array[0][3], "four");
      validate(example, persister);
   }
}
}
