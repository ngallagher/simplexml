package simple.xml.transform;

import java.io.File;


/**


int --> simple.xml.transform.lang.IntegerTransform
char --> simple.xml.transform.lang.CharacterTransform

char[] --> simple.xml.transform.CharacterArrayTransform
int[] --> simple.xml.transform.PrimitiveArrayTransform & simple.xml.transform.lang.IntegerTransform
String[] --> simple.xml.transform.lang.StringArrayTransform



*/
class DefaultMatcher extends PackageMatcher {
   
   private final Matcher array;
   
   public DefaultMatcher() {
      this.array = new ArrayMatcher(this);
   }
   
   public Transform match(Class type) throws Exception {
      if(type.isArray()) {
         return array.match(type);
      }
      return matchType(type);
   }
   
   private Transform matchType(Class type) throws Exception {
      Class real = getConversion(type);
      Class match = getClass(real);
      
      return (Transform)match.newInstance();      
   }
   
   private Class getConversion(Class type) {
      if(type == int.class) {
         return Integer.class;              
      }           
      if(type == boolean.class) {
         return Boolean.class;               
      }
      if(type == float.class) {
         return Float.class;                       
      }
      if(type == long.class) {
         return Long.class;                   
      }
      if(type == double.class) {
         return Double.class;              
      }
      if(type == byte.class) {
         return Byte.class;              
      }        
      if(type == short.class) {
         return Short.class;              
      }
      if(type == char.class) {
         return Character.class;
      }
      return type;
   }

}
