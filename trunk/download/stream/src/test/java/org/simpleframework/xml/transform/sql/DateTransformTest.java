package org.simpleframework.xml.transform.sql;

import java.sql.Date;
import junit.framework.TestCase;

public class DateTransformTest extends TestCase {
   
   public void testDate() throws Exception {
      long now = System.currentTimeMillis();
      Date date = new Date(now);
      DateTransform format = new DateTransform();
      String value = format.write(date);
      Date copy = format.read(value);
      
      assertEquals(date, copy);  
      assertEquals(copy.getTime(), now);
   }
}