

package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class IntegerArrayTransform implements Transform<Integer[]> {

   private final StringArrayTransform split;

   private final IntegerTransform single;

   public IntegerArrayTransform() {
      this.split = new StringArrayTransform();           
      this.single = new IntegerTransform();
   }        

   public Integer[] read(String value) {
      String[] list = split.read(value);

      if(list.length > 0) {   
         return read(list);
      }
      return new Integer[]{};
   }

   private Integer[] read(String[] text) {
      Integer[] list = new Integer[text.length];

      for(int i = 0; i < text.length; i++) {
         list[i] = single.read(text[i]);
      }
      return list;
   }

   public String write(Integer[] list) {
      String[] text = new String[list.length];           

      for(int i = 0; i < text.length; i++) {
         if(list[i] != null) {
             text[i] = single.write(list[i]);
         }
      }
      return split.write(text);
   }
}
