#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ContextualCallbackTest : TestCase {
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
         public Entry(){}
         [Validate]
         public void Validate(Dictionary map) {
            validated = true;
         }
         [Commit]
         public void Commit(Dictionary map) {
            if(validated) {
               committed = true;
            }
         }
         [Persist]
         public void Persist(Dictionary map) {
            persisted = true;
         }
         [Complete]
         public void Complete(Dictionary map) {
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
   }
}
