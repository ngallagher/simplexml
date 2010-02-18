package org.simpleframework.xml.strategy;


import java.net.URI;
import java.util.HashMap;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.w3c.dom.Node;

public class PackageParserTest extends TestCase {
   
   private static final int ITERATIONS = 100000;
   
   /**
    * 
    * <element xmlns:class='http://util.java/ArrayList'>
    *    <name>name</name>
    *    <value>value</value>
    * </element> 
    * 
    */
   public void testParser() throws Exception {
      assertEquals("http://util.java/HashMap", parse(HashMap.class));
      assertEquals("http://simpleframework.org/xml/Element", parse(Element.class));
      assertEquals("http://simpleframework.org/xml/ElementList", parse(ElementList.class));
      assertEquals("http://w3c.org/dom/Node", parse(Node.class));
      assertEquals("http://simpleframework.org/xml/strategy/PackageParserTest$PackageParser", parse(PackageParser.class));
      
      assertEquals(HashMap.class, revert("http://util.java/HashMap"));
      assertEquals(Element.class, revert("http://simpleframework.org/xml/Element"));
      assertEquals(ElementList.class, revert("http://simpleframework.org/xml/ElementList"));
      assertEquals(Node.class, revert("http://w3c.org/dom/Node"));
      assertEquals(PackageParser.class, revert("http://simpleframework.org/xml/strategy/PackageParserTest$PackageParser"));
      
      long start = System.currentTimeMillis();
      for(int i = 0; i < ITERATIONS; i++) {
         fastParse(ElementList.class);
      }
      long fast = System.currentTimeMillis() - start;
      start = System.currentTimeMillis();
      for(int i = 0; i < ITERATIONS; i++) {
         parse(ElementList.class);
      }
      long normal = System.currentTimeMillis() - start;
      System.out.printf("fast=%sms normal=%sms diff=%s%n", fast, normal, normal / fast);
   }
   
   public String fastParse(Class type) throws Exception {
      return new PackageParser().fastParse(type);
   }
   
   public String parse(Class type) throws Exception {
      return new PackageParser().parse(type);
   }
   
   public Class revert(String type) throws Exception {
      return new PackageParser().revert(type);
   }
   
   public static class PackageParser {
      
      private static final String scheme = "http://";
      
      public Class revert(String reference) throws Exception {
         URI uri = new URI(reference);
         String domain = uri.getHost();
         String path = uri.getPath();
         String[] list = domain.split("\\.");
         
         if(list.length > 1) {
            domain = list[1] + "." + list[0];
         } else {
            domain = list[0];
         }
         String type =  domain + path.replaceAll("\\/+", ".");
         return Class.forName(type);
      }
      
      public String fastParse(Class type) throws Exception {
         return new Convert(type.getName()).fastParse();
      }
      
      public static class Convert {
         private char[] array;
         private int count;
         private int mark; 
         private int size; 
         private int pos;
         
         public Convert(String type) {
            this.array = type.toCharArray();
         }
         
         public String fastParse() throws Exception {  
            char[] work = new char[array.length + 10];

            scheme(work);
            domain(work);
            path(work);
            
            return new String(work, 0, pos);
         }
         
         private void scheme(char[] work) {
            "http://".getChars(0, 7, work, 0);
            pos += 7;
         }
         
         private void path(char[] work) {
            for(int i = size; i < array.length; i++) {
               if(array[i] == '.') {
                  work[pos++] = '/';
               } else {
                  work[pos++] = array[i];
               }
            }
         }
         
         private void domain(char[] work) {
            while(size < array.length) { 
               if(array[size] == '.') { 
                  if(count++ == 1) {
                     break;
                  }
                  mark = size + 1;
               }
               size++;
            }
            for(int i = 0; i < size - mark; i++) {
               work[pos++] = array[mark + i];
            }
            work[pos++] = '.';
            work[size + 7] = '/';
            
            for(int i = 0; i < mark - 1; i++) {
               work[pos++] = array[i];
            }
         }
      }
   
      public String parse(Class type) {
         String name = type.getName();
         String[] list = name.split("\\.");
         
         return reference(list);
      }
      public String domain(String[] type) {
         StringBuilder builder = new StringBuilder();
   
         if(type.length < 2) {
            return type[0];
         }
         builder.append(type[1]);
         builder.append(".");
         builder.append(type[0]);
         return builder.toString();
      }
    
      public String path(String[] type) {
         StringBuilder builder = new StringBuilder();
   
         for(int i = 2; i < type.length; i++) {
            builder.append("/");
            builder.append(type[i]);
         }
         return builder.toString();
      }
      
      public String reference(String[] type) {
         return String.format("http://%s%s", domain(type), path(type));
      }
   }
}