package simple.xml.transform.util;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class GregorianCalendarTransformTest extends TestCase {
   
   public void testGregorianCalendar() throws Exception {
      GregorianCalendar date = new GregorianCalendar();
      GregorianCalendarTransform format = new GregorianCalendarTransform();
      
      date.setTime(new Date());
      
      String value = format.write(date);
      GregorianCalendar copy = format.read(value);
      
      assertEquals(date, copy);      
   }
}
