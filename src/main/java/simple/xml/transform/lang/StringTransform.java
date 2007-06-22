

package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class StringTransform implements Transform<String> {

   public String read(String value) {
      return value;     
   }

   public String write(String value) {
      return value;
   }
}
