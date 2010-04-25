#region Using directives
using SimpleFramework.Xml.Filter;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class PerformanceTest : ValidationTestCase {
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
      "   <list class='java.util.ArrayList'>\n"+
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
      "   <list class='java.util.ArrayList'>\n"+
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
      @Root(name="root")
      public static class RootEntry : Serializable {
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
         @Element(name="child")
         private ChildEntry entry;
         @ElementList(name="list", type=ElementEntry.class)
         private Collection list;
      }
      @Root(name="child")
      public static class ChildEntry : Serializable {
         @Attribute(name="name")
         private String name;
         @Element(name="one")
         private String one;
         @Element(name="two")
         private String two;
         @Element(name="three")
         private String three;
         @Element(name="grand-child")
         private GrandChildEntry grandChild;
      }
      @Root(name="grand-child")
      public static class GrandChildEntry : Serializable {
         @Element(name="entry-one")
         private ElementEntry entryOne;
         @Element(name="entry-two")
         private ElementEntry entryTwo;
      }
      @Root(name="entry")
      public static class ElementEntry : Serializable {
         @Attribute(name="key")
         private String name;
         @Element(name="value")
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
      public void SetUp() {
         systemSerializer = new Persister();
      }
      public void TestCompareToOtherSerializers() {
         Serializer simpleSerializer = new Persister(new VisitorStrategy(new Visitor(){
            public void Read(Type type, NodeMap<InputNode> node) {
               if(node.getNode().isRoot()) {
                  System.err.println(node.getNode().getSource().getClass());
               }
            }
            public void Write(Type type, NodeMap<OutputNode> node) {
         }));
         RootEntry entry = simpleSerializer.Read(RootEntry.class, BASIC_ENTRY);
         ByteArrayOutputStream simpleBuffer = new ByteArrayOutputStream();
         ByteArrayOutputStream javaBuffer = new ByteArrayOutputStream();
         ByteArrayOutputStream xstreamBuffer = new ByteArrayOutputStream();
         ObjectOutputStream javaSerializer = new ObjectOutputStream(javaBuffer);
         XStream xstreamSerializer = new XStream();
         simpleSerializer.Write(entry, simpleBuffer);
         xstreamSerializer.toXML(entry, xstreamBuffer);
         javaSerializer.writeObject(entry);
         byte[] simpleByteArray = simpleBuffer.toByteArray();
         byte[] xstreamByteArray = xstreamBuffer.toByteArray();
         byte[] javaByteArray = javaBuffer.toByteArray();
         System.err.println("SIMPLE TOOK "+TimeToSerializeWithSimple(RootEntry.class, simpleByteArray, ITERATIONS)+"ms");
         System.err.println("JAVA TOOK "+TimeToSerializeWithJava(RootEntry.class, javaByteArray, ITERATIONS)+"ms");
         System.err.println("XSTREAM TOOK "+TimeToSerializeWithXStream(RootEntry.class, xstreamByteArray, ITERATIONS)+"ms");
         System.err.println("XSTREAM --->>"+xstreamBuffer.toString());
         System.err.println("SIMPLE --->>"+simpleBuffer.toString());
      }
      public long TimeToSerializeWithSimple(Class type, byte[] buffer, int count) {
         Persister persister = new Persister();
         persister.Read(RootEntry.class, new ByteArrayInputStream(buffer));
         long now = System.currentTimeMillis();
         for(int i = 0; i < count; i++) {
            persister.Read(RootEntry.class, new ByteArrayInputStream(buffer));
         }
         return System.currentTimeMillis() - now;
      }
      public long TimeToSerializeWithJava(Class type, byte[] buffer, int count) {
         ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(buffer));
         stream.readObject();
         long now = System.currentTimeMillis();
         for(int i = 0; i < count; i++) {
            new ObjectInputStream(new ByteArrayInputStream(buffer)).readObject();
         }
         return System.currentTimeMillis() - now;
      }
      public long TimeToSerializeWithXStream(Class type, byte[] buffer, int count) {
         XStream stream = new XStream();
         stream.fromXML(new ByteArrayInputStream(buffer));
         long now = System.currentTimeMillis();
         for(int i = 0; i < count; i++) {
            stream.fromXML(new ByteArrayInputStream(buffer));
         }
         return System.currentTimeMillis() - now;
      }
      public void TestBasicDocument() {
         RootEntry entry = (RootEntry)systemSerializer.Read(RootEntry.class, BASIC_ENTRY);
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.Read(RootEntry.class, BASIC_ENTRY);
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to process %s documents\n", duration, ITERATIONS);
         systemSerializer.Write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.Write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.Read(RootEntry.class, out.toString());
         systemSerializer.Write(entry, System.out);
      }
      public void TestTemplateDocument() {
         RootEntry entry = (RootEntry)systemSerializer.Read(RootEntry.class, TEMPLATE_ENTRY);
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.Read(RootEntry.class, TEMPLATE_ENTRY);
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to process %s documents with templates\n", duration, ITERATIONS);
         systemSerializer.Write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.Write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.Read(RootEntry.class, out.toString());
         systemSerializer.Write(entry, System.out);
      }
      public void TestEmptyFilter() {
         systemSerializer = new Persister(new EmptyFilter());
         RootEntry entry = (RootEntry)systemSerializer.Read(RootEntry.class, TEMPLATE_ENTRY);
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.Read(RootEntry.class, TEMPLATE_ENTRY);
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to process %s documents with an empty filter\n", duration, ITERATIONS);
         systemSerializer.Write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.Write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.Read(RootEntry.class, out.toString());
         systemSerializer.Write(entry, System.out);
      }
      public void TestBasicWrite() {
         RootEntry entry = (RootEntry)systemSerializer.Read(RootEntry.class, BASIC_ENTRY);
         long start = System.currentTimeMillis();
         entry.constant = ">><<"; // this should be escaped
         entry.text = "this is text>> some more<<"; // this should be escaped
         for(int i = 0; i < ITERATIONS; i++) {
            systemSerializer.Write(entry, new StringWriter());
         }
         long duration = System.currentTimeMillis() - start;
         System.err.printf("Took '%s' ms to write %s documents\n", duration, ITERATIONS);
         systemSerializer.Write(entry, System.out);
         StringWriter out = new StringWriter();
         systemSerializer.Write(entry, out);
         validate(entry, systemSerializer);
         entry = (RootEntry)systemSerializer.Read(RootEntry.class, out.toString());
         systemSerializer.Write(entry, System.out);
      }
   }
}
