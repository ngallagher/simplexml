#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class DefaultAnnotationTest : TestCase {
      private const String SOURCE =
      "<orderList id='100' array='a, b, c, d'>"+
      "   <value>Some Example Value</value>"+
      "   <orders>"+
      "      <orderItem>"+
      "         <name>IR1234</name>" +
      "         <value>2</value>"+
      "         <price>7.4</price>"+
      "         <customer id='1'>"+
      "            <name>John Doe</name>"+
      "            <address>Sin City</address>"+
      "         </customer>"+
      "      </orderItem>"+
      "      <orderItem>"+
      "         <name>TZ346</name>" +
      "         <value>2</value>"+
      "         <price>10.4</price>"+
      "         <customer id='2'>"+
      "            <name>Jane Doe</name>"+
      "           <address>Sesame Street</address>"+
      "         </customer>"+
      "      </orderItem>"+
      "   </orders>"+
      "</orderList>";
      private const String MISMATCH =
      "<typeMisMatch/>";
      [Root]
      [Default(DefaultType.PROPERTY)]
      private static class OrderList {
         private List<OrderItem> list;
         private String[] array;
         private String secret;
         private @Attribute int id;
         private @Element String value;
         [Transient]
         public String Secret {
            get {
               return secret;
            }
            set {
               this.secret = value;
            }
         }
         //public String GetSecret() {
         //   return secret;
         //}
         //public void SetSecret(String secret) {
         //   this.secret = secret;
         //}
         public String[] Array {
            get {
               return array;
            }
            set {
               this.array = value;
            }
         }
         //public String[] GetArray() {
         //   return array;
         //}
         //public void SetArray(String[] array) {
         //   this.array = array;
         //}
            return list;
         }
         public List<OrderItem> Orders {
            set {
               this.list = value;
            }
         }
         //public void SetOrders(List<OrderItem> list) {
         //   this.list = list;
         //}
      [Root]
      [Default(DefaultType.FIELD)]
      private static class OrderItem {
         private Customer customer;
         private String name;
         private int value;
         private double price;
         private @Transient String hidden;
         private @Transient String secret;
      }
      [Root]
      [Default(DefaultType.FIELD)]
      private static class Customer {
         private @Attribute int id;
         private String name;
         private String address;
         public Customer(@Element(name="name", required=false) String name) {
            this.name = name;
         }
      }
      [Root]
      [Default(DefaultType.PROPERTY)]
      private static class TypeMisMatch {
         public String name;
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
      public void TestTypeMisMatch() {
         Persister persister = new Persister();
         bool failure = false;
         try {
            TypeMisMatch type = persister.read(TypeMisMatch.class, SOURCE);
            AssertNull(type);
         }catch(Exception e){
            e.printStackTrace();
            failure = true;
         }
         assertTrue(failure);
      }
      public void TestDefault() {
         MethodScanner methodScanner = new MethodScanner(OrderList.class, DefaultType.PROPERTY);
         Map<String, Contact> map = new HashMap<String, Contact>();
         for(Contact contact : methodScanner) {
            map.put(contact.Name, contact);
         }
         AssertEquals(map.get("orders").getClass(), MethodContact.class);
         AssertEquals(map.get("orders").getType(), List.class);
         AssertEquals(map.get("orders").getAnnotation().annotationType(), ElementList.class);
         Scanner scanner = new Scanner(OrderList.class);
         Support support = new Support();
         Strategy strategy = new TreeStrategy();
         Style style = new DefaultStyle();
         Context context = new Source(strategy, support, style);
         LabelMap attributes = scanner.getAttributes(context);
         LabelMap elements = scanner.getElements(context);
         AssertEquals(elements.get("orders").getType(), List.class);
         AssertEquals(elements.get("orders").getContact().getAnnotation().annotationType(), ElementList.class);
         AssertEquals(attributes.get("array").getType(), String[].class);
         AssertEquals(attributes.get("array").getContact().getAnnotation().annotationType(), Attribute.class);
         Persister persister = new Persister();
         OrderList list = persister.read(OrderList.class, SOURCE);
         AssertEquals(list.Array[0], "a");
         AssertEquals(list.Array[1], "b");
         AssertEquals(list.Array[2], "c");
         AssertEquals(list.Array[3], "d");
         AssertEquals(list.id, 100);
         AssertEquals(list.value, "Some Example Value");
         AssertEquals(list.Orders.get(0).name, "IR1234");
         AssertEquals(list.Orders.get(0).hidden, null);
         AssertEquals(list.Orders.get(0).secret, null);
         AssertEquals(list.Orders.get(0).value, 2);
         AssertEquals(list.Orders.get(0).price, 7.4);
         AssertEquals(list.Orders.get(0).customer.id, 1);
         AssertEquals(list.Orders.get(0).customer.name, "John Doe");
         AssertEquals(list.Orders.get(0).customer.address, "Sin City");
         AssertEquals(list.Orders.get(1).name, "TZ346");
         AssertEquals(list.Orders.get(0).hidden, null);
         AssertEquals(list.Orders.get(0).secret, null);
         AssertEquals(list.Orders.get(1).value, 2);
         AssertEquals(list.Orders.get(1).price, 10.4);
         AssertEquals(list.Orders.get(1).customer.id, 2);
         AssertEquals(list.Orders.get(1).customer.name, "Jane Doe");
         AssertEquals(list.Orders.get(1).customer.address, "Sesame Street");
      }
   }
}
