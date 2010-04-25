#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class CommentTest : ValidationTestCase {
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      private static class Comment : System.Attribute {
         public String Value();
      }
      @Root
      @Default
      private static class CommentExample {
         [Comment("This represents the name value")]
         private String name;
         [Comment("This is a value to be used")]
         private String value;
         [Comment("Yet another comment")]
         private Double price;
      }
      private static class CommentVisitor : Visitor {
         public void Read(Type type, NodeMap<InputNode> node) {
         public void Write(Type type, NodeMap<OutputNode> node) {
            if(!node.getNode().isRoot()) {
               Comment comment = type.getAnnotation(Comment.class);
               if(comment != null) {
                  node.getNode().setComment(comment.Value());
               }
            }
         }
      }
      public void TestComment() {
         Visitor visitor = new CommentVisitor();
         Strategy strategy = new VisitorStrategy(visitor);
         Persister persister = new Persister(strategy);
         CommentExample example = new CommentExample();
         example.name = "Some Name";
         example.value = "A value to use";
         example.price = 9.99;
         persister.Write(example, System.out);
      }
   }
}
