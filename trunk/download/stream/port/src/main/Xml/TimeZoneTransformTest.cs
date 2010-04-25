#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class TimeZoneTransformTest : TestCase {
   public void TestTimeZone() {
      TimeZone zone = TimeZone.getTimeZone("GMT");
      TimeZoneTransform format = new TimeZoneTransform();
      String value = format.write(zone);
      TimeZone copy = format.read(value);
      assertEquals(zone, copy);
   }
}
}
