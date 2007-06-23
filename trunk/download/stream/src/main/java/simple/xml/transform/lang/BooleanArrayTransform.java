package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class BooleanArrayTransform implements Transform<Boolean[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public BooleanArrayTransform() {
      this.array = new ArrayTransform(Boolean.class);          
      this.single = new BooleanTransform();
   }

   public Boolean[] read(String value) throws Exception {
      return (Boolean[]) array.read(value, single);           
   }
   
   public String write(Boolean[] value) throws Exception {
      return array.write(value, single);
   }
}
