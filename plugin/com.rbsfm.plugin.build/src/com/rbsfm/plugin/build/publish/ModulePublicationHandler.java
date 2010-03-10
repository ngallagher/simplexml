package com.rbsfm.plugin.build.publish;
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
import com.rbsfm.plugin.build.ivy.Module;
import com.rbsfm.plugin.build.ivy.ModuleParser;
public class ModulePublicationHandler extends AbstractHandler{
   public Object execute(ExecutionEvent event) throws ExecutionException{
      IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getActiveMenuSelection(event);
      Object firstElement = selection.getFirstElement();
      if(firstElement instanceof IFile){
         IFile file = (IFile)firstElement;
         try{
            File resource = new File(file.getRawLocationURI());
            InputStream source = file.getContents();
            Module module = ModuleParser.parse(source);
            Shell shell = HandlerUtil.getActiveShell(event);
            PluginWindow window = new PluginWindow(shell, module, resource);
            window.open();
         }catch(Exception e){
            throw new ExecutionException("Problem processing ivy.xml", e);
         }
      }else{
         MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information", "Please select an ivy.xml file");
      }
      return null;
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
