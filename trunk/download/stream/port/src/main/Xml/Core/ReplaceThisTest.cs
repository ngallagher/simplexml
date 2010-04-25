#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ReplaceThisTest : ValidationTestCase {
       @Root
       public static class RealParent {
           @Element
           private ReplaceThisParent inner;
           public RealParent() {
               this.inner = new ReplaceThisParent();
           }
           public RealParent(Set<String> children) {
               this.inner = new ReplaceThisParent(children);
           }
           public ReplaceThisParent Inner {
              get {
                  return inner;
              }
           }
           //public ReplaceThisParent GetInner() {
           //    return inner;
           //}
       @Root
       public static class ReplaceThisParent {
           @ElementList(required = false)
           Set<String> children;
           public ReplaceThisParent() {
               this.children = new TreeSet<String>();
           }
           public ReplaceThisParent(Set<String> children) {
              this.children = children;
           }
           @Replace
           public ReplaceThisParent ReplaceParent() {
              return new ReplaceThisParent(null);
           }
           public Set<String> Children {
              set {
                  this.children=value;
              }
           }
           //public void SetChildren(Set<String> children) {
           //    this.children=children;
           //}
               return children;
           }
       }
       public void TestReplaceParent() {
           Persister persister = new Persister();
           Set<String> children = new HashSet<String>();
           RealParent parent = new RealParent(children);
           children.add("Tom");
           children.add("Dick");
           children.add("Harry");
           StringWriter writer = new StringWriter();
           persister.write(parent, writer);
           String text = writer.toString();
           System.out.println(text);
           assertEquals(text.indexOf("Tom"), -1);
           assertEquals(text.indexOf("Dick"), -1);
           assertEquals(text.indexOf("Harry"), -1);
           validate(persister, parent);
       }
   }
}
