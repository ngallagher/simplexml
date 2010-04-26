#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class NodeWriterTest : ValidationTestCase {
      public void TestEarlyCommit() {
         StringWriter out = new StringWriter();
         OutputNode root = NodeBuilder.write(out);
         bool failure = false;
         try {
            root.commit();
         }catch(Exception e) {
            e.printStackTrace();
            failure = true;
         }
         assertTrue("Early commit should fail", failure);
      }
      public void TestBasicWrite() {
         StringWriter out = new StringWriter();
         OutputNode root = NodeBuilder.write(out).getChild("root");
         assertTrue(root.isRoot());
         AssertEquals("root", root.getName());
         root.setAttribute("id", "test");
         root.setAttribute("name", "testRoot");
         AssertEquals("test", root.getAttributes().get("id").getValue());
         AssertEquals("testRoot", root.getAttributes().get("name").getValue());
         OutputNode childOfRoot = root.getChild("child-of-root");
         childOfRoot.setAttribute("name", "child of root");
         assertFalse(childOfRoot.isRoot());
         AssertEquals("child-of-root", childOfRoot.getName());
         AssertEquals("child of root", childOfRoot.getAttributes().get("name").getValue());
         OutputNode childOfChildOfRoot = childOfRoot.getChild("child-of-child-of-root");
         childOfChildOfRoot.setValue("I am a child of the child-of-root element and a grand child of the root element");
         assertFalse(childOfChildOfRoot.isRoot());
         AssertEquals("child-of-child-of-root", childOfChildOfRoot.getName());
         OutputNode anotherChildOfRoot = root.getChild("another-child-of-root");
         anotherChildOfRoot.setAttribute("id", "yet another child of root");
         anotherChildOfRoot.setValue("I am a sibling to child-of-root");
         assertFalse(anotherChildOfRoot.isRoot());
         AssertEquals("another-child-of-root", anotherChildOfRoot.getName());
         OutputNode readonlyChildOfRoot = root.getChild("readonly-child-of-root");
         readonlyChildOfRoot.setValue("this element is a child of root");
         assertFalse(readonlyChildOfRoot.isRoot());
         assertTrue(root.isRoot());
         root.commit();
         validate(out.toString());
      }
      public void TestWrite() {
         StringWriter out = new StringWriter();
         OutputNode root = NodeBuilder.write(out).getChild("root");
         root.setAttribute("version", "1.0");
         assertTrue(root.isRoot());
         AssertEquals("root", root.getName());
         OutputNode firstChild = root.getChild("first-child");
         firstChild.setAttribute("key", "1");
         firstChild.setValue("some value for first child");
         assertFalse(firstChild.isRoot());
         AssertEquals("1", firstChild.getAttributes().get("key").getValue());
         OutputNode secondChild = root.getChild("second-child");
         secondChild.setAttribute("key", "2");
         secondChild.setValue("some value for second child");
         assertTrue(root.isRoot());
         assertFalse(secondChild.isRoot());
         AssertEquals(firstChild.getChild("test"), null);
         OutputNode test = secondChild.getChild("test");
         test.setValue("test value");
         AssertEquals(test.getName(), "test");
         AssertEquals(test.getValue(), "test value");
         secondChild.commit();
         AssertEquals(secondChild.getChild("example"), null);
         OutputNode thirdChild = root.getChild("third-child");
         thirdChild.setAttribute("key", "3");
         thirdChild.setAttribute("name", "three");
         AssertEquals(thirdChild.getAttributes().get("key").getValue(), "3");
         AssertEquals(thirdChild.getAttributes().get("name").getValue(), "three");
         thirdChild.commit();
         AssertEquals(thirdChild.getChild("foo"), null);
         AssertEquals(thirdChild.getChild("bar"), null);
         OutputNode fourthChild = root.getChild("fourth-child");
         fourthChild.setAttribute("key", "4");
         fourthChild.setAttribute("name", "four");
         OutputNode fourthChildChild = fourthChild.getChild("fourth-child-child");
         fourthChildChild.setAttribute("name", "foobar");
         fourthChildChild.setValue("some text for grand-child");
         fourthChild.commit();
         assertTrue(fourthChildChild.isCommitted());
         assertTrue(fourthChild.isCommitted());
         AssertEquals(fourthChildChild.getChild("blah"), null);
         AssertEquals(fourthChild.getChild("example"), null);
         root.commit();
         validate(out.toString());
      }
   }
}
