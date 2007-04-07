package simple.xml.temp;

import java.lang.annotation.Annotation;

public class MethodContact implements Contact {

   private MethodPart read;
   
   private MethodPart write;
   
   public MethodContact(MethodPart read, MethodPart write) {
      this.write = write;
      this.read = read;
   }
   
   public Class getType() {
      return read.getType();
   }
   
   public Annotation getAnnotation() {
      return read.getAnnotation();
   }
   
   public void set(Object source, Object value) throws Exception{
      write.getMethod().invoke(source, value);
   }
   
   public Object get(Object source) throws Exception {
      return read.getMethod().invoke(source);
   }
   
}
