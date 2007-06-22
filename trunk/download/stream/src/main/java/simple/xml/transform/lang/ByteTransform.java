package simple.xml.transform.lang;


import simple.xml.transform.Transform;

public class ByteTransform implements Transform<Byte> {

   public Byte read(String value) {
      return new Byte(value);
   }

   public String write(Byte value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}