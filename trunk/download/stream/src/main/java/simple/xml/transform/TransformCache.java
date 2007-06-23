package simple.xml.transform;

import java.util.concurrent.ConcurrentHashMap;

public class TransformCache extends ConcurrentHashMap<Class, Transform>{

   
   public Transform cache(Class type, Transform transform) {
      return put(type, transform);
   }
}
