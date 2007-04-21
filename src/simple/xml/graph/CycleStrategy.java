package simple.xml.graph;


import simple.xml.load.Strategy;
import simple.xml.load.Type;
import simple.xml.stream.NodeMap;
import java.util.Map;

/**

<xml>
   <object id="1">
      <text>example</text>
   </object>
   </object id="id">
</xml>


*/
public class CycleStrategy implements Strategy {
   
   private WriteMap write;
   
   private ReadMap read;
   
   public CycleStrategy(String label, String key, String mark){
      this.write = new WriteMap(label, key, mark);
      this.read = new ReadMap(label, key, mark);
   }

   public Type getRoot(Class field, NodeMap node, Map map) throws Exception {
      return getElement(field, node, map);
   }     

   public Type getElement(Class field, NodeMap node, Map map) throws Exception {
      ReadGraph graph = read.find(map);
      
      if(graph != null) {
         return graph.getElement(field, node);
      }
      return null;
   }
   
   public boolean setRoot(Class field, Object value, NodeMap node, Map map){
      return setElement(field, value, node, map);
   }   
     
   public boolean setElement(Class field, Object value, NodeMap node, Map map){
      WriteGraph graph = write.find(map);
      
      if(graph != null) {
         return graph.setElement(field, value, node);
      }
      return false;
   }

}
