package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class FloatArrayTransform implements Transform<Float[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public FloatArrayTransform() {
      this.array = new ArrayTransform(Float.class);          
      this.single = new FloatTransform();
   }

   public Float[] read(String value) throws Exception {
      return (Float[]) array.read(value, single);           
   }
   
   public String write(Float[] value) throws Exception {
      return array.write(value, single);
   }
}
