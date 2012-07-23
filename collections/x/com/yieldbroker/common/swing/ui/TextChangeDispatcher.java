package com.yieldbroker.common.swing.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import com.yieldbroker.common.swing.ui.annotation.TextChange;

public class TextChangeDispatcher extends FocusAdapter implements ActionListener {

	private final FunctionScanner scanner;
	private final Controller controller;
	private final Context context;

	public TextChangeDispatcher(Controller controller, Context context, String id) {
		this.scanner = new FunctionScanner(TextChange.class, id);
		this.controller = controller;
		this.context = context;
	}

	public void focusLost(FocusEvent event) {
		try {
			Object source = controller.resolve(context);
			Function function = scanner.scan(source);

			function.call(source, context);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
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