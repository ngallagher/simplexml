package com.rbsfm.plugin.build.svn;
import java.util.Date;
public class Change{
   public final String author;
   public final String message;
   public final long version;
   public final Date date;
   public Change(String author,String message,long version,Date date){
      this.author=author;
      this.message=message;
      this.version=version;
      this.date=date;
   }
   public String toString(){
      return message;
   }
}
