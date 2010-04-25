#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class PrimitiveListTest : TestCase {
      public const String TWO =
       "<array class='java.util.Vector'>"+
       "  <entry>one</entry>" +
       "  <entry>two</entry>" +
       "</array>";
      public void TestTwo() {
         Context context = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         PrimitiveList primitive = new PrimitiveList(context, new ClassType(List.class), new ClassType(String.class), "entry");
         InputNode node = NodeBuilder.read(new StringReader(TWO));
         Object value = primitive.read(node);
         assertEquals(value.getClass(), Vector.class);
         Vector vector = (Vector) value;
         assertEquals(vector.get(0), "one");
         assertEquals(vector.get(1), "two");
         InputNode newNode = NodeBuilder.read(new StringReader(TWO));
         assertTrue(primitive.validate(newNode));
      }
   }
}
