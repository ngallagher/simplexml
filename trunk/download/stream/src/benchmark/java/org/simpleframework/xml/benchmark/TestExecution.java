package org.simpleframework.xml.benchmark;

import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name="execute")
public class TestExecution {

   @Element(name="context")
   private TestContext context;
   
   @ElementList(name="list", type=TestRun.class)
   private Collection<TestRun> list;
   
   public void execute() throws Exception {
      for(TestRun test : list) {
         execute(test);
      }
   }
   
   public void execute(TestRun test) throws Exception {
      Class executorClass = test.getExecutorClass();
      Executor executor = (Executor) executorClass.newInstance();
      
      Duration readDuration = executor.read(test);
      Duration writeDuration = executor.write(test);
      
      System.err.printf("Execution of "+test.getId() + " " +test.getIterations() +
    		  " times: read=%s ms read-total=%s write=%s ms write-total=%s ms average-read=%s ms average-write=%s ms", 
    		  readDuration.getOperation(), readDuration.getTotal(), 
    		  writeDuration.getOperation(), writeDuration.getOperation(),
    		  readDuration.getAverage(), writeDuration.getAverage());
      
      System.err.println();
   }
   
}
