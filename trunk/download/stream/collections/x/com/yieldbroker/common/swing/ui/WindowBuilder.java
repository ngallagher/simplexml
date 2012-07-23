package com.yieldbroker.common.swing.ui;

import java.io.File;
import java.io.InputStream;

import javax.swing.JFrame;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class WindowBuilder {

	private final Serializer serializer;
	private final Controller controller;
	private final Context context;

	public WindowBuilder(Controller controller) {
		this.context = new WindowContext();
		this.serializer = new Persister();
		this.controller = controller;
	}

	public Window build(File source) throws Exception {
		Layout layout = serializer.read(Layout.class, source);
		JFrame frame = layout.build(controller, context);

		return new Window(frame, context);
	}

	public Window build(InputStream source) throws Exception {
		Layout layout = serializer.read(Layout.class, source);
		JFrame frame = layout.build(controller, context);

		return new Window(frame, context);
	}
}
