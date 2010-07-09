package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.ResourcesPlugin;
import com.rbsfm.plugin.build.svn.Info;
import com.rbsfm.plugin.build.svn.Repository;
public class Workspace {
   private final Repository repository;
   private final FileFilter filter;
   public Workspace(Repository repository) {
      this.filter = new WorkspaceFilter();
      this.repository = repository;
   }
   public List<Resource> find(SourceIdentifier identifier) throws Exception {
      String project = identifier.getProject();
      String folder = identifier.getFolder();
      String file = identifier.getFile();
      return find(project,folder,file);
   }
   public List<Resource> find(String project, String folder, String file) throws Exception {
      String sourcePath = ResourcesPlugin.getWorkspace().getRoot().getProject(project).getFolder(folder).getRawLocationURI().toString();
      String projectPath = ResourcesPlugin.getWorkspace().getRoot().getProject(project).getProject().getDescription().getLocationURI().toString();
      String sourceFile = String.format("%s/%s", sourcePath, file);
      File[] list = search(sourceFile);
      return build(list, projectPath, sourcePath, sourceFile);   
   }
   public List<Resource> find(String project, String folder) throws Exception {
      String sourcePath = ResourcesPlugin.getWorkspace().getRoot().getProject(project).getFolder(folder).getRawLocationURI().toString();
      String projectPath = ResourcesPlugin.getWorkspace().getRoot().getProject(project).getFolder(".").getRawLocationURI().toString();
      File[] list = search(sourcePath);
      return build(list, projectPath, sourcePath, sourcePath);      
   }
   private List<Resource> build(File[] files, String project, String directory, String path) throws Exception {
      List<Resource> list = new ArrayList<Resource>();
      for(File file : files) {
         Info info = repository.info(file);
         Resource resource = new WorkspaceResource(file, project, directory, path, info.version);
         list.add(resource);
      }
      return list;      
   }
   private File convert(String path) throws Exception {
      URI location = new URI(path);
      File file = new File(location);
      return file;
   }
   private File[] search(String path) throws Exception {
      URI location = new URI(path);
      File file = new File(location);
      if(file.isDirectory()) {
         return file.listFiles(filter);
      }
      return new File[]{file};
   }   
   private class WorkspaceFilter implements FileFilter {
      public boolean accept(File file) {
         return file.isFile();
      }      
   }
   private class WorkspaceResource implements Resource {
      private final File project;
      private final File directory;
      private final File file;
      private final String path;
      private final long version;
      public WorkspaceResource(File file, String project, String directory, String path, long version) throws Exception {
         this.file = file;
         this.project = convert(project);
         this.directory = convert(directory);
         this.version = version;
         this.path = path;
      }
      public String getPath() {
         return path;
      }
      public File getDirectory() {
         return directory;
      }
      public File getFile() {
         return file;
      }
      public String getName() {
         return file.getName();
      }
      public String getProject() {
         return project.getName();
      }
      public File getProjectDirectory() {
         return project;
      }
      public long getVersion() {
         return version;
      } 
   }
}
