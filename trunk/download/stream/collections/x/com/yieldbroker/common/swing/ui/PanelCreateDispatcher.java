package com.yieldbroker.common.swing.ui;

import com.yieldbroker.common.swing.ui.annotation.PanelCreate;

public class PanelCreateDispatcher {

	private final FunctionScanner scanner;
	private final Controller controller;
	private final Context context;

	public PanelCreateDispatcher(Controller controller, Context context, String id) {
		this.scanner = new FunctionScanner(PanelCreate.class, id);
		this.controller = controller;
		this.context = context;
	}

	public void execute() {
		try {
			Object source = controller.resolve(context);
			Function function = scanner.scan(source);

			function.call(source, context);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
	}
}
