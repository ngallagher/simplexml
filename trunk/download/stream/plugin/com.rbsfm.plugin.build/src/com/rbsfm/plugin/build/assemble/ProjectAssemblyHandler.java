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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import com.rbsfm.plugin.build.ivy.Project;
import com.rbsfm.plugin.build.ivy.ProjectParser;
public class ProjectAssemblyHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement = selection.getFirstElement();
      if(firstElement instanceof IFile){
         IFile file = (IFile)firstElement;
         try{
            File resource = new File(file.getRawLocationURI());
            InputStream source = file.getContents();
            Project project = ProjectParser.parse(source);
            Shell shell = HandlerUtil.getActiveShell(event);
            PluginWindow window = new PluginWindow(shell, project, resource);
            window.open();
         }catch(Exception e){
            throw new ExecutionException("Problem processing project", e);
         }
      }else{
         MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information", "Please select a project");
      }
      return null;
   }
   private static class PluginWindow extends ApplicationWindow{
      private final Project project;
      private final File resource;
      public PluginWindow(Shell shell,Project project,File resource){
         super(shell);
         this.project = project;
         this.resource = resource;
      }
      protected Control createContents(Composite composite){
         return new ProjectAssemblyWindow(composite, project, resource);
      }
   }
}
