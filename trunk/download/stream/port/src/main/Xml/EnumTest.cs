#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EnumTest : TestCase {
       private const String SOURCE =
       "<enumBug>\n"+
       "  <type>A</type>\n"+
       "</enumBug>";
       private const String LIST =
       "<enumVariableArgumentsBug>\n"+
       "  <types>A,B,A,A</types>\n"+
       "</enumVariableArgumentsBug>";
       enum PartType {
           A,
           B
       }
       [Root]
       public static class EnumBug {
           [Element]
           private PartType type;
           public EnumBug(@Element(name="type") PartType type) {
              this.type = type;
           }
           public PartType Type {
              get {
                 return type;
              }
           }
           //public PartType GetType() {
           //   return type;
           //}
       [Root]
       public static class EnumVariableArgumentsBug {
           [Element]
           private PartType[] types;
           public EnumVariableArgumentsBug(@Element(name="types") PartType... types) {
              this.types = types;
           }
           public PartType[] Types {
              get {
                 return types;
              }
           }
           //public PartType[] GetTypes() {
           //   return types;
           //}
       public void TestEnum() {
           Serializer serializer = new Persister();
           EnumBug bug = serializer.read(EnumBug.class, SOURCE);
           assertEquals(bug.Type, PartType.A);
       }
       public void TestVargsEnum() {
           Serializer serializer = new Persister();
           EnumVariableArgumentsBug bug = serializer.read(EnumVariableArgumentsBug.class, LIST);
           assertEquals(bug.Types[0], PartType.A);
       }
   }
}
