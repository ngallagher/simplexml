
package compare;


public interface Executor {

   public long read(TestRun test) throws Exception;
   
   public long write(TestRun test) throws Exception;
}
