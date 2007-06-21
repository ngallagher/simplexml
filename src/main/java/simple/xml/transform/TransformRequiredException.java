package simple.xml.transform;

import simple.xml.load.PersistenceException;

public class TransformRequiredException extends PersistenceException {
   
   public TransformRequiredException(String text, Object... list) {
      super(String.format(text, list));               
   }       


   public TransformRequiredException(Throwable cause, String text, Object... list) {
      super(String.format(text, list), cause);           
   }  
}
