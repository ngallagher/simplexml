package com.rbsfm.plugin.build.svn;
import java.io.File;
import java.util.List;
public interface Repository{
   public boolean update(File resource) throws Exception;
   public boolean commit(File resource, String message) throws Exception;
   public Location tag(File resource,String prefix,String message,boolean dryRun) throws Exception;
   public Location branch(File resource,String prefix,String message,boolean dryRun) throws Exception;
   public Status status(File resource) throws Exception;
   public List<Change> log(File resource) throws Exception;
   public Info info(File resource) throws Exception;
}
