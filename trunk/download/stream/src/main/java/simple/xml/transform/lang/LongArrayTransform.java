package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class LongArrayTransform implements Transform<Long[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public LongArrayTransform() {
      this.array = new ArrayTransform(Long.class);          
      this.single = new LongTransform();
   }

   public Long[] read(String value) throws Exception {
      return (Long[]) array.read(value, single);           
   }
   
   public String write(Long[] value) throws Exception {
      return array.write(value, single);
   }
}
