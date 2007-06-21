
package simple.xml.transform;

import simple.xml.transform.lang.StringArrayTransform;
import java.lang.reflect.Array;

public class PrimitiveArrayTransform implements Transform {           

   private final StringArrayTransform split;        

   private final Transform delegate;

   private final Class entry;

   public PrimitiveArrayTransform(Transform delegate, Class entry) {
      this.split = new StringArrayTransform();
      this.delegate = delegate;
      this.entry = entry;
   }        

   public Object read(String value) throws Exception {
      String[] list = split.read(value);      
      int length = list.length;

      return read(list, length);
   }

   private Object read(String[] list, int length) throws Exception {
      Object array = Array.newInstance(entry, length);

      for(int i = 0; i < length; i++) {
         Object item = delegate.read(list[i]);

         if(item != null) {
            Array.set(array, i, item);                 
         }         
      }
      return array;
   }

   public String write(Object value) throws Exception {
      int length = Array.getLength(value);

      return write(value, length);      
   }

   private String write(Object value, int length) throws Exception {
      String[] list = new String[length];

      for(int i = 0; i < length; i++) {
         Object entry = Array.get(value, i);

         if(entry != null) {
            list[i] = delegate.write(entry);                             
         }         
      }      
      return split.write(list);
   }
}
