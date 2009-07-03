package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Builder {

   private final Constructor factory;
   private final List<Parameter> list;

   public Builder(Constructor factory, List<Parameter> list) {
      this.factory = factory;
      this.list = list;
   } 
   
   public int score(Set<String> map)  {
      int score = 0;
      
      for(Parameter label : list) {
         String name = label.getName();
         
         if(!map.contains(name)) {
            return -1;
         }
         score++;
      }
      return score;
   }
   
   public Object build(Map<String, Pointer> store) throws Exception {
      List<Object> values = new ArrayList<Object>();
      
      for(Parameter parameter : list) {
         String name = parameter.getName();
         Pointer pointer = store.get(name);
         Object value = pointer.getValue();
         
         values.add(value);
         store.remove(name);
      }
      return build(values);
   }
   
   public Object build(List<Object> list) throws Exception {
      Object[] array = list.toArray();
      System.err.print("["+this.list.size()+"] ("+factory+") ARGS["+array.length+"]");
      for(Parameter param : this.list ) {
         System.out.print(">>>>>>"+param+"<<<<<<<");
      }
      System.out.println("<---");
      Object value = null;
      try {
         value = factory.newInstance(array);
      
      }catch(Exception e) {
         e.printStackTrace();
         System.err.println("Constructor: "+factory);
         throw e;
      }
      return value;
   }
   
   
   public String toString() {
      return factory.toString();
   }
}
