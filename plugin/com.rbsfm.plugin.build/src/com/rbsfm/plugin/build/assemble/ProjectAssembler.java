package com.rbsfm.plugin.build.assemble;
import java.io.File;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
import com.rbsfm.plugin.build.rpc.ResponseListener;
import com.rbsfm.plugin.build.svn.Repository;
import com.rbsfm.plugin.build.svn.Scheme;
import com.rbsfm.plugin.build.svn.Status;
import com.rbsfm.plugin.build.svn.Subversion;
import com.rbsfm.plugin.build.ui.MessageFormatter;
public class ProjectAssembler implements ResponseListener{
   private final Repository repository;
   private final Shell shell;
   public ProjectAssembler(Shell shell,String login,String password) throws Exception{
      this.repository = Subversion.login(Scheme.SVN, login, password);
      this.shell = shell;
   }
   public void assemble(File file, String projectName, String installName, String tagName, String environments, String mailAddress) throws Exception{
      String[] environmentList = Environment.parse(environments);
      if(valid(environmentList)) {
         RequestBuilder builder = new ProjectAssemblyRequestBuilder(projectName, installName, tagName, environmentList, mailAddress);
         Request request = new Request(builder, this, false);
         Status status = repository.status(file);
         if(status == Status.MODIFIED){
            repository.commit(file, projectName);
         }
         MessageDialog.openInformation(shell, "Project", "Publishing project " + projectName + " from " + tagName);
         request.execute(Method.POST);
      }
   }
   private boolean valid(String[] environmentList) {
      for(String token : environmentList) {
         if(!Environment.valid(token)) {
            MessageDialog.openError(shell, "Error", "Environment "+token+" is not valid");
            return false;
         }
      }
      return true;
   }
   public void exception(Throwable cause){
      MessageDialog.openInformation(shell, "Error", MessageFormatter.format(cause));
   }
   public void success(int status){
      MessageDialog.openInformation(shell, "Success", "Project has been assembled");
   }
   private static enum Environment{
      UAT,DEV,CONT,PROD,SUPPORTDEV;
      public static String[] parse(String tokens){
         String values = tokens.trim().toLowerCase();
         return values.split("\\s*,\\s*");
         
      }
      public static boolean valid(String token){
         String value = token.toUpperCase();
         for(Environment entry : values()) {
            if(entry.name().equals(value)) {
               return true;
            }
         }
         return false;
      }
   }
}
