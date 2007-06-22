package simple.xml.transform;

abstract class PackageMatcher implements Matcher {   
      
   protected Class getClass(Class type) throws Exception {      
      return getClass("%sTransform", type);
   }
   
   protected Class getArrayClass(Class entry) throws Exception {
      return getClass("%sArrayTransform", entry);       
   }
   
   protected Class getClass(String text, Class type) throws Exception {
      String name = getConversion(text, type);      
    
      try {  
         return Class.forName(name);
      } catch(Exception e) {
         throw new TransformRequiredException(e, "Transform %s is required for %s", type);
      }
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
