package simple.xml.transform;

import java.util.concurrent.ConcurrentHashMap;

class TransformCache extends ConcurrentHashMap<Class, Transform>{

   
   public Transform cache(Class type, Transform transform) {
      return put(type, transform);
   }
}
