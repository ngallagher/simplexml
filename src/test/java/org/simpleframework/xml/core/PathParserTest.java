package org.simpleframework.xml.core;

import junit.framework.TestCase;

public class PathParserTest extends TestCase {
   
   public void testExpressions() throws Exception {
      Expression expression = new PathParser("./some/path/to/parse");
      assertEquals(expression.getFirst(), "some");
      assertEquals(expression.getLast(), "parse");
      assertEquals(expression.toString(), "some/path/to/parse");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(2);
      assertEquals(expression.getFirst(), "to");
      assertEquals(expression.getLast(), "parse");
      assertEquals(expression.toString(), "to/parse");
      assertTrue(expression.isPath());
      
      expression = new PathParser("a/b/c/d/e/f/g");
      assertEquals(expression.getFirst(), "a");
      assertEquals(expression.getLast(), "g");
      assertEquals(expression.toString(), "a/b/c/d/e/f/g");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(2);
      assertEquals(expression.getFirst(), "c");
      assertEquals(expression.getLast(), "g");
      assertEquals(expression.toString(), "c/d/e/f/g");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(1);
      assertEquals(expression.getFirst(), "d");
      assertEquals(expression.getLast(), "g");
      assertEquals(expression.toString(), "d/e/f/g");
      assertTrue(expression.isPath());
      
      expression = new PathParser("1/2/3/4/5/6/7");
      assertEquals(expression.getFirst(), "1");
      assertEquals(expression.getLast(), "7");
      assertEquals(expression.toString(), "1/2/3/4/5/6/7");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(1, 1);
      assertEquals(expression.getFirst(), "2");
      assertEquals(expression.getLast(), "6");
      assertEquals(expression.toString(), "2/3/4/5/6");
      assertTrue(expression.isPath());

      expression = expression.getPath(1, 1);
      assertEquals(expression.getFirst(), "3");
      assertEquals(expression.getLast(), "5");
      assertEquals(expression.toString(), "3/4/5");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(1, 1);
      assertEquals(expression.getFirst(), "4");
      assertEquals(expression.getLast(), "4");
      assertEquals(expression.toString(), "4");
      assertFalse(expression.isPath());
      
      expression = new PathParser(".");
      assertFalse(expression.isPath());
      
      expression = new PathParser("./name");
      assertEquals(expression.getFirst(), "name");
      assertEquals(expression.getLast(), "name");
      assertEquals(expression.toString(), "name");
      assertFalse(expression.isPath()); 
      
      expression = new PathParser("./path/");
      assertEquals(expression.getFirst(), "path");
      assertEquals(expression.getLast(), "path");
      assertEquals(expression.toString(), "path");
      assertFalse(expression.isPath());    
      
      boolean exception = false;
      try {
         expression = new PathParser("a//b//c");         
      }catch(PathException e) {
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Exception should be thrown for double slash", exception);
      
   }

}
