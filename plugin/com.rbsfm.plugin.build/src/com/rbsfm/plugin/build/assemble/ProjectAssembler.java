package com.rbsfm.plugin.build.assemble;
import java.io.File;
import com.rbsfm.plugin.build.repository.Location;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.Status;
import com.rbsfm.plugin.build.rpc.RpcHandler;
public class ProjectAssembler{
   private final RpcHandler handler;
   private final Repository repository;
   private ProjectAssembler(RpcHandler handler,Repository repository) throws Exception{
      this.handler=handler;
      this.repository=repository;
   }
   public void publish(File file,String moduleName,String revision,String branch,String branchRevision,String mail) throws Exception{
      String tag =String.format("%s-%s-%s-%s",moduleName,revision,branch,branchRevision);
      Status status=repository.status(file);
      if(status==Status.MODIFIED){
         repository.commit(file);
      }
      Location location=repository.tag(file,tag,false);
      if(location!=null){
         handler.publish(moduleName,branch,revision,branchRevision,mail);
      }
   }     
}
