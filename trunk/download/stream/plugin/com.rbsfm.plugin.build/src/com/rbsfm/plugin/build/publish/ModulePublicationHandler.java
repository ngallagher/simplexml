package com.rbsfm.plugin.build.publish;
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
import com.rbsfm.plugin.build.ivy.Module;
import com.rbsfm.plugin.build.ivy.ModuleParser;
import com.rbsfm.plugin.build.ui.MessageFormatter;
import com.rbsfm.plugin.build.ui.MessageLogger;
/**
 * The <code>ModulePublicationHandler</code> object is the main point of entry
 * for the module publication command. This provides an event with the file 
 * that has been selected to perform the operation. A window is opened by this
 * handler, any relevant information is parsed from the selected file in 
 * order to populate the fields of the window.
 * 
 * @author Niall Gallagher
 * 
 * @see com.rbsfm.plugin.build.assemble.ModulePublicationWindow
 */
public class ModulePublicationHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement = selection.getFirstElement();
      Shell shell = HandlerUtil.getActiveShell(event);
      if(firstElement instanceof IFile){
         IFile file = (IFile)firstElement;
         try{
            URI location = file.getRawLocationURI();
            InputStream source = file.getContents();
            Module module = ModuleParser.parse(source);
            open(shell, location, module);
         }catch(Exception cause){
            MessageLogger.openError(shell, "Error", MessageFormatter.format(cause));
         }
      }else{
         MessageLogger.openInformation(HandlerUtil.getActiveShell(event), "Information", "Please select an ivy.xml file");
      }
      return null;
   }
   private void open(Shell shell, URI location, Module module){
      File resource = new File(location);
      PluginWindow window = new PluginWindow(shell, module, resource);
      window.open();
   }
   private static class PluginWindow extends ApplicationWindow{
      private final Module module;
      private final File resource;
      public PluginWindow(Shell shell, Module module, File resource){
         super(shell);
         this.module = module;
         this.resource = resource;
      }               
      protected Control createContents(Composite composite){
         return new ModulePublicationWindow(composite, module, resource);
      }
   }
}
