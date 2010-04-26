#region Using directives
using SimpleFramework.Xml;
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
      [One]
      [Two]
      public class Base {}
      [Three]
      [Four]
      public class Extended : Base{}
      public void TestScannerBuilder() {
         ScannerBuilder builder = new ScannerBuilder();
         Scanner scanner = builder.build(Extended.class);
         AssertNull(scanner.scan(Convert.class));
         AssertNotNull(scanner.scan(One.class));
         AssertNotNull(scanner.scan(Two.class));
         AssertNotNull(scanner.scan(Three.class));
         AssertNotNull(scanner.scan(Four.class));
         AssertEquals(scanner.scan(Convert.class), null);
         assertTrue(One.class.isAssignableFrom(scanner.scan(One.class).getClass()));
         assertTrue(Two.class.isAssignableFrom(scanner.scan(Two.class).getClass()));
         assertTrue(Three.class.isAssignableFrom(scanner.scan(Three.class).getClass()));
         assertTrue(Four.class.isAssignableFrom(scanner.scan(Four.class).getClass()));
      }
   }
}
