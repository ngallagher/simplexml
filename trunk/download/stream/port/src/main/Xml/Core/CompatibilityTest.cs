#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CompatibilityTest : TestCase {
      private const String IMPLICIT_VERSION_1 =
      "<example>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "  <list>\n"+
      "    <entry>entry 1</entry>\n"+
      "     <entry>entry 2</entry>\n"+
      "   </list>\n"+
      "</example>";
      private const String EXPLICIT_VERSION_1 =
      "<example version='1.0'>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "  <list>\n"+
      "    <entry>entry 1</entry>\n"+
      "     <entry>entry 2</entry>\n"+
      "   </list>\n"+
      "</example>";
      private const String INVALID_EXPLICIT_VERSION_1 =
      "<example version='1.0'>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "  <list>\n"+
      "    <entry>entry 1</entry>\n"+
      "     <entry>entry 2</entry>\n"+
      "   </list>\n"+
      "   <address>example address</address>\n"+
      "</example>";
      private const String INVALID_IMPLICIT_VERSION_1 =
      "<example>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "</example>";
      private const String VALID_EXPLICIT_VERSION_1_1 =
      "<example version='1.1'>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "  <address>example address</address>\n"+
      "  <list>\n"+
      "    <entry>entry 1</entry>\n"+
      "     <entry>entry 2</entry>\n"+
      "   </list>\n"+
      "</example>";
      public static interface Example {
         public abstract double Version {
            get;
         }
         //public double GetVersion();
         public abstract String Value {
            get;
         }
         //public String GetValue();
      [Root(Name="example")]
      public static class Example_v1 : Example {
        [Version]
        private double version;
        [Element]
        private String name;
        [Element]
        private String value;
        [ElementList]
        private List<String> list;
        public double Version {
           get {
              return version;
           }
        }
        //public double GetVersion() {
        //   return version;
        //}
           return name;
        }
        public String Value {
           get {
              return value;
           }
        }
        //public String GetValue() {
        //   return value;
        //}
      private const String VALID_EXPLICIT_VERSION_2 =
      "<example version='2.0' key='value'>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "  <map>\n"+
      "    <entry>\n"+
      "      <key>key 1</key>\n"+
      "      <value>value 1</value>\n"+
      "    </entry>\n"+
      "    <entry>\n"+
      "      <key>key 1</key>\n"+
      "      <value>value 1</value>\n"+
      "    </entry>\n"+
      "  </map>\n"+
      "</example>";
      private const String ACCEPTIBLE_INVALID_VERSION_1 =
      "<example>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "</example>";
      private const String INVALID_EXPLICIT_VERSION_2 =
      "<example version='2.0'>\n"+
      "  <name>example name</name>\n"+
      "  <value>example value</value>\n"+
      "  <map>\n"+
      "    <entry>\n"+
      "      <key>key 1</key>\n"+
      "      <value>value 1</value>\n"+
      "    </entry>\n"+
      "    <entry>\n"+
      "      <key>key 1</key>\n"+
      "      <value>value 1</value>\n"+
      "    </entry>\n"+
      "  </map>\n"+
      "</example>";
      [Root(Name="example")]
      public static class Example_v2 : Example {
        [Version(Revision=2.0)]
        [Namespace(Prefix="ver", Reference="http://www.domain.com/version")]
        private double version;
        [Element]
        private String name;
        [Element]
        private String value;
        [ElementMap]
        private Map<String, String> map;
        [Attribute]
        private String key;
        public double Version {
           get {
              return version;
           }
        }
        //public double GetVersion() {
        //   return version;
        //}
           return key;
        }
        public String Name {
           get {
              return name;
           }
        }
        //public String GetName() {
        //   return name;
        //}
           return value;
        }
      }
      [Root(Name="example")]
      public static class Example_v3 : Example {
        [Version(Revision=3.0)]
        [Namespace(Prefix="ver", Reference="http://www.domain.com/version")]
        private double version;
        [Element]
        private String name;
        [Element]
        private String value;
        [ElementMap]
        private Map<String, String> map;
        [ElementList]
        private List<String> list;
        [Attribute]
        private String key;
        public double Version {
           get {
              return version;
           }
        }
        //public double GetVersion() {
        //   return version;
        //}
           return key;
        }
        public String Name {
           get {
              return name;
           }
        }
        //public String GetName() {
        //   return name;
        //}
           return value;
        }
      }
      public void TestCompatibility() {
         Persister persister = new Persister();
         Example example = persister.read(Example_v1.class, IMPLICIT_VERSION_1);
         bool invalid = false;
         AssertEquals(example.Version, 1.0);
         AssertEquals(example.Name, "example name");
         AssertEquals(example.Value, "example value");
         example = persister.read(Example_v1.class, EXPLICIT_VERSION_1);
         AssertEquals(example.Version, 1.0);
         AssertEquals(example.Name, "example name");
         AssertEquals(example.Value, "example value");
         try {
            invalid = false;
            example = persister.read(Example_v1.class, INVALID_EXPLICIT_VERSION_1);
         }catch(Exception e) {
            e.printStackTrace();
            invalid = true;
         }
         assertTrue(invalid);
         try {
            invalid = false;
            example = persister.read(Example_v1.class, INVALID_IMPLICIT_VERSION_1);
         }catch(Exception e) {
            e.printStackTrace();
            invalid = true;
         }
         assertTrue(invalid);
         example = persister.read(Example_v1.class, VALID_EXPLICIT_VERSION_1_1);
         AssertEquals(example.Version, 1.1);
         AssertEquals(example.Name, "example name");
         AssertEquals(example.Value, "example value");
         Example_v2 example2 = persister.read(Example_v2.class, VALID_EXPLICIT_VERSION_2);
         AssertEquals(example2.Version, 2.0);
         AssertEquals(example2.Name, "example name");
         AssertEquals(example2.Value, "example value");
         AssertEquals(example2.Key, "value");
         example2 = persister.read(Example_v2.class, IMPLICIT_VERSION_1);
         AssertEquals(example2.Version, 1.0);
         AssertEquals(example2.Name, "example name");
         AssertEquals(example2.Value, "example value");
         AssertEquals(example2.Key, null);
         example2 = persister.read(Example_v2.class, ACCEPTIBLE_INVALID_VERSION_1);
         AssertEquals(example2.Version, 1.0);
         AssertEquals(example2.Name, "example name");
         AssertEquals(example2.Value, "example value");
         AssertEquals(example2.Key, null);
         try {
            invalid = false;
            example = persister.read(Example_v2.class, INVALID_EXPLICIT_VERSION_2);
         }catch(Exception e) {
            e.printStackTrace();
            invalid = true;
         }
         assertTrue(invalid);
      }
   }
}
