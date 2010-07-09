package com.rbsfm.plugin.build.patch;
import java.util.List;
import com.rbsfm.plugin.build.svn.Repository;
public class ProjectBuilder {
   private final Workspace workspace;
   public ProjectBuilder(Repository repository) throws Exception{
      this.workspace = new Workspace(repository);
   }
   public Project build(String tagName, Jardesc jardesc) throws Exception {
      List<SourceIdentifier> sourceFiles = jardesc.getSourceFiles();
      List<SourceIdentifier> files = jardesc.getFiles();
      return build(tagName, sourceFiles, files);
   }
   public Project build(String tagName, List<SourceIdentifier> sourceFiles, List<SourceIdentifier> files) throws Exception{
      Project project = new Project(tagName);
      for(SourceIdentifier file : sourceFiles) {
         List<Resource> list = workspace.find(file);
         for(Resource resource : list) {
            long revision = resource.getVersion();
            String path = resource.getPath();
            project.addResource(path, "rbsfm", revision);
         }
      }
      for(SourceIdentifier file : files) {
         List<Resource> list = workspace.find(file);
         for(Resource resource : list) {
            long revision = resource.getVersion();
            String path = resource.getPath();
            project.addResource(path, "rbsfm", revision);
         }
      }
      return project;
   }
} 
