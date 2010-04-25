#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class RootTest : ValidationTestCase {
      private const String ROOT_EXAMPLE =
      "<rootExample version='1'>\n"+
      "  <text>Some text example</text>\n"+
      "</rootExample>";
      private const String EXTENDED_ROOT_EXAMPLE =
      "<rootExample version='1'>\n"+
      "  <text>Some text example</text>\n"+
      "</rootExample>";
      private const String EXTENDED_OVERRIDDEN_ROOT_EXAMPLE =
      "<extendedOverriddenRootExample version='1'>\n"+
      "  <text>Some text example</text>\n"+
      "</extendedOverriddenRootExample>";
      private const String EXTENDED_EXPLICITLY_OVERRIDDEN_ROOT_EXAMPLE =
      "<explicitOverride version='1'>\n"+
      "  <text>Some text example</text>\n"+
      "</explicitOverride>";
      [Root]
      private static class RootExample {
         private int version;
         private String text;
         public RootExample() {
            super();
         }
         [Attribute]
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
      private static class ExtendedRootExample : RootExample {
         public ExtendedRootExample() {
            super();
         }
      }
      [Root]
      private static class ExtendedOverriddenRootExample : ExtendedRootExample {
         public ExtendedOverriddenRootExample() {
            super();
         }
      }
      [Root(Name="explicitOverride")]
      private static class ExtendedExplicitlyOverriddenRootExample : ExtendedRootExample {
         public ExtendedExplicitlyOverriddenRootExample() {
            super();
         }
      }
      private Persister persister;
      public void SetUp() {
         this.persister = new Persister();
      }
      public void TestRoot() {
         RootExample example = persister.read(RootExample.class, ROOT_EXAMPLE);
         assertEquals(example.version, 1);
         assertEquals(example.text, "Some text example");
         validate(example, persister);
         example = persister.read(ExtendedRootExample.class, ROOT_EXAMPLE);
         assertEquals(example.version, 1);
         assertEquals(example.text, "Some text example");
         validate(example, persister);
         example = persister.read(ExtendedRootExample.class, EXTENDED_ROOT_EXAMPLE);
         assertEquals(example.version, 1);
         assertEquals(example.text, "Some text example");
         validate(example, persister);
         example = persister.read(ExtendedOverriddenRootExample.class, EXTENDED_OVERRIDDEN_ROOT_EXAMPLE);
         assertEquals(example.version, 1);
         assertEquals(example.text, "Some text example");
         validate(example, persister);
         example = persister.read(ExtendedExplicitlyOverriddenRootExample.class, EXTENDED_EXPLICITLY_OVERRIDDEN_ROOT_EXAMPLE);
         assertEquals(example.version, 1);
         assertEquals(example.text, "Some text example");
         validate(example, persister);
      }
   }
}
