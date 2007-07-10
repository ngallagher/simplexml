package org.simpleframework.xml.transform;

class TypeMatcher implements Matcher {
   
   private Matcher primitive;   
   
   private Matcher matcher;
   
   private Matcher array; 
   
   public TypeMatcher(Matcher matcher) {
      this.matcher = new PackageMatcher(matcher);
      this.primitive = new PrimitiveMatcher();
      this.array = new ArrayMatcher(this);
   }
   
   public Transform match(Class type) throws Exception {
      if(type.isArray()) {
         return array.match(type);
      }
      if(type.isPrimitive()) {
         return primitive.match(type);
      } 
      return matcher.match(type);     
   }
}
 