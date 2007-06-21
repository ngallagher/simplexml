package simple.xml.transform.math;

import simple.xml.transform.Transform;
import java.math.BigDecimal;

public class BigDecimalTransform implements Transform<BigDecimal> {

   public BigDecimal read(String value) {
      return new BigDecimal(value);
   }

   public String write(BigDecimal value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}