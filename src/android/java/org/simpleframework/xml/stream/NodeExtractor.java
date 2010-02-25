package org.simpleframework.xml.stream;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class NodeExtractor extends LinkedList<Node> {

   public NodeExtractor(Document source) {
      this.extract(source);
   }
   
   private void extract(Document source) {
      Node node = source.getDocumentElement();
      
      if(node != null) {
         offer(node);
         extract(node);
      }
   }

   private void extract(Node node) {
      NodeList list = node.getChildNodes();
      int length = list.getLength();
      
      for(int i = 0; i < length; i++) {
         Node child = list.item(i);
         short type = child.getNodeType();
         
         if(type != Node.COMMENT_NODE) {
            offer(child);
            extract(child);
         }
      }
   }
}