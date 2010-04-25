#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class ResolverTest : ValidationTestCase {
      private const String LIST =
      "<?xml version=\"1.0\"?>\n"+
      "<test name='example'>\n"+
      "   <list>\n"+
      "      <match pattern='*.html' value='text/html'/>\n"+
      "      <match pattern='*.jpg' value='image/jpeg'/>\n"+
      "      <match pattern='/images/*' value='image/jpeg'/>\n"+
      "      <match pattern='/log/**' value='text/plain'/>\n"+
      "      <match pattern='*.exe' value='application/octetstream'/>\n"+
      "      <match pattern='**.txt' value='text/plain'/>\n"+
      "      <match pattern='/html/*' value='text/html'/>\n"+
      "   </list>\n"+
      "</test>";
      [Root(Name="match")]
      private static class ContentType : Match {
         [Attribute(Name="value")]
         private String value;
         [Attribute]
         private String pattern;
         public ContentType() {
            super();
         }
         public ContentType(String pattern, String value) {
            this.pattern = pattern;
            this.value = value;
         }
         public String Pattern {
            get {
               return pattern;
            }
         }
         //public String GetPattern() {
         //   return pattern;
         //}
            return String.format("%s=%s", pattern, value);
         }
      }
      [Root(Name="test")]
      private static class ContentResolver : Iterable<ContentType> {
         [ElementList(Name="list", Type=ContentType.class)]
         private Resolver<ContentType> list;
         [Attribute(Name="name")]
         private String name;
         private ContentResolver() {
            this.list = new Resolver<ContentType>();
         }
         public Iterator<ContentType> Iterator() {
            return list.Iterator();
         }
         public void Add(ContentType type) {
            list.Add(type);
         }
         public ContentType Resolve(String name) {
            return list.Resolve(name);
         }
         public int Size() {
            return list.Size();
         }
      }
   	private Persister serializer;
   	public void SetUp() {
   	   serializer = new Persister();
   	}
      public void TestResolver() {
         ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);
         assertEquals(7, resolver.Size());
         assertEquals("image/jpeg", resolver.Resolve("image.jpg").value);
         assertEquals("text/plain", resolver.Resolve("README.txt").value);
         assertEquals("text/html", resolver.Resolve("/index.html").value);
         assertEquals("text/html", resolver.Resolve("/html/image.jpg").value);
         assertEquals("text/plain", resolver.Resolve("/images/README.txt").value);
         assertEquals("text/plain", resolver.Resolve("/log/access.log").value);
         validate(resolver, serializer);
      }
      public void TestCache() {
         ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);
         assertEquals(7, resolver.Size());
         assertEquals("image/jpeg", resolver.Resolve("image.jpg").value);
         assertEquals("text/plain", resolver.Resolve("README.txt").value);
         Iterator<ContentType> it = resolver.Iterator();
         while(it.hasNext()) {
            ContentType type = it.next();
            if(type.value.equals("text/plain")) {
               it.remove();
               break;
            }
         }
         resolver.Add(new ContentType("*", "application/octetstream"));
         assertEquals("application/octetstream", resolver.Resolve("README.txt").value);
         assertEquals("application/octetstream", resolver.Resolve("README.txt").value);
         resolver.Add(new ContentType("README.*", "text/html"));
         resolver.Add(new ContentType("README.txt", "text/plain"));
         assertEquals("text/plain", resolver.Resolve("README.txt").value);
         assertEquals("text/html", resolver.Resolve("README.jsp").value);
         validate(resolver, serializer);
      }
      public void TestNoResolution() {
         ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);
         assertEquals(7, resolver.Size());
         assertEquals("text/plain", resolver.Resolve("README.txt").value);
         assertEquals(null, resolver.Resolve("README"));
      }
      public void TestNonGreedyMatch() {
         ContentResolver resolver = (ContentResolver) serializer.read(ContentResolver.class, LIST);
         assertEquals(7, resolver.Size());
         resolver.Add(new ContentType("/*?/html/*", "text/html"));
         assertEquals(8, resolver.Size());
         assertEquals(null, resolver.Resolve("/a/b/html/index.jsp"));
         assertEquals("text/html", resolver.Resolve("/a/html/index.jsp").value);
      }
      public void TestResolverCache() {
         ContentResolver resolver = new ContentResolver();
         for(int i = 0; i <= 2000; i++) {
            resolver.Add(new ContentType(String.valueOf(i), String.valueOf(i)));
         }
         assertEquals(resolver.Resolve("1").value, "1");
         assertEquals(resolver.Resolve("2000").value, "2000");
      }
   }
}
