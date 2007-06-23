package org.simpleframework.xml.load;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.load.Persister;

import org.simpleframework.xml.ValidationTestCase;

public class RootTest extends ValidationTestCase {
      
   private static final String ROOT_EXAMPLE =
   "<rootExample version='1'>\n"+
   "  <text>Some text example</text>\n"+
   "</rootExample>";
   
   private static final String EXTENDED_ROOT_EXAMPLE =
   "<rootExample version='1'>\n"+
   "  <text>Some text example</text>\n"+
   "</rootExample>";   
   
   private static final String EXTENDED_OVERRIDDEN_ROOT_EXAMPLE =
   "<extendedOverriddenRootExample version='1'>\n"+
   "  <text>Some text example</text>\n"+
   "</extendedOverriddenRootExample>";
   
   private static final String EXTENDED_EXPLICITLY_OVERRIDDEN_ROOT_EXAMPLE =
   "<explicitOverride version='1'>\n"+
   "  <text>Some text example</text>\n"+
   "</explicitOverride>";
   
   @Root
   private static class RootExample {
      
      private int version;
      
      private String text;
      
      @Attribute
      public void setVersion(int version) {
         this.version = version;
      }
      
      @Attribute
      public int getVersion() {
         return version;
      }
      
      @Element
      public void setText(String text) {
         this.text = text;
      }
      
      @Element
      public String getText() {
         return text;
      }
   }
   
   private static class ExtendedRootExample extends RootExample {      
   }
   
   @Root
   private static class ExtendedOverriddenRootExample extends ExtendedRootExample {      
   }
   
   @Root(name="explicitOverride")
   private static class ExtendedExplicitlyOverriddenRootExample extends ExtendedRootExample {      
   }
   
   private Persister persister;
   
   public void setUp() {
      this.persister = new Persister();
   }
   
   public void testRoot() throws Exception {
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