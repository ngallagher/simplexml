package simple.xml.graph;

import java.util.Map;
import java.util.WeakHashMap;

final class WriteMap extends WeakHashMap<Map, WriteGraph> {
   
   private String label;
   
   private String key;
   
   private String mark;
   
   public WriteMap(String label, String key, String mark) {
      this.label = label;
      this.key = key;
      this.mark = mark;
   }

   public WriteGraph find(Map map) {
      WriteGraph read = get(map);
      
      if(read == null) {
         read = new WriteGraph(label, key, mark);
         put(map, read);
      }
      return read;
   }
}
