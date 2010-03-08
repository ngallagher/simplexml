package com.rbsfm.plugin.build.rpc;
import com.google.gwt.http.client.RequestCallback;
public interface RequestHandler{
   public void publish(RequestCallback callback,String moduleName,String branch,String revision,String branchRevision,String mail) throws Exception;
   public void assemble(RequestCallback callback,String projectName,String tag,String deploy,String environments,String mail) throws Exception;
}
