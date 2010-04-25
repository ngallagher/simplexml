#region Using directives
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class ScannerBuilderTest : TestCase {
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      public static class One: System.Attribute {
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      public static class Two: System.Attribute {
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      public static class Three: System.Attribute {
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      public static class Four: System.Attribute {
      @One
      @Two
      public class Base {}
      @Three
      @Four
      public class Extended : Base{}
      public void TestScannerBuilder() {
         ScannerBuilder builder = new ScannerBuilder();
         Scanner scanner = builder.build(Extended.class);
         assertNull(scanner.scan(Convert.class));
         assertNotNull(scanner.scan(One.class));
         assertNotNull(scanner.scan(Two.class));
         assertNotNull(scanner.scan(Three.class));
         assertNotNull(scanner.scan(Four.class));
         assertEquals(scanner.scan(Convert.class), null);
         assertTrue(One.class.isAssignableFrom(scanner.scan(One.class).getClass()));
         assertTrue(Two.class.isAssignableFrom(scanner.scan(Two.class).getClass()));
         assertTrue(Three.class.isAssignableFrom(scanner.scan(Three.class).getClass()));
         assertTrue(Four.class.isAssignableFrom(scanner.scan(Four.class).getClass()));
      }
   }
}
