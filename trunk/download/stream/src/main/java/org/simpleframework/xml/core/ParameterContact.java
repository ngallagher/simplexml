package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

abstract class ParameterContact<T extends Annotation> implements Contact {

   protected final Constructor factory;
   protected final int index;
   protected final T label;
   
   public ParameterContact(T label, Constructor factory, int index) {
      this.factory = factory;
      this.index = index;
      this.label = label;
   }
   
   public Annotation getAnnotation() {
      return label;
   }
   
   public Class getType() {
      return factory.getParameterTypes()[index];
   }

   public Class getDependant() {
      return Reflector.getParameterDependant(factory, index);
   }

   public Class[] getDependants() {
      return Reflector.getParameterDependants(factory, index);
   }
   
   public Object get(Object source) {
      return null;
   }  

   public void set(Object source, Object value) {  
      return;
   }

   public <T extends Annotation> T getAnnotation(Class<T> type) {
      return null;
   }
   
   public boolean isFinal() {
      return true;
   }

   public int getIndex() {
      return index;
   }

   public abstract String getName();
}
