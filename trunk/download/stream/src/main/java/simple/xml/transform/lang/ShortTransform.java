package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class ShortTransform implements Transform<Short> {

   public Short read(String value) {
      return new Short(value);
   }

   public String write(Short value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}