#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class FieldScannerTest : TestCase {
      [Root(Name="name")]
      public static class Example {
         [ElementList(Name="list", Type=Entry.class)]
         private Collection<Entry> list;
         [Attribute(Name="version")]
         private int version;
         [Attribute(Name="name")]
         private String name;
      }
      [Root(Name="entry")]
      public static class Entry {
         [Attribute(Name="text")]
         public String text;
      }
      public void TestExample() {
         FieldScanner scanner = new FieldScanner(Example.class);
         ArrayList<Class> list = new ArrayList<Class>();
         for(Contact contact : scanner) {
            list.add(contact.getType());
         }
         AssertEquals(scanner.size(), 3);
         assertTrue(list.contains(Collection.class));
         assertTrue(list.contains(String.class));
         assertTrue(list.contains(int.class));
      }
   }
}
