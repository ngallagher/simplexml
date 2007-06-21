

package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class IntegerTransform implements Transform<Integer> {

   public Integer read(String value) {
      return new Integer(value);
   }

   public String write(Integer value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}
