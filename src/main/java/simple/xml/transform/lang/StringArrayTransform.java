

package simple.xml.transform.lang;

import simple.xml.transform.Transform;
import java.util.regex.Pattern;

public class StringArrayTransform implements Transform<String[]> {

   private final Pattern pattern;        

   private final String token;        

   public StringArrayTransform() {
      this(",");           
   }        

   public StringArrayTransform(String token) {
      this.pattern = Pattern.compile(token);           
      this.token = token;           
   }

   public String[] read(String value) {
      return read(value, token);           
   }

   public String[] read(String value, String token) {
      String[] list = pattern.split(value);

      for(int i = 0; i < list.length; i++) {
         String text = list[i];

         if(text != null) {              
            list[i] = text.trim();
         }
      }
      return list;
   }

   public String write(String[] list) {
      return write(list, token);
   }

   public String write(String[] list, String token) {                 
      StringBuilder text = new StringBuilder();           

      for(int i = 0; i < list.length; i++) {
         String item = list[i];

         if(item != null) { 
            if(text.length() > 0) {
               text.append(token);
               text.append(' ');
            }
            text.append(item);
         }
      }   
      return text.toString();
   }
}
