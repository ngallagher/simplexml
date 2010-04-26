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
      [Root(Name="root")]
      private static class Entry {
         [Attribute(Name="number")]
         private int number;
         [Attribute(Name="flag")]
         private bool bool;
         [Element(Name="constant")]
         private String constant;
         [Element(Name="name")]
         private String name;
         [Element(Name="path")]
         private String path;
         [Element(Name="text")]
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
         Entry entry = systemSerializer.Read(Entry.class, new StringReader(ENTRY));
         AssertEquals(entry.number, 1234);
         AssertEquals(entry.bool, true);
         AssertEquals(entry.name, "some name");
         AssertEquals(entry.path, "/some/path");
         AssertEquals(entry.constant, "some constant");
         assertTrue(entry.text.indexOf(entry.name) > 0);
         assertTrue(entry.text.indexOf(entry.path) > 0);
      }
      public void TestMap() {
         Entry entry = mapSerializer.Read(Entry.class, new StringReader(ENTRY));
         AssertEquals(entry.number, 1234);
         AssertEquals(entry.bool, true);
         AssertEquals(entry.name, "override name");
         AssertEquals(entry.path, "/some/override/path");
         AssertEquals(entry.constant, "some constant");
         assertTrue(entry.text.indexOf(entry.name) > 0);
         assertTrue(entry.text.indexOf(entry.path) > 0);
      }
      public void TestEnvironmentFilter() {
         Filter filter = new EnvironmentFilter(null);
         Persister persister = new Persister(filter);
         Entry entry = persister.Read(Entry.class, new StringReader(ENTRY));
         AssertEquals(entry.number, 1234);
         AssertEquals(entry.bool, true);
         AssertEquals(entry.name, "${example.name}");
         AssertEquals(entry.path, "${example.path}");
         Filter systemFilter = new SystemFilter();
         Filter environmentFilter = new EnvironmentFilter(systemFilter);
         Persister environmentPersister = new Persister(environmentFilter);
         Entry secondEntry = environmentPersister.Read(Entry.class, new StringReader(ENTRY));
         AssertEquals(secondEntry.number, 1234);
         AssertEquals(secondEntry.bool, true);
         AssertEquals(secondEntry.name, "some name");
         AssertEquals(secondEntry.path, "/some/path");
         AssertEquals(secondEntry.constant, "some constant");
      }
   }
}
