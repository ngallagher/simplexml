package org.simpleframework.xml;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestRunner  {  
   private final String[] suites;
   public TestRunner(String... suites) {
      this.suites = suites;
   }
   public String test() throws Exception  {
      List<TestSuite> list = new ArrayList<TestSuite>();
      StringWriter builder = new StringWriter();
      PrintWriter writer = new PrintWriter(builder);
      TestScanner scanner = new TestScanner();
      for(String suite : suites) {
         Class type = Class.forName(suite);
         TestSuite instance = (TestSuite)type.newInstance();
         list.add(instance);
      }
      for(TestSuite suite : list) {
         Class[] tests = suite.suite();
         for(Class type : tests) {
            try {
               TestCase test = (TestCase)type.newInstance();
               scanner.execute(test);
               writer.printf("SUCCESS: %s%n", type);
            }catch(Exception cause) {
               writer.printf("FAILED: %s%n", type);
               cause.printStackTrace(writer);
               return builder.toString();
            }
         }
      }
      return builder.toString();
   }
}