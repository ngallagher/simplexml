package simple.xml.transform.util;

import simple.xml.transform.Transform;
import java.util.Date;

public class DateTransform implements Transform<Date> {
   
   private final DateTaskPool pool;
   
   public DateTransform() {
      this("yyyy-MM-dd HH:mm:ss.S z");
   }
   
   public DateTransform(String format) {
      this.pool = new DateTaskPool(format);      
   }
   
   public Date read(String value) throws Exception {
      return pool.borrow().read(value);
   }
   
   public String write(Date value) throws Exception {
      return pool.borrow().write(value);
   }

}
