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
         Entry entry = serializer.Read(Entry.class, new StringReader(ENTRY));
         AssertEquals(entry.id, 12);
         AssertEquals(entry.text, "entry text");
      }
      public void TestInterface() {
         EntryInterface entry = serializer.Read(EntryInterface.class, new StringReader(INTERFACE));
         AssertEquals(entry.Id, 12);
         AssertEquals(entry.Text, "entry text");
      }
      public void TestList() {
         EntryList list = serializer.Read(EntryList.class, new StringReader(LIST));
         AssertEquals(list.name, "example");
         assertTrue(list.list instanceof Vector);
         Entry entry = list.GetEntry(0);
         AssertEquals(entry.id, 12);
         AssertEquals(entry.text, "some example text");
         entry = list.GetEntry(1);
         AssertEquals(entry.id, 34);
         AssertEquals(entry.text, "other example");
         entry = list.GetEntry(2);
         AssertEquals(entry.id, 56);
         AssertEquals(entry.text, "readonly example");
      }
      public void TestCopy() {
         EntryList list = serializer.Read(EntryList.class, new StringReader(LIST));
         AssertEquals(list.name, "example");
         assertTrue(list.list instanceof Vector);
         Entry entry = new Entry();
         entry.id = 1234;
         entry.text = "replacement";
         list.list = new ArrayList();
         list.name = "change";
         list.list.add(entry);
         StringWriter writer = new StringWriter();
         serializer.Write(list, writer);
         serializer.Write(list, System.out);
         assertTrue(writer.toString().indexOf("java.util.ArrayList") > 0);
         assertTrue(writer.toString().indexOf("change") > 0);
         list = serializer.Read(EntryList.class, new StringReader(writer.toString()));
         AssertEquals(list.name, "change");
         assertTrue(list.list instanceof ArrayList);
         entry = list.GetEntry(0);
         AssertEquals(entry.id, 1234);
         AssertEquals(entry.text, "replacement");
      }
   }
}
