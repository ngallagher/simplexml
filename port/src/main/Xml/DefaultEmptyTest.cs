#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class DefaultEmptyTest : ValidationTestCase {
      private const String SOURCE =
      "<defaultExample name='test'>\n" +
      "  <text>some text</text>\n"+
      "</defaultExample>";
      @Root
      private static class DefaultExample  {
         [ElementList(Empty=false, Required=false)]
         private List<String> stringList;
         [ElementMap(Empty=false, Required=false)]
         private Map<String, String> stringMap;
         [ElementArray(Empty=false, Required=false)]
         private String[] stringArray;
         @Attribute
         private String name;
         @Element
         private String text;
         public DefaultExample() {
            super();
         }
         public DefaultExample(String name, String text) {
            this.name = name;
            this.text = text;
         }
      }
      public void TestDefaults() {
         Persister persister = new Persister();
         DefaultExample example = persister.read(DefaultExample.class, SOURCE);
         assertEquals(example.name, "test");
         assertEquals(example.text, "some text");
         assertNotNull(example.stringList);
         assertNotNull(example.stringMap);
         assertNotNull(example.stringArray);
         persister.write(example, System.out);
         validate(persister, example);
         persister.write(new DefaultExample("name", "example text"), System.out);
      }
   }
}
