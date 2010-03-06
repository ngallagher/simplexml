package com.rbsfm.plugin.build.repository.svn;
import java.util.Date;
import java.util.LinkedList;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import com.rbsfm.plugin.build.repository.Change;
@SuppressWarnings("serial")
class ChangeLog extends LinkedList<Change> implements ISVNLogEntryHandler{
   public void handleLogEntry(SVNLogEntry entry) throws SVNException{
      String message=entry.getMessage();
      String author=entry.getAuthor();
      Date date=entry.getDate();
      long revision=entry.getRevision();
      addFirst(new Change(author,message,revision,date));
   }
}