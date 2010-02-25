package org.simpleframework.xml.stream;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

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
      dumpList();
   }
   
   private void dumpList() {
      for(Node node : this) {
         short type = node.getNodeType();
         String name = node.getNodeName();
         String text = dumpNode(node);
         if(type == Node.ELEMENT_NODE) {
            System.err.println("[START]["+type+"]"+ name +" -->" +text);
         }
      }
   }
   
   public static String dumpNode(Node node){
      StringBuilder builder = new StringBuilder();
      LinkedList<Node> path = new LinkedList<Node>();
      while(node != null) {
         path.addFirst(node);
         node = node.getParentNode();
      }
      for(Node next : path){
         builder.append("/").append(next.getNodeName());
      }
      return builder.toString();
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