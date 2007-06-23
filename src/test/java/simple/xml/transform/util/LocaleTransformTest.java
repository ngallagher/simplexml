package simple.xml.transform.util;

import java.util.Locale;

import junit.framework.TestCase;

public class LocaleTransformTest extends TestCase {
   
   public void testLocale() throws Exception {
      Locale locale = Locale.UK;
      LocaleTransform format = new LocaleTransform();
      String value = format.write(locale);
      Locale copy = format.read(value);
      
      assertEquals(locale, copy);   
      
      locale = Locale.ENGLISH;
      value = format.write(locale);
      copy = format.read(value);
      
      assertEquals(locale, copy);
   }
}
