#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class PrimitiveKeyTest : TestCase {
      private static class MockElementMap : ElementMap {
         private bool attribute;
         private bool data;
         private String entry;
         private bool inline;
         private String key;
         private Class keyType;
         private String name;
         private bool required;
         private String value;
         private Class valueType;
         public MockElementMap(
            bool attribute,
            bool data,
            String entry,
            bool inline,
            String key,
            Class keyType,
            String name,
            bool required,
            String value,
            Class valueType)
         {
            this.attribute = attribute;
            this.data = data;
            this.entry = entry;
            this.inline = inline;
            this.key = key;
            this.keyType = keyType;
            this.name = name;
            this.required = required;
            this.value = value;
            this.valueType = valueType;
         }
         public bool Empty() {
            return true;
         }
         public bool Attribute() {
            return attribute;
         }
         public bool Data() {
            return data;
         }
         public String Entry() {
            return entry;
         }
         public bool Inline() {
            return inline;
         }
         public String Key() {
            return key;
         }
         public Class KeyType() {
            return keyType;
         }
         public String Name() {
            return name;
         }
         public bool Required() {
            return required;
         }
         public String Value() {
            return value;
         }
         public double Since() {
            return 1.0;
         }
         public Class ValueType() {
            return valueType;
         }
         public Class<? : Annotation> annotationType() {
            return ElementMap.class;
         }
      }
      private static class PrimitiveType {
         private MockElementMap map;
         private String string;
         private int number;
         private byte octet;
         public PrimitiveType(MockElementMap map) {
            this.map = map;
         }
         public Contact String {
            get {
               return new FieldContact(PrimitiveType.class.getDeclaredField("string"), map);
            }
         }
         //public Contact GetString() {
         //   return new FieldContact(PrimitiveType.class.getDeclaredField("string"), map);
         //}
            return new FieldContact(PrimitiveType.class.getDeclaredField("number"), map);
         }
         public Contact Octet {
            get {
               return new FieldContact(PrimitiveType.class.getDeclaredField("octet"), map);
            }
         }
         //public Contact GetOctet() {
         //   return new FieldContact(PrimitiveType.class.getDeclaredField("octet"), map);
         //}
      public void TestInlineString() {
      {
         Source source = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         MockElementMap map = new MockElementMap(true, // attribute
                                                 false, // data
                                                 "entry", // entry
                                                 true,  // inline
                                                 "key", // key
                                                 String.class, // keyType
                                                 "name", // name
                                                 true, // required
                                                 "value", // value
                                                 String.class); // valueType
         PrimitiveType type = new PrimitiveType(map);
         Contact string = type.String;
         Entry entry = new Entry(string, map);
         PrimitiveKey value = new PrimitiveKey(source, entry, new ClassType(String.class));
         OutputNode node = NodeBuilder.write(new PrintWriter(System.out));
         value.write(node.getChild("inlineString"), "example");
         node.commit();
      }
      public void TestNotInlineString() {
      {
         Source source = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         MockElementMap map = new MockElementMap(false, // attribute
                                                 false, // data
                                                 "entry", // entry
                                                 true,  // inline
                                                 "key", // key
                                                 String.class, // keyType
                                                 "name", // name
                                                 true, // required
                                                 "value", // value
                                                 String.class); // valueType
         PrimitiveType type = new PrimitiveType(map);
         Contact string = type.String;
         Entry entry = new Entry(string, map);
         PrimitiveKey value = new PrimitiveKey(source, entry, new ClassType(String.class));
         OutputNode node = NodeBuilder.write(new PrintWriter(System.out));
         value.write(node.getChild("notInlineString"), "example");
         node.commit();
      }
      public void TestNoAttributeString() {
      {
         Source source = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         MockElementMap map = new MockElementMap(false, // attribute
                                                 false, // data
                                                 "entry", // entry
                                                 true,  // inline
                                                 "", // key
                                                 String.class, // keyType
                                                 "name", // name
                                                 true, // required
                                                 "value", // value
                                                 String.class); // valueType
         PrimitiveType type = new PrimitiveType(map);
         Contact string = type.String;
         Entry entry = new Entry(string, map);
         PrimitiveKey value = new PrimitiveKey(source, entry, new ClassType(String.class));
         OutputNode node = NodeBuilder.write(new PrintWriter(System.out));
         value.write(node.getChild("noAttributeString"), "example");
         node.commit();
      }
      public void TestAttributeNoKeyString() {
      {
         Source source = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         MockElementMap map = new MockElementMap(true, // attribute
                                                 false, // data
                                                 "entry", // entry
                                                 true,  // inline
                                                 "", // key
                                                 String.class, // keyType
                                                 "name", // name
                                                 true, // required
                                                 "value", // value
                                                 String.class); // valueType
         PrimitiveType type = new PrimitiveType(map);
         Contact string = type.String;
         Entry entry = new Entry(string, map);
         PrimitiveKey value = new PrimitiveKey(source, entry, new ClassType(String.class));
         OutputNode node = NodeBuilder.write(new PrintWriter(System.out));
         value.write(node.getChild("attributeNoKeyString"), "example");
         node.commit();
      }
   }
}
