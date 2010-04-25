#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class GregorianCalendarTransformTest : TestCase {
   public void TestGregorianCalendar() {
      GregorianCalendar date = new GregorianCalendar();
      GregorianCalendarTransform format = new GregorianCalendarTransform();
      date.setTime(new Date());
      String value = format.write(date);
      GregorianCalendar copy = format.read(value);
      assertEquals(date, copy);
   }
}
}
