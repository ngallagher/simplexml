#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class BigIntegerTransformTest : TestCase {
   public void TestBigInteger() {
      BigInteger integer = new BigInteger("1");
      BigIntegerTransform format = new BigIntegerTransform();
      String value = format.write(integer);
      BigInteger copy = format.read(value);
      assertEquals(integer, copy);
   }
}
}
