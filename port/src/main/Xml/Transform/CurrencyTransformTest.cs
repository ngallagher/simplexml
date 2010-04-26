#region Using directives
using SimpleFramework.Xml.Transform;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class CurrencyTransformTest : TestCase {
   public void TestCurrency() {
      Currency currency = Currency.getInstance(Locale.UK);
      CurrencyTransform format = new CurrencyTransform();
      String value = format.write(currency);
      Currency copy = format.read(value);
      AssertEquals(currency, copy);
   }
}
}
