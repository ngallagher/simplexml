package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class FieldDetail {

   private final Annotation[] list;
   private final Field field;
   private final String name;
   
   public FieldDetail(Field field) {
      this.list = field.getDeclaredAnnotations();
      this.name = field.getName();
      this.field = field;
   }
   
   public Annotation[] getAnnotations() {
      return list;
   }
   
   public Field getField() {
      return field;
   }
   
   public String getName() {
      return name;
   }
}
