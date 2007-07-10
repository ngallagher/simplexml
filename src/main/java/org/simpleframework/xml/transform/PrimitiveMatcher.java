package org.simpleframework.xml.transform;

class PrimitiveMatcher implements Matcher { 
   
   public PrimitiveMatcher() {
      super();
   }
   
   public Transform match(Class type) throws Exception {      
      if(type == boolean.class) {
         return new BooleanTransform();
      }
      if(type == int.class) {
         return new IntegerTransform();
      }
      if(type == long.class) {
         return new LongTransform();
      }
      if(type == double.class) {
         return new DoubleTransform();
      }
      if(type == float.class) {
         return new FloatTransform();
      }
      if(type == short.class) {
         return new ShortTransform();
      }
      if(type == byte.class) {
         return new ByteTransform();
      }
      if(type == char.class) {
         return new CharacterTransform();
      }     
      throw new TransformException("Transform of '%s' not supported", type);
   }
}