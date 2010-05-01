#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml {
   public class ResolverTest : TestCase {
      private class ContentType : Match {
         private readonly String value;
         private readonly String pattern;
         public ContentType(String pattern, String value) {
            this.pattern = pattern;
            this.value = value;
         }
         public String Value {
            get {
               return value;
            }
         }
         public override String Pattern {
            get {
               return pattern;
            }
         }
         public override String ToString() {
            return String.Format("{0}={1}", pattern, value);
         }
      }
      public void TestResolver() {
         Resolver<ContentType> resolver = new Resolver<ContentType>();
         resolver.Add(new ContentType("*.html", "text/html"));
         resolver.Add(new ContentType("*.jpg", "image/jpeg"));
         resolver.Add(new ContentType("/images/*", "image/jpeg"));
         resolver.Add(new ContentType("/log/**", "text/plain"));
         resolver.Add(new ContentType("*.exe", "application/octetstream"));
         resolver.Add(new ContentType("**.txt", "text/plain"));
         resolver.Add(new ContentType("/html/*", "text/html"));
         AssertEquals(7, resolver.Count);
         AssertEquals("image/jpeg", resolver.Resolve("image.jpg").Value);
         AssertEquals("text/plain", resolver.Resolve("README.txt").Value);
         AssertEquals("text/html", resolver.Resolve("/index.html").Value);
         AssertEquals("text/html", resolver.Resolve("/html/image.jpg").Value);
         AssertEquals("text/plain", resolver.Resolve("/images/README.txt").Value);
         AssertEquals("text/plain", resolver.Resolve("/log/access.log").Value);
      }
      public void TestCache() {
         Resolver<ContentType> resolver = new Resolver<ContentType>();
         resolver.Add(new ContentType("*.html", "text/html"));
         resolver.Add(new ContentType("*.jpg", "image/jpeg"));
         resolver.Add(new ContentType("/images/*", "image/jpeg"));
         resolver.Add(new ContentType("/log/**", "text/plain"));
         resolver.Add(new ContentType("*.exe", "application/octetstream"));
         resolver.Add(new ContentType("**.txt", "text/plain"));
         resolver.Add(new ContentType("/html/*", "text/html"));
         AssertEquals(7, resolver.Count);
         AssertEquals("image/jpeg", resolver.Resolve("image.jpg").Value);
         AssertEquals("text/plain", resolver.Resolve("README.txt").Value);
         int index = 0;
         for(int i = resolver.Count; i > 0; i--) {
            ContentType type = resolver[i - 1]; 
            if(type.Value.Equals("text/plain")) {
               index = i;
            }
         }
         resolver.RemoveAt(index);
         resolver.Add(new ContentType("*", "application/octetstream"));
         AssertEquals("application/octetstream", resolver.Resolve("README.txt").Value);
         AssertEquals("application/octetstream", resolver.Resolve("README.txt").Value);
         resolver.Add(new ContentType("README.*", "text/html"));
         resolver.Add(new ContentType("README.txt", "text/plain"));
         AssertEquals("text/plain", resolver.Resolve("README.txt").Value);
         AssertEquals("text/html", resolver.Resolve("README.jsp").Value);
      }
      public void TestNoResolution() {
         Resolver<ContentType> resolver = new Resolver<ContentType>();
         resolver.Add(new ContentType("*.html", "text/html"));
         resolver.Add(new ContentType("*.jpg", "image/jpeg"));
         resolver.Add(new ContentType("/images/*", "image/jpeg"));
         resolver.Add(new ContentType("/log/**", "text/plain"));
         resolver.Add(new ContentType("*.exe", "application/octetstream"));
         resolver.Add(new ContentType("**.txt", "text/plain"));
         resolver.Add(new ContentType("/html/*", "text/html"));
         AssertEquals(7, resolver.Count);
         AssertEquals("text/plain", resolver.Resolve("README.txt").Value);
         AssertEquals(null, resolver.Resolve("README"));
      }
      public void TestNonGreedyMatch() {
         Resolver<ContentType> resolver = new Resolver<ContentType>();
         resolver.Add(new ContentType("*.html", "text/html"));
         resolver.Add(new ContentType("*.jpg", "image/jpeg"));
         resolver.Add(new ContentType("/images/*", "image/jpeg"));
         resolver.Add(new ContentType("/log/**", "text/plain"));
         resolver.Add(new ContentType("*.exe", "application/octetstream"));
         resolver.Add(new ContentType("**.txt", "text/plain"));
         resolver.Add(new ContentType("/html/*", "text/html"));
         AssertEquals(7, resolver.Count);
         resolver.Add(new ContentType("/*?/html/*", "text/html"));
         AssertEquals(8, resolver.Count);
         AssertEquals(null, resolver.Resolve("/a/b/html/index.jsp"));
         AssertEquals("text/html", resolver.Resolve("/a/html/index.jsp").Value);
      }
      public void TestResolverCache() {
         Resolver<ContentType> resolver = new Resolver<ContentType>();
         for(int i = 0; i <= 2000; i++) {
            resolver.Add(new ContentType(i.ToString(), i.ToString()));
         }
         AssertEquals(resolver.Resolve("1").Value, "1");
         AssertEquals(resolver.Resolve("2000").Value, "2000");
      }
   }
}
