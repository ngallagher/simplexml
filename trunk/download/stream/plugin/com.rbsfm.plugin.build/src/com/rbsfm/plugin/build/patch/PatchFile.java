package com.rbsfm.plugin.build.patch;
import java.io.File;
public class PatchFile {
  private final String project;
  private final String path;
  private final long version;
  private final File root;
  public PatchFile(File root, String project, String path, long version) {
    this.project = project;
    this.path = path;
    this.version = version;
    this.root = root;
  }
  public File getProjectFolder() {
    return new File(root, project);
  }
  public String getProject(){
    return project;
  }
  public String getPath(){
    return path;
  }
  public long getVersion(){
    return version;
  }
}
