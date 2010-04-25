#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
class DateFormatter : Transform<Date> {
   private AtomicInteger count;
   private TaskQueue queue;
   private String format;
   public DateFormatter(String format) {
      this(format, 10);
   }
   public DateFormatter(String format, int count) {
      this.count = new AtomicInteger(count);
      this.queue = new TaskQueue(count);
      this.format = format;
   }
   public Date Read(String value) {
      return Borrow().Read(value);
   }
   public String Write(Date value) {
      return Borrow().Write(value);
   }
   public Task Borrow() {
      int size = count.get();
      if(size > 0) {
         int next = count.getAndDecrement();
         if(next > 0) {
            return new Task(format);
         }
      }
      return queue.take();
   }
   public void Release(Task task) {
      queue.offer(task);
   }
   private class Task {
      private SimpleDateFormat format;
      public Task(String format) {
         this.format = new SimpleDateFormat(format);
      }
      public Date Read(String value) {
         try {
            return format.parse(value);
         } finally {
            Release(this);
         }
      }
      public String Write(Date value) {
         try {
            return format.format(value);
         } finally {
            Release(this);
         }
      }
   }
   private class TaskQueue : LinkedBlockingQueue<Task> {
      public TaskQueue(int size) {
         super(size);
      }
   }
}
}
