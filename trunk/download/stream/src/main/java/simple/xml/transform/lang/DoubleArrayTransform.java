package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class DoubleArrayTransform implements Transform<Double[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public DoubleArrayTransform() {
      this.array = new ArrayTransform(Double.class);          
      this.single = new DoubleTransform();
   }

   public Double[] read(String value) throws Exception {
      return (Double[]) array.read(value, single);           
   }
   
   public String write(Double[] value) throws Exception {
      return array.write(value, single);
   }
}
