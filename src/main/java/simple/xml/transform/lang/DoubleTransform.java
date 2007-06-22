package simple.xml.transform.lang;


import simple.xml.transform.Transform;

public class DoubleTransform implements Transform<Double> {

   public Double read(String value) {
      return new Double(value);
   }

   public String write(Double value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}