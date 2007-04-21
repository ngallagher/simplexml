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
      return read.find(map).getElement(field, node);
   }
   
   public boolean setRoot(Class field, Object value, NodeMap node, Map map){
      return setElement(field, value, node, map);
   }   
     
   public boolean setElement(Class field, Object value, NodeMap node, Map map){
      return write.find(map).setElement(field, value, node);
   }

}
