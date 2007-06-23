package simple.xml.transform.util;

import simple.xml.transform.Transform;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateTask implements Transform<Date> {
   
   private final SimpleDateFormat format;
   
   private final DateTaskPool pool;
   
   public DateTask(DateTaskPool pool) {
      this(pool, "");
   }
   
   public DateTask(DateTaskPool pool, String format) {
      this.format = new SimpleDateFormat(format);
      this.pool = pool;
   }
   
   public Date read(String value) throws ParseException {
      try {
         return format.parse(value);
      } finally {
         pool.release(this);
      }
   }
   
   public String write(Date value) {
      try {
         return format.format(value);
      } finally {
         pool.release(this);
      }
   }
}
