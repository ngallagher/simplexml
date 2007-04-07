package simple.xml.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReadPart implements MethodPart {
   
   private Annotation label;
   
   private Method method;
   
   public ReadPart(Method method, Annotation label) {
      this.method = method;
      this.label = label;
   }
   
   public Class getType() {
      return method.getReturnType();
   }
   
   public Annotation getAnnotation() {
      return label;
   }
   
   public Method getMethod() {
      return method;
   }

}
