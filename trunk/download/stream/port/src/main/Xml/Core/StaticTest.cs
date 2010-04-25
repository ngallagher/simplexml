#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class StaticTest : ValidationTestCase {
      private const String SOURCE =
      "<document title='Secret Document' xmlns='http://www.domain.com/document'>\n"+
      "   <author>Niall Gallagher</author>\n"+
      "   <contact>niallg@users.sourceforge.net</contact>\n"+
      "   <detail xmlns='http://www.domain.com/detail'>\n"+
      "      <publisher>Stanford Press</publisher>\n"+
      "      <date>2001</date>\n"+
      "      <address>Palo Alto</address>\n"+
      "      <edition>1st</edition>\n"+
      "      <ISBN>0-69-697269-4</ISBN>\n"+
      "   </detail>\n"+
      "   <section name='Introduction' xmlns='http://www.domain.com/section'>\n"+
      "      <paragraph xmlns='http://www.domain.com/paragraph'>First paragraph of document</paragraph>\n"+
      "      <paragraph xmlns='http://www.domain.com/paragraph'>Second paragraph in the document</paragraph>\n"+
      "      <paragraph xmlns='http://www.domain.com/paragraph'>Third and readonly paragraph</paragraph>\n"+
      "   </section>\n"+
      "</document>";
      [Root]
      [Namespace(Reference="http://www.domain.com/detail")]
      private static class Detail {
         [Element]
         private String publisher;
         [Element]
         private String date;
         [Element]
         private String address;
         [Element]
         private String edition;
         [Element]
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
      [Root]
      [Namespace(Reference = "http://www.domain.com/document")]
      public static class Document {
         [Element(Name="author")]
         [Namespace(Prefix="user", Reference="http://www.domain.com/user")]
         private static String AUTHOR = "Niall Gallagher";
         [Element(Name="contact")]
         private static String CONTACT = "niallg@users.sourceforge.net";
         [Element(Name="detail")]
         private static Detail DETAIL = new Detail(
            "Stanford Press",
            "2001",
            "Palo Alto",
            "1st",
            "0-69-697269-4");
         [ElementList(Inline = true)]
         private List<Section> list;
         [Attribute]
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
      [Root]
      [NamespaceList]
         [Namespace(Prefix="para", Reference="http://www.domain.com/paragraph")]
      })
      private static class Section {
         [Attribute]
         private String name;
         [ElementList(Inline = true)]
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
      [Root]
      [Namespace(Reference = "http://www.domain.com/paragraph")]
      private static class Paragraph {
         private String text;
         [Text]
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
      public void TestStatic() {
         Persister persister = new Persister();
         Document document = new Document("Secret Document");
         Section section = new Section("Introduction");
         Paragraph first = new Paragraph();
         Paragraph second = new Paragraph();
         Paragraph third = new Paragraph();
         first.setContent("First paragraph of document");
         second.setContent("Second paragraph in the document");
         third.setContent("Third and readonly paragraph");
         section.Add(first);
         section.Add(second);
         section.Add(third);
         document.Add(section);
         persister.write(document, System.out);
         validate(persister, document);
      }
   }
}
