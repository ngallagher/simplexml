package com.rbsfm.plugin.build.repository.subversion;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rbsfm.plugin.build.repository.Change;
import com.rbsfm.plugin.build.repository.Info;
import com.rbsfm.plugin.build.repository.Repository;

public class SubversionTest  {
   
   private Repository repository;
   
   @Before
   public void login() {
      repository = Subversion.login("", "");
   }
   
   @Test
   public void info() throws Exception {
      Info info = repository.info(new File("/Users/niall/Plugins/com.rbsfm.plugin.build/src/com/rbsfm/plugin/build/repository/subversion/Subversion.java"));
      assertNotNull(info.author);
      assertNotNull(info.path);
      assertNotNull(info.repository);
      assertNotNull(info.version);
   }
   
   @Test
   public void update() throws Exception {
      repository.update(new File("/Users/niall/Plugins/com.rbsfm.plugin.build/src/com/rbsfm/plugin/build/repository/subversion/Subversion.java"));
   }
   
   @Test
   public void log() throws Exception {
      List<Change> log = repository.log(new File("/Users/niall/Plugins/com.rbsfm.plugin.build/src/com/rbsfm/plugin/build/repository/subversion/Subversion.java"));
      for(Change change : log) {
         System.err.printf("%s:%s:%s:%s%n", change.author, change.message, change.date, change.version);
      }
      assertNotNull(log);
   }
}
