#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class OverrideTest : TestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<root name='example'>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      private const String ENTRY =
      "<?xml version=\"1.0\"?>\n"+
      "<entry id='12'>\n"+
      "   <text>entry text</text>  \n\r"+
      "</entry>";
      private const String INTERFACE =
      "<entry id='12' class='SimpleFramework.Xml.Core.OverrideTest$Entry'>\n"+
      "   <text>entry text</text>  \n\r"+
      "</entry>";
      private static interface EntryInterface {
         public abstract int Id {
            get;
         }
         //public int GetId();
      }
      [Root(Name="entry")]
      private static class Entry : EntryInterface {
         [Attribute(Name="id")]
         private int id;
         [Element(Name="text")]
         private String text;
         public int Id {
            get {
               return id;
            }
         }
         //public int GetId() {
         //   return id;
         //}
            return text;
         }
      }
      [Root(Name="root")]
      private static class EntryList {
         [ElementList(Name="list", Type=Entry.class)]
         private List list;
         [Attribute(Name="name")]
         private String name;
         public Entry GetEntry(int index) {
            return (Entry) list.get(index);
         }
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestComposite() {
         Entry entry = serializer.read(Entry.class, new StringReader(ENTRY));
         assertEquals(entry.id, 12);
         assertEquals(entry.text, "entry text");
      }
      public void TestInterface() {
         EntryInterface entry = serializer.read(EntryInterface.class, new StringReader(INTERFACE));
         assertEquals(entry.Id, 12);
         assertEquals(entry.Text, "entry text");
      }
      public void TestList() {
         EntryList list = serializer.read(EntryList.class, new StringReader(LIST));
         assertEquals(list.name, "example");
         assertTrue(list.list instanceof Vector);
         Entry entry = list.GetEntry(0);
         assertEquals(entry.id, 12);
         assertEquals(entry.text, "some example text");
         entry = list.GetEntry(1);
         assertEquals(entry.id, 34);
         assertEquals(entry.text, "other example");
         entry = list.GetEntry(2);
         assertEquals(entry.id, 56);
         assertEquals(entry.text, "readonly example");
      }
      public void TestCopy() {
         EntryList list = serializer.read(EntryList.class, new StringReader(LIST));
         assertEquals(list.name, "example");
         assertTrue(list.list instanceof Vector);
         Entry entry = new Entry();
         entry.id = 1234;
         entry.text = "replacement";
         list.list = new ArrayList();
         list.name = "change";
         list.list.add(entry);
         StringWriter writer = new StringWriter();
         serializer.write(list, writer);
         serializer.write(list, System.out);
         assertTrue(writer.toString().indexOf("java.util.ArrayList") > 0);
         assertTrue(writer.toString().indexOf("change") > 0);
         list = serializer.read(EntryList.class, new StringReader(writer.toString()));
         assertEquals(list.name, "change");
         assertTrue(list.list instanceof ArrayList);
         entry = list.GetEntry(0);
         assertEquals(entry.id, 1234);
         assertEquals(entry.text, "replacement");
      }
   }
}
