
package org.simpleframework.xml.benchmark;

import java.io.File;
import java.io.FileNotFoundException;

import org.simpleframework.xml.core.Persister;


public class Benchmark {

   public static void main(String[] list) throws Exception {
      if(list.length < 1) {
         throw new IllegalStateException("File argument required");    
      }
      String source = list[0];
      File file = new File(source);
      
      if(!file.exists()) {
         throw new FileNotFoundException(source);
      }
      Persister persister = new Persister();
      TestExecution execution = persister.read(TestExecution.class, file);
      
      execution.execute();
   }
}
