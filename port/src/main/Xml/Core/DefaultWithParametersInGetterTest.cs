#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class DefaultWithParametersInGetterTest : ValidationTestCase {
      [Root]
      [Default(DefaultType.PROPERTY)]
      static class DefaultTestClass {
         private bool flag;
         private int foo;
         public int Foo {
            get {
               return foo;
            }
         }
         //public int GetFoo() {
         //   return foo;
         //}
            this.foo = foo;
         }
         public String GetWithParams(int foo) {
            return "foo";
         }
         public bool IsFlag() {
            return flag;
         }
         public bool Flag {
            set {
               this.flag = value;
            }
         }
         //public void SetFlag(bool flag) {
         //   this.flag = flag;
         //}
      [Root]
      [Default(DefaultType.PROPERTY)]
      static class DefaultTestClassWithInvalidTransient {
         private int foo;
         public int Foo {
            get {
               return foo;
            }
         }
         //public int GetFoo() {
         //   return foo;
         //}
            this.foo = foo;
         }
         [Transient]
         public String GetWithParams(int foo) {
            return "foo";
         }
      }
      [Root]
      [Default(DefaultType.PROPERTY)]
      static class DefaultTestClassWithInvalidElement {
         private String name;
         [Element]
         public String GetName(int foo) {
            return name;
         }
         [Element]
         public String Name {
            set {
               this.name = value;
            }
         }
         //public void SetName(String name) {
         //   this.name = name;
         //}
      public void TestDefaultWithParameters() {
         Persister persister = new Persister();
         DefaultTestClass type = new DefaultTestClass();
         type.foo = 100;
         persister.write(type, System.out);
         validate(type, persister);
      }
      public void TestDefaultWithTransientErrors() {
         Persister persister = new Persister();
         DefaultTestClassWithInvalidTransient type = new DefaultTestClassWithInvalidTransient();
         type.foo = 100;
         bool failure = false;
         try {
            persister.write(type, System.out);
         }catch(Exception e) {
            e.printStackTrace();
            failure=true;
         }
         assertTrue("Annotation on a method which is not a property succeeded", failure);
      }
      public void TestDefaultWithElementErrors() {
         Persister persister = new Persister();
         DefaultTestClassWithInvalidElement type = new DefaultTestClassWithInvalidElement();
         type.name = "name";
         bool failure = false;
         try {
            persister.write(type, System.out);
         }catch(Exception e) {
            e.printStackTrace();
            failure=true;
         }
         assertTrue("Annotation on a method which is not a property succeeded", failure);
      }
   }
}
