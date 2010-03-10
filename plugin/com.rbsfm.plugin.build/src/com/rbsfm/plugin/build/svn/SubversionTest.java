package com.rbsfm.plugin.build.svn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class SubversionTest {
   @Test
   public void status() throws Exception{
      Repository repository=Subversion.login(Scheme.SVN, "gallane", "password");
      File resource=new File("C:\\Temp\\cpbuild\\ivy.xml");
      
      assertTrue(resource.exists());
      
      Status status =repository.status(resource);
      Info info=repository.info(resource);
      
      assertEquals(status,Status.MODIFIED);
      assertNotNull(info);
   }
   @Test
   public void commit() throws Exception{
      Repository repository=Subversion.login(Scheme.SVN, "gallane", "password");
      File resource=new File("C:\\Temp\\cpbuild\\ivy.xml");
      
      assertTrue(resource.exists());
      
      Status status =repository.status(resource);
     /* boolean commit =repository.commit(resource,"Commit test message");
      
      assertEquals(status,Status.MODIFIED);
      assertTrue(commit);
      
      status =repository.status(resource);
      
      assertEquals(status,Status.OK);*/
   }
   @Test
   public void log()throws Exception{
      Repository repository=Subversion.login(Scheme.SVN, "gallane", "password");
      File resource=new File("C:\\Temp\\cpbuild\\ivy.xml");
      
      assertTrue(resource.exists());
      
      List<Change> log=repository.log(resource);
      assertFalse(log.isEmpty());
   }
   @Test
   public void update()throws Exception{
      Repository repository=Subversion.login(Scheme.SVN, "gallane", "password");
      File resource=new File("C:\\Temp\\cpbuild\\ivy.xml");
      
      assertTrue(resource.exists());
      
      Status status=repository.status(resource);
      assertEquals(status,Status.STALE);
      repository.update(resource);
   }
   @Test
   public void tag()throws Exception{
      Repository repository=Subversion.login(Scheme.SVN, "gallane", "password");
      File resource=new File("C:\\Temp\\cpbuild\\ivy.xml");
      
      assertTrue(resource.exists());
      
      Status status=repository.status(resource);
      assertEquals(status,Status.NORMAL);
      Location location=repository.tag(resource, "test-tag", "Test tag", false);
      assertEquals(location.prefix, "test-tag");
   }
}
