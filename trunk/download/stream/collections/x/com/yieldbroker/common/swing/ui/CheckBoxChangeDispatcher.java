package com.yieldbroker.common.swing.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.yieldbroker.common.swing.ui.annotation.CheckBoxChange;

public class CheckBoxChangeDispatcher implements ItemListener {

	private final FunctionScanner scanner;
	private final Controller controller;
	private final Context context;

	public CheckBoxChangeDispatcher(Controller controller, Context context, String id) {
		this.scanner = new FunctionScanner(CheckBoxChange.class, id);
		this.controller = controller;
		this.context = context;
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		try {
			Object source = controller.resolve(context);
			Function function = scanner.scan(source);

			function.call(source, context);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
	}
}
