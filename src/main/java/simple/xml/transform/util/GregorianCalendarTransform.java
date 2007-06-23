
package simple.xml.transform.util;

import simple.xml.transform.Transform;
import java.util.GregorianCalendar;
import java.util.Date;

public class GregorianCalendarTransform implements Transform<GregorianCalendar> {
   
   private DateTransform transform;
   
   public GregorianCalendarTransform() {
      this.transform = new DateTransform();
   }
   
   public GregorianCalendar read(String value) throws Exception {
      return read(transform.read(value));      
   }
   
   private GregorianCalendar read(Date value) throws Exception {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(value);
      return calendar;
   }
   
   public String write(GregorianCalendar value) throws Exception {
      return transform.write(value.getTime());
   }
}
