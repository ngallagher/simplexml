
package org.simpleframework.xml.benchmark.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.simpleframework.xml.benchmark.Duration;
import org.simpleframework.xml.benchmark.Executor;
import org.simpleframework.xml.benchmark.TestRun;



public class JAXBExecutor implements Executor {
   
   public Duration read(TestRun test) throws Exception {
	  long start = System.currentTimeMillis();
      JAXBContext context = JAXBContext.newInstance(test.getSchemaClass());      
      Unmarshaller unmarshaller = context.createUnmarshaller();
      
      // Perform once to build up internal caching
      Object result = unmarshaller.unmarshal(test.getXMLEventReader());     
      long startRead = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         result = unmarshaller.unmarshal(test.getXMLEventReader());        
      }
      return new Duration(start, startRead);      
   }        
   
   public Duration write(TestRun test) throws Exception {
	  long start = System.currentTimeMillis();
      JAXBContext context = JAXBContext.newInstance(test.getSchemaClass());      
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Object result = unmarshaller.unmarshal(test.getXMLEventReader());
      
      // Perform once to build up any caching
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(result, System.out);
      
      long startWrite = System.currentTimeMillis();
      
      for(int i = 0; i < test.getIterations(); i++) {
         marshaller.marshal(result, test.getResultWriter());        
      }
      return new Duration(start, startWrite);      
   }   
}
