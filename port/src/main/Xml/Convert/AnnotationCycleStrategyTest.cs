#region Using directives
using SimpleFramework.Xml.Convert;
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class AnnotationCycleStrategyTest : ValidationTestCase {
      [Root]
      public static class EntryListExample {
         [ElementList(Inline=true)]
         private List<Entry> list = new ArrayList<Entry>();
         [Element]
         [Convert(OtherEntryConverter.class)]
         private Entry primary;
         public Entry Primary {
            get {
               return primary;
            }
         }
         //public Entry GetPrimary() {
         //   return primary;
         //}
            this.primary = primary;
         }
         public void AddEntry(Entry entry) {
            list.add(entry);
         }
         public List<Entry> Entries {
            get {
               return list;
            }
         }
         //public List<Entry> GetEntries() {
         //   return list;
         //}
      public void TestCycle() {
         CycleStrategy inner = new CycleStrategy();
         AnnotationStrategy strategy = new AnnotationStrategy(inner);
         Persister persister = new Persister(strategy);
         EntryListExample list = new EntryListExample();
         StringWriter writer = new StringWriter();
         Entry a = new Entry("A", "a");
         Entry b = new Entry("B", "b");
         Entry c = new Entry("C", "c");
         Entry primary = new Entry("PRIMARY", "primary");
         list.Primary = primary;
         list.AddEntry(a);
         list.AddEntry(b);
         list.AddEntry(c);
         list.AddEntry(b);
         list.AddEntry(c);
         persister.write(list, writer);
         persister.write(list, System.out);
         String text = writer.toString();
         EntryListExample copy = persister.read(EntryListExample.class, text);
         AssertEquals(copy.Entries.get(0), list.Entries.get(0));
         AssertEquals(copy.Entries.get(1), list.Entries.get(1));
         AssertEquals(copy.Entries.get(2), list.Entries.get(2));
         AssertEquals(copy.Entries.get(3), list.Entries.get(3));
         AssertEquals(copy.Entries.get(4), list.Entries.get(4));
         assertTrue(copy.Entries.get(2) == copy.Entries.get(4)); // cycle
         assertTrue(copy.Entries.get(1) == copy.Entries.get(3)); // cycle
         assertElementExists(text, "/entryListExample");
         assertElementExists(text, "/entryListExample/entry[0]");
         assertElementExists(text, "/entryListExample/entry[0]/name");
         assertElementExists(text, "/entryListExample/entry[0]/value");
         assertElementHasValue(text, "/entryListExample/entry[0]/name", "A");
         assertElementHasValue(text, "/entryListExample/entry[0]/value", "a");
         assertElementExists(text, "/entryListExample/entry[1]/name");
         assertElementExists(text, "/entryListExample/entry[1]/value");
         assertElementHasValue(text, "/entryListExample/entry[1]/name", "B");
         assertElementHasValue(text, "/entryListExample/entry[1]/value", "b");
         assertElementExists(text, "/entryListExample/entry[2]/name");
         assertElementExists(text, "/entryListExample/entry[2]/value");
         assertElementHasValue(text, "/entryListExample/entry[2]/name", "C");
         assertElementHasValue(text, "/entryListExample/entry[2]/value", "c");
         assertElementExists(text, "/entryListExample/entry[3]");
         assertElementExists(text, "/entryListExample/entry[4]");
         assertElementHasAttribute(text, "/entryListExample/entry[3]", "reference", "2"); // cycle
         assertElementHasAttribute(text, "/entryListExample/entry[4]", "reference", "3"); // cycle
         assertElementExists(text, "/entryListExample/primary");
         assertElementHasAttribute(text, "/entryListExample/primary", "name", "PRIMARY"); // other converter
         assertElementHasAttribute(text, "/entryListExample/primary", "value", "primary"); // other converter
      }
   }
}
