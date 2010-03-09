package com.rbsfm.plugin.build.assemble;
import java.io.File;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
import com.rbsfm.plugin.build.rpc.ResponseListener;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Status;
public class ProjectAssembler implements ResponseListener{
   private final Repository repository;
   public ProjectAssembler(Repository repository){
      this.repository=repository;
   }
   public void assemble(File file,String projectName, String installName, String tagName, String[] environmentList, String mailAddress) throws Exception{
      RequestBuilder builder = new ProjectAssemblyRequestBuilder(projectName,installName,tagName,environmentList,mailAddress);
      Request request = new Request(builder,this);
      Status status=repository.status(file);
      if(status==Status.MODIFIED){
         repository.commit(file);
      }
      request.execute(Method.POST);
   }
   public void exception(Throwable cause){}
   public void success(int status){}
}
