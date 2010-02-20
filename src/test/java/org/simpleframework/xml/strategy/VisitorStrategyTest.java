package org.simpleframework.xml.strategy;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

public class VisitorStrategyTest extends ValidationTestCase {
   
   public static class Manipulator implements Visitor {
      public void read(Type field, NodeMap<InputNode> node) throws Exception{
          String namespace = node.getNode().getReference();
          if(namespace != null && namespace.length() > 0) {
              String type = new PackageParser().revert(namespace).getName();
              if(type == null) {
                  throw new PersistenceException("Could not match name %s", namespace);
              }
              node.put("class", type);
          }
      }
      public void write(Type field, NodeMap<OutputNode> node) throws Exception {
          OutputNode value = node.remove("class");
          if(value != null) {
              String type = value.getValue();
              String name = new PackageParser().parse(type);
              if(name == null) {
                  throw new PersistenceException("Could not match class %s", type);
              }
              node.getNode().setComment(type);
              node.getNode().getNamespaces().put(name, "class");
              node.getNode().setReference(name);
          }
      }
   }
   
   @Root
   @Default
   private static class OrderItem {
      private List<String> items;
      private Map<String, String> map;
      public OrderItem() {
         this.map = new HashMap<String, String>();
         this.items = new ArrayList<String>();
      }
      public void put(String name, String value) {
         map.put(name, value);
      }
      public void add(String value) {
         items.add(value);
      }
   }
   
   public void testStrategy() throws Exception {
      Visitor visitor = new Manipulator();
      Strategy strategy = new VisitorStrategy(visitor);
      Persister persister = new Persister(strategy);
      OrderItem item = new OrderItem();
      StringWriter writer = new StringWriter();
      
      item.put("1", "ONE");
      item.put("2", "TWO");
      item.add("A");
      item.add("B");
      
      persister.write(item, writer);
      
      String text = writer.toString();
      System.out.println(text);
      
      OrderItem recover = persister.read(OrderItem.class, text);
      
      assertTrue(recover.map.containsKey("1"));
      assertTrue(recover.map.containsKey("2"));
      assertTrue(recover.items.contains("A"));
      assertTrue(recover.items.contains("B"));  
      
      validate(recover, persister);
   }

}
