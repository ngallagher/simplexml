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
      [Root(Name="test")]
      private static class RequiredElement {
         [Element(Name="empty")]
         private String empty;
      }
      [Root(Name="test")]
      private static class OptionalElement {
         [Element(Name="empty", Required=false)]
         private String empty;
      }
      [Root(Name="test")]
      private static class EmptyCollection {
         [ElementList(Required=false)]
         private Collection<String> empty;
      }
      [Root(Name="test")]
      private static class RequiredMethodElement {
         private String text;
         [Element]
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
      [Root(Name="test")]
      private static class RequiredAttribute {
         [Attribute(Name="attribute")]
         private String attribute;
      }
      [Root(Name="test")]
      private static class OptionalAttribute {
         [Attribute(Name="attribute", Required=false)]
         private String attribute;
      }
      [Root(Name="test")]
      private static class DefaultedAttribute {
         [Attribute(Empty="NULL")]
         private String name;
         [Attribute(Empty="NULL")]
         private String address;
         [Element]
         private String description;
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestRequiredEmpty() {
         bool success = false;
         try {
            persister.Read(RequiredElement.class, EMPTY_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestRequiredEmptyMethod() {
         bool success = false;
         try {
            persister.Read(RequiredMethodElement.class, EMPTY_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestRequiredBlank() {
         bool success = false;
         try {
            persister.Read(RequiredElement.class, BLANK_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestOptionalEmpty() {
         bool success = false;
         try {
            persister.Read(RequiredElement.class, EMPTY_ELEMENT);
         } catch(ValueRequiredException e) {
            e.printStackTrace();
            success = true;
         }
         assertTrue(success);
      }
      public void TestOptionalBlank() {
         OptionalElement element = persister.Read(OptionalElement.class, BLANK_ELEMENT);
         AssertNull(element.empty);
      }
      public void TestEmptyCollection() {
         EmptyCollection element = persister.Read(EmptyCollection.class, BLANK_ELEMENT);
         AssertNotNull(element.empty);
         AssertEquals(element.empty.size(), 0);
         Validate(element, persister);
         element.empty = null;
         Validate(element, persister);
      }
      public void TestRequiredEmptyAttribute() {
         RequiredAttribute entry = persister.Read(RequiredAttribute.class, EMPTY_ATTRIBUTE);
         AssertEquals(entry.attribute, "");
      }
      public void TestOptionalEmptyAttribute() {
         OptionalAttribute entry = persister.Read(OptionalAttribute.class, EMPTY_ATTRIBUTE);
         AssertEquals(entry.attribute, "");
      }
      public void TestDefaultedAttribute() {
         DefaultedAttribute entry = persister.Read(DefaultedAttribute.class, DEFAULT_ATTRIBUTE);
         AssertEquals(entry.name, "John Doe");
         AssertEquals(entry.address, null);
         AssertEquals(entry.description, "Some description");
         Validate(entry, persister);
         entry.name = null;
         StringWriter out = new StringWriter();
         persister.Write(entry, out);
         String result = out.toString();
         assertXpathExists("/test[@name='NULL']", result);
         assertXpathExists("/test[@address='NULL']", result);
         assertXpathEvaluatesTo("Some description", "/test/description", result);
         Validate(entry, persister);
      }
   }
}
