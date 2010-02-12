package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.Node;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.util.WeakCache;


public class InterceptorTest extends ValidationTestCase { 
   
   public static interface Interceptor {
      
      public Class read(NodeMap<InputNode> node) throws Exception;
      public void write(NodeMap<OutputNode> node) throws Exception;
   }
   
   public static class InterceptorFactory {
      public static Interceptor getInterceptor(Class<? extends Interceptor> type) throws Exception {
         return type.newInstance();
      }
   }
   
   public static class InterceptorStrategy implements Strategy {
      
      private WeakCache<Object, Interceptor> cache;
      private Class<? extends Interceptor> type;
      private Strategy strategy;
      
      public InterceptorStrategy(Strategy strategy, Class<? extends Interceptor> type) {
         this.cache = new WeakCache<Object, Interceptor>();
         this.strategy = strategy;
         this.type = type;
      }
      
      public Value getRoot(Type field, NodeMap node, Map map) throws Exception {
         Interceptor interceptor = InterceptorFactory.getInterceptor(type);
         cache.cache(map, interceptor);
         return getElement(field, node, map);
      }

      public Value getElement(Type field, NodeMap node, Map map) throws Exception { 
         Interceptor interceptor = cache.fetch(map);
         Class type = interceptor.read(node);
         
         if(type == null) { // no override
            type = field.getType();
         }
         return strategy.getElement(new ClassType(type), node, map);
      }

      public boolean setElement(Type field, Object value, NodeMap node, Map map) throws Exception {
         boolean result = strategy.setElement(field, value, node, map);
         Interceptor interceptor = cache.fetch(map);
         interceptor.write(node);
         return result;
      }

      public boolean setRoot(Type field, Object value, NodeMap node, Map map) throws Exception {
         Interceptor interceptor = InterceptorFactory.getInterceptor(type);
         cache.cache(map, interceptor);
         return setElement(field, value, node, map);
      }
   }
   
   public static class TypeInterceptor implements Interceptor {
      
      public Class read(NodeMap map) {
         return null;
      }
      
      public void write(NodeMap<OutputNode> map) throws Exception {
         Node node = map.get("class");
         
         if(node != null) {
            String name = node.getValue();
            
            if(ConcurrentHashMap.class.getName().equals(name)) {
               map.put("class", HashMap.class.getName());
            }
            if(Vector.class.getName().equals(name)) {
               map.put("class", ArrayList.class.getName());
            }
            if(CopyOnWriteArraySet.class.getName().equals(name)) {
               map.put("class", HashSet.class.getName());
            }
         }
      }
   }
   
   @Root
   private static class Example {
   
      @ElementMap
      private Map<String, Object> map;
      
      @ElementList
      private Set<String> set;
      
      public Example() {
         this.map = new ConcurrentHashMap<String, Object>();
         this.set = new CopyOnWriteArraySet<String>();
      }
      
      public Set<String> getSet() {
         return set;
      }
      
      public Map<String, Object> getMap() {
         return map;
      }
   }
 
   public void testInterceptor() throws Exception {
      Example example = new Example();
      Strategy tree = new TreeStrategy();
      Strategy main = new InterceptorStrategy(tree, TypeInterceptor.class);
      Persister persister = new Persister(main);
      
      example.getSet().add("a");
      example.getSet().add("b");
      example.getSet().add("c");
      example.getSet().add("d");
      example.getSet().add("e");

      example.getMap().put("1", 1);
      example.getMap().put("true", true);
      example.getMap().put("text", "text");
      example.getMap().put("1.0", 1.0);
      
      persister.write(example, System.out);
      validate(persister, example);
   }

}
