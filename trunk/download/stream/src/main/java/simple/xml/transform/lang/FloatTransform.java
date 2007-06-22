package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class FloatTransform implements Transform<Float> {

   public Float read(String value) {
      return new Float(value);
   }

   public String write(Float value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}