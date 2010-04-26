#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class ClassTransformTest : TestCase {
   public void TestClassTransform() {
      Class c = Date.class;
      ClassTransform transform = new ClassTransform();
      String value = transform.write(c);
      Class copy = transform.read(value);
      AssertEquals(c, copy);
   }
}
}
