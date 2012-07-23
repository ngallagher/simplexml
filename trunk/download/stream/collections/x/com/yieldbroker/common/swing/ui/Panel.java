package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.core.Commit;

import com.yieldbroker.common.swing.ui.graph.Graph;
import com.yieldbroker.common.swing.ui.table.Table;

public class Panel extends Widget<JPanel> {

	@ElementListUnion({
		@ElementList(entry="label", type=Label.class, inline=true),
		@ElementList(entry="labelText", type=LabelText.class, inline=true),
		@ElementList(entry="text", type=Text.class, inline=true),
		@ElementList(entry="textArea", type=TextArea.class, inline=true),
		@ElementList(entry="layerVertical", type=LayerVertical.class, inline=true),
		@ElementList(entry="layerHorizontal", type=LayerHorizontal.class, inline=true),	
	    @ElementList(entry="splitVertical", type=SplitVertical.class, inline=true),
	    @ElementList(entry="splitHorizontal", type=SplitHorizontal.class, inline=true), 
		@ElementList(entry="comboBox", type=ComboBox.class, inline=true),
		@ElementList(entry="labelComboBox", type=LabelComboBox.class, inline=true),
		@ElementList(entry="button", type=Button.class, inline=true),
		@ElementList(entry="tabPane", type=TabPane.class, inline=true),
		@ElementList(entry="alignVertical", type=AlignVertical.class, inline=true),
		@ElementList(entry="alignHorizontal", type=AlignHorizontal.class, inline=true),
		@ElementList(entry="form", type=Form.class, inline=true),
		@ElementList(entry="image", type=Image.class, inline=true),
		@ElementList(entry="checkBox", type=CheckBox.class, inline=true),
		@ElementList(entry="spacer", type=Spacer.class, inline=true),
		@ElementList(entry="panel", type=Panel.class, inline=true),
		@ElementList(entry="table", type=Table.class, inline=true),
		@ElementList(entry="scroll", type=Scroll.class, inline=true),
		@ElementList(entry="graph", type=Graph.class, inline=true)	
	})
	protected List<Widget> widgets;	
	
	@Attribute(required=false)
	protected String boundary;
	
	@Attribute(required=false)
	protected String align;	
	
	public Panel() {
		this.widgets = new LinkedList<Widget>();		
	}
	
	public void add(Widget widget) {
		widgets.add(widget);
	}
	
	@Commit
	public void stretch() {
		for(Widget widget : widgets) {
			if(width < widget.width) {
				width = widget.width;
			}	
			if(height < widget.height) {
				height = widget.height;
			}
		}
		for(Widget widget : widgets) {
			if(widget instanceof AlignVertical) {
				widget.width = width;
			}
		}
	}
	
	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		JPanel panel = new JPanel();
		PanelCreateDispatcher handler = new PanelCreateDispatcher(controller, context, id);
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		
		if(id != null) {
			context.add(id, panel);
		}
		if(boundary != null) {
			Boundary type = Boundary.resolve(boundary);
			Border border = type.create(boundary);
			panel.setBorder(border);
		}
		for(Widget widget : widgets) {
			JComponent component = widget.build(controller, context);
			Alignment alignment = Alignment.resolve(align);			

			component.setAlignmentX(alignment.x);
			component.setAlignmentY(alignment.y);			
			panel.add(component);
		}
		panel.setLayout(layout);
		panel.setPreferredSize(size);	
		handler.execute();
		return panel;
	}
}
