#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class VersionTest : TestCase {
      private const String VERSION_1 =
      "<?xml version=\"1.0\"?>\n"+
      "<Example version='1.0'>\n"+
      "   <text>text value</text>  \n\r"+
      "</Example>";
      private const String VERSION_2 =
      "<?xml version=\"1.0\"?>\n"+
      "<Example version='2.0'>\n"+
      "   <name>example name</name>  \n\r"+
      "   <value>text value</value> \n"+
      "   <entry name='example'>\n"+
      "      <value>text value</value> \n"+
      "   </entry>\n"+
      "   <ignore>ignore this element</ignore>\n"+
      "</Example>";
      public interface Versionable {
         public abstract double Version {
            get;
         }
         //public double GetVersion();
      [Root(Name="Example")]
      private static abstract class Example : Versionable {
         [Version]
         [Namespace(Prefix="prefix", Reference="http://www.domain.com/reference")]
         private double version;
         public double Version {
            get {
               return version;
            }
         }
         //public double GetVersion() {
         //   return version;
         //}
      }
      private static class Example1 : Example {
         [Element(Name="text")]
         private String text;
         public String Value {
            get {
               return text;
            }
         }
         //public String GetValue() {
         //   return text;
         //}
      private static class Example2 : Example {
         [Element(Name="name")]
         private String name;
         [Element(Name="value")]
         private String value;
         [Element(Name="entry")]
         private Entry entry;
         public String Value {
            get {
               return value;
            }
         }
         //public String GetValue() {
         //   return value;
         //}
      private static class Entry {
         [Attribute(Name="name")]
         private String name;
         [Element(Name="value")]
         private String value;
      }
      public static class SimpleType : Value{
         private Class type;
         public SimpleType(Class type) {
            this.type = type;
         }
         public int Length {
            get {
               return 0;
            }
         }
         //public int GetLength() {
         //   return 0;
         //}
            try {
               Constructor method = type.getDeclaredConstructor();
               if(!method.isAccessible()) {
                  method.setAccessible(true);
               }
               return method.newInstance();
            }catch(Exception e) {
               throw new RuntimeException(e);
            }
         }
          public Object Value {
             set {
             }
          }
          //public void SetValue(Object value) {
          //}
             return false;
          }
         public Class Type {
            get {
              return type;
            }
         }
         //public Class GetType() {
         //  return type;
         //}
      public void testVersion1() {
         Serializer persister = new Persister();
         Example example = persister.read(Example1.class, VERSION_1);
         assertTrue(example instanceof Example1);
         assertEquals(example.Value, "text value");
         assertEquals(example.version, 1.0);
         persister.write(example, System.out);
         assertEquals(example.Value, "text value");
         assertEquals(example.version, 1.0);
      }
      public void testVersion2() {
         Serializer persister = new Persister();
         Example example = persister.read(Example2.class, VERSION_2);
         assertTrue(example instanceof Example2);
         assertEquals(example.Value, "text value");
         assertEquals(example.version, 2.0);
         persister.write(example, System.out);
         assertEquals(example.Value, "text value");
         assertEquals(example.version, 2.0);
      }
   }
}
