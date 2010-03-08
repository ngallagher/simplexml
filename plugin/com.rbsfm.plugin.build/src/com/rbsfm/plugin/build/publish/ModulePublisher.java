package com.rbsfm.plugin.build.publish;
import java.io.File;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.rbsfm.plugin.build.repository.Location;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.Status;
import com.rbsfm.plugin.build.rpc.RequestHandler;
public class ModulePublisher implements RequestCallback{
   private final RequestHandler handler;
   private final Repository repository;
   public ModulePublisher(RequestHandler handler,Repository repository){
      this.handler=handler;
      this.repository=repository;
   }
   public void publish(File file,String moduleName,String revision,String branch,String branchRevision,String mail) throws Exception{
      String tag=String.format("%s-%s-%s-%s",moduleName,revision,branch,branchRevision);
      Status status=repository.status(file);
      if(status==Status.MODIFIED){
         repository.commit(file);
      }
      Location location=repository.tag(file,tag,false);
      if(location!=null){
         handler.publish(this,moduleName,branch,revision,branchRevision,mail);
      }
   }
   public void onError(Request request,Throwable exception){}
   public void onResponseReceived(Request request,Response response){}
}
