package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class ParameterContact implements Contact {

   private final Constructor factory;
   private final Annotation label;
   private final String name;
   private final int index;
   
   public ParameterContact(Constructor factory, Annotation label, String name, int index) {
      this.factory = factory;
      this.index = index;
      this.label = label;
      this.name = name;
   }

   public Annotation getAnnotation() {
      return label;
   }
   
   public Class getType() {
      return factory.getDeclaringClass();
   }

   public Class getDependant() {
      return Reflector.getParameterDependant(factory, index);
   }

   public Class[] getDependants() {
      return Reflector.getParameterDependants(factory, index);
   }

   public int getIndex() {
      return index;
   }

   public String getName() throws Exception{
      return name;
   }

   public Object get(Object source) throws Exception {
      return null;
   }

   public <T extends Annotation> T getAnnotation(Class<T> type) {
      return null;
   }

   public void set(Object source, Object value) throws Exception {      
   }
}
