package simple.xml.transform;

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
      
      if(transform == null) {
         throw new IllegalStateException("Transformer not found");
      }
      return new PrimitiveArrayTransform(transform, entry);
   }
   
   private Transform matchArray(Class entry) throws Exception {
      Class type = getArrayClass(entry);
      
      try {
         return (Transform)type.newInstance();
      }catch (Exception e) {
         throw new TransformRequiredException("No transform found for %s", entry);
      }
   }
}
