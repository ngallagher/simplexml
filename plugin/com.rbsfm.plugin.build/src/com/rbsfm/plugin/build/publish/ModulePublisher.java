package com.rbsfm.plugin.build.publish;
import java.io.File;
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
import com.rbsfm.plugin.build.ui.MessageFormatter;
import com.rbsfm.plugin.build.ui.MessageLogger;
public class ModulePublisher implements ResponseListener{
   private final Repository repository;
   private final Shell shell;
   public ModulePublisher(Shell shell,String login,String password) throws Exception{
      this.repository = Subversion.login(Scheme.HTTP, login, password);
      this.shell = shell;
   }
   public void publish(File file, String moduleName, String revision, String branch, String branchRevision, String mailAddress, String id) throws Exception{
      RequestBuilder builder = new ModulePublicationRequestBuilder(moduleName, branch, revision, branchRevision, mailAddress);
      Request request = new Request(builder, this, false);
      String tag = String.format("%s-%s", moduleName, revision);
      if(branch != null && branch.length() > 0){
         tag = String.format("%s-%s", tag, branch);
      }
      if(branchRevision != null && branchRevision.length() > 0) {
         tag = String.format("%s-%s", tag, branchRevision);
      }
      Status status = repository.status(file);
      if(status == Status.MODIFIED){
         repository.commit(file, String.format("%s %s", id, tag));
      }
      if(status == Status.STALE){
         MessageLogger.openError(shell, "Error", "Can not publish as ivy.xml is not up to date");
      }else{
         if(!repository.queryTag(file, tag)) {
            Location location = repository.tag(file, tag, String.format("%s %s", id, tag), false);
            MessageLogger.openInformation(shell, "Tag created", location.prefix);
         } else {
            MessageLogger.openInformation(shell, "Tag", "Using existing tag "+ tag);
         }
         request.execute(Method.POST);
      }
   }
   public void exception(Throwable cause){
      MessageLogger.openInformation(shell, "Error", MessageFormatter.format(cause));
   }
   public void success(String message){
      MessageLogger.openInformation(shell, "Success", message);
   }
}
