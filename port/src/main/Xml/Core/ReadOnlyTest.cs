#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ReadOnlyTest : TestCase {
      private const String SOURCE =
         "<example name='name'>"+
         "   <value>some text here</value>"+
         "</example>";
      [Root(Name="example")]
      private static class ReadOnlyFieldExample {
         [Attribute(Name="name")]
         [Element(Name="value")]
         public ReadOnlyFieldExample(@Attribute(name="name") String name, @Element(name="value") String value) {
            this.name = name;
            this.value = value;
         }
      }
      [Root(Name="example")]
      private static class ReadOnlyMethodExample {
         private readonly String name;
         private readonly String value;
         public ReadOnlyMethodExample(@Attribute(name="name") String name, @Element(name="value") String value) {
            this.name = name;
            this.value = value;
         }
         [Attribute(Name="name")]
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
         public String Value {
            get {
               return value;
            }
         }
         //public String GetValue() {
         //   return value;
         //}
      [Root(Name="example")]
      private static class IllegalReadOnlyMethodExample {
         private readonly String name;
         private readonly String value;
         public IllegalReadOnlyMethodExample(@Attribute(name="name") String name, @Element(name="value") String value) {
            this.name = name;
            this.value = value;
         }
         [Attribute(Name="name")]
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
         public String Value {
            get {
               return value;
            }
         }
         //public String GetValue() {
         //   return value;
         //}
         public String IllegalValue {
            get {
               return value;
            }
         }
         //public String GetIllegalValue() {
         //   return value;
         //}
      public void TestReadOnlyField() {
         Persister persister = new Persister();
         ReadOnlyFieldExample example = persister.read(ReadOnlyFieldExample.class, SOURCE);
         AssertEquals(example.name, "name");
         AssertEquals(example.value, "some text here");
      }
      public void TestReadOnlyMethod() {
         Persister persister = new Persister();
         ReadOnlyMethodExample example = persister.read(ReadOnlyMethodExample.class, SOURCE);
         AssertEquals(example.Name, "name");
         AssertEquals(example.Value, "some text here");
      }
      public void TestIllegalReadOnlyMethod() {
         bool failure = false;
         try {
            Persister persister = new Persister();
            IllegalReadOnlyMethodExample example = persister.read(IllegalReadOnlyMethodExample.class, SOURCE);
         }catch(Exception e) {
            failure = true;
         }
         assertTrue(failure);
      }
   }
}
