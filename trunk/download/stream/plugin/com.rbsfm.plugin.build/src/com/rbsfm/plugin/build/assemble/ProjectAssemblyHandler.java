package com.rbsfm.plugin.build.assemble;
import java.io.File;
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
import com.rbsfm.plugin.build.ivy.Project;
import com.rbsfm.plugin.build.ivy.ProjectParser;
public class ProjectAssemblyHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection=(IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement=selection.getFirstElement();
      if(firstElement instanceof IFile){
         IFile file=(IFile)firstElement;
         try{
            final File resource=new File(file.getRawLocationURI());
            final InputStream source=file.getContents();
            final Project project=ProjectParser.parse(source);            
            ApplicationWindow window=new ApplicationWindow(HandlerUtil.getActiveShell(event)){
               protected Control createContents(Composite composite){
                  return new ProjectAssemblyWindow(composite,project,resource);
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
