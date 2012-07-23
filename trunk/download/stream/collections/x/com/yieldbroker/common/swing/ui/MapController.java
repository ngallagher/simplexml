package com.yieldbroker.common.swing.ui;

import java.util.Map;

public class MapController implements Controller {

	private final Map<String, Object> actions;
	private final Object main;
	
	public MapController(Map<String, Object> actions) {
		this(actions, null);
	}

	public MapController(Map<String, Object> actions, Object main) {
		this.actions = actions;
		this.main = main;
	}

	@Override
	public Object resolve(Context context) {
		String id = context.getId();
		
		if (id == null) {
			return main;
		}
		return actions.get(id);
	}
}
