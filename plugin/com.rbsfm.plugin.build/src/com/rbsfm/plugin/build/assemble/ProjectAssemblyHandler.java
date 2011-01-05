package com.rbsfm.plugin.build.assemble;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import com.rbsfm.plugin.build.ivy.Project;
import com.rbsfm.plugin.build.ivy.ProjectParser;
import com.rbsfm.plugin.build.ui.MessageFormatter;
import com.rbsfm.plugin.build.ui.MessageLogger;
/**
 * The <code>ProjectAssemblyHandler</code> object is the main point of entry
 * for the project assembly command. This provides an event with the file 
 * that has been selected to perform the operation. A window is opened by 
 * this handler, any relevant information is parsed from the selected file 
 * in order to populate the fields of the window.
 * 
 * @author Niall Gallagher
 * 
 * @see com.rbsfm.plugin.build.assemble.ProjectAssemblyWindow
 */
public class ProjectAssemblyHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement = selection.getFirstElement();
      Shell shell = HandlerUtil.getActiveShell(event);
      if(firstElement instanceof IFile){
         IFile file = (IFile)firstElement;
         try{
            URI location = file.getRawLocationURI();
            InputStream source = file.getContents();
            Project project = ProjectParser.parse(source);
            open(shell, location, project);
         }catch(Exception cause){
            MessageLogger.openError(shell, "Error", MessageFormatter.format(cause));
         }
      }else{
         MessageLogger.openInformation(shell, "Information", "Please select a project");
      }
      return null;
   }
   private void open(Shell shell, URI location, Project project){
      File resource = new File(location);
      PluginWindow window = new PluginWindow(shell, project, resource);
      window.open();
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
