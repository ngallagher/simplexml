
package org.simpleframework.xml.benchmark;


public interface Executor {

   public Duration read(TestRun test) throws Exception;
   
   public Duration write(TestRun test) throws Exception;
}
