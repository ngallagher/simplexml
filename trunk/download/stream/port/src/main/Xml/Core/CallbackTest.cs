#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CallbackTest : TestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<root number='1234' flag='true'>\n"+
      "   <value>complete</value>  \n\r"+
      "</root>";
      [Root(Name="root")]
      private static class Entry {
         [Attribute(Name="number", Required=false)]
         private int number = 9999;
         [Attribute(Name="flag")]
         private bool bool;
         [Element(Name="value", Required=false)]
         private String value = "default";
         private bool validated;
         private bool committed;
         private bool persisted;
         private bool completed;
         public Entry() {
            super();
         }
         [Validate]
         public void Validate() {
            validated = true;
         }
         [Commit]
         public void Commit() {
            if(validated) {
               committed = true;
            }
         }
         [Persist]
         public void Persist() {
            persisted = true;
         }
         [Complete]
         public void Complete() {
            if(persisted) {
               completed = true;
            }
         }
         public bool IsCommitted() {
            return committed;
         }
         public bool IsValidated() {
            return validated;
         }
         public bool IsPersisted() {
            return persisted;
         }
         public bool IsCompleted() {
            return completed;
         }
         public int Number {
            get {
               return number;
            }
         }
         //public int GetNumber() {
         //   return number;
         //}
            return bool;
         }
         public String Value {
            get {
               return value;
            }
         }
         //public String GetValue() {
         //   return value;
         //}
      private static class ExtendedEntry : Entry {
         public bool completed;
         public bool committed;
         public bool validated;
         public bool persisted;
         public ExtendedEntry() {
            super();
         }
         [Validate]
         public void ExtendedValidate() {
            validated = true;
         }
         [Commit]
         public void ExtendedCommit() {
            if(validated) {
               committed = true;
            }
         }
         [Persist]
         public void ExtendedPersist() {
            persisted = true;
         }
         [Complete]
         public void ExtendedComplete() {
            if(persisted) {
               completed = true;
            }
         }
         public bool IsExtendedCommitted() {
            return committed;
         }
         public bool IsExtendedValidated() {
            return validated;
         }
         public bool IsExtendedPersisted() {
            return persisted;
         }
         public bool IsExtendedCompleted() {
            return completed;
         }
      }
      private static class OverrideEntry : Entry {
         public bool validated;
         [Override]
         public void Validate() {
            validated = true;
         }
         public bool IsOverrideValidated() {
            return validated;
         }
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestReadCallbacks() {
         Entry entry = persister.Read(Entry.class, SOURCE);
         AssertEquals("complete", entry.Value);
         AssertEquals(1234, entry.Number);
         AssertEquals(true, entry.Flag);
         assertTrue(entry.IsValidated());
         assertTrue(entry.IsCommitted());
      }
      public void TestWriteCallbacks() {
         Entry entry = new Entry();
         assertFalse(entry.IsCompleted());
         assertFalse(entry.IsPersisted());
         persister.Write(entry, System.out);
         AssertEquals("default", entry.Value);
         AssertEquals(9999, entry.Number);
         assertTrue(entry.IsPersisted());
         assertTrue(entry.IsCompleted());
      }
      public void TestExtendedReadCallbacks() {
         ExtendedEntry entry = persister.Read(ExtendedEntry.class, SOURCE);
         AssertEquals("complete", entry.Value);
         AssertEquals(1234, entry.Number);
         AssertEquals(true, entry.Flag);
         assertFalse(entry.IsValidated());
         assertFalse(entry.IsCommitted());
         assertTrue(entry.IsExtendedValidated());
         assertTrue(entry.IsExtendedCommitted());
      }
      public void TestExtendedWriteCallbacks() {
         ExtendedEntry entry = new ExtendedEntry();
         assertFalse(entry.IsCompleted());
         assertFalse(entry.IsPersisted());
         assertFalse(entry.IsExtendedCompleted());
         assertFalse(entry.IsExtendedPersisted());
         persister.Write(entry, System.out);
         AssertEquals("default", entry.Value);
         AssertEquals(9999, entry.Number);
         assertFalse(entry.IsPersisted());
         assertFalse(entry.IsCompleted());
         assertTrue(entry.IsExtendedCompleted());
         assertTrue(entry.IsExtendedPersisted());
      }
      public void TestOverrideReadCallbacks() {
         OverrideEntry entry = persister.Read(OverrideEntry.class, SOURCE);
         AssertEquals("complete", entry.Value);
         AssertEquals(1234, entry.Number);
         AssertEquals(true, entry.Flag);
         assertFalse(entry.IsValidated());
         assertTrue(entry.IsOverrideValidated());
      }
   }
}
