#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class StrictModeTest : ValidationTestCase {
      private const String SOURCE =
      "<object name='name' version='1.0'>\n" +
      "   <integer>123</integer>\n" +
      "   <object name='key'>\n" +
      "      <integer>12345</integer>\n" +
      "   </object>\n" +
      "   <address type='full'>\n"+
      "      <name>example name</name>\n"+
      "      <address>example address</address>\n"+
      "      <city>example city</city>\n"+
      "   </address>\n"+
      "   <name>test</name>\n"+
      "</object>\n";
      private const String SOURCE_MISSING_NAME =
      "<object name='name' version='1.0'>\n" +
      "   <integer>123</integer>\n" +
      "   <object name='key'>\n" +
      "      <integer>12345</integer>\n" +
      "   </object>\n" +
      "   <address type='full'>\n"+
      "      <name>example name</name>\n"+
      "      <address>example address</address>\n"+
      "      <city>example city</city>\n"+
      "   </address>\n"+
      "</object>\n";
      [Root]
      private static class ExampleObject {
         [Element]
         [Attribute]
      }
      [Root]
      private static class Address {
         [Element]
         [Element]
      }
      [Root]
      private static class ExampleObjectWithAddress : ExampleObject {
         [Element]
         [Element]
      }
      public void TestStrictMode() {
         bool failure = false;
         try {
            Persister persister = new Persister();
            ExampleObjectWithAddress object = persister.read(ExampleObjectWithAddress.class, SOURCE);
            AssertNull(object);
         }catch(Exception e) {
            e.printStackTrace();
            failure = true;
         }
         assertTrue("Serialzed correctly", failure);
      }
      public void TestNonStrictMode() {
         Persister persister = new Persister();
         ExampleObjectWithAddress object = persister.read(ExampleObjectWithAddress.class, SOURCE, false);
         AssertEquals(object.version, 1.0);
         AssertEquals(object.integer, 123);
         AssertEquals(object.address.name, "example name");
         AssertEquals(object.address.address, "example address");
         AssertEquals(object.name, "test");
         validate(object, persister);
      }
      public void TestNonStrictModeMissingName() {
         bool failure = false;
         try {
            Persister persister = new Persister();
            ExampleObjectWithAddress object = persister.read(ExampleObjectWithAddress.class, SOURCE_MISSING_NAME, false);
            AssertNull(object);
         }catch(Exception e) {
            e.printStackTrace();
            failure = true;
         }
         assertTrue("Serialzed correctly", failure);
      }
      public void TestValidation() {
         Persister persister = new Persister();
         assertTrue(persister.validate(ExampleObjectWithAddress.class, SOURCE, false));
         try {
            assertFalse(persister.validate(ExampleObjectWithAddress.class, SOURCE));
            assertFalse(true);
         }catch(Exception e){
            e.printStackTrace();
         }
         try {
            assertFalse(persister.validate(ExampleObjectWithAddress.class, SOURCE_MISSING_NAME));
            assertFalse(true);
         }catch(Exception e){
            e.printStackTrace();
         }
         try {
            assertFalse(persister.validate(ExampleObjectWithAddress.class, SOURCE_MISSING_NAME, false));
            assertFalse(true);
         }catch(Exception e){
            e.printStackTrace();
         }
      }
   }
}
