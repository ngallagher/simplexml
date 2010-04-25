#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ScannerDefaultTest : TestCase {
      @Root
      [Default(DefaultType.FIELD)]
      private static class OrderItem {
         private Customer customer;
         private String name;
         @Transient
         private double price; // should be transient to avoid having prices as an attribute and an element, which is legal
         @Attribute
         public double Price {
            get {
               return price;
            }
            set {
               this.price = value;
            }
         }
         //public double GetPrice() {
         //   return price;
         //}
         //public void SetPrice(double price) {
         //   this.price = price;
         //}
      @Root
      [Default(DefaultType.PROPERTY)]
      private static class Customer {
         private String name;
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
            this.name = name;
         }
      }
      @Root
      [Default(DefaultType.FIELD)]
      private static class DuplicateExample {
         private int id;
         @Attribute
         public int Id {
            get {
               return id;
            }
            set {
               this.id = value;
            }
         }
         //public int GetId() {
         //   return id;
         //}
         //public void SetId(int id) {
         //   this.id = id;
         //}
      @Root
      [Default(DefaultType.PROPERTY)]
      private static class NonMatchingAnnotationExample {
         private String name;
         public String Name {
            get {
               return name;
            }
            set {
               this.name = value;
            }
         }
         //public String GetName() {
         //   return name;
         //}
         //public void SetName(String name) {
         //   this.name = name;
         //}
      public void TestNonMatchingAnnotationExample() {
         bool failure = false;
         try {
            new Scanner(NonMatchingAnnotationExample.class);
         }catch(Exception e) {
            e.printStackTrace();
            failure = true;
         }
         assertTrue("Failure should occur when annotations do not match", failure);
      }
      public void TestDuplicateExample() {
         Scanner scanner = new Scanner(DuplicateExample.class);
         Support support = new Support();
         Strategy strategy = new TreeStrategy();
         Style style = new DefaultStyle();
         Context context = new Source(strategy, support, style);
         LabelMap attributes = scanner.getAttributes(context);
         LabelMap elements = scanner.getElements(context);
         assertEquals(attributes.get("id").getType(), int.class);
         assertEquals(elements.get("id").getType(), int.class);
      }
      public void TestScanner() {
         Scanner scanner = new Scanner(OrderItem.class);
         Support support = new Support();
         Strategy strategy = new TreeStrategy();
         Style style = new DefaultStyle();
         Context context = new Source(strategy, support, style);
         LabelMap attributes = scanner.getAttributes(context);
         LabelMap elements = scanner.getElements(context);
         assertEquals(attributes.get("price").getType(), double.class);
         assertEquals(elements.get("customer").getType(), Customer.class);
      }
   }
}
