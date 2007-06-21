package simple.xml.transform.math;

import simple.xml.transform.Transform;
import java.math.BigInteger;

public class BigIntegerTransform implements Transform<BigInteger> {

   public BigInteger read(String value) {
      return new BigInteger(value);
   }

   public String write(BigInteger value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}
