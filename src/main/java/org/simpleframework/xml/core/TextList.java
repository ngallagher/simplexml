package org.simpleframework.xml.core;

import java.util.Collection;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

class TextList implements Repeater {
   
   private final CollectionFactory factory;
   private final Primitive primitive;
   private final Label label;
   
   public TextList(Context context, Type type, Label label) {
      this.factory = new CollectionFactory(context, type);
      this.primitive = new Primitive(context, new ClassType(String.class));
      this.label = label;
   }

   @Override
   public Object read(InputNode node) throws Exception {
      Instance value = factory.getInstance(node); 
      Object data = value.getInstance();
      
      if(value.isReference()) {      
         return value.getInstance(); 
      }
      return read(node, data);
   }

   @Override
   public boolean validate(InputNode node) throws Exception {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void write(OutputNode node, Object object) throws Exception {
      // TODO Auto-generated method stub
      
   }

   @Override
   public Object read(InputNode node, Object result) throws Exception {
      Collection list = (Collection) result;                 
      Object value = primitive.read(node);
      
      if(value != null) {
         list.add(value);
      }
      return result;
   } 
   
}