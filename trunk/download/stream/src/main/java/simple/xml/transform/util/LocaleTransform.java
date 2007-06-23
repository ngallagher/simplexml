package simple.xml.transform.util;

import simple.xml.transform.InvalidFormatException;
import simple.xml.transform.Transform;
import java.util.regex.Pattern;
import java.util.Locale;

public class LocaleTransform implements Transform<Locale>{

   private final Pattern pattern;
   
   public LocaleTransform() {
      this.pattern = Pattern.compile("_");
   }
   
   public Locale read(String locale) throws Exception {
      String[] list = pattern.split(locale);
      
      if(list.length < 1) {
         throw new InvalidFormatException("Invalid locale %s", locale);
      }
      return read(list);
   }
   
   private Locale read(String[] locale)  throws Exception {
      String[] list = new String[] {"", "", ""};
      
      for(int i = 0; i < list.length; i++) {
         if(i < locale.length) {         
            list[i] = locale[i];
         }
      }
      return new Locale(list[0], list[1], list[2]);
   }
   
   public String write(Locale locale) {
      return locale.toString();
   }
}
