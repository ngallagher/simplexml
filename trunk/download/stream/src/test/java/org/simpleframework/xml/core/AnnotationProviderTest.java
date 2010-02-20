package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

public class AnnotationProviderTest extends TestCase {
   
   public void testProvider() throws Exception {
      assertTrue(ElementMap.class.isAssignableFrom(AnnotationFactory.getInstance(Map.class).getClass()));
      assertTrue(ElementMap.class.isAssignableFrom(AnnotationFactory.getInstance(HashMap.class).getClass()));
      assertTrue(ElementMap.class.isAssignableFrom(AnnotationFactory.getInstance(ConcurrentHashMap.class).getClass()));
      assertTrue(ElementMap.class.isAssignableFrom(AnnotationFactory.getInstance(LinkedHashMap.class).getClass()));
      assertTrue(ElementMap.class.isAssignableFrom(AnnotationFactory.getInstance(Map.class).getClass()));
      assertTrue(ElementList.class.isAssignableFrom(AnnotationFactory.getInstance(Set.class).getClass()));
      assertTrue(ElementList.class.isAssignableFrom(AnnotationFactory.getInstance(Collection.class).getClass()));
      assertTrue(ElementList.class.isAssignableFrom(AnnotationFactory.getInstance(List.class).getClass()));
      assertTrue(ElementList.class.isAssignableFrom(AnnotationFactory.getInstance(TreeSet.class).getClass()));
      assertTrue(ElementList.class.isAssignableFrom(AnnotationFactory.getInstance(HashSet.class).getClass()));
      assertTrue(ElementList.class.isAssignableFrom(AnnotationFactory.getInstance(ArrayList.class).getClass()));
   }

}
