package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
class ConstructorScanner {

   private Map<String, Parameter> parameters;
   private List<Builder> builders;
   private Class type;
   
   public ConstructorScanner(Class type) throws Exception {
      this.parameters = new HashMap<String, Parameter>();
      this.builders = new ArrayList<Builder>();
      this.type = type;
      this.scan(type);
   }
   
   public Parameter getParameter(String name) {
      return parameters.get(name);
   }
   
   public Builder getBuilder(Set<String> names) {
      PriorityQueue<Rank> queue = new PriorityQueue<Rank>();
      
      for(Builder builder : builders) {
         queue.add(new Rank(names, builder));
      }
      return  queue.remove().getBuilder();
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
            Parameter value = process(factory, labels[i][j], i);
            
            if(value != null) {
               String name = value.getName();
               
               parameters.put(name, value);
               list.add(value);
            }
         }
      }
      build(factory, list);
   }
   
   private void build(Constructor factory, List<Parameter> list) throws Exception {
      Builder builder = new Builder(factory, list);
      Set set = new HashSet();
      
      for(Parameter value : list) {
         String name = value.getName();
         
         if(set.contains(name)) {
            throw new PersistenceException("Parameter '%s' is a duplicate in %s", name, factory);
         }
         set.add(name);
      }
      builders.add(builder);
      
   }
   
   private Parameter process(Constructor factory, Annotation label, int index) throws Exception{
      if(label instanceof Attribute) {
         return create(factory, label, index);
      }
      if(label instanceof ElementList) {
         return create(factory, label, index);
      }     
      if(label instanceof ElementArray) {
         return create(factory, label, index);
      }
      if(label instanceof ElementMap) {
         return create(factory, label, index);
      }
      if(label instanceof Element) {
         return create(factory, label, index);
      }
      return null;
   }
   
   private Parameter create(Constructor factory, Annotation label, int index) throws Exception {
      Parameter value = ParameterFactory.getInstance(factory, label, index);
      String name = value.getName();
      
      if(parameters.containsKey(name)) {
         validate(value, name);
      }
      return value;
   }
   
   private void validate(Parameter parameter, String name) throws Exception {
      Parameter other = parameters.get(name);
      Annotation label = other.getAnnotation();
      
      if(!parameter.getAnnotation().equals(label)) {
         throw new MethodException("Annotations do not match for '%s' in %s", name, type);
      }
      Class expect = other.getType();
      
      if(expect != parameter.getType()) {
         throw new MethodException("Method types do not match for %s in %s", name, type);
      }
   }
   
   private class Rank implements Comparable<Rank> {
      
      private final Set<String> names;
      private final Builder builder;
      
      public Rank(Set<String> names, Builder builder) {
         this.builder = builder;
         this.names = names;
      }
      
      public int compareTo(Rank rank) {
         try {
            return rank.builder.score(names) - builder.score(names) ;
         } catch(Exception e) {
            throw new IllegalStateException(e);
         }
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