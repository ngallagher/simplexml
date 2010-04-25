#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class TemplateTest : ValidationTestCase {
      private const String EXAMPLE =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='test'>\n"+
      "   <config>\n"+
      "     <var name='name' value='Niall Gallagher'/>\n"+
      "     <var name='mail' value='niallg@users.sf.net'/>\n"+
      "     <var name='title' value='Mr'/>\n"+
      "   </config>\n"+
      "   <details>  \n\r"+
      "     <title>${title}</title> \n"+
      "     <mail>${mail}</mail> \n"+
      "     <name>${name}</name> \n"+
      "   </details>\n"+
      "</test>";
      [Root(Name="var")]
      private static class Variable {
         [Attribute(Name="name")]
         private String name;
         [Attribute(Name="value")]
         private String value;
         [Commit]
         public void Commit(Dictionary map) {
            map.put(name, value);
         }
      }
      [Root(Name="test")]
      private static class Example {
         [Attribute(Name="name")]
         private String name;
         [ElementList(Name="config", Type=Variable.class)]
         private List list;
         [Element(Name="details")]
         private Details details;
      }
      private static class Details {
         [Element(Name="title")]
         private String title;
         [Element(Name="mail")]
         private String mail;
         [Element(Name="name")]
         private String name;
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestTemplate() {
         Example example = serializer.read(Example.class, EXAMPLE);
         assertEquals(example.name, "test");
         assertEquals(example.details.title, "Mr");
         assertEquals(example.details.mail, "niallg@users.sf.net");
         assertEquals(example.details.name, "Niall Gallagher");
         validate(example, serializer);
      }
   }
}
