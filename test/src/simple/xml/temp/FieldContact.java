package simple.xml.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldContact implements Contact {
   
   private Annotation label;
   
   private Field field;
   
   public FieldContact(Field field, Annotation label) {
      this.label = label;
      this.field = field;
   }
   
   public Class getType() {
      return field.getType();
   }
   
   public Annotation getAnnotation() {
      return label;
   }
   
   public void set(Object source, Object value) throws Exception {
      field.set(source, value);
   }
   
   public Object get(Object source) throws Exception {
      return field.get(source);
   }
}
