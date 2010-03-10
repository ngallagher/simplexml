package com.rbsfm.plugin.build.publish;
import java.io.File;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
import com.rbsfm.plugin.build.rpc.ResponseListener;
import com.rbsfm.plugin.build.svn.Location;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Status;
import com.rbsfm.plugin.build.svn.Subversion;
public class ModulePublisher implements ResponseListener{
   private final Repository repository;
   private final Shell shell;
   public ModulePublisher(Shell shell,String login,String password)throws Exception{
      this.repository=Subversion.login(Scheme.SVN,login,password);
      this.shell=shell;
   }
   public void publish(File file,String moduleName,String revision,String branch,String branchRevision,String mailAddress) throws Exception{
      RequestBuilder builder = new ModulePublicationRequestBuilder(moduleName,branch,revision,branchRevision,mailAddress);
      Request request=new Request(builder,this);
      String tag=String.format("%s-%s-%s-%s",moduleName,revision,branch,branchRevision);
      Status status=repository.status(file);
      if(status==Status.MODIFIED){
         repository.commit(file,tag);
      }
      if(status==Status.STALE){
         MessageDialog.openError(shell, "Error", "Can not publish as ivy.xml is not up to date");
      }else {
         Location location=repository.tag(file,tag,tag,true);
         MessageDialog.openInformation(shell, "Tag created", location.prefix);
         request.execute(Method.POST);
      }

   }
   public void exception(Throwable cause){
      MessageDialog.openInformation(shell,"Error","Failed to publish module");
   }
   public void success(int status){
      MessageDialog.openInformation(shell,"Success","Module has been published");
   }
}
