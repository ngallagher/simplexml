#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Filter;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class FilterTest : TestCase {
      private const String ENTRY =
      "<?xml version=\"1.0\"?>\n"+
      "<root number='1234' flag='true'>\n"+
      "   <name>${example.name}</name>  \n\r"+
      "   <path>${example.path}</path>\n"+
      "   <constant>${no.override}</constant>\n"+
      "   <text>\n"+
      "     Some example text where ${example.name} is replaced"+
      "     with the system property value and the path is "+
      "     replaced with the path ${example.path}"+
      "   </text>\n"+
      "</root>";
      @Root(name="root")
      private static class Entry {
         @Attribute(name="number")
         private int number;
         @Attribute(name="flag")
         private bool bool;
         @Element(name="constant")
         private String constant;
         @Element(name="name")
         private String name;
         @Element(name="path")
         private String path;
         @Element(name="text")
         private String text;
      }
      static {
         System.setProperty("example.name", "some name");
         System.setProperty("example.path", "/some/path");
         System.setProperty("no.override", "some constant");
      }
      private Persister systemSerializer;
      private Persister mapSerializer;
      public void SetUp() {
         Dictionary map = new HashMap();
         map.put("example.name", "override name");
         map.put("example.path", "/some/override/path");
         systemSerializer = new Persister();
         mapSerializer = new Persister(map);
      }
      public void TestSystem() {
         Entry entry = systemSerializer.read(Entry.class, new StringReader(ENTRY));
         assertEquals(entry.number, 1234);
         assertEquals(entry.bool, true);
         assertEquals(entry.name, "some name");
         assertEquals(entry.path, "/some/path");
         assertEquals(entry.constant, "some constant");
         assertTrue(entry.text.indexOf(entry.name) > 0);
         assertTrue(entry.text.indexOf(entry.path) > 0);
      }
      public void TestMap() {
         Entry entry = mapSerializer.read(Entry.class, new StringReader(ENTRY));
         assertEquals(entry.number, 1234);
         assertEquals(entry.bool, true);
         assertEquals(entry.name, "override name");
         assertEquals(entry.path, "/some/override/path");
         assertEquals(entry.constant, "some constant");
         assertTrue(entry.text.indexOf(entry.name) > 0);
         assertTrue(entry.text.indexOf(entry.path) > 0);
      }
      public void TestEnvironmentFilter() {
         Filter filter = new EnvironmentFilter(null);
         Persister persister = new Persister(filter);
         Entry entry = persister.read(Entry.class, new StringReader(ENTRY));
         assertEquals(entry.number, 1234);
         assertEquals(entry.bool, true);
         assertEquals(entry.name, "${example.name}");
         assertEquals(entry.path, "${example.path}");
         Filter systemFilter = new SystemFilter();
         Filter environmentFilter = new EnvironmentFilter(systemFilter);
         Persister environmentPersister = new Persister(environmentFilter);
         Entry secondEntry = environmentPersister.read(Entry.class, new StringReader(ENTRY));
         assertEquals(secondEntry.number, 1234);
         assertEquals(secondEntry.bool, true);
         assertEquals(secondEntry.name, "some name");
         assertEquals(secondEntry.path, "/some/path");
         assertEquals(secondEntry.constant, "some constant");
      }
   }
}
