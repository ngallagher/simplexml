package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Builder {

   private final Constructor factory;
   private final ParameterMap map;

   public Builder(Constructor factory, ParameterMap map) {
      this.factory = factory;
      this.map = map;
   } 
   
   public Object getDefault() throws Exception {
      return factory.newInstance();
   }
   
   public boolean isDefault() {
      return map.size() == 0;
   }
   
   public Parameter getParameter(String name) {
      return map.get(name);
   }
   
   public int score(Criteria criteria) throws Exception {
      int score = 0;
      
      for(String name : map.keySet()) {
         Label label = criteria.get(name);
         
         if(label == null) {
            return -1;
         }
         score++;
      }
      return score;
   }
   
   public Object build(Criteria criteria) throws Exception {
      List<Object> values = new ArrayList<Object>();
      
      for(Parameter parameter : map) {
         String name = parameter.getName();
         Pointer pointer = criteria.get(name);
         Object value = pointer.getValue();
         
         values.add(value);
         criteria.remove(name);
      }
      return build(values);
   }
   
   public Object build(List<Object> list) throws Exception {
      Object[] array = list.toArray();
         return factory.newInstance(array);
   }
   
   
   public Map<String, Parameter> getParameters() {
      return map;
   }
   
   
   public String toString() {
      return factory.toString();
   }
}
