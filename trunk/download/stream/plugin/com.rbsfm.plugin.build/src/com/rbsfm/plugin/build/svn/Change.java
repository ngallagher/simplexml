package com.rbsfm.plugin.build.svn;
import java.util.Date;
/**
 * The <code>Change</code> object represents a change performed on a resource.
 * It holds information such as the commit message the date of the change and
 * the author of the change. This can be used to determine the resource history.
 * @author Niall Gallagher
 * @see com.rbsfm.plugin.build.svn.Repository
 */
public class Change{
   public final String author;
   public final String message;
   public final long version;
   public final Date date;
   public Change(String author,String message,long version,Date date){
      this.author = author;
      this.message = message;
      this.version = version;
      this.date = date;
   }
   public String toString(){
      return message;
   }
}
