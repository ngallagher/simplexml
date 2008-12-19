package org.simpleframework.http.core;

import java.io.File;
import java.util.List;

import org.simpleframework.util.buffer.Buffer;
import org.simpleframework.xml.core.Persister;

public class Runner {
   
   public static void main(String[] list) throws Exception {
      if(list.length == 0) {
         throw new IllegalArgumentException("Must specify the test XML file");
      }
      Persister persister = new Persister();
      Test test = persister.read(Test.class, new File(list[0]));
      List<Buffer> result = test.execute();
      
      
   }

}
