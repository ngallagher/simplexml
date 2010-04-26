#region Using directives
using SimpleFramework.Xml.Transform;
using SimpleFramework.Xml;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class URLTransformTest : TestCase {
   public void TestURL() {
      URL file = new URL("http://www.google.com/");
      URLTransform format = new URLTransform();
      String value = format.write(file);
      URL copy = format.read(value);
      AssertEquals(file, copy);
   }
}
}
