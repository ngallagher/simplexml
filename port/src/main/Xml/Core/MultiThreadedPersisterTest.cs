#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MultiThreadedPersisterTest : TestCase {
      [Root]
      [Default]
      private static class Example {
         private String name;
         private String value;
         private int number;
         private Date date;
         private Locale locale;
      }
      private static enum Status {
         ERROR,
         SUCCESS
      }
      private static class Worker : Thread {
         private readonly CountDownLatch latch;
         private readonly Serializer serializer;
         private readonly Queue<Status> queue;
         private readonly Example example;
         public Worker(CountDownLatch latch, Serializer serializer, Queue<Status> queue, Example example) {
            this.serializer = serializer;
            this.example = example;
            this.latch = latch;
            this.queue = queue;
         }
         public void Run() {
            try {
               latch.countDown();
               latch.await();
               for(int i = 0; i < 100; i++) {
                  StringWriter writer = new StringWriter();
                  serializer.Write(example, writer);
                  String text = writer.toString();
                  Example copy = serializer.Read(Example.class, text);
                  Assert.AssertEquals(example.name, copy.name);
                  Assert.AssertEquals(example.value, copy.value);
                  Assert.AssertEquals(example.number, copy.number);
                  Assert.AssertEquals(example.date, copy.date);
                  Assert.AssertEquals(example.locale, copy.locale);
                  System.out.println(text);
               }
               queue.offer(Status.SUCCESS);
            }catch(Exception e) {
               e.printStackTrace();
               queue.offer(Status.ERROR);
            }
         }
      }
      public void TestConcurrency() {
         Persister persister = new Persister();
         CountDownLatch latch = new CountDownLatch(20);
         BlockingQueue<Status> status = new LinkedBlockingQueue<Status>();
         Example example = new Example();
         example.name = "Eample Name";
         example.value = "Some Value";
         example.number = 10;
         example.date = new Date();
         example.locale = Locale.UK;
         for(int i = 0; i < 20; i++) {
            Worker worker = new Worker(latch, persister, status, example);
            worker.start();
         }
         for(int i = 0; i < 20; i++) {
            AssertEquals("Serialization fails when used concurrently", status.take(), Status.SUCCESS);
         }
      }
   }
}
