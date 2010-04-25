#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MethodContactTest : TestCase {
      @Root(name="name")
      public static class Example {
         private Collection<Entry> list;
         private float version;
         private String name;
         @ElementList(name="list", type=Entry.class)
         public Collection<Entry> List {
            get {
               return list;
            }
            set {
               this.list = value;
            }
         }
         //public void SetList(Collection<Entry> list) {
         //   this.list = list;
         //}
         //public Collection<Entry> GetList() {
         //   return list;
         //}
         public float Version {
            get {
               return version;
            }
            set {
               this.version = value;
            }
         }
         //public void SetVersion(float version) {
         //   this.version = version;
         //}
         //public float GetVersion() {
         //   return version;
         //}
         public String Name {
            get {
               return name;
            }
            set {
               this.name = value;
            }
         }
         //public void SetName(String name) {
         //   this.name = name;
         //}
         //public String GetName() {
         //   return name;
         //}
      @Root(name="entry")
      public static class Entry {
         @Attribute(name="text")
         public String text;
      }
      public void TestContact() {
         MethodScanner scanner = new MethodScanner(Example.class);
         ArrayList<Class> types = new ArrayList<Class>();
         for(Contact contact : scanner) {
            types.add(contact.getType());
         }
         assertEquals(scanner.size(), 3);
         assertTrue(types.contains(String.class));
         assertTrue(types.contains(float.class));
         assertTrue(types.contains(Collection.class));
         ContactList contacts = scanner;
         Contact version = GetContact(float.class, contacts);
         Example example = new Example();
         version.set(example, 1.2f);
         assertEquals(example.version, 1.2f);
         assertNull(example.name);
         assertNull(example.list);
         Contact name = GetContact(String.class, contacts);
         name.set(example, "name");
         assertEquals(example.version, 1.2f);
         assertEquals(example.name, "name");
         assertNull(example.list);
         Contact list = GetContact(Collection.class, contacts);
         list.set(example, types);
         assertEquals(example.version, 1.2f);
         assertEquals(example.name, "name");
         assertEquals(example.list, types);
      }
      public Contact GetContact(Class type, ContactList from) {
         for(Contact contact : from) {
            if(type == contact.getType()) {
               return contact;
            }
         }
         return null;
      }
   }
}
