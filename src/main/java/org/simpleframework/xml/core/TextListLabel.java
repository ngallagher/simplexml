package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class TextListLabel implements Label {
   
   private final Label label;
   
   public TextListLabel(Label label) {
      this.label = label;
   }

   @Override
   public Decorator getDecorator() throws Exception {
      return null;
   }

   @Override
   public Type getType(Class type) throws Exception {
      return null;
   }

   @Override
   public Label getLabel(Class type) throws Exception {
      return null;
   }

   @Override
   public String[] getNames() throws Exception {
      return label.getNames();
   }

   @Override
   public String[] getPaths() throws Exception {
      return label.getPaths();
   }

   @Override
   public String getEmpty(Context context) throws Exception {
      return null;
   }

   @Override
   public Converter getConverter(Context context) throws Exception {
      Type type = getContact();
      
      
      if(!context.isPrimitive(type)) {
        // throw new TextException("Cannot use %s to represent %s", type, label);
      }
      return new Convert(context, type, label);
   }
   
   private static class Convert implements Repeater {
      
      private final CollectionFactory factory;
      private final Primitive primitive;
      private final Label label;
      
      public Convert(Context context, Type type, Label label) {
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

   @Override
   public String getName() throws Exception {
      return label.getName();
   }

   @Override
   public String getPath() throws Exception {
      return label.getPath();
   }

   @Override
   public Expression getExpression() throws Exception {
      return label.getExpression();
   }

   @Override
   public Type getDependent() throws Exception {
      return label.getDependent();
   }

   @Override
   public String getEntry() throws Exception {
      return label.getEntry();
   }

   @Override
   public Object getKey() throws Exception {
      return label.getKey();
   }

   @Override
   public Annotation getAnnotation() {
      return label.getAnnotation();
   }

   @Override
   public Contact getContact() {
      return label.getContact();
   }

   @Override
   public Class getType() {
      return label.getType();
   }

   @Override
   public String getOverride() {
      return label.getOverride();
   }

   @Override
   public boolean isData() {
      return label.isData();
   }

   @Override
   public boolean isRequired() {
      return label.isRequired();
   }

   @Override
   public boolean isAttribute() {
      return label.isAttribute();
   }

   @Override
   public boolean isCollection() {
      return label.isCollection();
   }

   @Override
   public boolean isInline() {
      return label.isInline();
   }

   @Override
   public boolean isText() {
      return false;
   }

   @Override
   public boolean isUnion() {
      return false;
   }

   @Override
   public boolean isTextList() {
      return true;
   }

}
