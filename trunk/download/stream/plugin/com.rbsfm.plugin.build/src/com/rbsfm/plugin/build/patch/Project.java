package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root
@SuppressWarnings("all")
public class Project{
   public static final String BASE_DIRECTORY = ".";
   public static final String GET_PATCH_TARGET = "get.patch.files";
   public static final String SET_PATCH_PROPERTIES = "set.patch.properties";
   public static final String IMPORT_FILE = "${basedir}/build/build-scm.xml";      
   @ElementList(inline=true)
   private List<Target> target;
   @Attribute
   private String name;
   @Attribute
   private String basedir = BASE_DIRECTORY;
   @Attribute(name="default")
   private String defaultTarget = GET_PATCH_TARGET;
   private List<File> projects;
   private Target properties;
   private Target files;
   public Project(@Attribute(name="name") String name) {
      this.target = new ArrayList<Target>();
      this.projects = new ArrayList<File>();
      this.properties = new Target(SET_PATCH_PROPERTIES);
      this.files = new Target(GET_PATCH_TARGET);
      this.target.add(properties);
      this.target.add(files);
      this.name = name;
   }
   public List<File> getProjects() {
      return projects;
   }
   public void addProject(File project) {
      projects.add(project);
   }
   public void addProperty(String name, String value) {
      properties.addProperty(name, value);
   }
   public void addResource(String path, String prefix, long revision) {
      files.addResource(path, prefix, name, revision);
   }
   @Root
   private static class Import {
      @Attribute
      private String file = IMPORT_FILE;
   }
   @Root
   private static class Target {
      @Attribute
      private String name;
      @ElementList(inline=true,required=false)
      private List<Property> properties;
      @ElementList(inline=true,required=false,entry="scmpatchcheckout")
      private List<PatchEntry> entries;
      public Target(@Attribute(name="name") String name) {
         this.properties = new ArrayList<Property>();
         this.entries = new ArrayList<PatchEntry>();
         this.name = name;
      }
      public void addResource(String path, String prefix, String tag, long revision) {
         String file = String.format("%s/%s", prefix, path);
         entries.add(new PatchEntry(revision, tag, file));
      }
      public void addProperty(String name, String value){
         properties.add(new Property(name, value));
      }
      @Root
      private static final class Property {
         @Attribute
         private String name;
         @Attribute
         private String value;
         public Property(@Attribute(name="name") String name, @Attribute(name="value") String value){
            this.name = name;
            this.value = value;
         }
      }
      @Root
      private static final class PatchEntry {
         @Attribute
         private long revision;
         @Attribute
         private String tag;
         @Attribute
         private String file;
         public PatchEntry(@Attribute(name="revision")long revision, @Attribute(name="tag")String tag, @Attribute(name="file")String file) {
            this.revision = revision;
            this.tag = tag;
            this.file = file;
         }        
      }
      
   }
}
