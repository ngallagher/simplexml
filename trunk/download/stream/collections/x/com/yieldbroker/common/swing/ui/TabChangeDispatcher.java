package com.yieldbroker.common.swing.ui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.yieldbroker.common.swing.ui.annotation.TabChange;

public class TabChangeDispatcher implements ChangeListener {

	private final FunctionScanner scanner;
	private final Controller controller;
	private final Context context;

	public TabChangeDispatcher(Controller controller, Context context, String id) {
		this.scanner = new FunctionScanner(TabChange.class, id);
		this.controller = controller;
		this.context = context;
	}

	public void stateChanged(ChangeEvent event) {
		try {
			Object source = controller.resolve(context);
			Function function = scanner.scan(source);

			function.call(source, context);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
	}
}