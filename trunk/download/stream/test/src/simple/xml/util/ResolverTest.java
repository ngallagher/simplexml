package simple.xml.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Set;

import simple.xml.ValidationTestCase;
import simple.xml.load.Persister;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.ElementList;
import simple.xml.Root;

public class ResolverTest extends ValidationTestCase {
        
   private static final String LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<test name='example'>\n"+
   "   <list>\n"+  
   "      <match pattern='*.html' value='text/html'/>\n"+
   "      <match pattern='*.jpg' value='image/jpeg'/>\n"+
   "      <match pattern='/images/*' value='image/jpeg'/>\n"+
   "      <match pattern='*.exe' value='application/octetstream'/>\n"+
   "      <match pattern='*.txt' value='text/plain'/>\n"+
   "      <match pattern='/html/*' value='text/html'/>\n"+
   "   </list>\n"+
   "</test>";  
   
   @Root(name="match")
   private static class ContentType extends Match {

      @Attribute(name="value")
      private String value;        

      public ContentType() {
         super();                  
      }

      public ContentType(String pattern, String value) {
         this.pattern = pattern;
         this.value = value;        
      }
   }
   
   @Root(name="test")
   private static class ContentResolver implements Iterable<ContentType> {

      @ElementList(name="list", type=ContentType.class)
      private Resolver<ContentType> list;           

      @Attribute(name="name")
      private String name;

      public Iterator<ContentType> iterator() {
         return list.iterator();
      }

      public void add(ContentType type) {
         list.add(type);              
      }

      public ContentType resolve(String name) {
         return list.resolve(name);              
      }

      public int size() {
         return list.size();              
      }
   }
        
	private Persister serializer;

	public void setUp() {
	   serializer = new Persister();
	}
	
   public void testResolver() throws Exception {    
      ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);

      assertEquals(6, resolver.size());
      assertEquals("image/jpeg", resolver.resolve("image.jpg").value);
      assertEquals("text/plain", resolver.resolve("README.txt").value);
      assertEquals("text/html", resolver.resolve("/index.html").value);
      assertEquals("text/html", resolver.resolve("/html/image.jpg").value);
      assertEquals("text/plain", resolver.resolve("/images/README.txt").value);
      assertEquals("image/jpeg", resolver.resolve("/images/image.JPEG").value);
      
      validate(resolver, serializer);
   }
   
   public void testCache() throws Exception {    
      ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);

      assertEquals(6, resolver.size());
      assertEquals("image/jpeg", resolver.resolve("image.jpg").value);
      assertEquals("text/plain", resolver.resolve("README.txt").value);
      
      Iterator<ContentType> it = resolver.iterator();
      
      while(it.hasNext()) {
         ContentType type = it.next();
         
         if(type.value.equals("text/plain")) {
            it.remove();
            break;
         }
      }
      resolver.add(new ContentType("*", "application/octetstream"));

      assertEquals("application/octetstream", resolver.resolve("README.txt").value);
      assertEquals("application/octetstream", resolver.resolve("README.txt").value);            
      
      resolver.add(new ContentType("README.*", "text/html"));
      resolver.add(new ContentType("README.txt", "text/plain"));

      assertEquals("text/plain", resolver.resolve("README.txt").value);
      assertEquals("text/html", resolver.resolve("README.jsp").value);

      validate(resolver, serializer);
   }

   public void testNoResolution() throws Exception {
      ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);

      assertEquals(6, resolver.size());
      assertEquals("text/plain", resolver.resolve("README.txt").value);
      assertEquals(null, resolver.resolve("README"));           
   }

   public void testNonGreedyMatch() throws Exception {
      ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);

      assertEquals(6, resolver.size());
      resolver.add(new ContentType("/*?/html/*", "text/html"));
      
      assertEquals(7, resolver.size());
      assertEquals(null, resolver.resolve("/a/b/html/index.jsp"));
      assertEquals("text/html", resolver.resolve("/a/html/index.jsp").value);
   }
}
