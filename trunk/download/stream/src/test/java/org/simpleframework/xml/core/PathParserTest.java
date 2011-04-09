package org.simpleframework.xml.core;

import junit.framework.TestCase;

public class PathParserTest extends TestCase {
   
   public void testSimplePath() throws Exception {
      Expression expression = new PathParser(PathParserTest.class, "path1/path2/path3/path4");
      assertEquals(expression.getFirst(), "path1");
      assertEquals(expression.getPrefix(), null);
      assertEquals(expression.getLast(), "path4");
      assertEquals(expression.toString(), "path1/path2/path3/path4");
      assertTrue(expression.isPath());
      assertFalse(expression.isAttribute());
      
      expression = expression.getPath(1);
      assertEquals(expression.getFirst(), "path2");
      assertEquals(expression.getPrefix(), null);
      assertEquals(expression.getLast(), "path4");
      assertEquals(expression.toString(), "path2/path3/path4");
      assertTrue(expression.isPath());
      assertFalse(expression.isAttribute());
   }
   
   public void testPathPrefix() throws Exception {
      Expression expression = new PathParser(PathParserTest.class, "ns1:path1/ns2:path2/path3/ns3:path4[2]");
      assertEquals(expression.getFirst(), "path1");
      assertEquals(expression.getPrefix(), "ns1");
      assertEquals(expression.getLast(), "path4");
      assertEquals(expression.toString(), "ns1:path1/ns2:path2/path3/ns3:path4[2]");
      assertTrue(expression.isPath());
      assertFalse(expression.isAttribute());
      
      expression = expression.getPath(1);
      assertEquals(expression.getFirst(), "path2");
      assertEquals(expression.getPrefix(), "ns2");
      assertEquals(expression.getLast(), "path4");
      assertEquals(expression.toString(), "ns2:path2/path3/ns3:path4[2]");
      assertTrue(expression.isPath());
      assertFalse(expression.isAttribute());
      
      expression = expression.getPath(1);
      assertEquals(expression.getFirst(), "path3");
      assertEquals(expression.getPrefix(), null);
      assertEquals(expression.getLast(), "path4");
      assertEquals(expression.toString(), "path3/ns3:path4[2]");
      assertTrue(expression.isPath());
      assertFalse(expression.isAttribute());
      
      expression = expression.getPath(1);
      assertEquals(expression.getFirst(), "path4");
      assertEquals(expression.getPrefix(), "ns3");
      assertEquals(expression.getLast(), "path4");
      assertEquals(expression.getIndex(), 2);
      assertEquals(expression.toString(), "ns3:path4[2]");
      assertFalse(expression.isPath());
      assertFalse(expression.isAttribute());
   }
   
   public void testAttribute() throws Exception {
      Expression expression = new PathParser(PathParserTest.class, "./some[3]/path[2]/to/parse/@attribute");
      assertEquals(expression.getFirst(), "some");
      assertEquals(expression.getIndex(), 3);
      assertEquals(expression.getLast(), "attribute");
      assertEquals(expression.toString(), "some[3]/path[2]/to/parse/@attribute");
      assertTrue(expression.isPath());
      assertTrue(expression.isAttribute());
      
      expression = expression.getPath(2);
      assertEquals(expression.getFirst(), "to");
      assertEquals(expression.getIndex(), 1);
      assertEquals(expression.getLast(), "attribute");
      assertEquals(expression.toString(), "to/parse/@attribute");
      assertTrue(expression.isPath());
      assertTrue(expression.isAttribute());
      
      expression = expression.getPath(2);
      assertEquals(expression.getFirst(), "attribute");
      assertEquals(expression.getIndex(), 1);
      assertEquals(expression.getLast(), "attribute");
      assertEquals(expression.toString(), "@attribute");
      assertFalse(expression.isPath());
      assertTrue(expression.isAttribute());
   }

   public void testIndex() throws Exception {
      Expression expression = new PathParser(PathParserTest.class, "./some[3]/path[2]/to/parse");
      assertEquals(expression.getFirst(), "some");
      assertEquals(expression.getIndex(), 3);
      assertEquals(expression.getLast(), "parse");
      assertEquals(expression.toString(), "some[3]/path[2]/to/parse");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(1);
      assertEquals(expression.getFirst(), "path");
      assertEquals(expression.getIndex(), 2);
      assertEquals(expression.getLast(), "parse");
      assertEquals(expression.toString(), "path[2]/to/parse");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(0, 1);
      assertEquals(expression.getFirst(), "path");
      assertEquals(expression.getIndex(), 2);
      assertEquals(expression.getLast(), "to");
      assertEquals(expression.toString(), "path[2]/to");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(0, 1);
      assertEquals(expression.getFirst(), "path");
      assertEquals(expression.getIndex(), 2);
      assertEquals(expression.getLast(), "path");
      assertEquals(expression.toString(), "path[2]");
      assertFalse(expression.isPath());
      
      expression = new PathParser(PathParserTest.class, "./a[10]/b[2]/c/d");      
      assertEquals(expression.getFirst(), "a");
      assertEquals(expression.getIndex(), 10);
      assertEquals(expression.getLast(), "d");
      assertEquals(expression.toString(), "a[10]/b[2]/c/d");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(1, 1);
      assertEquals(expression.getFirst(), "b");
      assertEquals(expression.getIndex(), 2);
      assertEquals(expression.getLast(), "c");
      assertEquals(expression.toString(), "b[2]/c");
      assertTrue(expression.isPath());
      
      expression = new PathParser(PathParserTest.class, "a[10]/b[2]/c/d[300]");      
      assertEquals(expression.getFirst(), "a");
      assertEquals(expression.getIndex(), 10);
      assertEquals(expression.getLast(), "d");
      assertEquals(expression.toString(), "a[10]/b[2]/c/d[300]");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(3);      
      assertEquals(expression.getFirst(), "d");
      assertEquals(expression.getIndex(), 300);
      assertEquals(expression.getLast(), "d");
      assertEquals(expression.toString(), "d[300]");
      assertFalse(expression.isPath());
      
      expression = new PathParser(PathParserTest.class, "b[1]/c/d[300]/");      
      assertEquals(expression.getFirst(), "b");
      assertEquals(expression.getIndex(), 1);
      assertEquals(expression.getLast(), "d");
      assertEquals(expression.toString(), "b[1]/c/d[300]");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(1);      
      assertEquals(expression.getFirst(), "c");
      assertEquals(expression.getIndex(), 1);
      assertEquals(expression.getLast(), "d");
      assertEquals(expression.toString(), "c/d[300]");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(0, 1);      
      assertEquals(expression.getFirst(), "c");
      assertEquals(expression.getIndex(), 1);
      assertEquals(expression.getLast(), "c");
      assertEquals(expression.toString(), "c");
      assertFalse(expression.isPath());
   }
   
   public void testExpressions() throws Exception {
      Expression expression = new PathParser(PathParserTest.class, "./some/path/to/parse");
      assertEquals(expression.getFirst(), "some");
      assertEquals(expression.getLast(), "parse");
      assertEquals(expression.toString(), "some/path/to/parse");
      assertTrue(expression.isPath());
      
      expression = expression.getPath(2);
      assertEquals(expression.getFirst(), "to");
      assertEquals(expression.getLast(), "parse");
      assertEquals(expression.toString(), "to/parse");
      assertTrue(expression.isPath());
      
      expression = new PathParser(PathParserTest.class, "a/b/c/d/e/f/g");
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
      
      expression = new PathParser(PathParserTest.class, "1/2/3/4/5/6/7");
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
      
      expression = new PathParser(PathParserTest.class, ".");
      assertFalse(expression.isPath());
      
      expression = new PathParser(PathParserTest.class, "./name");
      assertEquals(expression.getFirst(), "name");
      assertEquals(expression.getLast(), "name");
      assertEquals(expression.toString(), "name");
      assertFalse(expression.isPath()); 
      
      expression = new PathParser(PathParserTest.class, "./path/");
      assertEquals(expression.getFirst(), "path");
      assertEquals(expression.getLast(), "path");
      assertEquals(expression.toString(), "path");
      assertFalse(expression.isPath());   
   }
   
   
   public void testExceptions() throws Exception {
      ensureException("//");
      ensureException("some[[1]/path");
      ensureException("some1]/path");
      ensureException("a/b//c");
      ensureException("./a[100]//b");
      ensureException("./a[100]/b/@@attribute");
      ensureException("../a[100]/b/@attribute");
      ensureException("../a[100]/@b/@attribute");
      ensureException("a//b//c");
      ensureException("a/@b/@c");
      ensureException("a/@b/");
      ensureException("a/b[1]/c[");
      ensureException("a/b[1]/c/@");
      ensureException("a@");
   }
   
   private void ensureException(String path) {
      boolean exception = false;
      try{
         new PathParser(PathParserTest.class, path);
      }catch(Exception e){
         e.printStackTrace();
         exception = true;
      }
      assertTrue("Exception should be thrown for "+path, exception);
   }

}
