package simple.xml.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

interface MethodPart {
   
   public Annotation getAnnotation();
   
   public Method getMethod();
   
   public Class getType();
}
