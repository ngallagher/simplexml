package simple.xml.temp;

import java.lang.annotation.Annotation;

interface Contact {
   
   public Annotation getAnnotation();
   
   public Class getType();
   
   public void set(Object source, Object value) throws Exception;
   
   public Object get(Object source) throws Exception;
}
