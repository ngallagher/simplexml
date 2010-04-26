#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CaseTest : ValidationTestCase {
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
      "</Example>";
      [Root(Name="Example")]
      private static class CaseExample {
         [ElementList(Name="List", Entry="ListEntry")]
         private List<TextEntry> list;
         [ElementList(Name="URLList")]
         private List<URLEntry> domainList;
         [ElementList(Name="TextList", Inline=true)]
         private List<TextEntry> textList;
         [Attribute(Name="Version")]
         private float version;
         [Attribute(Name="Name")]
         private String name;
         [Attribute]
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
         [Text]
         private String location;
      }
      public void TestCase() {
         Persister persister = new Persister();
         CaseExample example = persister.read(CaseExample.class, SOURCE);
         AssertEquals(example.version, 1.0f);
         AssertEquals(example.name, "example");
         AssertEquals(example.URL, "http://domain.com/");
         validate(example, persister);
      }
   }
}
