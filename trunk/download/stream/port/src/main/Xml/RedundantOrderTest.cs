#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class RedundantOrderTest : ValidationTestCase {
      [Order(Elements={"a", "b", "c"})]
      public static class ElementEntry {
         [Element]
         private String a;
         [Element]
         private String b;
         [Transient]
         private String c;
         private ElementEntry() {
            super();
         }
         public ElementEntry(String a, String b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
         }
      }
      [Order(Attributes={"a", "b", "c"})]
      public static class AttributeEntry {
         [Attribute]
         private String a;
         [Attribute]
         private String b;
         [Transient]
         private String c;
         private AttributeEntry() {
            super();
         }
         public AttributeEntry(String a, String b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
         }
      }
      public void TestRedundantElementOrder() {
         ElementEntry entry = new ElementEntry("a", "b", "c");
         Persister persister = new Persister();
         bool exception = false;
         try {
            validate(entry, persister);
         }catch(ElementException e) {
            e.printStackTrace();
            exception = true;
         }
         assertTrue(exception);
      }
      public void TestRedundantAttributeOrder() {
         AttributeEntry entry = new AttributeEntry("a", "b", "c");
         Persister persister = new Persister();
         bool exception = false;
         try {
            validate(entry, persister);
         }catch(AttributeException e) {
            e.printStackTrace();
            exception = true;
         }
         assertTrue(exception);
      }
   }
}
