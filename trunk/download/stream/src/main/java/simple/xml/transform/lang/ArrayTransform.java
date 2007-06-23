package simple.xml.transform.lang;

import java.lang.reflect.Array;

import simple.xml.transform.Transform;

class ArrayTransform {
   
   private final StringArrayTransform split;        

   private final Class entry;

   public ArrayTransform(Class entry) {
      this.split = new StringArrayTransform();
      this.entry = entry;
   }
   
   public <T> T read(String value, Transform single) throws Exception {
      String[] list = split.read(value);      
      int length = list.length;

      return (T)read(list, single, length);
   }
   
   private Object read(String[] list, Transform single, int length) throws Exception {
      Object array = Array.newInstance(entry, length);

      for(int i = 0; i < length; i++) {
         Object item = single.read(list[i]);

         if(item != null) {
            Array.set(array, i, item);                 
         }         
      }
      return array;
   }
   
   public String write(Object[] value, Transform single) throws Exception {
      String[] list = new String[value.length];

      for(int i = 0; i < value.length; i++) {
         Object entry = Array.get(value, i);

         if(entry != null) {
            list[i] = single.write(entry);                             
         }         
      }      
      return split.write(list);      
   }
}
