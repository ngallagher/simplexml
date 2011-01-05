package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import com.rbsfm.plugin.build.svn.Info;
import com.rbsfm.plugin.build.svn.Repository;
public class PatchFileParser implements FileFilter { 
  private final Repository repository;
  private final File root;
  public PatchFileParser(Repository repository, File root) {
    this.repository = repository;
    this.root = root;
  }
  public List<PatchFile> parseFile(String file) throws Exception {
    List<PatchFile> patchFiles = new ArrayList<PatchFile>();
    String token = file.replaceAll("^/", "");
    String project = token.replaceAll("/.*", "");
    File sourceFile = new File(root, token);
    Info info = repository.info(sourceFile);
    patchFiles.add(new PatchFile(root, project, token, info.version));
    return patchFiles;
  }
  public List<PatchFile> parseIdentifier(String identifier) throws Exception {
    List<PatchFile> patchFiles = new ArrayList<PatchFile>();
    String token = identifier.replaceAll("^=", "");
    String sourcePath = token.replaceAll("<.*$", "");
    String project = token.replaceAll("/.*$", "");
    String path = token.replaceAll(".*<", "").replaceAll("\\{.*$", "").replace('.', '/');
    if(token.indexOf('{') != -1) {
      path = path + token.replaceAll(".*\\{", "/");
    }
    File sourceFolder = new File(root, sourcePath);
    File sourceFile = new File(sourceFolder, path);
    List<String> paths = expand(sourceFile, root);
    for(String pathEntry : paths) {
      File file = new File(root, pathEntry);
      Info info = repository.info(file);
      patchFiles.add(new PatchFile(root, project, pathEntry, info.version));
    }
    return patchFiles;    
  }
  private List<String> expand(File sourceFile, File root) throws Exception {
    String rootPath = root.getCanonicalPath();
    int clipIndex = rootPath.length() + 1;
    List<String> paths = new ArrayList<String>();
    if(sourceFile.isDirectory()) {
      File[] list = sourceFile.listFiles(this);
      for(File file : list) {
        String canonicalPath = file.getCanonicalPath();
        String relativePath = canonicalPath.substring(clipIndex);
        paths.add(relativePath.replace('\\', '/'));                
      }
    } else {
      String canonicalPath = sourceFile.getCanonicalPath();
      String relativePath = canonicalPath.substring(clipIndex);
      paths.add(relativePath.replace('\\', '/')); 
    }
    return paths;
  }
  public boolean accept(File file) {
    return !file.isDirectory() &&
      !file.getName().equals("CVS") &&
      !file.getName().equals(".svn") &&
      !file.getName().endsWith(".class");    
  }
}


