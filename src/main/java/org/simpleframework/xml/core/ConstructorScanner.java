package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;


/**
 * Here we scan all constructors that have annotations for the
 * parameters. Each constructor scanned is converted in to a 
 * Builder object as follows.
 * 
 * 
 * public interface Builder {
 * 
 *    public Object build(List<Label> list, List<Object> value);
 * }
 * 
 * The builder looks through the list of labels finds the best 
 * constructor match based on the label names. And then validates
 * the constructor parameter types (WHICH SHOULD REALLY BE VALID
 * ANYWAY BECAUSE THE SCAN PHASE IS RESPONSIBLE FOR THIS). Then 
 * it injects in the values.
 * <p> 
 * Example constructors could be something like.
 * <pre>
 * 
 * @Root
 * public class Property {
 *    private final String name;
 *    private final String value;
 *    
 *    public Property(@Attribute("name") String name,
 *                   @Attribute("value") String value)
 *    {
 *       this.name = name;
 *       this.value = value;
 *    }
 *    
 *    @Resolve
 *    public Property getProperty() {
 *       return null;
 *    }
 * }
 * 
 * @author gallagna
 *
 */
public class ConstructorScanner {
   
   private static final String NAME = "name";
   
   private List<Builder> builders;
   private Class type;
   
   public ConstructorScanner(Class type) throws Exception {
      this.builders = new ArrayList<Builder>();
      this.type = type;
      this.scan(type);
   }
   
   public Builder getBuilder(Set<String> names) {
      PriorityQueue<Rank> queue = new PriorityQueue<Rank>();
      
      for(Builder builder : builders) {
         queue.add(new Rank(names, builder));
      }
      Builder builder = null;
      try {
      builder =  queue.remove().getBuilder();
      }catch(Exception e) {
         System.err.println(builders.size() +":"+type);
         e.printStackTrace();
      }
      return builder;
   }
   
   private void scan(Class type) throws Exception {
      Constructor[] array = type.getDeclaredConstructors();
      
      for(Constructor factory: array){
         List<Parameter> list = new ArrayList<Parameter>();
         
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         scan(factory, list);
      } 
   }
   
   private void scan(Constructor factory, List<Parameter> list) throws Exception {
      Annotation[][] labels = factory.getParameterAnnotations();
      Class[] types = factory.getParameterTypes();

      for(int i = 0; i < types.length; i++) {
         for(int j = 0; j < labels[i].length; j++) {
            Parameter value = process(types[i], labels[i][j]);
            
            if(value != null) {
               list.add(value);
            }
         }
      }
      build(factory, list);
   }
   
   private void build(Constructor factory, List<Parameter> list) throws Exception {
      Builder builder = new Builder(factory, list);
      
      builders.add(builder);
      
   }
   
   private Parameter process(Class type, Annotation label) throws Exception{
      if(label instanceof Attribute) {
         return create(type, label);
      }
      if(label instanceof ElementList) {
         return create(type, label);
      }     
      if(label instanceof ElementArray) {
         return create(type, label);
      }
      if(label instanceof ElementMap) {
         return create(type, label);
      }
      if(label instanceof Element) {
         return create(type, label);
      }
      return null;
   }
   
   private Parameter create(Class type, Annotation label) throws Exception {
      String name = getName(label);
      
      if(name.equals("")) {
         throw new PersistenceException("");
      }
      return new Parameter(type, label, name);

   }
   
   private String getName(Annotation label) throws Exception {
      Class type = label.getClass();
      Method method = type.getMethod(NAME);
      Object name =  method.invoke(label);
      
      return name.toString();
   }
   
   private class Rank implements Comparable<Rank> {
      
      private final Set<String> names;
      private final Builder builder;
      
      public Rank(Set<String> names, Builder builder) {
         this.builder = builder;
         this.names = names;
      }
      
      public int compareTo(Rank rank) {
         return rank.builder.score(names) - builder.score(names) ;
      }
      
      public Builder getBuilder() {
         return builder;
      }
   }

   /**
    * Here we validate to ensure the constructors have correctly
    * annotated the parameters with a matching method or field.
    * If there is no match here then there is an exception. 
    * <p>
    * In particular here we must ensure that IF THE METHOD IS A
    * READ ONLY METHOD THAT THERE IS AT LEAST ONE CONSTRUCTOR THAT
    * WILL ACCEPT THE VALUE.  
    * 
    * @param attributes
    * @param elements
    */
   public void validate(LabelMap attributes, LabelMap elements){
      
   }
}