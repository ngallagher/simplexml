#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class TutorialTest : TestCase {
      private const String SOURCE =
      "<document title='Secret Document' xmlns='http://www.domain.com/document'>\n"+
      "   <user:author xmlns:user='http://www.domain.com/user'>Niall Gallagher</user:author>\n"+
      "   <contact>niallg@users.sourceforge.net</contact>\n"+
      "   <detail xmlns='http://www.domain.com/detail'>\n"+
      "      <publisher>Stanford Press</publisher>\n"+
      "      <date>2001</date>\n"+
      "      <address>Palo Alto</address>\n"+
      "      <edition>1st</edition>\n"+
      "      <ISBN>0-69-697269-4</ISBN>\n"+
      "   </detail>\n"+
      "   <section name='Introduction' xmlns:para='http://www.domain.com/paragraph'>\n"+
      "      <para:paragraph>First paragraph of document</para:paragraph>\n"+
      "      <para:paragraph>Second paragraph in the document</para:paragraph>\n"+
      "      <para:paragraph>Third and readonly paragraph</para:paragraph>\n"+
      "   </section>\n"+
      "</document>\n";
      @Root
      @Namespace(reference = "http://www.domain.com/document")
      public static class Document {
         @Element(name="author")
         @Namespace(prefix="user", reference="http://www.domain.com/user")
         private const String AUTHOR = "Niall Gallagher";
         @Element(name="contact")
         private const String CONTACT = "niallg@users.sourceforge.net";
         @Element(name="detail")
         private const Detail DETAIL = new Detail(
            "Stanford Press",
            "2001",
            "Palo Alto",
            "1st",
            "0-69-697269-4");
         @ElementList(inline=true)
         private List<Section> list;
         @Attribute
         private String title;
         private Document() {
            super();
         }
         public Document(String title) {
            this.list = new ArrayList<Section>();
            this.title = title;
         }
         public void Add(Section section) {
            list.Add(section);
         }
      }
      @Root
      @Namespace(reference="http://www.domain.com/detail")
      public static class Detail {
         @Element
         private String publisher;
         @Element
         private String date;
         @Element
         private String address;
         @Element
         private String edition;
         @Element
         private String ISBN;
         private Detail() {
            super();
         }
         public Detail(String publisher, String date, String address, String edition, String ISBN) {
            this.publisher = publisher;
            this.address = address;
            this.edition = edition;
            this.date = date;
            this.ISBN = ISBN;
         }
      }
      @Root
      @NamespaceList({
      @Namespace(prefix="para", reference="http://www.domain.com/paragraph")})
      public static class Section {
         @Attribute
         private String name;
         @ElementList(inline = true)
         private List<Paragraph> list;
         private Section() {
            super();
         }
         public Section(String name) {
            this.list = new ArrayList<Paragraph>();
            this.name = name;
         }
         public void Add(Paragraph paragraph) {
            list.Add(paragraph);
         }
      }
      @Root
      @Namespace(reference = "http://www.domain.com/paragraph")
      public static class Paragraph {
         private String text;
         @Text
         public String Content {
            get {
               return text;
            }
            set {
               this.text = value;
            }
         }
         //public String GetContent() {
         //   return text;
         //}
         //public void SetContent(String text) {
         //   this.text = text;
         //}
      public void TestTutorial() {
         Persister persister = new Persister();
         Document document = persister.read(Document.class, SOURCE);
         persister.write(document,System.out);
      }
   }
}
