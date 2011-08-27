package org.simpleframework.xml.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Text;

public class ConstructorScannerTest extends TestCase {

   public static class ExampleWithTextAndElement {    
      public ExampleWithTextAndElement(
            @Element(name="a") String a, 
            @Element(name="b") String b) {}     
      public ExampleWithTextAndElement(
            @Path("a") @Text String a, 
            @Element(name="b") String b, 
            @Element(name="c") String c) {}  
   }
   
   public static class ClashBetweenElementAndText {    
      public ClashBetweenElementAndText(
            @Path("a/b") @Element(name="c") String a) {}     
      public ClashBetweenElementAndText(
            @Path("a/b/c") @Text String a,
            @Element(name="c") String c) {}  
   }
   
   public static class AmbiguousParameters {
      public AmbiguousParameters(
            @Attribute(name="a") String x,
            @Element(name="a") String y) {}
   }
   
   public static class SameNameWithPath {
      public SameNameWithPath(
            @Path("path[1]") @Attribute(name="a") String x,
            @Path("path[1]") @Element(name="a") String y) {}
   }
   
   public static class SameNameAndAnnotationWithPath {
      public SameNameAndAnnotationWithPath(
            @Path("path[1]") @Element(name="a") String x,
            @Path("path[1]") @Element(name="a") String y) {}
   }
   
   public static class SameNameWithDifferentPath{
      public SameNameWithDifferentPath(
            @Path("path[1]") @Element(name="a") String x,
            @Path("path[2]") @Element(name="a") String y) {}
   }
   
   public void testElementWithPath() throws Exception {
      ConstructorScanner scanner = new ConstructorScanner(ExampleWithTextAndElement.class);
      Creator creator = scanner.getCreator();
      List<Parameter> parameters = creator.getParameters();
      Set<String> names = new HashSet<String>();
      
      for(Parameter parameter : parameters) {
         String path = parameter.getPath();
         names.add(path);
      }
      assertTrue(names.contains("a"));
      assertTrue(names.contains("a[1]"));
      assertTrue(names.contains("b"));
      assertTrue(names.contains("c"));     
   }
   
   public void testClashOnElementByPath() throws Exception {
      boolean failure = false;
      
      try {
         new ConstructorScanner(ClashBetweenElementAndText.class);
      }catch(Exception e) {
         e.printStackTrace();
         failure = true;
      }
      assertTrue("Failure should occur when there is an ambiguous parameter", failure);
   }
   
   public void testAmbiguousParameters() throws Exception {
      boolean failure = false;
      
      try {
         new ConstructorScanner(AmbiguousParameters.class);
      }catch(Exception e) {
         e.printStackTrace();
         failure = true;
      }
      assertTrue("Failure should occur when there is an ambiguous parameter", failure);
   }
   
   public void testSameNameWithPath() throws Exception {
      ConstructorScanner scanner = new ConstructorScanner(SameNameWithPath.class);
      Creator creator = scanner.getCreator();
      List<Parameter> parameters = creator.getParameters();
      Set<String> names = new HashSet<String>();
      
      for(Parameter parameter : parameters) {
         String path = parameter.getPath();
         names.add(path);
      }
      assertTrue(names.contains("path[1]/@a"));
      assertTrue(names.contains("path[1]/a[1]"));
   }
   
   public void testSameNameAndAnnotationWithPath() throws Exception {
      boolean failure = false;
      
      try {
         new ConstructorScanner(SameNameAndAnnotationWithPath.class);
      }catch(Exception e) {
         e.printStackTrace();
         failure = true;
      }
      assertTrue("Failure should occur when there is an ambiguous parameter", failure);
   }
   
   public void testSameNameWithDifferentPath() throws Exception {
      ConstructorScanner scanner = new ConstructorScanner(SameNameWithDifferentPath.class);
      Creator creator = scanner.getCreator();
      List<Parameter> parameters = creator.getParameters();
      Set<String> names = new HashSet<String>();
      
      for(Parameter parameter : parameters) {
         String path = parameter.getPath();
         names.add(path);
      }
      assertTrue(names.contains("path[1]/a[1]"));
      assertTrue(names.contains("path[2]/a[1]"));
   }
}
