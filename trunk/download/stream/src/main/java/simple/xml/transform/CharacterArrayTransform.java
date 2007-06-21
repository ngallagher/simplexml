

package simple.xml.transform;

public class CharacterArrayTransform implements Transform<char[]> {

   public char[] read(String value) throws Exception {
      return value.toCharArray();           
   }        

   public String write(char[] value) throws Exception {
      return new String(value);           
   }
}
