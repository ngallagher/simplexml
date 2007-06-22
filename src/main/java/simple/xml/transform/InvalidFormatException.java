package simple.xml.transform;

import simple.xml.load.PersistenceException;

public class InvalidFormatException extends PersistenceException {
   
   public InvalidFormatException(String text, Object... list) {
      super(String.format(text, list));               
   }       


   public InvalidFormatException(Throwable cause, String text, Object... list) {
      super(String.format(text, list), cause);           
   }  
}