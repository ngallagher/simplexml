package com.rbsfm.plugin.build.rpc;
public interface RpcHandler{
   public void publish(String moduleName,String branch,String revision,String branchRevision,String mail) throws Exception;
   public void assemble(String projectName,String tag,String deploy,String environments,String mail) throws Exception;
}
