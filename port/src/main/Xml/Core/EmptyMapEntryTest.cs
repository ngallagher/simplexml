#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EmptyMapEntryTest : ValidationTestCase {
      /// <summary>
     /// </summary>
      [Root]
      public static class SimpleBug1 {
          [Element]
          protected Mojo test1;
          public SimpleBug1() {
              test1 = new Mojo();
              test1.data.put("key1", "value1");
              test1.data.put("key2", "value1");
              test1.data.put("key3", "");
              test1.data.put("key4", "");
              test1.data.put("", "");
          }
          public static class Mojo {
              [ElementMap]
              public Mojo() {
                  data = new HashMap<String, Object>();
              }
          }
      }
      /// <summary>
      /// </summary>
      /// <param name="args">
      /// the command line arguments
      /// </param>
      public void TestEmptyMapEntry() {
          Strategy resolver = new CycleStrategy("id", "ref");
          Serializer s = new Persister(resolver);
          StringWriter w = new StringWriter();
          SimpleBug1 bug1 = new SimpleBug1();
          assertEquals(bug1.test1.data.get("key1"), "value1");
          assertEquals(bug1.test1.data.get("key2"), "value1");
          assertEquals(bug1.test1.data.get("key3"), "");
          assertEquals(bug1.test1.data.get(""), "");
          s.write(bug1, w);
          System.err.println(w.toString());
          SimpleBug1 bug2 = s.read(SimpleBug1.class, w.toString());
          assertEquals(bug1.test1.data.get("key1"), bug2.test1.data.get("key1"));
          assertEquals(bug1.test1.data.get("key2"), bug2.test1.data.get("key2"));
          assertEquals(bug2.test1.data.get("key1"), "value1");
          assertEquals(bug2.test1.data.get("key2"), "value1");
          assertNull(bug2.test1.data.get("key3"));
          assertNull(bug2.test1.data.get(null));
          validate(s, bug1);
      }
   }
}
