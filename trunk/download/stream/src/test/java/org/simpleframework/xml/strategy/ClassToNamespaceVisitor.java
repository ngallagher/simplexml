package org.simpleframework.xml.strategy;

import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

public class ClassToNamespaceVisitor implements Visitor {
   
   public void read(Type field, NodeMap<InputNode> node) throws Exception {
      String namespace = node.getNode().getReference();
      if(namespace != null && namespace.length() > 0) {
         String type = new PackageParser().revert(namespace).getName();
         if(type == null) {
            throw new PersistenceException("Could not match name %s", namespace);
         }
         node.put("class", type);
      }
   }

   public void write(Type field, NodeMap<OutputNode> node) throws Exception {
      OutputNode value = node.remove("class");
      if(value != null) {
         String type = value.getValue();
         String name = new PackageParser().parse(type);
         if(name == null) {
            throw new PersistenceException("Could not match class %s", type);
         }
         node.getNode().setComment(type);
         node.getNode().getNamespaces().put(name, "class");
         node.getNode().setReference(name);
      }
   }
}