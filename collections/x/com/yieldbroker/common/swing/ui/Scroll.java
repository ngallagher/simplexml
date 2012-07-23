package com.yieldbroker.common.swing.ui;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;

import com.yieldbroker.common.swing.ui.table.Table;

public class Scroll extends Widget<JScrollPane>{
	
	@ElementUnion({ 
		@Element(name="panel", type=Panel.class), 
		@Element(name="layer", type=LayerVertical.class),
		@Element(name="split", type=SplitVertical.class), 
		@Element(name="tabPane", type=TabPane.class),
		@Element(name="align", type=AlignVertical.class),
		@Element(name="table", type=Table.class)
	})
	protected Widget widget;

	@Override
	public JScrollPane build(Controller controller, Context context, Dimension size) {
		JComponent component = widget.build(controller, context, size);		
		return new JScrollPane(component, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);		
	} 
	

}
