package simple.xml.transform;

abstract class PackageMatcher implements Matcher {   
      
   protected Class getClass(Class type) throws Exception {
      try {
         return getClass("%sTransform", type);
      }catch(Exception e) {
         throw new TransformRequiredException("No framsform found for %s", type);
      }
   }
   
   protected Class getArrayClass(Class entry) throws Exception {
      try {
         return getClass("%sArrayTransform", entry);
      }catch(Exception e) {
         throw new TransformRequiredException("No framsform found for %s[]", entry);
      }         
   }
   
   protected Class getClass(String text, Class type) throws Exception {
      String name = getConversion(text, type);      
      
      return Class.forName(name);
   }
   
   protected String getConversion(String text, Class type) {
      String name = type.getName();
      
      if(name.startsWith("javax")) {
         name = name.replace("javax", "simple.xml.transform");
      } 
      if(name.startsWith("java")) {
         name = name.replace("java", "simple.xml.transform");
      }
      return String.format(text, name); 
   }
}
