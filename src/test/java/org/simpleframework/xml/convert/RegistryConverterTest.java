package org.simpleframework.xml.convert;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class RegistryConverterTest extends ValidationTestCase {

   public static class EnvelopeConverter implements Converter<Envelope> {
      private final Serializer serializer;
      public EnvelopeConverter(Serializer serializer) {
         this.serializer = serializer;
      }
      public Envelope read(InputNode node) throws Exception {
         return serializer.read(Envelope.class, node);
      }
      public void write(OutputNode node, Envelope value) throws Exception {
         serializer.write(value.getValue(), node);
      }
   }
   
   @Root
   private static class OrderItem {
      @Element
      private Envelope envelope;
      public OrderItem() {
         super();
      }
      public OrderItem(String name, String address) {
         this.envelope = new Envelope(new Customer(name, address));
      }
   }
   
   @Root
   @Default
   private static class Customer {
      private String name;
      private String address;
      public Customer() {
         super();
      }
      public Customer(String name, String address) {
         this.name = name;
         this.address = address;
      }
   }
  
   private static class Envelope {
      private final Object value;
      public Envelope(Object value) {
         this.value = value;  
      }
      public Object getValue() {
         return value;
      }
   }
   
   public void testConverter() throws Exception {
      Registry registry = new Registry();
      RegistryStrategy strategy = new RegistryStrategy(registry);
      Serializer serializer = new Persister(strategy);
      Converter converter = new EnvelopeConverter(serializer);
      
      registry.bind(Envelope.class, converter);
      
      OrderItem order = new OrderItem("Niall", "Gallagher");
      serializer.write(order, System.out);
   }
}
