
package simple.xml.transform;

public class Transformer {

   private TransformCache cache;
   
   private Matcher matcher;
   
   public Transformer() {
      this(new DefaultMatcher());
   }
   
   private Transformer(Matcher matcher) {      
      this.cache = new TransformCache();
      this.matcher = matcher;
   }
   
   public Object read(String value, Class type) throws Exception {
      Transform transform = cache.get(type);
      
      if(transform == null) {
         transform = matcher.match(type);
         cache.cache(type, transform);
      }
      return transform.read(value);
   }

   public String write(Object value, Class type) throws Exception {
      Transform transform = cache.get(type);
      
      if(transform == null) {
         transform = matcher.match(type);
         cache.cache(type, transform);
      }
      return transform.write(value);
   }
}
