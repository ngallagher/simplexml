

package simple.xml;

import java.util.Map;

/**
 * This is used to create a substitute object.
 *
 * @author Niall Gallagher
 */ 
public interface Substitute<T, F> {

   /**
    * Provides the substitution result given the deserialized value.
    */         
   public F read(T type, Map map) throws Exception;

   /**
    * Provides a serialization value given the result from the object.
    */ 
   public T write(F field, Map map) throws Exception;
                   
}
