package org.simpleframework.xml.transform;

class ArrayMatcher extends PackageMatcher {

   private final Matcher primary;
   
   public ArrayMatcher(Matcher primary) {
      this.primary = primary;
   }
   
   public Transform match(Class type) throws Exception {
      Class entry = type.getComponentType();
      
      if(entry.isPrimitive()) {
         return matchPrimitive(entry);
      }
      return matchArray(entry);
   }
   
   private Transform matchPrimitive(Class entry) throws Exception {            
      Transform transform = primary.match(entry);

      if(entry == char.class) {
         return new CharacterArrayTransform();
      }      
      if(transform == null) {
         throw new TransformRequiredException("Transform for %s not found", entry);
      }
      return new PrimitiveArrayTransform(transform, entry);
   }
   
   private Transform matchArray(Class entry) throws Exception {
      Class type = getArrayClass(entry);      
      
      return (Transform)type.newInstance();
   }
}
