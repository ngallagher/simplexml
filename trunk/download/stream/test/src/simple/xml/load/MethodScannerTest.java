package simple.xml.load;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementList;
import simple.xml.Root;
import simple.xml.Text;

public class MethodScannerTest extends TestCase {
   
   @Root(name="name")
   public static class Example {

      private int version;
      
      private String name;
      
      @Element(name="version")
      public void setVersion(int version) {
         this.version = version;  
      }
      
      @Element(name="version")
      public int getVersion() {
         return version;
      }
      
      @Attribute(name="name")
      public void setName(String name) {
         this.name = name;
      }
      
      @Attribute(name="name")
      public String getName() {
         return name;
      }
   }
   
   public static class IllegalOverload extends Example {
      
      private int name;
      
      @Attribute(name="name")
      public void setName(int name) {
         this.name = name;
      }
   }
   
   public static class NonMatchingMethods extends Example {
      
      private int type;
      
      @Attribute(name="type")
      public void setType(int type) {
         this.type = type;
      }
   }
   
   public static class NotBeanMethod extends Example {
      
      private String type;
      
      @Element(name="type")
      public void setType(String type) {
         this.type = type;
      }
      
      @Element(name="type")
      public String readType() {
         return type;   
      }
   }
   
   public static class TextMethod extends Example {
      
      private long length;      
      
      @Text
      public void setLength(long length) {
         this.length = length;
      }
      
      @Text
      public long getLength() {
         return length;
      }
   }
   
   public static class CollectionMethod extends TextMethod {
      
      private Collection list;
      
      @ElementList(name="list", type=Example.class)
      public void setList(Collection list) {
         this.list = list;
      }
      
      @ElementList(name="list", type=Example.class)
      public Collection getList() {
         return list;
      }
      
   }
   
   public void testExample() throws Exception {
      MethodScanner scanner = new MethodScanner(Example.class);
      ArrayList<Class> list = new ArrayList<Class>();
      
      scanner.run();
      
      for(Contact contact : scanner.getContacts()) {
         list.add(contact.getType());
      }
      assertEquals(scanner.getContacts().size(), 2);
      assertTrue(list.contains(String.class));
      assertTrue(list.contains(int.class));
   }
   
   public void testIllegalOverload() throws Exception {      
      boolean success = false;
      
      try {
         new MethodScanner(IllegalOverload.class).run();
      }catch(Exception e){
         success = true;
      }
      assertTrue(success);
   }
   
   public void testNonMatchingMethods() throws Exception {      
      boolean success = false;
      
      try {
         new MethodScanner(NonMatchingMethods.class).run();
      }catch(Exception e){
         success = true;
      }
      assertTrue(success);
   }
   
   public void testNotBeanMethod() throws Exception {      
      boolean success = false;
      
      try {
         new MethodScanner(NotBeanMethod.class).run();
      }catch(Exception e){
         success = true;
      }
      assertTrue(success);
   }
   
   
   public void testText() throws Exception {
      MethodScanner scanner = new MethodScanner(TextMethod.class);
      ArrayList<Class> list = new ArrayList<Class>();
      
      scanner.run();
      
      for(Contact contact : scanner.getContacts()) {
         list.add(contact.getType());
      }
      assertEquals(scanner.getContacts().size(), 3);
      assertTrue(list.contains(String.class));
      assertTrue(list.contains(int.class));
      assertTrue(list.contains(long.class));
   }
   
   public void testCollection() throws Exception {
      MethodScanner scanner = new MethodScanner(CollectionMethod.class);
      ArrayList<Class> list = new ArrayList<Class>();
      
      scanner.run();
      
      for(Contact contact : scanner.getContacts()) {
         list.add(contact.getType());
      }
      assertEquals(scanner.getContacts().size(), 4);
      assertTrue(list.contains(String.class));
      assertTrue(list.contains(int.class));
      assertTrue(list.contains(long.class));
      assertTrue(list.contains(Collection.class));
   }
}
