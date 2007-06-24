package org.simpleframework.xml.transform.sql;

import java.sql.Time;
import junit.framework.TestCase;

public class TimeTransformTest extends TestCase {
   
   public void testTime() throws Exception {
      long now = System.currentTimeMillis();
      Time date = new Time(now);
      TimeTransform format = new TimeTransform();
      String value = format.write(date);
      Time copy = format.read(value);
      
      assertEquals(date, copy);  
      assertEquals(copy.getTime(), now);
   }
}