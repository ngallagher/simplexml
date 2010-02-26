package org.simpleframework.xml.core;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

public class MethodPartFactoryTest extends TestCase {
   
   private static interface Bean {
      public int getInteger();
      public void setInteger(int value);
      public List<String> getList();
      public void setList(List<String> list);
      public Map<String, String> getMap();
      public void setMap(Map<String, String> map);
      public String[] getArray();
      public void setArray(String[] array);
   }

   public void testMethodPart() throws Exception {
      assertTrue(Element.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getInteger")).getAnnotation().getClass()));
      assertTrue(Element.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setInteger", int.class)).getAnnotation().getClass()));
      assertTrue(ElementMap.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getMap")).getAnnotation().getClass()));
      assertTrue(ElementMap.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setMap", Map.class)).getAnnotation().getClass()));
      assertTrue(ElementList.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getList")).getAnnotation().getClass()));
      assertTrue(ElementList.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setList", List.class)).getAnnotation().getClass()));
      assertTrue(ElementArray.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("getArray")).getAnnotation().getClass()));
      assertTrue(ElementArray.class.isAssignableFrom(new MethodPartFactory().getInstance(Bean.class.getMethod("setArray", String[].class)).getAnnotation().getClass()));
   }
}
