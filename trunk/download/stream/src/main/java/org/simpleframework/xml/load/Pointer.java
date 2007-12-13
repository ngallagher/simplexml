package org.simpleframework.xml.load;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;

class Pointer implements Label {
   
   private Object value;
   
   private Label label;
   
   public Pointer(Label label, Object value) {
      this.label = label;
      this.value = value;
   }
   
   public Converter getConverter(Source root) throws Exception {
      Converter reader = label.getConverter(root);
      
      if(reader instanceof Adapter) {
         return reader;
      }
      return new Adapter(reader, value);
   }

   public Contact getContact() {
      return label.getContact();
   }

   public Class getDependant() throws Exception {
      return label.getDependant();
   }

   public String getEmpty() {
      return label.getEmpty();
   }

   public String getEntry() throws Exception {
      return label.getEntry();
   }

   public String getName() throws Exception {
      return label.getName();
   }

   public String getOverride() {
      return label.getOverride();
   }

   public Class getType() {
      return label.getType();
   }

   public boolean isData() {
      return label.isData();
   }

   public boolean isInline() {
      return label.isInline();
   }

   public boolean isRequired() {
      return label.isRequired();
   }
   
   public Object getValue() {
      return value;
   }
   
   private class Adapter implements Repeater {
      
      private Converter reader;
      
      private Object value;
      
      public Adapter(Converter reader, Object value) {
         this.reader = reader;
         this.value = value;
      }
      
      public Object read(InputNode node)throws Exception {
         return read(node, value);
      }
      
      public Object read(InputNode node, Object value) throws Exception {
         Position line = node.getPosition();
         String name = node.getName();         
         
         if(reader instanceof Repeater) {
            Repeater repeat = (Repeater) reader;
            
            return repeat.read(node, value);
         }
         throw new PersistenceException("Element '%s' declared twice at %s", name, line);
      }
      
      public void write(OutputNode node, Object value) throws Exception {
         write(node, value);
      }
   }
   
}
