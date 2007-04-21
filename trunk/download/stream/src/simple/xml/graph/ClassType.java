package simple.xml.graph;

import simple.xml.load.Type;

public class ClassType implements Type {

   private Class field;
   
   public ClassType(Class field) {
      this.field = field;
   }
   
   public Object getInstance() throws Exception {     
      return field.newInstance();
   }

   public Class getType() {      
      return field;
   }

   public boolean isReference() {      
      return false;
   }

}
