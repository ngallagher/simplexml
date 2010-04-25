#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class PrimitiveArrayTransformTest : ValidationTestCase {
   [Root]
   public static class IntegerArrayExample {
      [Attribute(Required=false)]
      private int[] attribute;
      [Element(Required=false)]
      private int[] element;
      [ElementList]
      private List<int[]> list;
      [ElementArray]
      private int[][] array;
      [Element]
      private NonPrimitive test;
      [ElementList]
      private List<NonPrimitive> testList;
      [ElementArray]
      private NonPrimitive[] testArray;
      public IntegerArrayExample() {
         super();
      }
      public IntegerArrayExample(int[] list) {
         this.attribute = list;
         this.element = list;
         this.list = new ArrayList<int[]>();
         this.list.add(list);
         this.list.add(list);
         this.array = new int[1][];
         this.array[0] = list;
         this.testList = new ArrayList<NonPrimitive>();
         this.testList.add(null);
         this.testList.add(null);
         this.test = new NonPrimitive();
         this.testArray = new NonPrimitive[1];
      }
   }
   [Root]
   private static class NonPrimitive {
      [Attribute]
      private String value = "text";
   }
   public void TestRead() {
      ArrayTransform transform = new ArrayTransform(new IntegerTransform(), int.class);
      int[] list = (int[])transform.read("1,2,3,4");
      assertEquals(1, list[0]);
      assertEquals(2, list[1]);
      assertEquals(3, list[2]);
      assertEquals(4, list[3]);
      list = (int[])transform.read("  123 ,\t\n "+
                                   "1\n\r," +
                                   "100, 23, \t32,\t 0\n,\n"+
                                   "3\n\t");
      assertEquals(123, list[0]);
      assertEquals(1, list[1]);
      assertEquals(100, list[2]);
      assertEquals(23, list[3]);
      assertEquals(32, list[4]);
      assertEquals(0, list[5]);
      assertEquals(3, list[6]);
   }
   public void TestWrite() {
      ArrayTransform transform = new ArrayTransform(new IntegerTransform(), int.class);
      String value = transform.write(new int[] { 1, 2, 3, 4});
      assertEquals(value, "1, 2, 3, 4");
      value = transform.write(new int[] {1, 0, 3, 4});
      assertEquals(value, "1, 0, 3, 4");
   }
   public void TestPersistence() {
      int[] list = new int[] { 1, 2, 3, 4 };
      Persister persister = new Persister();
      IntegerArrayExample example = new IntegerArrayExample(list);
      StringWriter out = new StringWriter();
      assertEquals(example.attribute[0], 1);
      assertEquals(example.attribute[1], 2);
      assertEquals(example.attribute[2], 3);
      assertEquals(example.attribute[3], 4);
      assertEquals(example.element[0], 1);
      assertEquals(example.element[1], 2);
      assertEquals(example.element[2], 3);
      assertEquals(example.element[3], 4);
      assertEquals(example.list.get(0)[0], 1);
      assertEquals(example.list.get(0)[1], 2);
      assertEquals(example.list.get(0)[2], 3);
      assertEquals(example.list.get(0)[3], 4);
      assertEquals(example.array[0][0], 1);
      assertEquals(example.array[0][1], 2);
      assertEquals(example.array[0][2], 3);
      assertEquals(example.array[0][3], 4);
      persister.write(example, out);
      String text = out.toString();
      System.out.println(text);
      example = persister.read(IntegerArrayExample.class, text);
      assertEquals(example.attribute[0], 1);
      assertEquals(example.attribute[1], 2);
      assertEquals(example.attribute[2], 3);
      assertEquals(example.attribute[3], 4);
      assertEquals(example.element[0], 1);
      assertEquals(example.element[1], 2);
      assertEquals(example.element[2], 3);
      assertEquals(example.element[3], 4);
      assertEquals(example.list.get(0)[0], 1);
      assertEquals(example.list.get(0)[1], 2);
      assertEquals(example.list.get(0)[2], 3);
      assertEquals(example.list.get(0)[3], 4);
      assertEquals(example.array[0][0], 1);
      assertEquals(example.array[0][1], 2);
      assertEquals(example.array[0][2], 3);
      assertEquals(example.array[0][3], 4);
      validate(example, persister);
      example = new IntegerArrayExample(null);
      out = new StringWriter();
      persister.write(example, out);
      text = out.toString();
      validate(example, persister);
      example = persister.read(IntegerArrayExample.class, text);
      assertEquals(example.attribute, null);
      assertEquals(example.element, null);
      assertEquals(example.list.size(), 0);
      assertEquals(example.array[0], null);
   }
   public void TestCyclicPersistence() {
      int[] list = new int[] { 1, 2, 3, 4 };
      CycleStrategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      IntegerArrayExample example = new IntegerArrayExample(list);
      StringWriter out = new StringWriter();
      assertEquals(example.attribute[0], 1);
      assertEquals(example.attribute[1], 2);
      assertEquals(example.attribute[2], 3);
      assertEquals(example.attribute[3], 4);
      assertEquals(example.element[0], 1);
      assertEquals(example.element[1], 2);
      assertEquals(example.element[2], 3);
      assertEquals(example.element[3], 4);
      assertEquals(example.list.get(0)[0], 1);
      assertEquals(example.list.get(0)[1], 2);
      assertEquals(example.list.get(0)[2], 3);
      assertEquals(example.list.get(0)[3], 4);
      assertEquals(example.array[0][0], 1);
      assertEquals(example.array[0][1], 2);
      assertEquals(example.array[0][2], 3);
      assertEquals(example.array[0][3], 4);
      persister.write(example, out);
      String text = out.toString();
      assertXpathExists("/integerArrayExample[@id='0']", text);
      assertXpathExists("/integerArrayExample/element[@id='1']", text);
      assertXpathExists("/integerArrayExample/list[@id='2']", text);
      assertXpathExists("/integerArrayExample/array[@id='3']", text);
      assertXpathExists("/integerArrayExample/list/int[@reference='1']", text);
      assertXpathExists("/integerArrayExample/array/int[@reference='1']", text);
      assertXpathEvaluatesTo("1, 2, 3, 4", "/integerArrayExample/element", text);
      validate(example, persister);
      example = new IntegerArrayExample(null);
      out = new StringWriter();
      persister.write(example, out);
      text = out.toString();
      validate(example, persister);
      example = persister.read(IntegerArrayExample.class, text);
      assertEquals(example.attribute, null);
      assertEquals(example.element, null);
      assertEquals(example.list.size(), 0);
      assertEquals(example.array[0], null);
      assertXpathExists("/integerArrayExample[@id='0']", text);
      assertXpathExists("/integerArrayExample/list[@id='1']", text);
      assertXpathExists("/integerArrayExample/array[@id='2']", text);
   }
}
}
