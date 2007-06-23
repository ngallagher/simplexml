package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class IntegerArrayTransform implements Transform<Integer[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public IntegerArrayTransform() {
      this.array = new ArrayTransform(Integer.class);          
      this.single = new IntegerTransform();
   }

   public Integer[] read(String value) throws Exception {
      return (Integer[]) array.read(value, single);           
   }
   
   public String write(Integer[] value) throws Exception {
      return array.write(value, single);
   }
}
