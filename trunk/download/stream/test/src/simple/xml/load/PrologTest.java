package simple.xml.load;

import java.io.StringReader;
import java.io.StringWriter;

import simple.xml.stream.Format;
import simple.xml.ValidationTestCase;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;

public class PrologTest extends ValidationTestCase {

   private static final String SOURCE =
   "<source id='12' flag='true'>\n"+
   "   <text>entry text</text>  \n\r"+
   "   <name>some name</name> \n"+
   "</source>";


   @Root(name="source")
   private static class PrologExample {

      @Attribute(name="id")           
      public int id;           

      @Element(name="name")
      public String name;
           
      @Element(name="text")
      public String text;  
      
      @Attribute(name="flag")
      public boolean bool;              
   }
        
	private Persister serializer;

	public void setUp() {
	   serializer = new Persister(new Format(4, "UTF-8"));
	}
	
   public void testProlog() throws Exception {    
      PrologExample example = serializer.read(PrologExample.class, SOURCE);
      
      assertEquals(example.id, 12);
      assertEquals(example.text, "entry text");
      assertEquals(example.name, "some name");
      assertTrue(example.bool);

      StringWriter buffer = new StringWriter();
      serializer.write(example, buffer);
      String text = buffer.toString();

      assertTrue(text.startsWith("<?xml"));
      validate(example, serializer);
   }
}
