package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

class Parameter2{

   private final Constructor factory;
   private final Annotation label;
   private final String name;
   private final int index;
   
   public Parameter2(Constructor factory, Annotation label, String name, int index) {
      this.factory = factory;
      this.label = label;
      this.name = name;
      this.index = index;
   }
   
   public int getIndex() {
      return index;
   }
   
   public Class getDependant() {
      return Reflector.getParameterDependant(factory, index);
   }

   public Class[] getDependants() {
      return Reflector.getParameterDependants(factory, index);
   }
   
   public Class getType() {
      return factory.getDeclaringClass();
   }
   
   public Annotation getAnnotation() {
      return label;
   }
   
   public String getName() {
      return name;
   }
   
   public String toString() {
      return String.format("parameter %s of %s", name, factory);
   }
}
