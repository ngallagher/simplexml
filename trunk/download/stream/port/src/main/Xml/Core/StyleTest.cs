#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class StyleTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<Example Version='1.0' Name='example' URL='http://domain.com/'>\n"+
      "   <List>\n"+
      "      <ListEntry id='1'>\n"+
      "         <Text>one</Text>  \n\r"+
      "      </ListEntry>\n\r"+
      "      <ListEntry id='2'>\n"+
      "         <Text>two</Text>  \n\r"+
      "      </ListEntry>\n"+
      "      <ListEntry id='3'>\n"+
      "         <Text>three</Text>  \n\r"+
      "      </ListEntry>\n"+
      "   </List>\n"+
      "   <TextEntry id='4'>\n" +
      "      <Text>example 4</Text>\n" +
      "   </TextEntry>\n" +
      "   <URLList>\n"+
      "     <URLEntry>http://a.com/</URLEntry>\n"+
      "     <URLEntry>http://b.com/</URLEntry>\n"+
      "     <URLEntry>http://c.com/</URLEntry>\n"+
      "   </URLList>\n"+
      "   <TextEntry id='5'>\n" +
      "      <Text>example 5</Text>\n" +
      "   </TextEntry>\n" +
      "   <TextEntry id='6'>\n" +
      "      <Text>example 6</Text>\n" +
      "   </TextEntry>\n" +
      "   <TextArray length='3'>\n"+
      "      <TextEntry id='6'>\n" +
      "         <Text>example 6</Text>\n" +
      "      </TextEntry>\n" +
      "      <TextEntry id='7'>\n" +
      "         <Text>example 7</Text>\n" +
      "      </TextEntry>\n" +
      "      <TextEntry id='8'>\n" +
      "         <Text>example 8</Text>\n" +
      "      </TextEntry>\n" +
      "   </TextArray>\n"+
      "   <TextEntry id='9'>\n" +
      "      <Text>example 9</Text>\n" +
      "   </TextEntry>\n" +
      "   <URLMap>\n"+
      "     <URLItem Key='a'>\n"+
      "        <URLEntry>http://a.com/</URLEntry>\n"+
      "     </URLItem>\n"+
      "     <URLItem Key='b'>\n"+
      "        <URLEntry>http://b.com/</URLEntry>\n"+
      "     </URLItem>\n"+
      "     <URLItem Key='c'>\n"+
      "        <URLEntry>http://c.com/</URLEntry>\n"+
      "     </URLItem>\n"+
      "   </URLMap>\n"+
      "</Example>";
      [Root(Name="Example")]
      private static class CaseExample {
         [ElementList(Name="List", Entry="ListEntry")]
         private List<TextEntry> list;
         [ElementList(Name="URLList")]
         private List<URLEntry> domainList;
         [ElementList(Name="TextList", Inline=true)]
         private List<TextEntry> textList;
         [ElementArray(Name="TextArray", Entry="TextEntry")]
         private TextEntry[] textArray;
         [ElementMap(Name="URLMap", Entry="URLItem", Key="Key", Value="URLItem", Attribute=true)]
         private Map<String, URLEntry> domainMap;
         [Attribute(Name="Version")]
         private float version;
         [Attribute(Name="Name")]
         private String name;
         @Attribute
         private String URL; // Java Bean property is URL
      }
      [Root(Name="TextEntry")]
      private static class TextEntry {
         [Attribute(Name="id")]
         private int id;
         [Element(Name="Text")]
         private String text;
      }
      [Root(Name="URLEntry")]
      private static class URLEntry {
         @Text
         private String location;
      }
      public void TestCase() {
         Style style = new HyphenStyle();
         Format format = new Format(style);
         Persister writer = new Persister(format);
         Persister reader = new Persister();
         CaseExample example = reader.read(CaseExample.class, SOURCE);
         assertEquals(example.version, 1.0f);
         assertEquals(example.name, "example");
         assertEquals(example.URL, "http://domain.com/");
         writer.write(example, System.err);
         validate(example, reader);
         validate(example, writer);
      }
   }
}
