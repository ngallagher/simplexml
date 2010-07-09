package com.rbsfm.plugin.build.patch;
public interface SourceIdentifier {
   public boolean isFile();
   public String getProject();
   public String getFolder();
   public String getFile();
}
