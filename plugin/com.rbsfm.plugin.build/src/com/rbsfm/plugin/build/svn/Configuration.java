package com.rbsfm.plugin.build.svn;
public interface Configuration{
   public final boolean INCLUDE_MERGED_REVISIONS=false;
   public final boolean DISCOVER_CHANGED_PATHS=false;
   public final boolean STOP_ON_COPY=false;
   public final boolean ALLOW_OBSTRUCTIONS=true;
   public final boolean DEPTH_IS_STICKY=true;
   public final boolean REMOTE_STATUS=true;
   public final boolean FORCE_COMMIT=false;
   public final boolean KEEP_CHANGE_LIST=false;
   public final boolean KEEP_LOCKS=true;
   public final boolean MAKE_PARENTS=true;
   public final boolean IS_MOVE=false;
   public final boolean FAIL_WHEN_EXISTS=true;
   public final int CHANGE_LOG_DEPTH=20;
}
