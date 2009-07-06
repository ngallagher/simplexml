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

   private ParameterMap all;
   private List<Builder> done;
   private Builder primary;
   private Class type;
   
   public ConstructorScanner(Class type) throws Exception {
      this.done = new ArrayList<Builder>();
      this.all = new ParameterMap();
      this.type = type;
      this.scan(type);
   }
   
   public Creator getCreator() {
      return new ClassCreator(done, all, primary);
   }
   
   private void scan(Class type) throws Exception {
      Constructor[] array = type.getDeclaredConstructors();
      
      for(Constructor factory: array){
         ParameterMap map = new ParameterMap();
         
         if(!factory.isAccessible()) {
            factory.setAccessible(true);
         }
         scan(factory, map);
      } 
   }
   
   private void scan(Constructor factory, ParameterMap map) throws Exception {
      Annotation[][] labels = factory.getParameterAnnotations();
      Class[] types = factory.getParameterTypes();

      for(int i = 0; i < types.length; i++) {         
         for(int j = 0; j < labels[i].length; j++) {
            Parameter value = process(factory, labels[i][j], i);
            
            if(value != null) {
               String name = value.getName();
               
               if(map.containsKey(name)) {
                  throw new PersistenceException("Parameter '%s' is a duplicate in %s", name, factory);
               }
               all.put(name, value);
               map.put(name, value);
            }
         }
      }
      if(types.length == map.size()) {
         build(factory, map);
      }
   }
   
   private void build(Constructor factory, ParameterMap map) throws Exception {
      Builder builder = new Builder(factory, map);
      
      if(builder.isDefault()) {
         primary = builder;
      }
      done.add(builder);   
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
      
      if(all.containsKey(name)) {
         validate(value, name);
      }
      return value;
   }
   
   private void validate(Parameter parameter, String name) throws Exception {
      Parameter other = all.get(name);
      Annotation label = other.getAnnotation();
      
      if(!parameter.getAnnotation().equals(label)) {
         throw new MethodException("Annotations do not match for '%s' in %s", name, type);
      }
      Class expect = other.getType();
      
      if(expect != parameter.getType()) {
         throw new MethodException("Method types do not match for '%s' in %s", name, type);
      }
   }
}