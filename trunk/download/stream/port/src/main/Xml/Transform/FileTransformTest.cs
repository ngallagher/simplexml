#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class FileTransformTest : TestCase {
   public void TestFile() {
      File file = new File("..");
      FileTransform format = new FileTransform();
      String value = format.write(file);
      File copy = format.read(value);
      assertEquals(file, copy);
   }
}
}
