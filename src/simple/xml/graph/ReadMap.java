package simple.xml.graph;

import java.util.Map;
import java.util.WeakHashMap;

final class ReadMap extends WeakHashMap<Map, ReadCycle>{
   
   private String label;
   
   private String key;
   
   private String mark;
   
   public ReadMap(String label, String key, String mark) {
      this.label = label;
      this.key = key;
      this.mark = mark;
   }

   public ReadCycle find(Map map) {
      ReadCycle read = get(map);
      
      if(read == null) {
         read = new ReadCycle(label, key, mark);
         put(map, read);
      }
      return read;
   }
}
