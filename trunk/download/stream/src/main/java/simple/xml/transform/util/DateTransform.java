package simple.xml.transform.util;

import simple.xml.transform.Transform;
import java.util.Date;

public class DateTransform implements Transform<Date> {
   
   private DateFormatter transform;
   
   public DateTransform() {
      this("yyyy-MM-dd HH:mm:ss.S z");
   }
   
   public DateTransform(String format) {
      this.transform = new DateFormatter(format);      
   }
   
   public Date read(String value) throws Exception {
      return transform.read(value);
   }
   
   public String write(Date value) throws Exception {
      return transform.write(value);
   }

}
