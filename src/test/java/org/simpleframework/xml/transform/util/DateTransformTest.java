package org.simpleframework.xml.transform.util;

import java.util.Date;

import org.simpleframework.xml.transform.util.DateTransform;

import junit.framework.TestCase;

public class DateTransformTest extends TestCase {
   
   public void testDate() throws Exception {
      Date date = new Date();
      DateTransform format = new DateTransform();
      String value = format.write(date);
      Date copy = format.read(value);
      
      assertEquals(date, copy);      
   }
}
