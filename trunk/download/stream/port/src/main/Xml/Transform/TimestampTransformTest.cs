#region Using directives
using SimpleFramework.Xml.Transform;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class TimestampTransformTest : TestCase {
   public void TestTimestamp() {
      long now = System.currentTimeMillis();
      Timestamp date = new Timestamp(now);
      DateTransform format = new DateTransform(Timestamp.class);
      String value = format.write(date);
      Date copy = format.read(value);
      AssertEquals(date, copy);
      AssertEquals(copy.getTime(), now);
      assertTrue(copy instanceof Timestamp);
   }
}
}
