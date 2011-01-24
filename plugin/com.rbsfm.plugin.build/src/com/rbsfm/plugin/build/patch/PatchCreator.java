package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.swt.widgets.Shell;
import org.simpleframework.xml.core.Persister;
import com.rbsfm.plugin.build.svn.Location;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Subversion;
import com.rbsfm.plugin.build.ui.MessageLogger;
public class PatchCreator {
  private final Repository repository;
  private final Shell shell;
  public PatchCreator(Shell shell, String login, String password) throws Exception {
    this.repository = Subversion.login(Scheme.HTTP, login, password);
    this.shell = shell;
  }
  public void create(Patch patch, File root, String patchName, String configGroup, String tagName, String hostDir, String includeRegex, String excludeRegex, String id) throws Exception {
    PatchTemplate template = new PatchTemplate(patchName, configGroup, tagName, hostDir, includeRegex, excludeRegex);
    File patchPath = resolvePatchPath(root, tagName);
    if(patchPath == null) {
      MessageLogger.openInformation(shell, "Error", "Project 'cppatches' is not checked out in workspace " + root);
    } else {
      List<PatchFile> patchFiles = patch.collect(repository, root);
      if(!patchFiles.isEmpty()) {
        String taggedProjects = tagProjects(patchFiles, tagName, patchName, id);
        MessageLogger.openInformation(shell, "Projects tagged", taggedProjects);
      }
      String patchSource = template.build(patchFiles);
      File patchFile = new File(patchPath, String.format("%s.xml", patchName));
      if(patchFile.exists()) {
        MessageLogger.openInformation(shell, "Error" , String.format("Patch '%s' already exists", patchFile));
      } else {
        String patchResult = writePatch(patchFile, patchSource);
        if(patchResult != null) {
          MessageLogger.openInformation(shell, "Patch created", patchResult);
        } else {
          MessageLogger.openInformation(shell, "Tags exist", "All tags already exist");
        }
      }
    }
  }
  private String writePatch(File patchFile, String patchSource) throws Exception {
    FileWriter outputFile = new FileWriter(patchFile);
    outputFile.write(patchSource);
    outputFile.flush();
    outputFile.close();
    return patchFile.getCanonicalPath();
  }
  private String tagProjects(List<PatchFile> patchFiles, String tagName, String patchName, String id) throws Exception {
    Set<File> projectRoots = new HashSet<File>();
    Set<Location> tags = new HashSet<Location>();
    StringBuilder builder = new StringBuilder();
    for(PatchFile patchFile : patchFiles) {
      File projectFolder = patchFile.getProjectFolder();
      projectRoots.add(projectFolder);
    }
    String patchFullName = String.format("%s-%s", tagName, patchName);
    String patchMessage = String.format("%s Tag for patch '%s'", id, patchFullName);
    for(File projectFolder : projectRoots){      
      if(!repository.queryTag(projectFolder, patchFullName)) {
        Location location = repository.tag(projectFolder, patchFullName, patchMessage, false);
        tags.add(location);
      }
    }
    if(!tags.isEmpty()) {
      for(Location tag : tags) {
        builder.append(tag.getAbsolutePath());
        builder.append("\n");
      }
      return builder.toString();
    }
    return null;
  }
  private File resolvePatchPath(File root, String tagName) throws Exception {
    File patchProject = new File(root, "cppatches");
    if(patchProject.exists() && patchProject.isDirectory()) {
      File patchPath = new File(patchProject, tagName);
      if(!patchPath.exists()) {
        patchPath.mkdirs();
      }
      return patchPath;
    }
    return null;
  }
  public static void main(String[] list) throws Exception {
    PatchCreator creator = new PatchCreator(null, "gallana", "bevendenN16BL1");
    Persister persister = new Persister();
    Patch patch = persister.read(Patch.class, new File("C:\\work\\development\\corptok-credit-1\\egpricing\\frn.jardesc"), false);
    creator.create(patch, 
                   new File("C:\\work\\development\\corptok-credit-1"), 
                   "patch-001", 
                   "rates", 
                   "credit-03_05_05aq", 
                   "\\\\sinms00273.fm.rbsgrp.net\\d$\\uat\\rbsfm\\cp\\credit-03_05_05aq", 
                   "corptok-autoquoter-master5|corptok-expricepub-master5",
                   //"corptok-pricing-master5",
                   null,
                   "CPAPAC-270");   
  }
}
