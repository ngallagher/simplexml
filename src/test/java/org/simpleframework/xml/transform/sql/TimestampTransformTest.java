package org.simpleframework.xml.transform.sql;

import java.sql.Timestamp;
import junit.framework.TestCase;

public class TimestampTransformTest extends TestCase {
   
   public void testTimestamp() throws Exception {
      long now = System.currentTimeMillis();
      Timestamp date = new Timestamp(now);
      TimestampTransform format = new TimestampTransform();
      String value = format.write(date);
      Timestamp copy = format.read(value);
      
      assertEquals(date, copy);  
      assertEquals(copy.getTime(), now);
   }
}