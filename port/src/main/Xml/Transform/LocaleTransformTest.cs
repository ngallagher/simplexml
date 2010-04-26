#region Using directives
using SimpleFramework.Xml.Transform;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class LocaleTransformTest : TestCase {
   public void TestLocale() {
      Locale locale = Locale.UK;
      LocaleTransform format = new LocaleTransform();
      String value = format.write(locale);
      Locale copy = format.read(value);
      AssertEquals(locale, copy);
      locale = Locale.ENGLISH;
      value = format.write(locale);
      copy = format.read(value);
      AssertEquals(locale, copy);
   }
}
}
