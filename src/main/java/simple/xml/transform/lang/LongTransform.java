package simple.xml.transform.lang;


import simple.xml.transform.Transform;

public class LongTransform implements Transform<Long> {

   public Long read(String value) {
      return new Long(value);
   }

   public String write(Long value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}