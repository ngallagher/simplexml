#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MethodScannerTest : TestCase {
      [Root(Name="name")]
      public static class Example {
         private int version;
         private String name;
         [Element(Name="version")]
         public int Version {
            get {
               return version;
            }
            set {
               this.version = value;
            }
         }
         //public void SetVersion(int version) {
         //   this.version = version;
         //}
         //public int GetVersion() {
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
      public static class IllegalOverload : Example {
         private int name;
         [Attribute(Name="name")]
         public int Name {
            set {
               this.name = value;
            }
         }
         //public void SetName(int name) {
         //   this.name = name;
         //}
      public static class NonMatchingMethods : Example {
         private int type;
         [Attribute(Name="type")]
         public int Type {
            set {
               this.type = value;
            }
         }
         //public void SetType(int type) {
         //   this.type = type;
         //}
      public static class NotBeanMethod : Example {
         private String type;
         [Element(Name="type")]
         public String Type {
            set {
               this.type = value;
            }
         }
         //public void SetType(String type) {
         //   this.type = type;
         //}
         public String ReadType() {
            return type;
         }
      }
      public static class TextMethod : Example {
         private long length;
         [Text]
         public long Length {
            get {
               return length;
            }
            set {
               this.length = value;
            }
         }
         //public void SetLength(long length) {
         //   this.length = length;
         //}
         //public long GetLength() {
         //   return length;
         //}
      public static class CollectionMethod : TextMethod {
         private Collection list;
         [ElementList(Name="list", Type=Example.class)]
         public Collection List {
            get {
               return list;
            }
            set {
               this.list = value;
            }
         }
         //public void SetList(Collection list) {
         //   this.list = list;
         //}
         //public Collection GetList() {
         //   return list;
         //}
      public void TestExample() {
         MethodScanner scanner = new MethodScanner(Example.class);
         ArrayList<Class> list = new ArrayList<Class>();
         for(Contact contact : scanner) {
            list.add(contact.getType());
         }
         assertEquals(scanner.size(), 2);
         assertTrue(list.contains(String.class));
         assertTrue(list.contains(int.class));
      }
      public void TestIllegalOverload() {
         bool success = false;
         try {
            new MethodScanner(IllegalOverload.class);
         }catch(Exception e){
            success = true;
         }
         assertTrue(success);
      }
      public void TestNonMatchingMethods() {
         bool success = false;
         try {
            new MethodScanner(NonMatchingMethods.class);
         }catch(Exception e){
            success = true;
         }
         assertTrue(success);
      }
      public void TestNotBeanMethod() {
         bool success = false;
         try {
            new MethodScanner(NotBeanMethod.class);
         }catch(Exception e){
            success = true;
         }
         assertTrue(success);
      }
      public void TestText() {
         MethodScanner scanner = new MethodScanner(TextMethod.class);
         ArrayList<Class> list = new ArrayList<Class>();
         for(Contact contact : scanner) {
            list.add(contact.getType());
         }
         assertEquals(scanner.size(), 3);
         assertTrue(list.contains(String.class));
         assertTrue(list.contains(int.class));
         assertTrue(list.contains(long.class));
      }
      public void TestCollection() {
         MethodScanner scanner = new MethodScanner(CollectionMethod.class);
         ArrayList<Class> list = new ArrayList<Class>();
         for(Contact contact : scanner) {
            list.add(contact.getType());
         }
         assertEquals(scanner.size(), 4);
         assertTrue(list.contains(String.class));
         assertTrue(list.contains(int.class));
         assertTrue(list.contains(long.class));
         assertTrue(list.contains(Collection.class));
      }
   }
}
