#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   import org.w3c.dom.Node;
   public class PackageParserTest : TestCase {
      private const int ITERATIONS = 100000;
      /*
       * javax.xml.namespace.NamespaceContext.getNamespaceURI(String prefix)
       *
       * e.g
       *
       * String reference = context.getNamespaceURI("class")
       * Class type = parser.Parse(reference);
       *
       * <element xmlns:class='http://util.java/ArrayList'>
       *    <name>name</name>
       *    <value>value</value>
       * </element>
       *
       */
      public void TestParser() {
         assertEquals("http://util.java/HashMap", Parse(HashMap.class));
         assertEquals("http://simpleframework.org/xml/Element", Parse(Element.class));
         assertEquals("http://simpleframework.org/xml/ElementList", Parse(ElementList.class));
         assertEquals("http://w3c.org/dom/Node", Parse(Node.class));
         assertEquals("http://simpleframework.org/xml/strategy/PackageParser", Parse(PackageParser.class));
         assertEquals(HashMap.class, Revert("http://util.java/HashMap"));
         assertEquals(Element.class, Revert("http://simpleframework.org/xml/Element"));
         assertEquals(ElementList.class, Revert("http://simpleframework.org/xml/ElementList"));
         assertEquals(Node.class, Revert("http://w3c.org/dom/Node"));
         assertEquals(PackageParser.class, Revert("http://simpleframework.org/xml/strategy/PackageParser"));
         long start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            FastParse(ElementList.class);
         }
         long fast = System.currentTimeMillis() - start;
         start = System.currentTimeMillis();
         for(int i = 0; i < ITERATIONS; i++) {
            Parse(ElementList.class);
         }
         long normal = System.currentTimeMillis() - start;
         System.out.printf("fast=%sms normal=%sms diff=%s%n", fast, normal, normal / fast);
      }
      public String FastParse(Class type) {
         return new PackageParser().Parse(type);
      }
      public String Parse(Class type) {
         return new PackageParser().Parse(type);
      }
      public Class Revert(String type) {
         return new PackageParser().Revert(type);
      }
   }
}
