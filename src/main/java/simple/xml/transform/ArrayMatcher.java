

package simple.xml.transform;

public class ArrayMatcher {


   public Transform match(Class type) throws Exception {
      Class entry = type.getComponentType();
   
      if(entry.isPrimitive()) {
         matchPrimitive(type, entry);              
      }      
      return match(type, entry);
   }        

   private Transform matchPrimitive(Class type, Class entry) {
      Class promote = promoteType(entry);
      Transform delegate = match(promote);

      return new PrimitiveArrayTransform(delegate, entry);
   }

   private Transform match(Class type, Class entry) {
      // resolve the name here and load it           
   }
}
