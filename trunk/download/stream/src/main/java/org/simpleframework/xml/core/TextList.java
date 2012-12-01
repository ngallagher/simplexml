package org.simpleframework.xml.core;

import java.util.Collection;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

class TextList implements Repeater {
   
   private final CollectionFactory factory;
   private final Primitive primitive;
   private final Type type;
   
   public TextList(Context context, Type list, Label label) {
      this.type = new ClassType(String.class);
      this.factory = new CollectionFactory(context, list);
      this.primitive = new Primitive(context, type);
   }

   public Object read(InputNode node) throws Exception {
      Instance value = factory.getInstance(node); 
      Object data = value.getInstance();
      
      if(value.isReference()) {      
         return value.getInstance(); 
      }
      return read(node, data);
   }

   public boolean validate(InputNode node) throws Exception {
      // TODO Auto-generated method stub
      return false;
   }

   public Object read(InputNode node, Object result) throws Exception {
      Collection list = (Collection) result;                 
      Object value = primitive.read(node);
      
      if(value != null) {
         list.add(value);
      }
      return result;
   } 
   
   public void write(OutputNode node, Object object) throws Exception {
      Collection list = (Collection) object;
      OutputNode parent = node.getParent();
      
      for(Object item : list) {
         primitive.write(parent, item);
      }
   }
}