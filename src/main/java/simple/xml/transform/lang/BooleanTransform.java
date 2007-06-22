package simple.xml.transform.lang;


import simple.xml.transform.Transform;

public class BooleanTransform implements Transform<Boolean> {

   public Boolean read(String value) {
      return new Boolean(value);
   }

   public String write(Boolean value) {
      if(value != null) {
          return value.toString();
      }
      return null;
   }
}