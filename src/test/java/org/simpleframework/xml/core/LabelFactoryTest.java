package org.simpleframework.xml.core;

import java.util.List;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Union;
import org.simpleframework.xml.UnionList;

public class LabelFactoryTest extends TestCase {
   
   private static class Example {
      @Union({
         @Element(name="a", type=String.class),
         @Element(name="b", type=Integer.class)
      })
      private Object value;
   }
   
   private static class ExampleList {
      @UnionList({
         @ElementList(name="a", type=String.class),
         @ElementList(name="b", type=Integer.class)
      })
      private List<Object> value;
   }

   public void testUnion() throws Exception {
      FieldScanner scanner = new FieldScanner(Example.class);
      Contact contact = scanner.get(0);
      Element element = ((Union)contact.getAnnotation()).value()[0];
      Label unionLabel = LabelFactory.getInstance(contact, contact.getAnnotation(), element);
      Label elementLabel = LabelFactory.getInstance(contact, element);
      
      assertEquals(unionLabel.getName(), "a");
      assertEquals(elementLabel.getName(), "a");
   }
   
   public void testUnionList() throws Exception {
      FieldScanner scanner = new FieldScanner(ExampleList.class);
      Contact contact = scanner.get(0);
      ElementList element = ((UnionList)contact.getAnnotation()).value()[0];
      Label unionLabel = LabelFactory.getInstance(contact, contact.getAnnotation(), element);
      Label elementLabel = LabelFactory.getInstance(contact, element);
      
      assertEquals(unionLabel.getName(), "a");
      assertEquals(elementLabel.getName(), "a");
   }
}
