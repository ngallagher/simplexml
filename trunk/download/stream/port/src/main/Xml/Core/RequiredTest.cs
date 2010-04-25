#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class RequiredTest : TestCase {
      private const String COMPLETE =
      "<?xml version=\"1.0\"?>\n"+
      "<root number='1234' flag='true'>\n"+
      "   <value>complete</value>  \n\r"+
      "</root>";
      private const String OPTIONAL =
      "<?xml version=\"1.0\"?>\n"+
      "<root flag='true'/>";
      [Root(Name="root")]
      private static class Entry {
         [Attribute(Name="number", Required=false)]
         private int number = 9999;
         [Attribute(Name="flag")]
         private bool bool;
         [Element(Name="value", Required=false)]
         private String value = "default";
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
      public void TestComplete() {
         Entry entry = persister.read(Entry.class, new StringReader(COMPLETE));
         assertEquals("complete", entry.Value);
         assertEquals(1234, entry.Number);
         assertEquals(true, entry.Flag);
      }
      public void TestOptional() {
         Entry entry = persister.read(Entry.class, new StringReader(OPTIONAL));
         assertEquals("default", entry.Value);
         assertEquals(9999, entry.Number);
         assertEquals(true, entry.Flag);
      }
   }
}
