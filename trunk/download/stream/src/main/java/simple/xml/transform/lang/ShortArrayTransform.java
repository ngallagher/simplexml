package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class ShortArrayTransform implements Transform<Short[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public ShortArrayTransform() {
      this.array = new ArrayTransform(Short.class);          
      this.single = new ShortTransform();
   }

   public Short[] read(String value) throws Exception {
      return (Short[]) array.read(value, single);           
   }
   
   public String write(Short[] value) throws Exception {
      return array.write(value, single);
   }
}
