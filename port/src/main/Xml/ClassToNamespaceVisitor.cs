#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class ClassToNamespaceVisitor : Visitor {
      private readonly bool comment;
      public ClassToNamespaceVisitor(){
         this(true);
      }
      public ClassToNamespaceVisitor(bool comment){
         this.comment = comment;
      }
      public void Read(Type field, NodeMap<InputNode> node) {
         String namespace = node.getNode().getReference();
         if(namespace != null && namespace.length() > 0) {
            String type = new PackageParser().revert(namespace).getName();
            if(type == null) {
               throw new PersistenceException("Could not match name %s", namespace);
            }
            node.put("class", type);
         }
      }
      public void Write(Type field, NodeMap<OutputNode> node) {
         OutputNode value = node.remove("class");
         if(value != null) {
            String type = value.getValue();
            String name = new PackageParser().parse(type);
            if(name == null) {
               throw new PersistenceException("Could not match class %s", type);
            }
            if(comment) {
               node.getNode().setComment(type);
            }
            node.getNode().getNamespaces().put(name, "class");
            node.getNode().setReference(name);
         }
      }
   }
}
