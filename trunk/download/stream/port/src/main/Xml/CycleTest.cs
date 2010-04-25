#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class CycleTest : ValidationTestCase {
      private const int ITERATIONS = 1000;
      @Root(name="example")
      public static class CycleExample {
         @ElementList(name="list", type=Entry.class)
         private List<Entry> list;
         @Element(name="cycle")
         private CycleExample cycle;
         public CycleExample() {
            this.list = new ArrayList();
            this.cycle = this;
         }
         public void Add(Entry entry) {
            list.Add(entry);
         }
         public Entry Get(int index) {
            return list.Get(index);
         }
      }
      @Root(name="entry")
      public static class Entry {
         @Attribute(name="key")
         private String name;
         @Element(name="value")
         private String value;
         protected Entry() {
            super();
         }
         public Entry(String name, String value) {
            this.name = name;
            this.value = value;
         }
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister(new CycleStrategy("id", "ref"));
      }
      public void TestCycle() {
         CycleExample example = new CycleExample();
         Entry one = new Entry("1", "one");
         Entry two = new Entry("2", "two");
         Entry three = new Entry("3", "three");
         Entry threeDuplicate = new Entry("3", "three");
         example.Add(one);
         example.Add(two);
         example.Add(three);
         example.Add(one);
         example.Add(two);
         example.Add(threeDuplicate);
         assertEquals(example.Get(0).value, "one");
         assertEquals(example.Get(1).value, "two");
         assertEquals(example.Get(2).value, "three");
         assertEquals(example.Get(3).value, "one");
         assertEquals(example.Get(4).value, "two");
         assertEquals(example.Get(5).value, "three");
         assertTrue(example.Get(0) == example.Get(3));
         assertTrue(example.Get(1) == example.Get(4));
         assertFalse(example.Get(2) == example.Get(5));
         StringWriter out = new StringWriter();
         persister.write(example, System.out);
         persister.write(example, out);
         example = persister.read(CycleExample.class, out.toString());
         assertEquals(example.Get(0).value, "one");
         assertEquals(example.Get(1).value, "two");
         assertEquals(example.Get(2).value, "three");
         assertEquals(example.Get(3).value, "one");
         assertEquals(example.Get(4).value, "two");
         assertTrue(example.Get(0) == example.Get(3));
         assertTrue(example.Get(1) == example.Get(4));
         assertFalse(example.Get(2) == example.Get(5));
         validate(example, persister);
      }
      public void TestMemory() {
         CycleExample example = new CycleExample();
   	   Entry one = new Entry("1", "one");
   	   Entry two = new Entry("2", "two");
   	   Entry three = new Entry("3", "three");
   	   Entry threeDuplicate = new Entry("3", "three");
   	   example.Add(one);
   	   example.Add(two);
   	   example.Add(three);
   	   example.Add(one);
   	   example.Add(two);
   	   example.Add(threeDuplicate);
   	   StringWriter out = new StringWriter();
   	   persister.write(example, System.out);
   	   persister.write(example, out);
   	   for(int i = 0; i < ITERATIONS; i++) {
   		  persister.write(example, new StringWriter());
   	   }
   	   for(int i = 0; i < ITERATIONS; i++) {
   	      persister.read(CycleExample.class, out.toString());
   	   }
   	   validate(example, persister);
      }
   }
}
