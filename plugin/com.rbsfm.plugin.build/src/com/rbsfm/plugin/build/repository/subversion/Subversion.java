package com.rbsfm.plugin.build.repository.subversion;

import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;

import com.rbsfm.plugin.build.repository.Repository;

public final class Subversion {

   static {
      DAVRepositoryFactory.setup();
   }

   public static Repository login(String login, String password) {
      return new Implementation(login, password);
   }
}