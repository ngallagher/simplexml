package org.simpleframework.xml.core;

import junit.framework.TestCase;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.stream.Format;

public class ClassScannerTest extends TestCase {
   
   @Root
   @Order(elements={"a", "b"}, attributes={"A", "B"})
   @Namespace(prefix="prefix", reference="http://domain/reference")
   private static class Example {
      
      @Commit
      public void commit() {
         return;
      }
      
      @Validate
      public void validate() {
         return;
      }
   }

   public void testClassScanner() throws Exception {
      ClassScanner scanner = new ClassScanner(Example.class, new Format());
      
      assertNotNull(scanner.getRoot());
      assertNotNull(scanner.getOrder());
   }
}
