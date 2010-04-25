#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class LiteralTest : ValidationTestCase {
      [Root(Strict=false)]
      private static class LiteralExample {
         [Attribute]
         private String name;
         [Attribute]
         private String key;
         [Text(Required=false)]
         private readonly Literal literal = new Literal(
         "<literal id='a' value='a'>\n"+
         "   <child>some example text</child>\n"+
         "</literal>\n");
         private LiteralExample() {
            super();
         }
         public LiteralExample(String name, String key) {
            this.name = name;
            this.key = key;
         }
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
      private static class LiteralMatcher : Matcher {
         public Transform Match(Class type) {
            if(type == Literal.class) {
               return new LiteralTransform();
            }
            return null;
         }
      }
      private static class LiteralTransform : Transform {
         public Object Read(String value) {
            return new Literal(value);
         }
         public String Write(Object value) {
            return value.ToString();
         }
      }
      private static class Literal {
         private readonly String content;
         public Literal(String content) {
            this.content = content;
         }
         public String ToString() {
            return content;
         }
      }
      public void TestLiteral() {
         Matcher matcher = new LiteralMatcher();
         Persister persister = new Persister(matcher);
         LiteralExample example = new LiteralExample("name", "key");
         persister.Write(example, System.out);
      }
   }
}
