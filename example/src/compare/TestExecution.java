package compare;

import java.util.Collection;

import simple.xml.Element;
import simple.xml.ElementList;
import simple.xml.Root;

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
      
      long readDuration = executor.read(test);
      long writeDuration = executor.write(test);
      
      System.err.printf("Execution of "+test.getId()+": read=%s ms write=%s ms", readDuration, writeDuration);
      System.err.println();
   }
   
}
