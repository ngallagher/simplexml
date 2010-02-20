package org.simpleframework.xml.stream;

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

class DocumentReader implements EventReader {
   
   private final NodeExtractor queue;
   private final NodeStack stack;
   
   public DocumentReader(Document document) {
      this.queue = new NodeExtractor(document);
      this.stack = new NodeStack();
      this.stack.push(document);
   }
   
   public NodeEvent next() {
      return next(false);
   }
   
   public NodeEvent peek() {
      return next(true);
   }
   
   private NodeEvent next(boolean peek) {
      Node node = queue.peek();
      
      if(node == null) {
         return new EndEvent();
      }
      return next(node, peek);
   }
   
   private NodeEvent next(Node node, boolean peek) {
      Node parent = node.getParentNode();
      Node top = stack.peek();
      
      if(parent != top) {
         if(!peek) {
            stack.pop();
         }
         return new EndEvent();
      }
      if(!peek) {
         queue.poll();
      }
      return create(node, peek);
   }
   
   private NodeEvent create(Node node, boolean peek) {
      if(node instanceof Element) {
         if(!peek) {
            stack.push(node);
         }
         return element(node);
      }
      if(node instanceof Text) {
         return text(node);
      }
      return next();
   }
   
   private NodeEvent text(Node node) {
      Text text = (Text)node;
      return new TextEvent(text);
   }
   
   private NodeEvent element(Node node) {
      Element element = (Element)node;
      return new ElementEvent(element);
   }
   
   private static class NodeStack extends Stack<Node> {
 
      public NodeStack() {
         super();
      }
      
      public Node peek() {
         int size = size();

         if(size == 0) {
            return null;
         }
         return elementAt(size - 1);
      }
   }
}