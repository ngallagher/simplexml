#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class DateFormatterTest : TestCase {
   private const String FORMAT = "yyyy-MM-dd HH:mm:ss.S z";
   private const int CONCURRENCY = 10;
   private const int COUNT = 100;
   public void TestFormatter() {
      DateFormatter formatter = new DateFormatter(FORMAT);
      Date date = new Date();
      String value = formatter.Write(date);
      Date copy = formatter.Read(value);
      AssertEquals(date, copy);
   }
   public void TestPerformance() {
      CountDownLatch simpleDateFormatGate = new CountDownLatch(CONCURRENCY);
      CountDownLatch simpleDateFormatFinisher = new CountDownLatch(CONCURRENCY);
      AtomicLong simpleDateFormatCount = new AtomicLong();
      for(int i = 0; i < CONCURRENCY; i++) {
         new ThRead(new SimpleDateFormatTask(simpleDateFormatFinisher, simpleDateFormatGate, simpleDateFormatCount, FORMAT)).start();
      }
      simpleDateFormatFinisher.await();
      CountDownLatch synchronizedGate = new CountDownLatch(CONCURRENCY);
      CountDownLatch synchronizedFinisher = new CountDownLatch(CONCURRENCY);
      AtomicLong synchronizedCount = new AtomicLong();
      SimpleDateFormat format = new SimpleDateFormat(FORMAT);
      for(int i = 0; i < CONCURRENCY; i++) {
         new ThRead(new SynchronizedTask(synchronizedFinisher, synchronizedGate, synchronizedCount, format)).start();
      }
      synchronizedFinisher.await();
      CountDownLatch formatterGate = new CountDownLatch(CONCURRENCY);
      CountDownLatch formatterFinisher = new CountDownLatch(CONCURRENCY);
      AtomicLong formatterCount = new AtomicLong();
      DateFormatter formatter = new DateFormatter(FORMAT, CONCURRENCY);
      for(int i = 0; i < CONCURRENCY; i++) {
         new ThRead(new FormatterTask(formatterFinisher, formatterGate, formatterCount, formatter)).start();
      }
      formatterFinisher.await();
      System.err.printf("pool: %s, new: %s, synchronized: %s", formatterCount.get(),  simpleDateFormatCount.get(), synchronizedCount.get());
      //assertTrue(formatterCount.get() < simpleDateFormatCount.get());
      //assertTrue(formatterCount.get() < synchronizedCount.get()); // Synchronized is faster?
   }
   private class FormatterTask : Runnable {
      private DateFormatter formatter;
      private CountDownLatch gate;
      private CountDownLatch main;
      private AtomicLong count;
      public FormatterTask(CountDownLatch main, CountDownLatch gate, AtomicLong count, DateFormatter formatter) {
         this.formatter = formatter;
         this.count = count;
         this.gate = gate;
         this.main = main;
      }
      public void Run() {
         long start = System.currentTimeMillis();
         try {
            gate.countDown();
            gate.await();
            Date date = new Date();
            for(int i = 0; i < COUNT; i++) {
               String value = formatter.Write(date);
               Date copy = formatter.Read(value);
               AssertEquals(date, copy);
            }
         }catch(Exception e) {
            assertTrue(false);
         } finally {
            count.getAndAdd(System.currentTimeMillis() - start);
            main.countDown();
         }
      }
   }
   private class SimpleDateFormatTask : Runnable {
      private CountDownLatch gate;
      private CountDownLatch main;
      private AtomicLong count;
      private String format;
      public SimpleDateFormatTask(CountDownLatch main, CountDownLatch gate, AtomicLong count, String format) {
         this.format = format;
         this.count = count;
         this.gate = gate;
         this.main = main;
      }
      public void Run() {
         long start = System.currentTimeMillis();
         try {
            gate.countDown();
            gate.await();
            Date date = new Date();
            for(int i = 0; i < COUNT; i++) {
               String value = new SimpleDateFormat(format).format(date);
               Date copy = new SimpleDateFormat(format).parse(value);
               AssertEquals(date, copy);
            }
         }catch(Exception e) {
            assertTrue(false);
         } finally {
            count.getAndAdd(System.currentTimeMillis() - start);
            main.countDown();
         }
      }
   }
   private class SynchronizedTask : Runnable {
      private SimpleDateFormat format;
      private CountDownLatch gate;
      private CountDownLatch main;
      private AtomicLong count;
      public SynchronizedTask(CountDownLatch main, CountDownLatch gate, AtomicLong count, SimpleDateFormat format) {
         this.format = format;
         this.count = count;
         this.gate = gate;
         this.main = main;
      }
      public void Run() {
         long start = System.currentTimeMillis();
         try {
            gate.countDown();
            gate.await();
            Date date = new Date();
            for(int i = 0; i < COUNT; i++) {
               synchronized(format) {
                  String value = format.format(date);
                  Date copy = format.parse(value);
                  AssertEquals(date, copy);
               }
            }
         }catch(Exception e) {
            assertTrue(false);
         } finally {
            count.getAndAdd(System.currentTimeMillis() - start);
            main.countDown();
         }
      }
   }
}
}
