#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class TimeTransformTest : ValidationTestCase {
   [Root]
   public static class TimeExample {
      [Attribute]
      private Date attribute;
      [Element]
      private Date element;
      [Element]
      private Time time;
      [ElementList]
      private Collection<Time> list;
      [ElementArray]
      private Time[] array;
      public TimeExample() {
         super();
      }
      public TimeExample(long time) {
         this.attribute = new Time(time);
         this.element = new Time(time);
         this.time = new Time(time);
         this.list = new ArrayList<Time>();
         this.list.add(new Time(time));
         this.list.add(new Time(time));
         this.array = new Time[1];
         this.array[0] = new Time(time);
      }
   }
   public void TestTime() {
      long now = System.currentTimeMillis();
      Time date = new Time(now);
      DateTransform format = new DateTransform(Time.class);
      String value = format.write(date);
      Date copy = format.read(value);
      assertEquals(date, copy);
      assertEquals(copy.getTime(), now);
   }
   public void TestPersistence() {
      long now = System.currentTimeMillis();
      Persister persister = new Persister();
      TimeExample example = new TimeExample(now);
      validate(example, persister);
   }
}
}
