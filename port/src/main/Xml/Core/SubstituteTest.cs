#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class SubstituteTest : ValidationTestCase {
      private const String REPLACE_SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<substituteExample>\n"+
      "   <substitute class='SimpleFramework.Xml.Core.SubstituteTest$SimpleSubstitute'>some example text</substitute>  \n\r"+
      "</substituteExample>";
      private const String RESOLVE_SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<substituteExample>\n"+
      "   <substitute class='SimpleFramework.Xml.Core.SubstituteTest$YetAnotherSubstitute'>some example text</substitute>  \n\r"+
      "</substituteExample>";
      @Root
      private static class SubstituteExample {
         @Element
         public Substitute substitute;
         public SubstituteExample() {
            super();
         }
         public SubstituteExample(Substitute substitute) {
            this.substitute = substitute;
         }
      }
      @Root
      private static class Substitute {
         @Text
         public String text;
      }
      private static class SimpleSubstitute : Substitute {
         @Replace
         public Substitute Replace() {
            return new OtherSubstitute("this is the other substitute", text);
         }
         @Persist
         public void Persist() {
            throw new IllegalStateException("Simple substitute should never be written only read");
         }
      }
      private static class OtherSubstitute : Substitute {
         @Attribute
         public String name;
         public OtherSubstitute() {
            super();
         }
         public OtherSubstitute(String name, String text) {
            this.text = text;
            this.name = name;
         }
      }
      private static class YetAnotherSubstitute : Substitute {
         public YetAnotherSubstitute() {
            super();
         }
         @Validate
         public void Validate() {
            return;
         }
         @Resolve
         public Substitute Resolve() {
            return new LargeSubstitute(text, "John Doe", "Sesame Street", "Metropilis");
         }
      }
      private static class LargeSubstitute : Substitute {
         @Attribute
         private String name;
         @Attribute
         private String street;
         @Attribute
         private String city;
         public LargeSubstitute() {
            super();
         }
         public LargeSubstitute(String text, String name, String street, String city) {
            this.name = name;
            this.street = street;
            this.city = city;
            this.text = text;
         }
      }
      private Persister serializer;
      public void SetUp() {
         serializer = new Persister();
      }
      public void TestReplace() {
         SubstituteExample example = serializer.read(SubstituteExample.class, REPLACE_SOURCE);
         assertEquals(example.substitute.getClass(), SimpleSubstitute.class);
         assertEquals(example.substitute.text, "some example text");
         Validate(example, serializer);
         StringWriter out = new StringWriter();
         serializer.write(example, out);
         String text = out.toString();
         example = serializer.read(SubstituteExample.class, text);
         assertEquals(example.substitute.getClass(), OtherSubstitute.class);
         assertEquals(example.substitute.text, "some example text");
         Validate(example, serializer);
      }
      public void TestResolve() {
         SubstituteExample example = serializer.read(SubstituteExample.class, RESOLVE_SOURCE);
         assertEquals(example.substitute.getClass(), LargeSubstitute.class);
         assertEquals(example.substitute.text, "some example text");
         Validate(example, serializer);
         StringWriter out = new StringWriter();
         serializer.write(example, out);
         String text = out.toString();
         example = serializer.read(SubstituteExample.class, text);
         assertEquals(example.substitute.getClass(), LargeSubstitute.class);
         assertEquals(example.substitute.text, "some example text");
         Validate(example, serializer);
      }
   }
}
