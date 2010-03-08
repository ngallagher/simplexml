package com.rbsfm.plugin.build.assemble;
import static com.rbsfm.plugin.build.Constants.HOST;
import static com.rbsfm.plugin.build.Constants.LOGIN;
import static com.rbsfm.plugin.build.Constants.PASSWORD;
import static com.rbsfm.plugin.build.Constants.PORT;
import static com.rbsfm.plugin.build.Constants.REPOSITORY;
import java.io.File;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.handlers.HandlerUtil;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.svn.Subversion;
import com.rbsfm.plugin.build.rpc.RequestHandler;
import com.rbsfm.plugin.build.rpc.SocketRpcHandler;
public class ProjectAssemblyHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection=(IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement=selection.getFirstElement();
      if(firstElement instanceof IFile){
         IFile file=(IFile)firstElement;
         try{
            final Repository repository=Subversion.login(REPOSITORY,LOGIN,PASSWORD);
            final RequestHandler handler=new SocketRpcHandler(HOST,PORT);
            final File resource=file.getFullPath().toFile();
            ApplicationWindow window=new ApplicationWindow(HandlerUtil.getActiveShell(event)){
               protected Control createContents(Composite composite){
                  return new ProjectAssemblyWindow(composite,handler,repository,resource);
               }
            };
            window.open();
         }catch(Exception e){
            throw new ExecutionException("Problem processing project",e);
         }
      }else{
         MessageDialog.openInformation(HandlerUtil.getActiveShell(event),"Information","Please select a project");
      }
      return null;
   }
}
