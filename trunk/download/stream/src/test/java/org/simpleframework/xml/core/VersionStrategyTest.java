package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.util.Map;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.Node;
import org.simpleframework.xml.stream.NodeMap;

public class VersionStrategyTest extends TestCase {

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

      public Value getRoot(Type field, NodeMap root, Map map) throws Exception {
         Node version = root.get(VERSION_ATTRIBUTE);                       

         if(version != null){
            map.put(VERSION_ATTRIBUTE, version.getValue());
         }
         return getElement(field, root, map);
      }

      public Value getElement(Type field, NodeMap node, Map map) throws Exception {
         String value = field.getType().getName() + map.get(VERSION_ATTRIBUTE);
     
         try {    
            Class type = Class.forName(value);
            
            return new SimpleType(type);
         } catch(ClassNotFoundException e) {
            return null;                 
         }            
      }         

      public boolean setRoot(Type field, Object value, NodeMap root, Map map) throws Exception {
         Class type = value.getClass();
         
         if(Versionable.class.isAssignableFrom(type)) {
            Versionable versionable = (Versionable)value;
            map.put(VERSION_ATTRIBUTE, String.valueOf(versionable.getVersion()));
         }              
         return setElement(field, value, root, map);
      }              

      public boolean setElement(Type field, Object value, NodeMap node, Map map) throws Exception {
         node.put(VERSION_ATTRIBUTE, (String)map.get(VERSION_ATTRIBUTE));
         return false;
      }
   }
   
   public static class SimpleType implements Value{
	   
	   private Class type;
	   
	   public SimpleType(Class type) {
		   this.type = type;
	   }
	   
	   public int getLength() {
	      return 0;
	   }
	   
	   public Object getValue(){
	      try {
   		   Constructor method = type.getDeclaredConstructor();
   
   		   if(!method.isAccessible()) {
   		      method.setAccessible(true);              
   		   }
   		   return method.newInstance();   
	      }catch(Exception e) {
	         throw new RuntimeException(e);
	      }
	   }   
       
       public void setValue(Object value){
       }
       
       public boolean isReference() {
          return false;
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
