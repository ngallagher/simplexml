#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class FieldScannerTest : TestCase {
      @Root(name="name")
      public static class Example {
         @ElementList(name="list", type=Entry.class)
         private Collection<Entry> list;
         @Attribute(name="version")
         private int version;
         @Attribute(name="name")
         private String name;
      }
      @Root(name="entry")
      public static class Entry {
         @Attribute(name="text")
         public String text;
      }
      public void TestExample() {
         FieldScanner scanner = new FieldScanner(Example.class);
         ArrayList<Class> list = new ArrayList<Class>();
         for(Contact contact : scanner) {
            list.add(contact.getType());
         }
         assertEquals(scanner.size(), 3);
         assertTrue(list.contains(Collection.class));
         assertTrue(list.contains(String.class));
         assertTrue(list.contains(int.class));
      }
   }
}
