package com.rbsfm.plugin.build.patch;
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
import com.rbsfm.plugin.build.ui.MessageFormatter;
import com.rbsfm.plugin.build.ui.MessageLogger;
/**
 * The <code>PatchHAndler</code> object is the main point of entry for 
 * generating a patch. This provides an event with the file that has been
 * selected to perform the operation. A window is opened by this handler, 
 * any relevant information is parsed from the selected file in order to 
 * populate the fields of the window.
 * 
 * @author Niall Gallagher
 * 
 * @see com.rbsfm.plugin.build.patch.PatchWindow
 */
public class PatchHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement = selection.getFirstElement();
      Shell shell = HandlerUtil.getActiveShell(event);
      if(firstElement instanceof IFile){
         IFile file = (IFile)firstElement;
         try{
            URI location = file.getRawLocationURI();
            InputStream source = file.getContents();
            Patch patch = PatchParser.parse(source);
            open(shell, location, patch);
         }catch(Exception cause){
            MessageLogger.openError(shell, "Error", MessageFormatter.format(cause));
         }
      }else{
         MessageLogger.openInformation(shell, "Information", "Please select a jardesc patch");
      }
      return null;
   }
   private void open(Shell shell, URI location, Patch patch){
      File resource = new File(location);
      PluginWindow window = new PluginWindow(shell, patch, resource);
      window.open();
   }
   private static class PluginWindow extends ApplicationWindow{
      private final Patch patch;
      private final File resource;
      public PluginWindow(Shell shell,Patch patch,File resource){
         super(shell);
         this.patch = patch;
         this.resource = resource;
      }
      protected Control createContents(Composite composite){
         return new PatchWindow(composite, patch, resource);
      }
   }
}
