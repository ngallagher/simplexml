
package simple.xml.stream;

import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.Location;

public class InputPosition implements Position {

   private XMLEvent source;
        
   public InputPosition(XMLEvent source) {
      this.source = source;
   }        

   public int getLine() {
      if(source != null) {
         return getLocation().getLineNumber();
      }   
      return -1;
   }

   private Location getLocation() {
      return source.getLocation();           
   }

   public String toString() {
      return String.format("line=%s", getLine());           
   }
}
