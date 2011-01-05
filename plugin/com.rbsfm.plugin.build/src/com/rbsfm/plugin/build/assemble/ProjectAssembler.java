package com.rbsfm.plugin.build.assemble;
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
public class ProjectAssembler implements ResponseListener{
   private final Repository repository;
   private final Shell shell;
   public ProjectAssembler(Shell shell,String login,String password) throws Exception{
      this.repository = Subversion.login(Scheme.HTTP, login, password);
      this.shell = shell;
   }
   public void assemble(File file, String projectName, String installName, String tag, String environments, String mailAddress, String id) throws Exception{
      String[] environmentList = Environment.parse(environments);
      if(valid(environmentList)) {
         RequestBuilder builder = new ProjectAssemblyRequestBuilder(projectName, installName, tag, environmentList, mailAddress);
         Request request = new Request(builder, this, false);
         Status status = repository.status(file);
         if(status == Status.MODIFIED){
            repository.commit(file, String.format("%s %s", id, projectName));
         }
         if(!repository.queryTag(file, tag)) {
            Location location = repository.tag(file, tag, String.format("%s %s", id, tag), false);
            MessageLogger.openInformation(shell, "Tag created", location.prefix);
         } else {
            MessageLogger.openInformation(shell, "Tag", "Using existing tag "+ tag);
         }
         MessageLogger.openInformation(shell, "Project", "Publishing project " + projectName + " from " + tag);
         request.execute(Method.POST);
      }
   }
   private boolean valid(String[] environmentList) {
      for(String token : environmentList) {
         if(!Environment.valid(token)) {
            MessageLogger.openError(shell, "Error", "Environment "+token+" is not valid");
            return false;
         }
      }
      return true;
   }
   public void exception(Throwable cause){
      MessageLogger.openInformation(shell, "Error", MessageFormatter.format(cause));
   }
   public void success(String message){
      MessageLogger.openInformation(shell, "Success", message);
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
