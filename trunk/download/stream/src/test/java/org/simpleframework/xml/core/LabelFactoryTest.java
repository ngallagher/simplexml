package org.simpleframework.xml.core;

import java.util.List;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Variant;
import org.simpleframework.xml.VariantList;

public class LabelFactoryTest extends TestCase {
   
   private static class Example {
      @Variant({
         @Element(name="a", type=String.class),
         @Element(name="b", type=Integer.class)
      })
      private Object value;
   }
   
   private static class ExampleList {
      @VariantList({
         @ElementList(name="a", type=String.class),
         @ElementList(name="b", type=Integer.class)
      })
      private List<Object> value;
   }

   public void testVariant() throws Exception {
      FieldScanner scanner = new FieldScanner(Example.class);
      Contact contact = scanner.get(0);
      Element element = ((Variant)contact.getAnnotation()).value()[0];
      Label variantLabel = LabelFactory.getInstance(contact, contact.getAnnotation(), element);
      Label elementLabel = LabelFactory.getInstance(contact, element);
      
      assertEquals(variantLabel.getName(), "a");
      assertEquals(elementLabel.getName(), "a");
   }
   
   public void testVariantList() throws Exception {
      FieldScanner scanner = new FieldScanner(ExampleList.class);
      Contact contact = scanner.get(0);
      ElementList element = ((VariantList)contact.getAnnotation()).value()[0];
      Label variantLabel = LabelFactory.getInstance(contact, contact.getAnnotation(), element);
      Label elementLabel = LabelFactory.getInstance(contact, element);
      
      assertEquals(variantLabel.getName(), "a");
      assertEquals(elementLabel.getName(), "a");
   }
}
