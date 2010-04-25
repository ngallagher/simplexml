#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class BigDecimalTransformTest : TestCase {
   public void TestBigDecimal() {
      BigDecimal decimal = new BigDecimal("1.1");
      BigDecimalTransform format = new BigDecimalTransform();
      String value = format.write(decimal);
      BigDecimal copy = format.read(value);
      assertEquals(decimal, copy);
   }
}
}
