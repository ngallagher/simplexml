package org.simpleframework.xml.transform;

public class EmptyMatcher implements Matcher {

   public Transform match(Class type) throws Exception {
      throw new TransformException("Transform of '%s' not supported");
   }
}
