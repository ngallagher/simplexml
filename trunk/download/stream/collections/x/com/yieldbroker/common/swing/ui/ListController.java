package com.yieldbroker.common.swing.ui;

import java.util.List;


public class ListController implements Controller {

	private final ResolveContextScanner scanner;
	private final Object main;

	public ListController(List<Object> actions) {
		this(actions, null);
	}
	
	public ListController(List<Object> actions, Object main) {
		this.scanner = new ResolveContextScanner(actions);
		this.main = main;
	}

	@Override
	public Object resolve(Context context) {
		String id = context.getId();

		try {
			if (id == null) {
				return main;
			}
			return scanner.lookup(id);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
	}
}
