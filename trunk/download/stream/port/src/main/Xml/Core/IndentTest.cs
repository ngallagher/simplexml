#region Using directives
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class IndentTest : ValidationTestCase {
      private const String EXAMPLE =
      "<?xml version=\"1.0\"?>\n"+
      "<contact id='some id'>\n"+
      "   <details>  \n\r"+
      "     <title>Some Title</title> \n"+
      "     <mail>email@domain.com</mail> \n"+
      "     <name>Name Surname</name> \n"+
      "   </details>\n"+
      "</contact>";
      [Root(Name="contact")]
      private static class Contact {
         [Attribute(Name="id")]
         private String id;
         [Element(Name="details")]
         private Details details;
      }
      [Root(Name="details")]
      private static class Details {
         [Element(Name="title")]
         private String title;
         [Element(Name="mail")]
         private String mail;
         [Element(Name="name")]
         private String name;
      }
      public void TestIndent() {
         Persister serializer = new Persister(new Format(5));
         Contact contact = serializer.read(Contact.class, EXAMPLE);
         assertEquals(contact.id, "some id");
         assertEquals(contact.details.title, "Some Title");
         assertEquals(contact.details.mail, "email@domain.com");
         assertEquals(contact.details.name, "Name Surname");
         StringWriter buffer = new StringWriter();
         serializer.write(contact, buffer);
         String text = buffer.toString();
         assertTrue(text.indexOf("     ") > 0); // indents
         assertTrue(text.indexOf('\n') > 0); // line feed
         validate(contact, serializer);
      }
      public void TestNoIndent() {
         Persister serializer = new Persister(new Format(0));
         Contact contact = serializer.read(Contact.class, EXAMPLE);
         assertEquals(contact.id, "some id");
         assertEquals(contact.details.title, "Some Title");
         assertEquals(contact.details.mail, "email@domain.com");
         assertEquals(contact.details.name, "Name Surname");
         StringWriter buffer = new StringWriter();
         serializer.write(contact, buffer);
         String text = buffer.toString();
         assertTrue(text.indexOf("  ") < 0); // no indents
         assertTrue(text.indexOf('\n') < 0); // no line feed
         assertTrue(text.indexOf('\r') < 0); // no carrige return
         validate(contact, serializer);
      }
   }
}
