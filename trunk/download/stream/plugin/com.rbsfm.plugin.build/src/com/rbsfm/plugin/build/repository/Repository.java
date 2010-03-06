package com.rbsfm.plugin.build.repository;
import java.io.File;
import java.util.List;
public interface Repository{
   public boolean update(File resource) throws Exception;
   public boolean commit(File resource) throws Exception;
   public Location tag(File resource,String prefix,boolean dryRun) throws Exception;
   public Location branch(File resource,String prefix,boolean dryRun) throws Exception;
   public Status status(File resource) throws Exception;
   public List<Change> log(File resource) throws Exception;
   public Info info(File resource) throws Exception;
}
