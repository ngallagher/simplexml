package com.rbsfm.plugin.build.ui;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
public abstract class ButtonWindow extends Composite{
   private List<Text> fields;
   public ButtonWindow(Composite parent){
      super(parent,SWT.NONE);
      this.fields=new ArrayList<Text>();
   }
   protected void addTextField(Text text){
      fields.add(text);
   }
   protected void clearFields(){
      for(Text text:fields){
         text.setText("");
      }
   }
   protected Text createLabelledText(Composite parent,String label){
      return createLabelledText(parent,label,20,null,null);
   }
   protected Text createLabelledText(Composite parent,String label,int limit,String tip,String value){
      Label l=new Label(parent,SWT.LEFT);
      l.setText(label);
      Text text=new Text(parent,SWT.SINGLE|SWT.BORDER);
      if(limit>0){
         text.setTextLimit(limit);
      }
      if(tip!=null){
         text.setToolTipText(tip);
      }
      if(value!=null){
         text.setText(value);
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
   public static class SelectionAdapter implements SelectionListener{
      public void widgetSelected(SelectionEvent e){}
      public void widgetDefaultSelected(SelectionEvent e){
         widgetSelected(e);
      }
   };
}