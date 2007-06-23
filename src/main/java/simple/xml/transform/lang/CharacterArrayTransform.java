package simple.xml.transform.lang;

import simple.xml.transform.Transform;

public class CharacterArrayTransform implements Transform<Character[]> {

   public Character[] read(String value) throws Exception {
      int length = value.length();
      
      if(length > 0) {
         read(value, length);
      }
      return new Character[]{};
   }
   
   private Character[] read(String value, int length) throws Exception {
      Character[] array = new Character[length];
      
      for(int i = 0; i < length; i++) {
         array[i] = value.charAt(i);
      }
      return array;
   }
   
   public String write(Character[] value) throws Exception {
      StringBuilder text = new StringBuilder();
      
      for(int i = 0; i < value.length; i++) {
         if(value[i] != null) {
            text.append(value[i]);
         }
      }
      return text.toString();
   }
}
