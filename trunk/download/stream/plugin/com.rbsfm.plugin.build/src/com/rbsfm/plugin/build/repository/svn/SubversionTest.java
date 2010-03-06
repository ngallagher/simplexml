package com.rbsfm.plugin.build.repository.svn;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.rbsfm.plugin.build.repository.Change;
import com.rbsfm.plugin.build.repository.Info;
import com.rbsfm.plugin.build.repository.Repository;
public class SubversionTest{
   private Repository repository;
   @Before
   public void login() throws Exception{
      repository=Subversion.login("https://simple.svn.sourceforge.net/svnroot/simple","","");
   }
   @Test
   public void info() throws Exception{
      Info info=repository.info(new File("/Users/niall/Workspace/test/build.xml"));
      assertNotNull(info.author);
      assertNotNull(info.location);;
      assertNotNull(info.version);
   }
   @Test
   public void update() throws Exception{
      repository.update(new File("/Users/niall/Workspace/test/build.xml"));
   }
   @Test
   public void log() throws Exception{
      List<Change> log=repository.log(new File("/Users/niall/Workspace/test/build.xml"));
      for(Change change:log){
         System.err.printf("%s:%s:%s:%s%n",change.author,change.message,change.date,change.version);
      }
      assertNotNull(log);
   }
   @Test
   public void commit() throws Exception{
      repository.commit(new File("/Users/niall/Workspace/test/build.xml"));
   }
}
