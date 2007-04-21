package simple.xml.graph;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import simple.xml.Attribute;
import simple.xml.ElementList;
import simple.xml.Root;
import simple.xml.Text;
import simple.xml.ValidationTestCase;
import simple.xml.load.Persister;

public class CycleTest extends ValidationTestCase {
   
   @Root(name="example")
   public static class CycleExample {
      
      @ElementList(name="list", type=Entry.class)
      private List<Entry> list;
      
      public CycleExample() {
         this.list = new ArrayList();
      }
      
      public void add(Entry entry) {
         list.add(entry);
      }
      
      public Entry get(int index) {
         return list.get(index);
      }
   }
   
   @Root(name="entry")
   public static class Entry {
      
      @Attribute(name="key")
      private String name;
      
      @Text
      private String value;
      
      protected Entry() {
         super();
      }
      
      public Entry(String name, String value) {
         this.name = name;
         this.value = value;
      }
   }
   
   private Persister persister;
   
   public void setUp() throws Exception {
      persister = new Persister(new CycleStrategy("class", "id", "ref"));
   }
   
   public void testCycle() throws Exception {
      CycleExample example = new CycleExample();
      Entry one = new Entry("1", "one");
      Entry two = new Entry("2", "two");
      Entry three = new Entry("3", "three");
      
      example.add(one);
      example.add(two);
      example.add(three);
      example.add(one);
      example.add(two);
      
      assertTrue(example.get(0) == example.get(3));
      assertTrue(example.get(1) == example.get(4));
      
      StringWriter out = new StringWriter();
      persister.write(example, out);
      
      example = persister.read(CycleExample.class, out.toString());
      
      assertTrue(example.get(0) == example.get(3));
      assertTrue(example.get(1) == example.get(4)); 
      
      validate(example, persister);
   }

}
