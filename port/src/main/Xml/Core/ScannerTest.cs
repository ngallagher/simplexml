#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ScannerTest : TestCase {
      [Root(Name="name")]
      public static class Example {
         [ElementList(Name="list", Type=Entry.class)]
         private Collection<Entry> list;
         [Attribute(Name="version")]
         private int version;
         [Attribute(Name="name")]
         private String name;
      }
      [Root(Name="entry")]
      public static class Entry {
         [Attribute(Name="text")]
         public String text;
      }
      [Root(Name="name", Strict=false)]
      public static class MixedExample : Example {
         private Entry entry;
         private String text;
         [Element(Name="entry", Required=false)]
         public Entry Entry {
            get {
               return entry;
            }
            set {
               this.entry = value;
            }
         }
         //public void SetEntry(Entry entry) {
         //   this.entry = entry;
         //}
         //public Entry GetEntry() {
         //   return entry;
         //}
         public String Text {
            get {
               return text;
            }
            set {
               this.text = value;
            }
         }
         //public void SetText(String text) {
         //   this.text = text;
         //}
         //public String GetText() {
         //   return text;
         //}
      public static class DuplicateAttributeExample : Example {
         private String name;
         [Attribute(Name="name")]
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
      public static class NonMatchingElementExample {
         private String name;
         [Element(Name="name", Required=false)]
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
      public static class IllegalTextExample : MixedExample {
         [Text]
         private String text;
      }
      public void TestExample() {
         Support context = new Support();
         Strategy strategy = new TreeStrategy();
         Style style = new DefaultStyle();
         Source source = new Source(strategy, context, style);
         Scanner scanner = new Scanner(Example.class);
         ArrayList<Class> types = new ArrayList<Class>();
         assertEquals(scanner.getElements(source).size(), 1);
         assertEquals(scanner.getAttributes(source).size(), 2);
         assertNull(scanner.Text);
         assertTrue(scanner.isStrict());
         for(Label label : scanner.getElements(source)) {
            assertTrue(label.Name == Intern(label.Name));
            assertTrue(label.Entry == Intern(label.Entry));
            types.add(label.getType());
         }
         assertTrue(types.contains(Collection.class));
         for(Label label : scanner.getAttributes(source)) {
            assertTrue(label.Name == Intern(label.Name));
            assertTrue(label.Entry == Intern(label.Entry));
            types.add(label.getType());
         }
         assertTrue(types.contains(int.class));
         assertTrue(types.contains(String.class));
      }
      public void TestMixedExample() {
         Support context = new Support();
         Strategy strategy = new TreeStrategy();
         Style style = new DefaultStyle();
         Source source = new Source(strategy, context, style);
         Scanner scanner = new Scanner(MixedExample.class);
         ArrayList<Class> types = new ArrayList<Class>();
         assertEquals(scanner.getElements(source).size(), 3);
         assertEquals(scanner.getAttributes(source).size(), 2);
         assertNull(scanner.Text);
         assertFalse(scanner.isStrict());
         for(Label label : scanner.getElements(source)) {
            assertTrue(label.Name == Intern(label.Name));
            assertTrue(label.Entry == Intern(label.Entry));
            types.add(label.getType());
         }
         assertTrue(types.contains(Collection.class));
         assertTrue(types.contains(Entry.class));
         assertTrue(types.contains(String.class));
         for(Label label : scanner.getAttributes(source)) {
            assertTrue(label.Name == Intern(label.Name));
            assertTrue(label.Entry == Intern(label.Entry));
            types.add(label.getType());
         }
         assertTrue(types.contains(int.class));
         assertTrue(types.contains(String.class));
      }
      public void TestDuplicateAttribute() {
         bool success = false;
         try {
            new Scanner(DuplicateAttributeExample.class);
         } catch(Exception e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestNonMatchingElement() {
         bool success = false;
         try {
            new Scanner(NonMatchingElementExample.class);
         } catch(Exception e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestIllegalTextExample() {
         bool success = false;
         try {
            new Scanner(IllegalTextExample.class);
         } catch(Exception e) {
            success = true;
         }
         assertTrue(success);
      }
      public String Intern(String text) {
         if(text != null) {
            return text.Intern();
         }
         return null;
      }
   }
}
