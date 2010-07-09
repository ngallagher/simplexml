package com.rbsfm.plugin.build.patch;
import java.io.File;
import java.io.InputStream;
import java.net.URI;

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
import com.rbsfm.plugin.build.ui.MessageFormatter;
/**
 * The <code>PatchBuilderHandler</code> object is the main point of entry
 * for the build patch command. This provides an event with the file 
 * that has been selected to perform the operation. A window is opened by 
 * this handler, any relevant information is parsed from the selected file 
 * in order to populate the fields of the window.
 * 
 * @author Niall Gallagher
 * 
 * @see com.rbsfm.plugin.build.patch.PatchBuilderWindow
 */
public class PatchBuilderHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement = selection.getFirstElement();
      Shell shell = HandlerUtil.getActiveShell(event);
      if(firstElement instanceof IFile){
         IFile file = (IFile)firstElement;
         try{
            URI location = file.getRawLocationURI();
            InputStream source = file.getContents();
            Jardesc jardesc = JardescParser.parse(source);
            open(shell, location, jardesc);
         }catch(Exception cause){
            MessageDialog.openError(shell, "Error", MessageFormatter.format(cause));
         }
      }else{
         MessageDialog.openInformation(shell, "Information", "Please select a jardesc");
      }
      return null;
   }
   private void open(Shell shell, URI location, Jardesc jardesc){
      File resource = new File(location);
      PluginWindow window = new PluginWindow(shell, jardesc, resource);
      window.open();
   }
   private static class PluginWindow extends ApplicationWindow{
      private final Jardesc jardesc;
      private final File resource;
      public PluginWindow(Shell shell,Jardesc jardesc,File resource){
         super(shell);
         this.jardesc = jardesc;
         this.resource = resource;
      }
      protected Control createContents(Composite composite){
         return new PatchBuilderWindow(composite, jardesc);
      }
   }
}
