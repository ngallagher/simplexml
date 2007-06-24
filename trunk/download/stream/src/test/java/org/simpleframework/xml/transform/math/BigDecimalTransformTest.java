package org.simpleframework.xml.transform.math;

import java.math.BigDecimal;
import junit.framework.TestCase;

public class BigDecimalTransformTest extends TestCase {
   
   public void testBigDecimal() throws Exception {
      BigDecimal decimal = new BigDecimal("1.1");
      BigDecimalTransform format = new BigDecimalTransform();
      String value = format.write(decimal);
      BigDecimal copy = format.read(value);
      
      assertEquals(decimal, copy);      
   }
}