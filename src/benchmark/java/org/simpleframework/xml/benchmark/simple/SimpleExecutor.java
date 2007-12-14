
package org.simpleframework.xml.benchmark.simple;

import org.simpleframework.xml.benchmark.Duration;
import org.simpleframework.xml.benchmark.Executor;
import org.simpleframework.xml.benchmark.TestRun;
import org.simpleframework.xml.load.Persister;



public class SimpleExecutor implements Executor {
   
   public Duration read(TestRun test) throws Exception {
	  long start = System.currentTimeMillis();
      Persister persister = new Persister();
      Class schemaClass = test.getSchemaClass();
      
      // Perform once to build up schema cache
      Object result = persister.read(schemaClass, test.getSourceStream());        
      long startRead = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         result = persister.read(schemaClass, test.getSourceStream());        
      }
      return new Duration(start, startRead);
   }        
   
   public Duration write(TestRun test) throws Exception {
	  long start = System.currentTimeMillis();
      Persister persister = new Persister();
      Class schemaClass = test.getSchemaClass();
      Object result = persister.read(schemaClass, test.getSourceStream());
      
      // Perform once to build up schema cache
      persister.write(result, System.out);
      long startWrite = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         persister.write(result, test.getResultWriter());        
      }
      return new Duration(start, startWrite);
   }        
}
