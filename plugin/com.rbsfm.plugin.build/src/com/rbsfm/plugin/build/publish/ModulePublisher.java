package com.rbsfm.plugin.build.publish;
import java.io.File;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
import com.rbsfm.plugin.build.rpc.ResponseListener;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Status;
public class ModulePublisher implements ResponseListener{
   private final Repository repository;
   public ModulePublisher(Repository repository){
      this.repository=repository;
   }
   public void publish(File file,String moduleName,String revision,String branch,String branchRevision,String mailAddress) throws Exception{
      RequestBuilder builder = new ModulePublicationRequestBuilder(moduleName,branch,revision,branchRevision,mailAddress);
      Request request=new Request(builder,this);
      String tag=String.format("%s-%s-%s-%s",moduleName,revision,branch,branchRevision);
      Status status=repository.status(file);
      if(status==Status.MODIFIED){
         repository.commit(file);
      }
      repository.tag(file,tag,false);
      request.execute(Method.POST);

   }
   public void exception(Throwable exception){}
   public void success(int status){}
}
