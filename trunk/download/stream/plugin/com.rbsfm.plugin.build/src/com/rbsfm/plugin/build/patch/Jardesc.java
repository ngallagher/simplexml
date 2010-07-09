package com.rbsfm.plugin.build.patch;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
@Root
public class Jardesc {
   @Element
   private SelectedElements selectedElements;   
   public List<SourceIdentifier> getSourceFiles() {
      return selectedElements.getIdentifiers();
   }   
   public List<SourceIdentifier> getFiles() {
      return selectedElements.getFiles();
   }
   @Root
   private static class SelectedElements {
      @ElementList(inline=true, required=false)
      private List<JavaElement> javaElements;  
      @ElementList(inline=true, required=false)
      private List<File> fileElements;
      private Pattern source;
      private Pattern file;
      public SelectedElements() {
         this.source = Pattern.compile("=([a-zA-Z0-9\\.-_]+?)/([a-zA-Z0-9\\.-_]+)<([a-zA-Z0-9_\\.]+)(.*)");
         this.file = Pattern.compile("/([a-zA-Z0-9\\.-_]+?)/(.*)");
      }   
      @Commit
      private void commit() {
         for(JavaElement element : javaElements) {
            element.build(source);
         }
         for(File element : fileElements) {
            element.build(file);
         }
      }      
      public List<SourceIdentifier> getIdentifiers() {
         List<SourceIdentifier> list = new ArrayList<SourceIdentifier>();
         for(JavaElement element : javaElements) {
            list.add(element);
         }
         return list;
      } 
      public List<SourceIdentifier> getFiles() {
         List<SourceIdentifier> list = new ArrayList<SourceIdentifier>();
         for(File element : fileElements) {
            list.add(element);
         }
         return list;
      }
   }
   @Root
   private static class File implements SourceIdentifier {
      @Attribute
      private String path;
      private String projectName;
      private String sourcePath;
      private void build(Pattern pattern) {
         Matcher matcher = pattern.matcher(path);
         if(matcher.matches()) {
            projectName = matcher.group(1);
            sourcePath = matcher.group(2);
         }
      }
      public boolean isFile() {
         return true;
      }
      public String getProject() {
         return projectName;
      }
      public String getFolder() {
         int index = sourcePath.lastIndexOf('/');
         if(index == -1) {
            return "/";
         }
         return sourcePath.substring(0, index);
      }
      public String getFile() {
         int index = sourcePath.lastIndexOf('/');
         if(index == -1) {
            return "";
         }
         return sourcePath.substring(index + 1);
      }
   }
   @Root
   private static class JavaElement implements SourceIdentifier{      
      @Attribute
      private String handleIdentifier;
      private String projectName;
      private String sourcePath;
      private String packageName;
      private String classFile;
      private void build(Pattern pattern) {
         Matcher matcher = pattern.matcher(handleIdentifier);
         if(matcher.matches()) {
            projectName = matcher.group(1);
            sourcePath = matcher.group(2);
            packageName = matcher.group(3);
            classFile = matcher.group(4);
         }
      }
      public boolean isFile() {
         return classFile != null && classFile.length() > 0;
      }
      public String getProject() {
         return projectName;
      }
      public String getPackageDirectory() {
         return packageName.replace('.', '/');
      }
      public String getFile() {
         if(isFile()) {
            return classFile.substring(1);
         }
         return classFile;
      }
      public String getFolder() {
         return String.format("%s/%s", sourcePath, getPackageDirectory());
      }
   }
}
