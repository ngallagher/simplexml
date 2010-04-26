#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ExceptionTest : TestCase {
      private const String VALID =
      "<?xml version=\"1.0\"?>\n"+
      "<root name='example'>\n"+
      "   <text>some text element</text>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      private const String NO_NAME_ATTRIBUTE =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <text>some text element</text>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      private const String NO_TEXT_ELEMENT =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      private const String EXTRA_ELEMENT =
      "<?xml version=\"1.0\"?>\n"+
      "<root name='example'>\n"+
      "   <error>this is an extra element</error>\n"+
      "   <text>some text element</text>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      private const String EXTRA_ATTRIBUTE =
      "<?xml version=\"1.0\"?>\n"+
      "<root error='some extra attribute' name='example'>\n"+
      "   <text>some text element</text>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      private const String MISSING_ROOT =
      "<?xml version=\"1.0\"?>\n"+
      "<root name='example'>\n"+
      "   <text>some text element</text>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34'>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56'>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "   <error-list class='java.util.ArrayList'>\n"+
      "      <entry id='10'>\n"+
      "         <text>some text</text>\n"+
      "      </entry>\n"+
      "   </error-list>\n"+
      "</root>";
      private const String LIST_ENTRY_WITH_NO_ROOT =
      "<?xml version=\"1.0\"?>\n"+
      "<root>\n"+
      "   <list class='java.util.Vector'>\n"+
      "      <entry id='12' value='some value' class='SimpleFramework.Xml.Core.ExceptionTest$RootListEntry'>\n"+
      "         <text>some example text</text>  \n\r"+
      "      </entry>\n\r"+
      "      <entry id='34' class='SimpleFramework.Xml.Core.ExceptionTest$NoRootListEntry'>\n"+
      "         <value>this is the value</value>\n"+
      "         <text>other example</text>  \n\r"+
      "      </entry>\n"+
      "      <entry id='56' class='SimpleFramework.Xml.Core.ExceptionTest$NoRootListEntry'>\n"+
      "         <value>this is some other value</value>\n"+
      "         <text>readonly example</text>  \n\r"+
      "      </entry>\n"+
      "   </list>\n"+
      "</root>";
      [Root(Name="entry")]
      private static class Entry {
         [Attribute(Name="id", Required=false)]
         private int id;
         [Element(Name="text", Required=true)]
         private String text;
      }
      [Root(Name="root")]
      private static class EntryList {
         [ElementList(Name="list", Type=Entry.class, Required=false)]
         private List list;
         [Attribute(Name="name", Required=true)]
         private String name;
         [Element(Name="text", Required=true)]
         private String text;
      }
      private static class ListEntry {
         [Attribute(Name="id", Required=false)]
         private int id;
         [Element(Name="text", Required=true)]
         private String text;
      }
      [Root(Name="entry")]
      private static class RootListEntry : ListEntry {
         [Attribute(Name="value", Required=true)]
         private String value;
      }
      private static class NoRootListEntry : ListEntry {
         [Element(Name="value", Required=true)]
         private String value;
      }
      [Root(Name="root")]
      private static class ListEntryList {
         [ElementList(Name="list", Type=ListEntry.class, Required=true)]
         private List list;
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestValid() {
         try {
            serializer.Read(EntryList.class, VALID);
         }catch(Exception e) {
            assertTrue(false);
         }
      }
      public void TestNoAttribute() {
         bool success = false;
         try {
            serializer.Read(EntryList.class, NO_NAME_ATTRIBUTE);
         }catch(ValueRequiredException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestNoElement() {
         bool success = false;
         try {
            serializer.Read(EntryList.class, NO_TEXT_ELEMENT);
         }catch(ValueRequiredException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestExtraAttribute() {
         bool success = false;
         try {
            serializer.Read(EntryList.class, EXTRA_ATTRIBUTE);
         }catch(AttributeException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestExtraElement() {
         bool success = false;
         try {
            serializer.Read(EntryList.class, EXTRA_ELEMENT);
         }catch(ElementException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestMissingElement() {
         bool success = false;
         try {
            Entry entry = new Entry();
            entry.id = 1;
            serializer.Write(entry, new StringWriter());
         } catch(ElementException e) {
            success = true;
         }
         assertTrue(success);
      }
      public void TestMissingAttribute() {
         bool success = false;
         try {
            EntryList list = new EntryList();
            list.text = "some text";
            serializer.Write(list, new StringWriter());
         } catch(AttributeException e) {
            success = true;
         }
         assertTrue(success);
      }
   }
}
