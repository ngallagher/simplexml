#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class DictionaryTest : ValidationTestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list>\n"+
      "      <property name='1'>\n"+
      "         <text>one</text>  \n\r"+
      "      </property>\n\r"+
      "      <property name='2'>\n"+
      "         <text>two</text>  \n\r"+
      "      </property>\n"+
      "      <property name='3'>\n"+
      "         <text>three</text>  \n\r"+
      "      </property>\n"+
      "   </list>\n"+
      "</test>";
      @Root(name="property")
      private static class Property : Entry {
         @Element(name="text")
         private String text;
         @Attribute
         private String name;
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
      @Root(name="test")
      private static class PropertySet : Iterable<Property> {
         @ElementList(name="list", type=Property.class)
         private Dictionary<Property> list;
         @Attribute(name="name")
         private String name;
         public Iterator<Property> Iterator() {
            return list.Iterator();
         }
         public Property Get(String name) {
            return list.Get(name);
         }
         public int Size() {
            return list.Size();
         }
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestDictionary() {
         PropertySet set = (PropertySet) serializer.read(PropertySet.class, LIST);
         assertEquals(3, set.Size());
         assertEquals("one", set.Get("1").text);
         assertEquals("two", set.Get("2").text);
         assertEquals("three", set.Get("3").text);
         validate(set, serializer);
      }
   }
}
