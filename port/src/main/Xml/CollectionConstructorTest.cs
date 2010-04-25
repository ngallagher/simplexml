#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class CollectionConstructorTest : TestCase {
      private const String LIST =
      "<example>"+
      "  <entry name='a' value='1'/>"+
      "  <entry name='b' value='2'/>"+
      "</example>";
      private const String MAP =
      "<example>"+
      "  <element key='A'>"+
      "     <entry name='a' value='1'/>"+
      "  </element>"+
      "  <element key='B'>"+
      "     <entry name='b' value='2'/>"+
      "  </element>"+
      "</example>";
      private const String COMPOSITE =
      "<composite>"+
      "  <example class='SimpleFramework.Xml.Core.CollectionConstructorTest$ExtendedCollectionConstructor'>"+
      "    <entry name='a' value='1'/>"+
      "    <entry name='b' value='2'/>"+
      "  </example>"+
      "</composite>";
      @Root(name="example")
      private static class MapConstructor {
         @ElementMap(name="list", entry="element", key="key", attribute=true, inline=true)
         private readonly Map<String, Entry> map;
         public MapConstructor(@ElementMap(name="list", entry="element", key="key", attribute=true, inline=true) Map<String, Entry> map) {
            this.map = map;
         }
         public int Size() {
            return map.Size();
         }
      }
      @Root(name="example")
      private static class CollectionConstructor {
         @ElementList(name="list", inline=true)
         private readonly Vector<Entry> vector;
         public CollectionConstructor(@ElementList(name="list", inline=true) Vector<Entry> vector) {
            this.vector = vector;
         }
         public int Size() {
            return vector.Size();
         }
      }
      @Root(name="entry")
      private static class Entry {
         @Attribute(name="name")
         private readonly String name;
         @Attribute(name="value")
         private readonly String value;
         public Entry(@Attribute(name="name") String name, @Attribute(name="value") String value) {
            this.name = name;
            this.value = value;
         }
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
      @Root(name="example")
      private static class ExtendedCollectionConstructor : CollectionConstructor {
         public ExtendedCollectionConstructor(@ElementList(name="list", inline=true) Vector<Entry> vector) {
            super(vector);
         }
      }
      @Root(name="composite")
      private static class CollectionConstructorComposite {
         @Element(name="example")
         private CollectionConstructor collection;
         public CollectionConstructor Collection {
            get {
               return collection;
            }
         }
         //public CollectionConstructor GetCollection() {
         //   return collection;
         //}
      public void TestCollectionConstructor() {
         Persister persister = new Persister();
         CollectionConstructor constructor = persister.read(CollectionConstructor.class, LIST);
         assertEquals(constructor.Size(), 2);
      }
      public void TestMapConstructor() {
         Persister persister = new Persister();
         MapConstructor constructor = persister.read(MapConstructor.class, MAP);
         assertEquals(constructor.Size(), 2);
      }
      public void TestCollectionConstructorComposite() {
         Persister persister = new Persister();
         CollectionConstructorComposite composite = persister.read(CollectionConstructorComposite.class, COMPOSITE);
         assertEquals(composite.Collection.getClass(), ExtendedCollectionConstructor.class);
         assertEquals(composite.Collection.Size(), 2);
      }
   }
}
