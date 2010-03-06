package com.rbsfm.plugin.build.assemble;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.rbsfm.plugin.build.publish.ModulePublisher;
import com.rbsfm.plugin.build.rpc.RpcHandler;
public class ProjectAssemblyWindow extends Composite{
   // input fields; members so they can be referenced from event handlers
   Text projectField;
   Text tagField;
   Text installField;
   Text environmentsField;
   Text mailField;
   RpcHandler handler;
   ArrayList fields=new ArrayList(); // all fields
   // reset all registered fields
   protected void clearFields(){
      for(Iterator i=fields.iterator();i.hasNext();){
         ((Text)i.next()).setText("");
      }
   }

   public ProjectAssemblyWindow(Composite parent,RpcHandler handler){
      super(parent,SWT.NONE); // must always supply parent and style
      this.handler = handler;
      createGui();
   }
   // GUI creation helpers
   protected Text createLabelledText(Composite parent,String label){
      return createLabelledText(parent,label,20,null);
   }
   protected Text createLabelledText(Composite parent,String label,int limit,String tip){
      Label l=new Label(parent,SWT.LEFT);
      l.setText(label);
      Text text=new Text(parent,SWT.SINGLE|SWT.BORDER);
      if(limit>0){
         text.setTextLimit(limit);
      }
      if(tip!=null){
         text.setToolTipText(tip);
      }
      text.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
      fields.add(text);
      return text;
   }
   protected Button createButton(Composite parent,String label,SelectionListener l){
      return createButton(parent,label,l);
   }
   protected Button createButton(Composite parent,String label,String tip,SelectionListener l){
      Button b=new Button(parent,SWT.NONE);
      b.setText(label);
      if(tip!=null){
         b.setToolTipText(tip);
      }
      if(l!=null){
         b.addSelectionListener(l);
      }
      return b;
   }
   // partial selection listener
   class MySelectionAdapter implements SelectionListener{
      public void widgetSelected(SelectionEvent e){
      // default is to do nothing
      }
      public void widgetDefaultSelected(SelectionEvent e){
         widgetSelected(e);
      }
   };
   protected void createGui(){
      setLayout(new GridLayout(1,true));
      // create the input area
      Group entryGroup=new Group(this,SWT.NONE);
      entryGroup.setText("Module Details");
      // use 2 columns, not same width
      GridLayout entryLayout=new GridLayout(2,false);
      entryGroup.setLayout(entryLayout);
      entryGroup.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
      projectField=createLabelledText(entryGroup,"Project: ",40,"Enter the project name");
      tagField=createLabelledText(entryGroup,"Tag: ",40,"Enter the tag");
      installField=createLabelledText(entryGroup,"Install Name: ",20,"Enter install name");
      environmentsField=createLabelledText(entryGroup,"Environments: ",20,"Enter a comma separated list of environments");
      mailField=createLabelledText(entryGroup,"Mail: ",20,"Enter your mail address");
      // create the button area
      Composite buttons=new Composite(this,SWT.NONE);
      buttons.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
      // make all buttons the same size
      FillLayout buttonLayout=new FillLayout();
      buttonLayout.marginHeight=2;
      buttonLayout.marginWidth=2;
      buttonLayout.spacing=5;
      buttons.setLayout(buttonLayout);
      // OK button prints input values
      Button okButton=createButton(buttons,"&Assemble","Assemble",new MySelectionAdapter(){
         public void widgetSelected(SelectionEvent event){
            try{
               handler.assemble(projectField.getText(),tagField.getText(),installField.getText(),environmentsField.getText(),mailField.getText());
            }catch(Exception e){
               MessageDialog.openInformation(getShell(),"Error",e.getMessage());
            }
         }
      });
      // Clear button resets input values
      Button clearButton=createButton(buttons,"&Clear","Clear inputs",new MySelectionAdapter(){
         public void widgetSelected(SelectionEvent e){
            clearFields();
            projectField.forceFocus();
         }
      });
   }
}