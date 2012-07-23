package com.yieldbroker.common.swing.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.yieldbroker.common.swing.ui.annotation.ButtonClick;

public class ButtonClickDispatcher implements ActionListener {

	private final FunctionScanner scanner;
	private final Controller controller;
	private final Context context;

	public ButtonClickDispatcher(Controller controller, Context context, String id) {
		this.scanner = new FunctionScanner(ButtonClick.class, id);
		this.controller = controller;
		this.context = context;
	}

	public void actionPerformed(ActionEvent event) {
		try {
			Object source = controller.resolve(context);
			Function function = scanner.scan(source);

			function.call(source, context);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
	}
}