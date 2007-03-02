package simple.xml.load;

import junit.framework.TestCase;
import simple.xml.stream.Node;
import simple.xml.stream.NodeMap;
import simple.xml.Serializer;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;

import java.lang.reflect.Constructor;
import java.util.Map;

public class VersionTest extends TestCase {

   private static final String VERSION_ATTRIBUTE = "version";        

   private static final String VERSION_1 =
   "<?xml version=\"1.0\"?>\n"+
   "<Example version='1'>\n"+
   "   <text>text value</text>  \n\r"+
   "</Example>";

   private static final String VERSION_2 =
   "<?xml version=\"1.0\"?>\n"+
   "<Example version='2'>\n"+
   "   <name>example name</name>  \n\r"+
   "   <value>text value</value> \n"+
   "   <entry name='example'>\n"+   
   "      <value>text value</value> \n"+   
   "   </entry>\n"+
   "</Example>";
   
   
   public interface Versionable {
      
      public int getVersion();
   }

   @Root(name="Example")
   private static abstract class Example implements Versionable {

      @Attribute(name="version")
      private int version;
      
      public int getVersion() {
         return version;
      }
      
      public abstract String getValue();   
   }
   
   private static class Example1 extends Example {

      @Element(name="text")
      private String text;           

      public String getValue() {
         return text;              
      }
   }

   private static class Example2 extends Example {          

      @Element(name="name")
      private String name;

      @Element(name="value")
      private String value;

      @Element(name="entry")
      private Entry entry;

      public String getValue() {
         return value;              
      }
   }

   private static class Entry {

      @Attribute(name="name")
      private String name;           

      @Element(name="value")
      private String value;              
   }

   public class VersionStrategy implements Strategy {

      private String version;           

      public Type getRoot(Class field, NodeMap root, Map map) throws Exception {
         Node version = root.get(VERSION_ATTRIBUTE);                       

         if(version != null){
            map.put(VERSION_ATTRIBUTE, version.getValue());
         }
         return getElement(field, root, map);
      }

      public Type getElement(Class field, NodeMap node, Map map) throws Exception {
         String value = field.getName() + map.get(VERSION_ATTRIBUTE);
     
         try {    
            Class type = Class.forName(value);
            
            return new SimpleType(type);
         } catch(ClassNotFoundException e) {
            return null;                 
         }            
      }         

      public void setRoot(Class field, Object value, NodeMap root, Map map) throws Exception {
         Class type = value.getClass();
         
         if(Versionable.class.isAssignableFrom(type)) {
            Versionable versionable = (Versionable)value;
            map.put(VERSION_ATTRIBUTE, String.valueOf(versionable.getVersion()));
         }              
         setElement(field, value, root, map);
      }              

      public void setElement(Class field, Object value, NodeMap node, Map map) throws Exception {
         node.put(VERSION_ATTRIBUTE, (String)map.get(VERSION_ATTRIBUTE));
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

	   public Class getType() {
		  return type;
	   } 
   }
   
   public void testVersion1() throws Exception {    
      Strategy strategy = new VersionStrategy();           
      Serializer persister = new Persister(strategy);
      Example example = persister.read(Example.class, VERSION_1);
      
      assertTrue(example instanceof Example1);
      assertEquals(example.getValue(), "text value");
   }

   public void testVersion2() throws Exception {    
      Strategy strategy = new VersionStrategy();           
      Serializer persister = new Persister(strategy);
      Example example = persister.read(Example.class, VERSION_2);      
      
      assertTrue(example instanceof Example2);
      assertEquals(example.getValue(), "text value");
   }
}
