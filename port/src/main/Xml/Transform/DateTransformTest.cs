#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class DateTransformTest : ValidationTestCase {
   @Root
   public static class DateExample {
      @ElementArray
      private Date[] array;
      @Element
      private Date element;
      @Attribute
      private Date attribute;
      @ElementList
      private List<Date> list;
      public DateExample() {
         super();
      }
      public DateExample(Date date) {
         this.attribute = date;
         this.element = date;
         this.list = new ArrayList<Date>();
         this.list.add(date);
         this.list.add(date);
         this.array = new Date[1];
         this.array[0] = date;
      }
   }
   public void TestDate() {
      Date date = new Date();
      DateTransform format = new DateTransform(Date.class);
      String value = format.write(date);
      Date copy = format.read(value);
      assertEquals(date, copy);
   }
   public void TestPersistence() {
      long now = System.currentTimeMillis();
      Date date = new Date(now);
      Persister persister = new Persister();
      DateExample example = new DateExample(date);
      StringWriter out = new StringWriter();
      assertEquals(example.attribute, date);
      assertEquals(example.element, date);
      assertEquals(example.array[0], date);
      assertEquals(example.list.get(0), date);
      assertEquals(example.list.get(1), date);
      persister.write(example, out);
      String text = out.toString();
      example = persister.read(DateExample.class, text);
      assertEquals(example.attribute, date);
      assertEquals(example.element, date);
      assertEquals(example.array[0], date);
      assertEquals(example.list.get(0), date);
      assertEquals(example.list.get(1), date);
      validate(example, persister);
   }
   public void TestCyclicPersistence() {
      long now = System.currentTimeMillis();
      Date date = new Date(now);
      CycleStrategy strategy = new CycleStrategy();
      Persister persister = new Persister(strategy);
      DateExample example = new DateExample(date);
      StringWriter out = new StringWriter();
      assertEquals(example.attribute, date);
      assertEquals(example.element, date);
      assertEquals(example.array[0], date);
      assertEquals(example.list.get(0), date);
      assertEquals(example.list.get(1), date);
      persister.write(example, out);
      String text = out.toString();
      assertXpathExists("/dateExample[@id='0']", text);
      assertXpathExists("/dateExample/array[@id='1']", text);
      assertXpathExists("/dateExample/array/date[@id='2']", text);
      assertXpathExists("/dateExample/element[@reference='2']", text);
      assertXpathExists("/dateExample/list[@id='3']", text);
      example = persister.read(DateExample.class, text);
      assertEquals(example.attribute, date);
      assertEquals(example.element, date);
      assertEquals(example.array[0], date);
      assertEquals(example.list.get(0), date);
      assertEquals(example.list.get(1), date);
      validate(example, persister);
   }
}
}
