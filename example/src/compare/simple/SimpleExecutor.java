
package compare.simple;

import simple.xml.load.Persister;

import compare.Executor;
import compare.TestRun;

public class SimpleExecutor implements Executor {
   
   public long read(TestRun test) throws Exception {
      Persister persister = new Persister();
      Class schemaClass = test.getSchemaClass();
      
      // Perform once to build up schema cache
      Object result = persister.read(schemaClass, test.getSourceStream());        
      long start = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         result = persister.read(schemaClass, test.getSourceStream());        
      }
      return System.currentTimeMillis() - start;
   }        
   
   public long write(TestRun test) throws Exception {
      Persister persister = new Persister();
      Class schemaClass = test.getSchemaClass();
      Object result = persister.read(schemaClass, test.getSourceStream());
      
      // Perform once to build up schema cache
      persister.write(result, System.out);
      long start = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         persister.write(result, test.getResultStream());        
      }
      return System.currentTimeMillis() - start;
   }        
}
