#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EmptyTest : ValidationTestCase {
      private const String EMPTY_ELEMENT =
      "<?xml version=\"1.0\"?>\n"+
      "<test>\n"+
      "  <empty></empty>\r\n"+
      "</test>\n";
      private const String BLANK_ELEMENT =
      "<?xml version=\"1.0\"?>\n"+
      "<test>\n"+
      "  <empty/>\r\n"+
      "</test>";
      private const String EMPTY_ATTRIBUTE =
      "<?xml version=\"1.0\"?>\n"+
      "<test attribute=''/>\n";
      private const String DEFAULT_ATTRIBUTE =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='John Doe' address='NULL'>\n"+
      "  <description>Some description</description>\r\n"+
      "</test>";
      @Root(name="test")
      private static class RequiredElement {
         @Element(name="empty")
         private String empty;
      }
      @Root(name="test")
      private static class OptionalElement {
         @Element(name="empty", required=false)
         private String empty;
      }
      @Root(name="test")
      private static class EmptyCollection {
         @ElementList(required=false)
         private Collection<String> empty;
      }
      @Root(name="test")
      private static class RequiredMethodElement {
         private String text;
         @Element
         public String Empty {
            get {
               return text;
            }
            set {
               this.text = value;
            }
         }
         //public void SetEmpty(String text) {
         //   this.text = text;
         //}
         //public String GetEmpty() {
         //   return text;
         //}
      @Root(name="test")
      private static class RequiredAttribute {
         @Attribute(name="attribute")
         private String attribute;
      }
      @Root(name="test")
      private static class OptionalAttribute {
         @Attribute(name="attribute", required=false)
         private String attribute;
      }
      @Root(name="test")
      private static class DefaultedAttribute {
         @Attribute(empty="NULL")
         private String name;
         @Attribute(empty="NULL")
         private String address;
         @Element
         private String description;
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestRequiredEmpty() {
         bool success = false;
         try {
            persister.read(RequiredElement.class, EMPTY_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestRequiredEmptyMethod() {
         bool success = false;
         try {
            persister.read(RequiredMethodElement.class, EMPTY_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestRequiredBlank() {
         bool success = false;
         try {
            persister.read(RequiredElement.class, BLANK_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestOptionalEmpty() {
         bool success = false;
         try {
            persister.read(RequiredElement.class, EMPTY_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestOptionalBlank() {
         OptionalElement element = persister.read(OptionalElement.class, BLANK_ELEMENT);
         assertNull(element.empty);
      }
      public void TestEmptyCollection() {
         EmptyCollection element = persister.read(EmptyCollection.class, BLANK_ELEMENT);
         assertNotNull(element.empty);
         assertEquals(element.empty.size(), 0);
         validate(element, persister);
         element.empty = null;
         validate(element, persister);
      }
      public void TestRequiredEmptyAttribute() {
         RequiredAttribute entry = persister.read(RequiredAttribute.class, EMPTY_ATTRIBUTE);
         assertEquals(entry.attribute, "");
      }
      public void TestOptionalEmptyAttribute() {
         OptionalAttribute entry = persister.read(OptionalAttribute.class, EMPTY_ATTRIBUTE);
         assertEquals(entry.attribute, "");
      }
      public void TestDefaultedAttribute() {
         DefaultedAttribute entry = persister.read(DefaultedAttribute.class, DEFAULT_ATTRIBUTE);
         assertEquals(entry.name, "John Doe");
         assertEquals(entry.address, null);
         assertEquals(entry.description, "Some description");
         validate(entry, persister);
         entry.name = null;
         StringWriter out = new StringWriter();
         persister.write(entry, out);
         String result = out.toString();
         assertXpathExists("/test[@name='NULL']", result);
         assertXpathExists("/test[@address='NULL']", result);
         assertXpathEvaluatesTo("Some description", "/test/description", result);
         validate(entry, persister);
      }
   }
}
