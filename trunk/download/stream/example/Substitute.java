

package simple.xml;

import java.util.Map;

/**
 * This is used to create a substitute object.
 *
 * @author Niall Gallagher
 */ 
public interface Substitute<R, V> {

   /**
    * Provides the substitution result given the deserialized value.
    */         
   public R read(Class field, V value, Map map) throws Exception;

   /**
    * Provides a serialization value given the result from the object.
    */ 
   public V write(Class type, R value, Map map) throws Exception;
                   
}
