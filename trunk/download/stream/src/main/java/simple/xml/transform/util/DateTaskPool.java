package simple.xml.transform.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class DateTaskPool extends LinkedBlockingQueue<DateTask> {
   
   private final AtomicInteger count;
   
   private final String format;
   
   public DateTaskPool(String format) {
      this(format, 10);
   }
   
   public DateTaskPool(String format, int count) {      
      this.count = new AtomicInteger(count);
      this.format = format;
   }
   
   public DateTask borrow() throws InterruptedException {      
      int size = count.get();
      
      if(size > 0) {
         int next = count.getAndDecrement();
         
         if(next > 0) {
            return new DateTask(this, format);
         }
      }
      return take();
   }
   
   
   public void release(DateTask task) {
      offer(task);
   }
}


