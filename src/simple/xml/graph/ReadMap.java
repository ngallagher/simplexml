package simple.xml.graph;

import java.util.Map;
import java.util.WeakHashMap;

final class ReadMap extends WeakHashMap<Map, ReadGraph>{
   
   private String label;
   
   private String key;
   
   private String mark;
   
   public ReadMap(String label, String key, String mark) {
      this.label = label;
      this.key = key;
      this.mark = mark;
   }

   public ReadGraph find(Map map) {
      ReadGraph read = get(map);
      
      if(read == null) {
         read = new ReadGraph(label, key, mark);
         put(map, read);
      }
      return read;
   }
}
