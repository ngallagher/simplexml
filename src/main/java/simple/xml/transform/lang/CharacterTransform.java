

package simple.xml.transform.lang;

import simple.xml.transform.InvalidFormatException;
import simple.xml.transform.Transform;

public class CharacterTransform implements Transform<Character> {

   public Character read(String value) throws Exception {
      if(value.length() != 1) {
         throw new InvalidFormatException("Cannot convert %s to a character", value);
      }
      return value.charAt(0);     
   }

   public String write(Character value) throws Exception {
      return value.toString();
   }
}
