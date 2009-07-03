package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

class Parameter {

   private final Annotation label;
   private final Class type;
   private final String name;
   
   public Parameter(Class type, Annotation label, String name) {
      this.label = label;
      this.name = name;
      this.type = type;
   }
   
   public Class getType() {
      return type;
   }
   
   public Annotation getAnnotation() {
      return label;
   }
   
   public String getName() {
      return name;
   }
}
