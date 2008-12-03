package org.simpleframework.xml.stream;

import java.io.StringWriter;

import org.simpleframework.xml.ValidationTestCase;

public class NamespaceMapTest extends ValidationTestCase {

   public void testNamespace() throws Exception {
      StringWriter out = new StringWriter();           
      OutputNode top = NodeBuilder.write(out);
      OutputNode root = top.getChild("root");
      NamespaceMap map = root.getNamespaces();
      
      root.setReference("http://www.sun.com/jsp");
      map.put("http://www.w3c.com/xhtml", "xhtml");
      map.put("http://www.sun.com/jsp", "jsp");
      
      OutputNode child = root.getChild("child");
     
      child.setAttribute("name.1", "1");
      child.setAttribute("name.2", "2");
     
      OutputNode attribute = child.getAttributes().get("name.1");
      
      attribute.setReference("http://www.w3c.com/xhtml");
      
      OutputNode otherChild = root.getChild("otherChild");
      
      otherChild.setAttribute("name.a", "a");
      otherChild.setAttribute("name.b", "b");
      
      map = otherChild.getNamespaces();
      map.put("http://www.w3c.com/xhtml", "ignore");
      
      OutputNode yetAnotherChild = otherChild.getChild("yetAnotherChild");
      
      yetAnotherChild.setReference("http://www.w3c.com/xhtml");
      yetAnotherChild.setValue("example text for yet another namespace");
      
      OutputNode finalChild = otherChild.getChild("finalChild");
      
      map = finalChild.getNamespaces();
      map.put("http://www.w3c.com/anonymous");
      
      finalChild.setReference("http://www.w3c.com/anonymous");
      
      OutputNode veryLastChild = finalChild.getChild("veryLastChild");
      
      map = veryLastChild.getNamespaces();
      map.put("");
      
      OutputNode veryVeryLastChild = veryLastChild.getChild("veryVeryLastChild");
      
      map = veryVeryLastChild.getNamespaces();
      map.put("");
      
      veryVeryLastChild.setReference("");
      veryVeryLastChild.setValue("very very last child");
      
      OutputNode otherVeryVeryLastChild = veryLastChild.getChild("otherVeryVeryLastChild");
      
      // Problem here with anonymous namespace
      otherVeryVeryLastChild.setReference("http://www.w3c.com/anonymous");
      otherVeryVeryLastChild.setValue("other very very last child");
      
      OutputNode yetAnotherVeryVeryLastChild = veryLastChild.getChild("yetAnotherVeryVeryLastChild");
      
      yetAnotherVeryVeryLastChild.setReference("http://www.w3c.com/xhtml");
      yetAnotherVeryVeryLastChild.setValue("yet another very very last child");
    
      root.commit();
      validate(out.toString());
      
   }
}
