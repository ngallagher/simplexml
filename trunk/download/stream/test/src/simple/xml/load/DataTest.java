package simple.xml.load;

import java.io.StringReader;

import simple.xml.util.Entry;
import simple.xml.util.Dictionary;
import simple.xml.ValidationTestCase;
import simple.xml.Attribute;
import simple.xml.ElementList;
import simple.xml.Element;
import simple.xml.Root;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class DataTest extends ValidationTestCase {

   private static final String SOURCE =
   "<scrape section='one' address='http://localhost:9090/'>\n"+
   "   <query-list>\n"+
   "      <query name='title' type='text'>\n"+
   "         <data>\n"+
   "            <![CDATA[ \n"+
   "               <title> \n"+
   "               { \n"+
   "                  for $text in .//TITLE\n"+
   "                  return $text\n"+
   "               }\n"+
   "               </title>\n"+
   "            ]]>\n"+
   "         </data>\n"+
   "      </query>\n"+   
   "      <query name='news' type='image'>\n"+
   "         <data>\n"+
   "            <![CDATA[   \n"+
   "               <news>\n"+
   "               { \n"+
   "                  for $text in .//B\n"+
   "                  return $text\n"+
   "               }\n"+
   "               </news>\n"+
   "            ]]>\n"+
   "         </data>\n"+
   "      </query>  \n"+   
   "   </query-list> \n"+   
   "</scrape>";

   @Root(name="scrape")
   private static class Scrape {

      @Attribute(name="section")           
      private String section;           
           
      @Attribute(name="address")
      private String address;           

      @ElementList(name="query-list", type=Query.class)
      private Dictionary<Query> list;              
   }

   @Root(name="query")
   private static class Query extends Entry {

      @Attribute(name="type")
      private String type;

      @Element(name="data")
      private String data;
   }
   
	private Persister serializer;

	public void setUp() {
	   serializer = new Persister();
	}
	
   public void testData() throws Exception {    
      Scrape scrape = serializer.read(Scrape.class, SOURCE);
      
      assertEquals(scrape.section, "one");
      assertEquals(scrape.address, "http://localhost:9090/");
      assertEquals(scrape.list.get("title").type, "text");
      assertTrue(scrape.list.get("title").data.indexOf("<title>") > 0);
      assertEquals(scrape.list.get("news").type, "image");
      assertTrue(scrape.list.get("news").data.indexOf("<news>") > 0);

      validate(scrape, serializer);
   }
}
