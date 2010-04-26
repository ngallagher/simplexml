#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CompositeTest : ValidationTestCase {
      private const String SOURCE =
      "<compositeObject>" +
      "   <interfaceEntry name='name.1' class='SimpleFramework.Xml.Core.CompositeTest$CompositeEntry'>value.1</interfaceEntry>" +
      "   <objectEntry name='name.2' class='SimpleFramework.Xml.Core.CompositeTest$CompositeEntry'>value.2</objectEntry>" +
      "   <interfaceList>" +
      "      <interfaceEntry name='a' class='SimpleFramework.Xml.Core.CompositeTest$CompositeEntry'>a</interfaceEntry>" +
      "      <interfaceEntry name='b' class='SimpleFramework.Xml.Core.CompositeTest$CompositeEntry'>b</interfaceEntry>" +
      "   </interfaceList>" +
      "   <objectList>" +
      "      <objectEntry name='a' class='SimpleFramework.Xml.Core.CompositeTest$CompositeEntry'>a</objectEntry>" +
      "      <objectEntry name='b' class='SimpleFramework.Xml.Core.CompositeTest$CompositeEntry'>b</objectEntry>" +
      "   </objectList>" +
      "</compositeObject>";
      private static interface Entry {
         public abstract String Name {
            get;
         }
         //public String GetName();
      }
      [Root]
      public static class CompositeEntry : Entry {
         [Attribute]
         private String name;
         [Text]
         private String value;
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
      [Root]
      private static class CompositeObject {
         [ElementList]
         private List<Entry> interfaceList;
         [ElementList]
         private List<Object> objectList;
         [Element]
         private Entry interfaceEntry;
         [Element]
         private Object objectEntry;
         public CompositeEntry Interface {
            get {
               return (CompositeEntry) interfaceEntry;
            }
         }
         //public CompositeEntry GetInterface() {
         //   return (CompositeEntry) interfaceEntry;
         //}
            return (CompositeEntry) objectEntry;
         }
         public List<Entry> InterfaceList {
            get {
               return interfaceList;
            }
         }
         //public List<Entry> GetInterfaceList() {
         //   return interfaceList;
         //}
            return objectList;
         }
      }
      public void TestComposite() {
         Persister persister = new Persister();
         CompositeObject object = persister.read(CompositeObject.class, SOURCE);
         CompositeEntry objectEntry = object.Object;
         CompositeEntry interfaceEntry = object.Interface;
         AssertEquals(interfaceEntry.Name, "name.1");
         AssertEquals(interfaceEntry.Name, "name.1");
         AssertEquals(objectEntry.Name, "name.2");
         AssertEquals(objectEntry.GetValue(), "value.2");
         List<Entry> interfaceList = object.InterfaceList;
         List<Object> objectList = object.ObjectList;
         AssertEquals(interfaceList.get(0).Name, "a");
         AssertEquals(interfaceList.get(0).GetValue(), "a");
         AssertEquals(interfaceList.get(1).Name, "b");
         AssertEquals(interfaceList.get(1).GetValue(), "b");
         AssertEquals(((Entry)objectList.get(0)).Name, "a");
         AssertEquals(((Entry)objectList.get(0)).Name, "a");
         AssertEquals(((Entry)objectList.get(1)).Name, "b");
         AssertEquals(((Entry)objectList.get(1)).Name, "b");
      }
   }
}
