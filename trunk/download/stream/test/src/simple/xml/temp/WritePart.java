package simple.xml.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class WritePart implements MethodPart {
   
   private Annotation label;
   
   private Method method;
   
   public WritePart(Method method, Annotation label) {
      this.method = method;
      this.label = label;
   }
   
   public Class getType() {
      return method.getParameterTypes()[0];
   }
   
   public Annotation getAnnotation() {
      return label;
   }
   
   public Method getMethod() {
      return method;
   }

}
