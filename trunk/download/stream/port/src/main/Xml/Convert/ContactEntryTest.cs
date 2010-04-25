#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class ContactEntryTest : ValidationTestCase {
      [Root]
      public static class EntryList {
         [ElementList(Inline=true)]
         private List<Entry> list;
         [Element]
         [Convert(OtherEntryConverter.class)]
         private Entry other;
         [Element]
         private Entry inheritConverter;
         [Element]
         private Entry polymorhic;
         [Element]
         [Convert(EntryListConverter.class)]
         private List<Entry> otherList;
         public EntryList() {
            this("Default", "Value");
         }
         public EntryList(String name, String value){
            this.list = new ArrayList<Entry>();
            this.otherList = new ArrayList<Entry>();
            this.other = new Entry(name, value);
            this.inheritConverter = new Entry("INHERIT", "inherit");
            this.polymorhic = new ExtendedEntry("POLY", "poly", 12);
         }
         public Entry Inherit {
            get {
               return inheritConverter;
            }
         }
         //public Entry GetInherit() {
         //   return inheritConverter;
         //}
            return other;
         }
         public List<Entry> List {
            get {
               return list;
            }
         }
         //public List<Entry> GetList() {
         //   return list;
         //}
            return otherList;
         }
      }
      public void TestContact() {
         Strategy strategy = new AnnotationStrategy();
         Serializer serializer = new Persister(strategy);
         EntryList list = new EntryList("Other", "Value");
         StringWriter writer = new StringWriter();
         list.List.add(new Entry("a", "A"));
         list.List.add(new Entry("b", "B"));
         list.List.add(new Entry("c", "C"));
         list.OtherList.add(new Entry("1", "ONE"));
         list.OtherList.add(new Entry("2", "TWO"));
         list.OtherList.add(new Entry("3", "THREE"));
         serializer.write(list, writer);
         String text = writer.toString();
         EntryList copy = serializer.read(EntryList.class, text);
         assertEquals(copy.List.get(0).getName(), list.List.get(0).getName());
         assertEquals(copy.List.get(0).getValue(), list.List.get(0).getValue());
         assertEquals(copy.List.get(1).getName(), list.List.get(1).getName());
         assertEquals(copy.List.get(1).getValue(), list.List.get(1).getValue());
         assertEquals(copy.List.get(2).getName(), list.List.get(2).getName());
         assertEquals(copy.List.get(2).getValue(), list.List.get(2).getValue());
         assertEquals(copy.OtherList.get(0).getName(), list.OtherList.get(0).getName());
         assertEquals(copy.OtherList.get(0).getValue(), list.OtherList.get(0).getValue());
         assertEquals(copy.OtherList.get(1).getName(), list.OtherList.get(1).getName());
         assertEquals(copy.OtherList.get(1).getValue(), list.OtherList.get(1).getValue());
         assertEquals(copy.OtherList.get(2).getName(), list.OtherList.get(2).getName());
         assertEquals(copy.OtherList.get(2).getValue(), list.OtherList.get(2).getValue());
         System.out.println(text);
      }
   }
}
