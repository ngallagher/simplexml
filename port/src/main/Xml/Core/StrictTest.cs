#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class StrictTest : ValidationTestCase {
      private const String SOURCE =
       "<root version='2.1' id='234'>\n" +
       "   <list length='3' type='sorted'>\n" +
       "      <item name='1'>\n" +
       "         <value>value 1</value>\n" +
       "      </item>\n" +
       "      <item name='2'>\n" +
       "         <value>value 2</value>\n" +
       "      </item>\n" +
       "      <item name='3'>\n" +
       "         <value>value 3</value>\n" +
       "      </item>\n" +
       "   </list>\n" +
       "   <object name='name'>\n" +
       "      <integer>123</integer>\n" +
       "      <object name='key'>\n" +
       "         <integer>12345</integer>\n" +
       "      </object>\n" +
       "   </object>\n" +
       "</root>";
      private const String SIMPLE =
       "<object name='name'>\n" +
       "   <integer>123</integer>\n" +
       "   <object name='key'>\n" +
       "      <integer>12345</integer>\n" +
       "   </object>\n" +
       "   <name>test</name>\n"+
       "</object>\n";
      private const String SIMPLE_MISSING_NAME =
      "<object name='name'>\n" +
      "   <integer>123</integer>\n" +
      "   <object name='key'>\n" +
      "      <integer>12345</integer>\n" +
      "   </object>\n" +
      "</object>\n";
      [Root(Name="root", Strict=false)]
      private static class StrictExample {
         [ElementArray(Name="list", Entry="item")]
         private StrictEntry[] list;
         [Element(Name="object")]
         private StrictObject object;
      }
      [Root(Name="entry", Strict=false)]
      private static class StrictEntry {
         [Element(Name="value")]
         private String value;
      }
      [Root(Strict=false)]
      private static class StrictObject {
         [Element(Name="integer")]
         private int integer;
      }
      [Root(Name="object", Strict=false)]
      private static class NamedStrictObject : StrictObject {
         [Element(Name="name")]
         private String name;
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestStrict() {
         StrictExample example = persister.Read(StrictExample.class, SOURCE);
         AssertEquals(example.list.length, 3);
         AssertEquals(example.list[0].value, "value 1");
         AssertEquals(example.list[1].value, "value 2");
         AssertEquals(example.list[2].value, "value 3");
         AssertEquals(example.object.integer, 123);
         Validate(example, persister);
      }
      //public void testUnnamedStrict() {
      //   bool success = false;
      //
      //   try {
      //      persister.Read(StrictObject.class, SIMPLE);
      //   } catch(RootException e) {
      //      success = true;
      //   }
      //   assertTrue(success);
      //}
      public void TestNamedStrict() {
         StrictObject object = persister.Read(NamedStrictObject.class, SIMPLE);
         AssertEquals(object.integer, 123);
         Validate(object, persister);
      }
      public void TestNamedStrictMissingName() {
         bool failure = false;
         try {
            StrictObject object = persister.Read(NamedStrictObject.class, SIMPLE_MISSING_NAME);
            AssertNotNull(object);
         }catch(Exception e) {
            e.printStackTrace();
            failure = true;
         }
         assertTrue("Did not fail", failure);
      }
   }
}
