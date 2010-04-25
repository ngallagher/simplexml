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
      @Root(name="var")
      private static class Variable {
         @Attribute(name="name")
         private String name;
         @Attribute(name="value")
         private String value;
         @Commit
         public void Commit(Dictionary map) {
            map.put(name, value);
         }
      }
      @Root(name="test")
      private static class Example {
         @Attribute(name="name")
         private String name;
         @ElementList(name="config", type=Variable.class)
         private List list;
         @Element(name="details")
         private Details details;
      }
      private static class Details {
         @Element(name="title")
         private String title;
         @Element(name="mail")
         private String mail;
         @Element(name="name")
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
