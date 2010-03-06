package com.rbsfm.plugin.build.publish;
import java.io.InputStream;
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
import com.rbsfm.plugin.build.ivy.Module;
import com.rbsfm.plugin.build.ivy.ModuleParser;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.svn.Subversion;
import com.rbsfm.plugin.build.rpc.RpcHandler;
import com.rbsfm.plugin.build.rpc.SocketRpcHandler;
public class ModulePublicationHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection=(IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement=selection.getFirstElement();
      if(firstElement instanceof IFile){
         IFile file=(IFile)firstElement;
         try{
            final InputStream source=file.getContents();
            final Module module=ModuleParser.parse(source);
            final Repository repository=Subversion.login("svn://","gallane","password");
            final RpcHandler handler=new SocketRpcHandler("lonms04037.fm.rbsgrp.net",59009);
            ApplicationWindow window=new ApplicationWindow(HandlerUtil.getActiveShell(event)){
               protected Control createContents(Composite composite){
                  return new ModulePublicationWindow(composite,module,handler,repository);
               }
            };
            window.open();
         }catch(Exception e){
            throw new ExecutionException("Problem processing ivy.xml", e);
         }
      }else{
         MessageDialog.openInformation(HandlerUtil.getActiveShell(event),"Information","Please select an ivy.xml file");
      }
      return null;
   }
}
