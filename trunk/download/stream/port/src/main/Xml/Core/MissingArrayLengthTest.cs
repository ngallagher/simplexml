#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MissingArrayLengthTest : TestCase {
       private const String SOURCE =
       "<missingArrayLengthExample>"+
       " <array>"+
       "   <item>one</item>" +
       "   <item>two</item>" +
       "   <item>three</item>" +
       " </array>"+
       "</missingArrayLengthExample>";
       @Root
       private static class MissingArrayLengthExample {
           [ElementArray(Entry="item")]
           private String[] array;
       }
       public void TestMissingArrayLength() {
           Persister persister = new Persister();
           bool exception = false;
           try {
               MissingArrayLengthExample value = persister.read(MissingArrayLengthExample.class, SOURCE);
           } catch(ElementException e) {
               exception = true;
           }
           assertTrue("Exception not thrown", exception);
       }
   }
}
