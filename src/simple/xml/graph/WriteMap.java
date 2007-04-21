package simple.xml.graph;

import java.util.Map;
import java.util.WeakHashMap;

public class WriteMap extends WeakHashMap<Map, WriteCycle> {
   
   private String label;
   
   private String key;
   
   private String mark;
   
   public WriteMap(String label, String key, String mark) {
      this.label = label;
      this.key = key;
      this.mark = mark;
   }

   public WriteCycle find(Map map) {
      WriteCycle read = get(map);
      
      if(read == null) {
         read = new WriteCycle(label, key, mark);
         put(map, read);
      }
      return read;
   }
}
