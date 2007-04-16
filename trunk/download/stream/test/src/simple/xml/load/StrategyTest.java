package simple.xml.load;

import junit.framework.TestCase;
import simple.xml.stream.Node;
import simple.xml.stream.NodeMap;
import simple.xml.Serializer;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Map;

public class StrategyTest extends TestCase {

   private static final String ELEMENT_NAME = "example-attribute";        

   private static final String ELEMENT =
   "<?xml version=\"1.0\"?>\n"+
   "<root key='attribute-example-key' example-attribute='simple.xml.load.StrategyTest$ExampleExample'>\n"+
   "   <text>attribute-example-text</text>  \n\r"+
   "</root>";

   @Root(name="root")
   private static abstract class Example {

      public abstract String getValue();   
      
      public abstract String getKey();
   }
   
   private static class ExampleExample extends Example {

      @Attribute(name="key")           
      public String key;           
           
      @Element(name="text")
      public String text;           

      public String getValue() {
         return text;              
      }
      
      public String getKey() {
         return key;
      }
   }

   public class ExampleStrategy implements Strategy {

      public int writeRootCount = 0;           

      public int readRootCount = 0;

      private StrategyTest test;

      public ExampleStrategy(StrategyTest test){
         this.test = test;              
      }

      public Type getRoot(Class type, NodeMap root, Map map) throws Exception {
         readRootCount++;
         return getElement(type, root, map);              
      }

      public Type getElement(Class field, NodeMap node, Map map) throws Exception {
         Node value = node.remove(ELEMENT_NAME);

         if(readRootCount != 1) {
            test.assertTrue("Root must only be read once", false);                 
         }         
         if(value == null) {
        	 return null;
         }
         String name = value.getValue();
         Class type = Class.forName(name);
         
         return new SimpleType(type);
      }         

      public void setRoot(Class field, Object value, NodeMap root, Map map) throws Exception {                       
         writeRootCount++;              
         setElement(field, value, root, map);              
      }              

      public void setElement(Class field, Object value, NodeMap node, Map map) throws Exception {
         if(writeRootCount != 1) {
            test.assertTrue("Root must be written only once", false);                 
         }                 
         if(field != value.getClass()) {                       
            node.put(ELEMENT_NAME, value.getClass().getName());
         }            
      }
   }
   
   public static class SimpleType implements Type{
	   
	   private Class type;
	   
	   public SimpleType(Class type) {
		   this.type = type;
	   }
	   
	   public Object getInstance() throws Exception {
		   return getInstance(type);
	   }

       private Object getInstance(Class type) throws Exception {
		   Constructor method = type.getDeclaredConstructor();

		   if(!method.isAccessible()) {
		      method.setAccessible(true);              
		   }
		   return method.newInstance();   
	   }   
       
       public Object getArray(int size) throws Exception {
          return Array.newInstance(type, size);
       }

	   public Class getType() {
		  return type;
	   } 
   }

   public void testExampleStrategy() throws Exception {    
      ExampleStrategy strategy = new ExampleStrategy(this);           
      Serializer persister = new Persister(strategy);
      Example example = persister.read(Example.class, ELEMENT);
      
      assertTrue(example instanceof ExampleExample);
      assertEquals(example.getValue(), "attribute-example-text");
      assertEquals(example.getKey(), "attribute-example-key");
      assertEquals(1, strategy.readRootCount);
      assertEquals(0, strategy.writeRootCount);
      
      persister.write(example, System.err);
      
      assertEquals(1, strategy.readRootCount);
      assertEquals(1, strategy.writeRootCount);
   }
}
