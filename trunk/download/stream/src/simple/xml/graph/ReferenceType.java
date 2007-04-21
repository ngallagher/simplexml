package simple.xml.graph;

import simple.xml.load.Type;

public class ReferenceType implements Type {
   
   private Object value;
   
   private Class type;
   
   public ReferenceType(Object value) {
      this.type = value.getClass();
      this.value = value;      
   }
   
   public Object getInstance() throws Exception {     
      return value;      
   }

   public Class getType() {
      return type;
   }

   public boolean isReference() {      
      return true;
   }
}
