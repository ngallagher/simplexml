#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CollectionTest : ValidationTestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String ARRAY_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list class='java.util.ArrayList'>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String HASH_SET =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list class='java.util.HashSet'>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String TREE_SET =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list class='java.util.TreeSet'>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String ABSTRACT_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list class='SimpleFramework.Xml.Core.CollectionTest$AbstractList'>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String NOT_A_COLLECTION =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list class='java.util.Hashtable'>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String MISSING_COLLECTION =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list class='example.MyCollection'>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String EXTENDED_ENTRY_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list>\n"+
      "      <extended-entry id='1' class='SimpleFramework.Xml.Core.CollectionTest$ExtendedEntry'>\n"+
      "         <text>one</text>  \n\r"+
      "         <description>this is an extended entry</description>\n\r"+
      "      </extended-entry>\n\r"+
      "      <extended-entry id='2' class='SimpleFramework.Xml.Core.CollectionTest$ExtendedEntry'>\n"+
      "         <text>two</text>  \n\r"+
      "         <description>this is the second one</description>\n"+
      "      </extended-entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</test>";
      private const String TYPE_FROM_FIELD_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<typeFromFieldList name='example'>\n"+
      "   <list>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</typeFromFieldList>";
      private const String TYPE_FROM_METHOD_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<typeFromMethodList name='example'>\n"+
      "   <list>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</typeFromMethodList>";
      private const String PRIMITIVE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<primitiveCollection name='example'>\n"+
      "   <list>\n"+
      "      <text>one</text>  \n\r"+
      "      <text>two</text>  \n\r"+
      "      <text>three</text>  \n\r"+
      "   </list>\n"+
      "</primitiveCollection>";
      private const String COMPOSITE_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<compositeCollection name='example'>\n"+
      "   <list>\n"+
      "      <entry id='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </entry>\n\r"+
      "   </list>\n"+
      "</compositeCollection>";
      private const String PRIMITIVE_DEFAULT_LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<primitiveDefaultCollection name='example'>\n"+
      "   <list>\n"+
      "      <string>one</string>  \n\r"+
      "      <string>two</string>  \n\r"+
      "      <string>three</string>  \n\r"+
      "   </list>\n"+
      "</primitiveDefaultCollection>";
      [Root(Name="entry")]
      private static class Entry : Comparable {
         [Attribute(Name="id")]
         private int id;
         [Element(Name="text")]
         private String text;
         public int CompareTo(Object entry) {
            return id - ((Entry)entry).id;
         }
      }
      [Root(Name="extended-entry")]
      private static class ExtendedEntry : Entry {
         [Element(Name="description")]
         private String description;
      }
      [Root(Name="test")]
      private static class EntrySet : Iterable<Entry> {
         [ElementList(Name="list", Type=Entry.class)]
         private Set<Entry> list;
         [Attribute(Name="name")]
         private String name;
         public Iterator<Entry> Iterator() {
            return list.Iterator();
         }
      }
      [Root(Name="test")]
      private static class EntrySortedSet : Iterable<Entry> {
         [ElementList(Name="list", Type=Entry.class)]
         private SortedSet<Entry> list;
         [Attribute(Name="name")]
         private String name;
         public Iterator<Entry> Iterator() {
            return list.Iterator();
         }
      }
      [Root(Name="test")]
      private static class EntryList : Iterable<Entry> {
         [ElementList(Name="list", Type=Entry.class)]
         private List<Entry> list;
         [Attribute(Name="name")]
         private String name;
         public Iterator<Entry> Iterator() {
            return list.Iterator();
         }
      }
      [Root(Name="test")]
      public static class InvalidList {
         [ElementList(Name="list", Type=Entry.class)]
         private String list;
         [Attribute(Name="name")]
         private String name;
      }
      [Root(Name="test")]
      public static class UnknownCollectionList : Iterable<Entry> {
         [ElementList(Name="list", Type=Entry.class)]
         private UnknownCollection<Entry> list;
         [Attribute(Name="name")]
         private String name;
         public Iterator<Entry> Iterator() {
            return list.Iterator();
         }
      }
      [Root]
      private static class TypeFromFieldList : Iterable<Entry> {
         [ElementList]
         private List<Entry> list;
         [Attribute]
         private String name;
         public Iterator<Entry> Iterator() {
            return list.Iterator();
         }
      }
      [Root]
      private static class TypeFromMethodList : Iterable<Entry> {
         private List<Entry> list;
         [Attribute]
         private String name;
         [ElementList]
         public List<Entry> List {
            get {
               return list;
            }
            set {
               this.list = value;
            }
         }
         //public List<Entry> GetList() {
         //   return list;
         //}
         //public void SetList(List<Entry> list) {
         //   this.list = list;
         //}
            return list.Iterator();
         }
      }
      [Root]
      private static class PrimitiveCollection : Iterable<String> {
         [ElementList(Name="list", Type=String.class, Entry="text")]
         private List<String> list;
         [Attribute(Name="name")]
         private String name;
         public Iterator<String> Iterator() {
            return list.Iterator();
         }
      }
      [Root]
      private static class CompositeCollection : Iterable<Entry> {
         [ElementList(Name="list", Entry="text")]
         private List<Entry> list;
         [Attribute(Name="name")]
         private String name;
         public Iterator<Entry> Iterator() {
            return list.Iterator();
         }
      }
      [Root]
      private static class PrimitiveDefaultCollection : Iterable<String> {
         [ElementList]
         private List<String> list;
         [Attribute]
         private String name;
         public Iterator<String> Iterator() {
            return list.Iterator();
         }
      }
      private abstract class AbstractList<T> : ArrayList<T> {
         public AbstractList() {
            super();
         }
      }
      private abstract class UnknownCollection<T> : Collection<T> {
         public UnknownCollection() {
            super();
         }
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestSet() {
         EntrySet set = serializer.Read(EntrySet.class, LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
      }
      public void TestSortedSet() {
         EntrySortedSet set = serializer.Read(EntrySortedSet.class, LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(set, serializer);
      }
      public void TestList() {
         EntryList set = serializer.Read(EntryList.class, LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(set, serializer);
      }
      public void TestHashSet() {
         EntrySet set = serializer.Read(EntrySet.class, HASH_SET);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(set, serializer);
      }
      public void TestTreeSet() {
         EntrySortedSet set = serializer.Read(EntrySortedSet.class, TREE_SET);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(set, serializer);
      }
      public void TestArrayList() {
         EntryList list = serializer.Read(EntryList.class, ARRAY_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : list) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(list, serializer);
      }
      public void TestSortedSetToSet() {
         EntrySet set = serializer.Read(EntrySet.class, TREE_SET);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
      }
      public void TestExtendedEntry() {
         EntrySet set = serializer.Read(EntrySet.class, EXTENDED_ENTRY_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         StringWriter out = new StringWriter();
         serializer.Write(set, out);
         serializer.Write(set, System.err);
         EntrySet other = serializer.Read(EntrySet.class, out.toString());
         for(Entry entry : set) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 2);
         AssertEquals(two, 2);
         AssertEquals(three, 2);
         serializer.Write(other, System.err);
      }
      public void TestTypeFromFieldList() {
         TypeFromFieldList list = serializer.Read(TypeFromFieldList.class, TYPE_FROM_FIELD_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : list) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(list, serializer);
      }
      public void TestTypeFromMethodList() {
         TypeFromMethodList list = serializer.Read(TypeFromMethodList.class, TYPE_FROM_METHOD_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : list) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(list, serializer);
      }
      public void TestPrimitiveCollection() {
         PrimitiveCollection list = serializer.Read(PrimitiveCollection.class, PRIMITIVE_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(String entry : list) {
            if(entry.equals("one")) {
               one++;
            }
            if(entry.equals("two")) {
               two++;
            }
            if(entry.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(list, serializer);
      }
      // XXX This test needs to inline the entry= attribute so that
      // XXX we can use it to name the inserted entries
      public void TestCompositeCollection() {
         CompositeCollection list = serializer.Read(CompositeCollection.class, COMPOSITE_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(Entry entry : list) {
            if(entry.id == 1 && entry.text.equals("one")) {
               one++;
            }
            if(entry.id == 2 && entry.text.equals("two")) {
               two++;
            }
            if(entry.id == 3 && entry.text.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(list, serializer);
      }
      public void TestPrimitiveDefaultCollection() {
         PrimitiveDefaultCollection list = serializer.Read(PrimitiveDefaultCollection.class, PRIMITIVE_DEFAULT_LIST);
         int one = 0;
         int two = 0;
         int three = 0;
         for(String entry : list) {
            if(entry.equals("one")) {
               one++;
            }
            if(entry.equals("two")) {
               two++;
            }
            if(entry.equals("three")) {
               three++;
            }
         }
         AssertEquals(one, 1);
         AssertEquals(two, 1);
         AssertEquals(three, 1);
         Validate(list, serializer);
      }
      public void TestSetToSortedSet() {
         bool success = false;
         try {
            EntrySortedSet set = serializer.Read(EntrySortedSet.class, HASH_SET);
         } catch(InstantiationException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestListToSet() {
         bool success = false;
         try {
            EntrySet set = serializer.Read(EntrySet.class, ARRAY_LIST);
         } catch(InstantiationException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestInvalidList() {
         bool success = false;
         try {
            InvalidList set = serializer.Read(InvalidList.class, LIST);
         } catch(InstantiationException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestUnknownCollectionList() {
         bool success = false;
         try {
            UnknownCollectionList set = serializer.Read(UnknownCollectionList.class, LIST);
         } catch(InstantiationException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestAbstractList() {
         bool success = false;
         try {
            EntryList set = serializer.Read(EntryList.class, ABSTRACT_LIST);
         } catch(InstantiationException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestNotACollection() {
         bool success = false;
         try {
            EntryList set = serializer.Read(EntryList.class, NOT_A_COLLECTION);
         } catch(InstantiationException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestMissingCollection() {
         bool success = false;
         try {
            EntrySet set = serializer.Read(EntrySet.class, MISSING_COLLECTION);
         } catch(ClassNotFoundException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
   }
}
