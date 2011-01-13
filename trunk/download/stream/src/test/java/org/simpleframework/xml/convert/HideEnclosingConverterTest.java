package org.simpleframework.xml.convert;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class HideEnclosingConverterTest extends TestCase {

   public static class EntryConverter implements Converter<Entry> {
      private final Serializer serializer;
      public EntryConverter() {
         this.serializer = new Persister();
      }
      public Entry read(InputNode node) throws Exception {
         return serializer.read(Entry.class, node);
      }
      public void write(OutputNode node, Entry entry) throws Exception {
         if(!node.isCommitted()) {
            node.remove();
         }
         serializer.write(entry, node.getParent());
      }
   }
   
   @Default(required=false)
   public static class Entry {
      private final String name;
      private final String value;
      public Entry(@Element(name="name", required=false) String name, @Element(name="value", required=false) String value){
         this.name = name;
         this.value = value;
      }
      public String getName(){
         return name;
      }
      public String getValue(){
         return value;
      }
   }
   
   @Default
   public static class EntryHolder {
      @Convert(EntryConverter.class)
      private final Entry entry;
      private final String name;
      @Attribute
      private final int code;
      public EntryHolder(@Element(name="entry") Entry entry, @Element(name="name") String name, @Attribute(name="code") int code) {
         this.entry = entry;
         this.name = name;
         this.code = code;
      }
      public Entry getEntry(){
         return entry;
      }
      public String getName() {
         return name;
      }
      public int getCode() {
         return code;
      }
   }

   public void testWrapper() throws Exception{
      Strategy strategy = new AnnotationStrategy();
      Serializer serializer = new Persister(strategy);
      Entry entry = new Entry("name", "value");
      EntryHolder holder = new EntryHolder(entry, "test", 10);
      StringWriter writer = new StringWriter();
      serializer.write(holder, writer);
      System.out.println(writer.toString());
      serializer.read(EntryHolder.class, writer.toString());
      System.err.println(writer.toString());
   }
}
