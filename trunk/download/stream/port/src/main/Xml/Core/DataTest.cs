#region Using directives
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class DataTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version='1.0' encoding='UTF-8'?>\n"+
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
      [Root(Name="scrape")]
      private static class Scrape {
         [Attribute(Name="section")]
         private String section;
         [Attribute(Name="address")]
         private String address;
         [ElementList(Name="query-list", Type=Query.class)]
         private Dictionary<Query> list;
      }
      [Root(Name="query")]
      private static class Query : Entry {
         [Attribute(Name="type")]
         private String type;
         [Element(Name="data", Data=true)]
         private String data;
         [Attribute]
         private String name;
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestData() {
         Scrape scrape = serializer.Read(Scrape.class, SOURCE);
         AssertEquals(scrape.section, "one");
         AssertEquals(scrape.address, "http://localhost:9090/");
         AssertEquals(scrape.list.get("title").type, "text");
         assertTrue(scrape.list.get("title").data.indexOf("<title>") > 0);
         AssertEquals(scrape.list.get("news").type, "image");
         assertTrue(scrape.list.get("news").data.indexOf("<news>") > 0);
         Validate(scrape, serializer);
         String news = scrape.list.get("news").data;
         String title = scrape.list.get("title").data;
         StringWriter out = new StringWriter();
         serializer.Write(scrape, out);
         String text = out.toString();
         Scrape copy = serializer.Read(Scrape.class, text);
         AssertEquals(news, copy.list.get("news").data);
         AssertEquals(title, copy.list.get("title").data);
      }
      public void TestDataFromByteStream() {
         byte[] data = SOURCE.getBytes("UTF-8");
         InputStream source = new ByteArrayInputStream(data);
         Scrape scrape = serializer.Read(Scrape.class, source);
         AssertEquals(scrape.section, "one");
         AssertEquals(scrape.address, "http://localhost:9090/");
         AssertEquals(scrape.list.get("title").type, "text");
         assertTrue(scrape.list.get("title").data.indexOf("<title>") > 0);
         AssertEquals(scrape.list.get("news").type, "image");
         assertTrue(scrape.list.get("news").data.indexOf("<news>") > 0);
         Validate(scrape, serializer);
         String news = scrape.list.get("news").data;
         String title = scrape.list.get("title").data;
         StringWriter out = new StringWriter();
         serializer.Write(scrape, out);
         String text = out.toString();
         Scrape copy = serializer.Read(Scrape.class, text);
         AssertEquals(news, copy.list.get("news").data);
         AssertEquals(title, copy.list.get("title").data);
      }
   }
}
