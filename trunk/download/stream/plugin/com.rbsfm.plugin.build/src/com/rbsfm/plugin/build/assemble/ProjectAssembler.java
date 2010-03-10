package com.rbsfm.plugin.build.assemble;
import java.io.File;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
import com.rbsfm.plugin.build.rpc.ResponseListener;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Status;
public class ProjectAssembler implements ResponseListener{
   private final Repository repository;
   private final Shell shell;
   public ProjectAssembler(Repository repository,Shell shell){
      this.repository=repository;
      this.shell=shell;
   }
   public void assemble(File file,String projectName, String installName, String tagName, String[] environmentList, String mailAddress) throws Exception{
      RequestBuilder builder = new ProjectAssemblyRequestBuilder(projectName,installName,tagName,environmentList,mailAddress);
      Request request = new Request(builder,this);
      Status status=repository.status(file);
      if(status==Status.MODIFIED){
         repository.commit(file,projectName);
      }
      request.execute(Method.POST);
   }
   public void exception(Throwable cause){
      MessageDialog.openInformation(shell,"Error","Failed to assemble project");
   }
   public void success(int status){
      MessageDialog.openInformation(shell,"Success","Project has been assembled");
   }
}
