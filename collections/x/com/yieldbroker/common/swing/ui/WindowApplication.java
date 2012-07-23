package com.yieldbroker.common.swing.ui;

import java.io.File;

import javax.swing.UIManager;

/**
 * A window application requires a {@link Controller} object and the
 * location of a user interface XML definition file. To start the
 * user interface simply invoke the start method. This will cause the
 * screen to be loaded from the specified XML file and controlled
 * using the provided {@link Controller} implementation.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.swing.ui.Controller
 */
public class WindowApplication {

	private final WindowBuilder builder;
	private final File source;

	public WindowApplication(Controller controller, File source) {
		this.builder = new WindowBuilder(controller);
		this.source = source;
	}

	public void start() throws Exception {
		skin();
		build();
	}

	private void skin() throws Exception {
		String skin = UIManager.getSystemLookAndFeelClassName();

		if (skin != null) {
			UIManager.setLookAndFeel(skin);
		}
	}

	private void build() throws Exception {
		Window window = builder.build(source);

		if (window != null) {
			window.show();
		}
	}
}
