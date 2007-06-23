package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class ByteArrayTransform implements Transform<Byte[]> {

   private final ArrayTransform array;
   
   private final Transform single;
   
   public ByteArrayTransform() {
      this.array = new ArrayTransform(Byte.class);          
      this.single = new ByteTransform();
   }

   public Byte[] read(String value) throws Exception {
      return (Byte[]) array.read(value, single);           
   }
   
   public String write(Byte[] value) throws Exception {
      return array.write(value, single);
   }
}
