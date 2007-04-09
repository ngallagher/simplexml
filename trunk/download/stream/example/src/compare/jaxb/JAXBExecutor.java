
package compare.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import compare.Executor;
import compare.TestRun;

public class JAXBExecutor implements Executor {
   
   public long read(TestRun test) throws Exception {
      JAXBContext context = JAXBContext.newInstance(test.getSchemaClass());      
      Unmarshaller unmarshaller = context.createUnmarshaller();
      
      // Perform once to build up internal caching
      Object result = unmarshaller.unmarshal(test.getSourceStream());     
      long start = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         result = unmarshaller.unmarshal(test.getSourceStream());        
      }
      return System.currentTimeMillis() - start;
   }        
   
   public long write(TestRun test) throws Exception {
      JAXBContext context = JAXBContext.newInstance(test.getSchemaClass());      
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Object result = unmarshaller.unmarshal(test.getSourceStream());
      
      // Perform once to build up any caching
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(result, System.out);
      
      long start = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         marshaller.marshal(result, test.getResultStream());        
      }
      return System.currentTimeMillis() - start;
   }   
}
