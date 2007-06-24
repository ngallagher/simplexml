package org.simpleframework.xml.transform.io;

import java.io.File;
import junit.framework.TestCase;

public class FileTransformTest extends TestCase {
   
   public void testFile() throws Exception {
      File file = new File("..");
      FileTransform format = new FileTransform();
      String value = format.write(file);
      File copy = format.read(value);
      
      assertEquals(file, copy);      
   }
}