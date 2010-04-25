#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Filter;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class TreeStrategyTest : ValidationTestCase {
      public const int ITERATIONS = 10000;
      public const int MAXIMUM = 1000;
      public const String BASIC_ENTRY =
      "<?xml version=\"1.0\"?>\n"+
      "<root number='1234' flag='true'>\n"+
      "   <name>{example.name}</name>  \n\r"+
      "   <path>{example.path}</path>\n"+
      "   <constant>{no.override}</constant>\n"+
      "   <text>\n"+
      "        Some example text where {example.name} is replaced\n"+
      "        with the system property value and the path is\n"+
      "        replaced with the path {example.path}\n"+
      "   </text>\n"+
      "   <child name='first'>\n"+
      "      <one>this is the first element</one>\n"+
      "      <two>the second element</two>\n"+
      "      <three>the third elment</three>\n"+
      "      <grand-child>\n"+
      "         <entry-one key='name.1'>\n"+
      "            <value>value.1</value>\n"+
      "         </entry-one>\n"+
      "         <entry-two key='name.2'>\n"+
      "            <value>value.2</value>\n"+
      "         </entry-two>\n"+
      "      </grand-child>\n"+
      "   </child>\n"+
      "   <list TYPE='java.util.ArrayList'>\n"+
      "     <entry key='name.1'>\n"+
      "        <value>value.1</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.2'>\n"+
      "        <value>value.2</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.3'>\n"+
      "        <value>value.4</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.4'>\n"+
      "        <value>value.4</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.5'>\n"+
      "        <value>value.5</value>\n"+
      "     </entry>\n"+
      "  </list>\n"+
      "</root>";
      public const String TEMPLATE_ENTRY =
      "<?xml version=\"1.0\"?>\n"+
      "<root number='1234' flag='true'>\n"+
      "   <name>${example.name}</name>  \n\r"+
      "   <path>${example.path}</path>\n"+
      "   <constant>${no.override}</constant>\n"+
      "   <text>\n"+
      "        Some example text where ${example.name} is replaced\n"+
      "        with the system property value and the path is \n"+
      "        replaced with the path ${example.path}\n"+
      "   </text>\n"+
      "   <child name='first'>\n"+
      "      <one>this is the first element</one>\n"+
      "      <two>the second element</two>\n"+
      "      <three>the third elment</three>\n"+
      "      <grand-child>\n"+
      "         <entry-one key='name.1'>\n"+
      "            <value>value.1</value>\n"+
      "         </entry-one>\n"+
      "         <entry-two key='name.2'>\n"+
      "            <value>value.2</value>\n"+
      "         </entry-two>\n"+
      "      </grand-child>\n"+
      "   </child>\n"+
      "   <list TYPE='java.util.ArrayList'>\n"+
      "     <entry key='name.1'>\n"+
      "        <value>value.1</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.2'>\n"+
      "        <value>value.2</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.3'>\n"+
      "        <value>value.4</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.4'>\n"+
      "        <value>value.4</value>\n"+
      "     </entry>\n"+
      "     <entry key='name.5'>\n"+
      "        <value>value.5</value>\n"+
      "     </entry>\n"+
      "  </list>\n"+
      "</root>";
      [Root(Name="root")]
      public static class RootEntry {
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
         [Element(Name="child")]
         private ChildEntry entry;
         [ElementList(Name="list", Type=ElementEntry.class)]
         private Collection list;
      }
      [Root(Name="child")]
      public static class ChildEntry {
         [Attribute(Name="name")]
         private String name;
         [Element(Name="one")]
         private String one;
         [Element(Name="two")]
         private String two;
         [Element(Name="three")]
         private String three;
         [Element(Name="grand-child")]
         private GrandChildEntry grandChild;
      }
      [Root(Name="grand-child")]
      public static class GrandChildEntry {
         [Element(Name="entry-one")]
         private ElementEntry entryOne;
         [Element(Name="entry-two")]
         private ElementEntry entryTwo;
      }
      [Root(Name="entry")]
      public static class ElementEntry {
         [Attribute(Name="key")]
         private String name;
         [Element(Name="value")]
         private String value;
      }
      private static class EmptyFilter : Filter {
         public String Replace(String name) {
            return null;
         }
      }
      static {
         System.setProperty("example.name", "some name");
         System.setProperty("example.path", "/some/path");
         System.setProperty("no.override", "some constant");
      }
      private Persister systemSerializer;
      private Strategy strategy;
      public void SetUp() {
         strategy = new TreeStrategy("TYPE", "LENGTH");
         systemSerializer = new Persister(strategy);
      }
      public void TestBasicDocument() {
         RootEntry entry = (RootEntry)systemSerializer.read(RootEntry.class, BASIC_ENTRY);
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.read(RootEntry.class, BASIC_ENTRY);
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to process %s documents\n", duration, ITERATIONS);
         systemSerializer.write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.read(RootEntry.class, out.toString());
         systemSerializer.write(entry, System.out);
      }
      public void TestTemplateDocument() {
         RootEntry entry = (RootEntry)systemSerializer.read(RootEntry.class, TEMPLATE_ENTRY);
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.read(RootEntry.class, TEMPLATE_ENTRY);
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to process %s documents with templates\n", duration, ITERATIONS);
         systemSerializer.write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.read(RootEntry.class, out.toString());
         systemSerializer.write(entry, System.out);
      }
      public void TestEmptyFilter() {
         systemSerializer = new Persister(strategy, new EmptyFilter());
         RootEntry entry = (RootEntry)systemSerializer.read(RootEntry.class, TEMPLATE_ENTRY);
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.read(RootEntry.class, TEMPLATE_ENTRY);
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to process %s documents with an empty filter\n", duration, ITERATIONS);
         systemSerializer.write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.read(RootEntry.class, out.toString());
         systemSerializer.write(entry, System.out);
      }
      public void TestBasicWrite() {
         RootEntry entry = (RootEntry)systemSerializer.read(RootEntry.class, BASIC_ENTRY);
         long start = System.currentTimeMillis();
         entry.constant = ">><<"; // this should be escaped
         entry.text = "this is text>> some more<<"; // this should be escaped
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.write(entry, new StringWriter());
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to write %s documents\n", duration, ITERATIONS);
         systemSerializer.write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.read(RootEntry.class, out.toString());
         systemSerializer.write(entry, System.out);
      }
   }
}
