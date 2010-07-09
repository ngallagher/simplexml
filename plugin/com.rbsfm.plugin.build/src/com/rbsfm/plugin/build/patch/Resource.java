package com.rbsfm.plugin.build.patch;
import java.io.File;
public interface Resource {
   public File getFile();
   public File getDirectory();
   public File getProjectDirectory();   
   public long getVersion();
   public String getProject();
   public String getPath();
}
